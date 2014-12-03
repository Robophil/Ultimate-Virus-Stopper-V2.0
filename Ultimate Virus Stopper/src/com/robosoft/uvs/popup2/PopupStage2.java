package com.robosoft.uvs.popup2;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class PopupStage2{
	public PopupStage2(Stage stageroot) throws IOException {
		// TODO Auto-generated constructor stub
		Parent root1 = FXMLLoader.load(getClass().getResource("/com/robosoft/uvs/popup2/Popup2.fxml"));
		Scene scene = new Scene(root1);
		Stage stage = new Stage();
		stage.initOwner(stageroot);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setScene(scene);        
                stage.show();
	}
}
