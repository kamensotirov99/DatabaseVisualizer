package dbProject.classes;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class initController {

	@FXML private Button docButton;
	@FXML private Button patButton;
	@FXML private Button deptButton;
	@FXML private Button specButton;
	@FXML private Button trButton;
	@FXML private Button diagButton;
	
	
	public void docButtonPressed(ActionEvent event) {
		
		FXMLLoader docViewLoader=new FXMLLoader(getClass().getResource("Doc_Scene.fxml"));
		Parent root;
		try {
			root = (Parent)docViewLoader.load();
			Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.show();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
public void patButtonPressed(ActionEvent event) {
		
		FXMLLoader patViewLoader=new FXMLLoader(getClass().getResource("Pat_Scene.fxml"));
		Parent root;
		try {
			root = (Parent)patViewLoader.load();
			Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.show();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

public void specButtonPressed(ActionEvent event) {
	
	FXMLLoader specViewLoader=new FXMLLoader(getClass().getResource("Spec_Scene.fxml"));
	Parent root;
	try {
		root = (Parent)specViewLoader.load();
		Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
	stage.setScene(new Scene(root));
	stage.show();
	} catch (IOException e) {
		
		e.printStackTrace();
	}
}

public void deptButtonPressed(ActionEvent event) {
	
	FXMLLoader deptViewLoader=new FXMLLoader(getClass().getResource("Dept_Scene.fxml"));
	Parent root;
	try {
		root = (Parent)deptViewLoader.load();
		Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
	stage.setScene(new Scene(root));
	stage.show();
	} catch (IOException e) {
		
		e.printStackTrace();
	}
}

public void trButtonPressed(ActionEvent event) {
	
	FXMLLoader trViewLoader=new FXMLLoader(getClass().getResource("Treat_Scene.fxml"));
	Parent root;
	try {
		root = (Parent)trViewLoader.load();
		Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
	stage.setScene(new Scene(root));
	stage.show();
	} catch (IOException e) {
		
		e.printStackTrace();
	}
}

public void diagButtonPressed(ActionEvent event) {
	
	FXMLLoader diagViewLoader=new FXMLLoader(getClass().getResource("Diag_Scene.fxml"));
	Parent root;
	try {
		root = (Parent)diagViewLoader.load();
		Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
	stage.setScene(new Scene(root));
	stage.show();
	} catch (IOException e) {
		
		e.printStackTrace();
	}
}
}
