package com.robosoft.uvs.popup1;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Popup1 implements Initializable{
	@FXML private Button closeBtn;
	@FXML private AnchorPane root;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	@FXML
	private void clickedBtn(ActionEvent e){
		if (e.getSource() == closeBtn) {
			final Stage stage = (Stage) root.getScene().getWindow();
			stage.close();
		}
	}
	
}
