package com.robosoft.uvs.splash;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class splashFxml implements Runnable{
    private final Stage primaryStage;
    private double xOffset=0;
    private double yOffset=0;
	public splashFxml(Stage stageroot) throws IOException {///com/robosoft/uvs/splash/splash.fxml
		// TODO Auto-generated constructor stub
		this.primaryStage = stageroot;
	}

    @Override
    public void run() {
            try {
                Thread.sleep(6000);
                
                Platform.runLater(()->{
                    try {
                        Parent root1 = FXMLLoader.load(getClass().getResource("/com/robosoft/uvs/page1/Uvs.fxml"));
                        Scene scene = new Scene(root1);
                        primaryStage.setScene(scene);
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
                    } catch (IOException e) {
                    }
                });
                
            } catch (InterruptedException ex) {
            }
    }
}
