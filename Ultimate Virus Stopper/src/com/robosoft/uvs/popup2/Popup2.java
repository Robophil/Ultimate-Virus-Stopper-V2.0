package com.robosoft.uvs.popup2;

import java.net.URL;
import java.util.ResourceBundle;

import com.robosoft.uvs.constants.Key;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Popup2 implements Initializable{
	@FXML private Button closeBtn;
	@FXML private AnchorPane root;
	@FXML private Text textInfo;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		textInfo.setText(Key.threatDeleted+" threat deleted");
                Key.textInfo = textInfo;
	}

	@FXML
	private void clickedBtn(ActionEvent e){
		if (e.getSource() == closeBtn) {
			final Stage stage = (Stage) root.getScene().getWindow();
			stage.close();
			Key.threatDeleted = 0;
		}
	}
	
}
