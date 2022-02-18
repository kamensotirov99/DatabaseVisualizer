package dbProject.classes;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

public class diagSceneController implements Initializable{
	@FXML TableView <Diagnosis> diags;
	@FXML TableColumn<Diagnosis,Integer> diagIdColumn;
	@FXML TableColumn<Diagnosis,String> diagDescColumn;
	@FXML TextField diagDesc;
	
	
	
	
@Override
public void initialize(URL arg0, ResourceBundle arg1) {
	diagIdColumn.setCellValueFactory(new PropertyValueFactory<>("diagId"));
	diagDescColumn.setCellValueFactory(new PropertyValueFactory<>("diagDesc"));
	diags.setItems(getDiags());
	diags.setEditable(true);
	diagDescColumn.setCellFactory(TextFieldTableCell.forTableColumn());
}

public List<Diagnosis> findAllDiags() {
	CriteriaBuilder cb = MainClass.getCurrentSessionfromConfig().getCriteriaBuilder();
    CriteriaQuery<Diagnosis> cq = cb.createQuery(Diagnosis.class);
    Root<Diagnosis> rootEntry = cq.from(Diagnosis.class);
    CriteriaQuery<Diagnosis> all = cq.select(rootEntry);
 
    TypedQuery<Diagnosis> allQuery = MainClass.getCurrentSessionfromConfig().createQuery(all);
    return allQuery.getResultList();    
}


public ObservableList<Diagnosis> getDiags(){
	ObservableList<Diagnosis>diagnosis=FXCollections.observableArrayList();
	List<Diagnosis>diag=findAllDiags();
	for(int i=0;i<diag.size();i++) {
		diagnosis.add(diag.get(i));
	}
	return diagnosis;
}


public void backButtonPressed(ActionEvent event) {
		
		FXMLLoader initViewLoader=new FXMLLoader(getClass().getResource("Starter_Scene.fxml"));
		Parent root;
		try {
			root = (Parent)initViewLoader.load();
			Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.show();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

public void createDiag() {
	if(diagDesc.getText().isBlank()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Create diagnosis");
		alert.setContentText("Text field is empty");
		alert.showAndWait();
	}else {
		boolean flag=false;
		List<Diagnosis>allDiags=findAllDiags();
		for(int i=0;i<allDiags.size();i++) {
			if(allDiags.get(i).getDiagDesc().equals(diagDesc.getText())) {
				flag=true;
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Error");
				alert.setContentText("Diagnosis already exists");
				alert.showAndWait();
				diagDesc.setText("");
			}
		}
		
		if(flag==false) {
			Session session1=MainClass.getCurrentSessionfromConfig();
			Diagnosis newDiag=new Diagnosis();
			newDiag.setDiagDesc(diagDesc.getText().toString());
			try {
				
				session1.getSession();
				session1.beginTransaction();
				session1.save(newDiag);
				session1.getTransaction().commit();
				session1.close();
				diags.getItems().add(newDiag);
				diagDesc.setText("");
				}catch(ConstraintViolationException e) {
					session1.getTransaction().rollback();
					System.out.println("error");
					diags.getItems().remove(newDiag);
				}
		}
	}
}


public void deleteDiag() {
	if(diags.getSelectionModel().isEmpty()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Delete diagnosis");
		alert.setContentText("No diagnosis selected");
		alert.showAndWait();
	}else {
		Session session1=MainClass.getCurrentSessionfromConfig();
		Diagnosis diagnosis=diags.getSelectionModel().getSelectedItem();
		diagnosis=session1.find(Diagnosis.class, diagnosis.getDiagId());
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete Diagnosis?", ButtonType.YES, ButtonType.NO);
	    ButtonType result = alert.showAndWait().orElse(ButtonType.YES);
	    if (ButtonType.YES.equals(result)) {
	    	if(diagnosis.getTreatmentses().isEmpty()) {
	       try {
			
			session1.getSession();
			session1.beginTransaction();
			session1.delete(diagnosis);
			session1.getTransaction().commit();
			session1.close();
			diags.setItems(getDiags());
			
			}catch(ConstraintViolationException e) {
				session1.getTransaction().rollback();
				System.out.println("error");
			
			}}else {
				Alert alert1 = new Alert(AlertType.WARNING);
				alert1.setTitle("Delete diagnosis");
				alert1.setContentText("There must be no treatment with this diagnosis in order to delete it");
				alert1.showAndWait();
			}
	    }
	}
}


public void editDiagDesc(CellEditEvent editCell) {
	if(editCell.getNewValue().toString().isBlank()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
		diags.setItems(getDiags());
	}
	else {
	List<Diagnosis>allDiags=findAllDiags();
	boolean flag=false;
	for(int i=0;i<allDiags.size();i++) {
		if(allDiags.get(i).getDiagDesc().equals(editCell.getNewValue().toString())) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setContentText("Diagnosis already exists");
			alert.showAndWait();
			diags.setItems(getDiags());
			flag=true;
		}
	}
	
	if(flag==false) {
		Session session=MainClass.getCurrentSessionfromConfig();
		Diagnosis diag=diags.getSelectionModel().getSelectedItem();
		diag=session.find(Diagnosis.class, diag.getDiagId());
		diag.setDiagDesc(editCell.getNewValue().toString());
		session.getSessionFactory().openSession();
		session.getSession();
		session.beginTransaction();
		session.saveOrUpdate(diag);
		session.getTransaction().commit();
		session.close();
		diags.setItems(getDiags());
	}
}}
}
