package com.robosoft.uvs;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.robosoft.uvs.monitors.ProcessMonitor;
import com.robosoft.uvs.monitors.RegistryMonitor;
import com.robosoft.uvs.splash.splashFxml;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Uvs extends Application {
	private double xOffset = 0;
    private double yOffset = 0;
    
	@Override
	public void start(Stage primaryStage) throws IOException, InterruptedException {
		//load the fxmlFile
		Parent root1 = FXMLLoader.load(getClass().getResource("/com/robosoft/uvs/splash/splashFxml.fxml"));
		Platform.setImplicitExit(false);
		
		//helps the stage to be dragged around the screen
		root1.setOnMousePressed((MouseEvent event)->{
			xOffset = primaryStage.getX() - event.getScreenX();
                        yOffset = primaryStage.getY() - event.getScreenY();
		});
        
		//helps the stage to be dragged around the screen
                root1.setOnMouseDragged((MouseEvent event)->{
                        primaryStage.setX(event.getScreenX() + xOffset);
                        primaryStage.setY(event.getScreenY() + yOffset);
                });
        
        //create a scene from the parent
        Scene scene1 = new Scene(root1);
        
        //set logFile as output
        setLogfile();
        
        //set the stage attributes
        primaryStage.getIcons().add(new Image("/com/robosoft/uvs/res/logo.png"));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene1);
        primaryStage.show();
        
        //start the thread to monitor PC
        ExecutorService ex = Executors.newCachedThreadPool();
        ProcessMonitor p = new ProcessMonitor();
        RegistryMonitor r = new RegistryMonitor();
        splashFxml s = new splashFxml(primaryStage);
        ex.execute(p);
        ex.execute(r);
        ex.execute(s);
        ex.shutdown();
        
        //clear the temp_ folder
        clearTemp();
        
        
	}
	
	/**
         * create a new logFile for output
         */
        private void setLogfile(){
            String dir = System.getProperty("user.home")+"/Ultimate Virus Stopper/";
            String logfile = System.getProperty("user.home")+"/Ultimate Virus Stopper/logfile.txt";
            try {
                if(!(new File(dir).exists()))
                    new File(dir).mkdirs();
                
                if(!(new File(logfile).exists()))
                    new File(logfile).createNewFile();
                
                PrintStream
                    stdout = new PrintStream(new FileOutputStream(logfile));
                
                System.setOut(stdout);
            } catch (IOException ex) {
            }
        }
        
	/**
	 * clear the temporary files location which could be hiding place for virus
	 */
	private void clearTemp(){
		try {
                    String path2temp = System.getenv("TMP");
                    File file = new File(path2temp);
                    File[] tempfiles = file.listFiles();

                    //delete all tempFiles
                    for (File file2 : tempfiles) {
                            file2.delete();
                    }
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}