package com.robosoft.uvs.page1;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import com.robosoft.uvs.constants.Key;
import com.robosoft.uvs.monitors.scanDirectory;
import com.robosoft.uvs.popup.PopupStage;
import com.robosoft.uvs.popup2.PopupStage2;
import java.awt.Desktop;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Uvs implements Initializable{
	@FXML private ToggleButton protectionBtn;
	@FXML private ToggleButton registrationBtn;
	@FXML private ImageView settingsIV;
	@FXML private ImageView helpIV;
	@FXML private ImageView aboutIV;
	@FXML private ImageView exitIV;
	@FXML private ImageView minIV;
	@FXML private Parent background;
	@FXML private Button scanBtn;
	
	private TrayIcon icon;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		protectionBtn.setStyle("-fx-base: lightgreen;");
		protectionBtn.setSelected(false);
		
		registrationBtn.setSelected(false);
		registrationBtn.setStyle("-fx-base: lightgreen;");
		
		addToTray();
	}
	
	/**
	 * action to be performed if a button is clicked by a mouse
	 * @param e
	 */
	@FXML
	private void buttonClicked(MouseEvent e){
		//if button clicked is the exit button
		if (e.getSource() == exitIV) {
			System.exit(0);
		}
		
		//minimize the stage
		if (e.getSource() == minIV) {
                    final Stage stage = (Stage) background.getScene().getWindow();
                    Platform.runLater(()->{
                    stage.hide();
                    icon.displayMessage("Ultimate Virus Stopper", "running in background", TrayIcon.MessageType.INFO);
        	});
		}
		
		else if (e.getSource() == aboutIV) {
			final Stage stage = (Stage) background.getScene().getWindow();
			try {
				new PopupStage(stage);
			} catch (IOException e1) {
			}
		}
		
		else if (e.getSource() == helpIV) {
			final Stage stage = (Stage) background.getScene().getWindow();
			try {
				new com.robosoft.uvs.popup1.PopupStage1(stage);
			} catch (IOException e1) {
			}
		}
	}
	/**
	 * actions to be performed depending on clicked button
	 * @param e
	 */
	@FXML
	private void btnActionCommand(ActionEvent e){
		if (e.getSource() == protectionBtn) {
			if(protectionBtn.isSelected()){
				protectionBtn.setStyle("-fx-base: red;");
				protectionBtn.setText("OFF");
				Key.RealTimeProtection = false;
                                System.out.println(Key.dateTime()+" Turned off");
			}else{
				protectionBtn.setStyle("-fx-base: lightgreen;");
				protectionBtn.setText("ON");
				Key.RealTimeProtection = true;
                                System.out.println(Key.dateTime()+" Turned on");
			}
		}
		
		if (e.getSource() == scanBtn) {
			fixDrive();
		}
	}
	
	/**
	 * try to recover the hidden files in the drive
	 */
	private void fixDrive(){
		final Stage stage = (Stage) background.getScene().getWindow();
		
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Select drive for fixing");
		File file = chooser.showDialog(stage);
		
		//if no file was selected, return
		if (file == null) {
			return;
		}
		
		scanBtn.setDisable(true);
		String driveName = file.getAbsolutePath().substring(0, 1);
		//perform operation
		Runtime runtime = Runtime.getRuntime();
		String [] cmd = {"CMD","/C",driveName+": && attrib -s -h /s /d *.* <NUL"};
		try {
			runtime.exec(cmd);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(Key.dateTime()+" "+e.getMessage());
		}
		
		finally{
                        //show dialog
			try {
                            new PopupStage2(stage);
			} catch (IOException e) {
			}
                        
			cleanDrive(driveName);
                        clearTemp();
			scanBtn.setDisable(false);
			
			
		}
	}
	
	/**
	 * deletes all *.lnk, *.ini, *.vbs files in the root directory of the drive
	 * @param drive
	 */
	private void cleanDrive(String drive){
		//get the selected directory
                final ExecutorService ex = Executors.newCachedThreadPool();
		final File dir = new File(drive+":/");
		final File[] files = dir.listFiles();
		int i = 0; 
                Key.threatDeleted = i;
		
		//get all the files inside the drive's root folder
		for (File file : files) {
                    String filePath = file.getAbsolutePath();
                    if(file.isDirectory()){
                        ex.execute(new scanDirectory(file));
                    }else{
                        if(containsIgnoreCase(filePath, ".lnk") || containsIgnoreCase(filePath, ".inf") || containsIgnoreCase(filePath, ".vbs")
                                || containsIgnoreCase(filePath, "AutoIt3.exe")){
                            file.delete();
                            Key.threatDeleted+=1;
                            System.out.println(Key.dateTime()+" deleted "+file.getAbsolutePath());
                        }
                    }
                }
		ex.shutdown();
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
        
        private boolean containsIgnoreCase( String haystack, String needle ) {
          if(needle.equals(""))
            return true;

          if(haystack == null || needle == null || haystack .equals(""))
            return false; 

          Pattern p = Pattern.compile(needle,Pattern.CASE_INSENSITIVE+Pattern.LITERAL);
          Matcher m = p.matcher(haystack);
          return m.find();
	}
	
	/**
	 * add this stage to the tray
	 */
	private void addToTray(){
		//f system tray is not supported
		if(!SystemTray.isSupported())
			return;
		SystemTray tray = SystemTray.getSystemTray();
		icon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(Uvs.class.getResource("/com/robosoft/uvs/res/logo2.png")), "Ultimate Virus Stopper");
		
		//when close is clicked
		final ActionListener closeListener = (java.awt.event.ActionEvent e) -> {
                    System.out.println(Key.dateTime()+" Exited");
                    System.exit(0);
                };
                
                //when close is clicked
		final ActionListener logListener = (java.awt.event.ActionEvent e) -> {
                    String val = System.getProperty("user.home")+"/Ultimate Virus Stopper/logfile.txt";
                    Platform.runLater(()->{
                        try {
                            Desktop.getDesktop().open(new File(System.getProperty("user.home")+"/Ultimate Virus Stopper/"));
                        } catch (IOException ex) {
                        }
                    });
                };
		//when show is clicked
		final ActionListener showListener = (java.awt.event.ActionEvent e) -> {
                    Platform.runLater(()->{
                        final Stage stage = (Stage) background.getScene().getWindow();
                        stage.show();
                    });
                };
		
		final MouseAdapter showAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				Platform.runLater(()->{
					final Stage stage = (Stage) background.getScene().getWindow();
					stage.show();
				});

			}
		};
		
		PopupMenu popup = new PopupMenu();

		MenuItem showItem = new MenuItem("Show");
		showItem.addActionListener(showListener);
		popup.add(showItem);
                
                MenuItem log = new MenuItem("Log");
		log.addActionListener(logListener);
		popup.add(log);

		MenuItem closeItem = new MenuItem("Exit");
		closeItem.addActionListener(closeListener);
		popup.add(closeItem);
		
		icon.addMouseListener(showAdapter);
		icon.setPopupMenu(popup);
		
		try {
			tray.add(icon);
		} catch (AWTException e) {
			System.err.println(e);
		}
		
	}
	
}
