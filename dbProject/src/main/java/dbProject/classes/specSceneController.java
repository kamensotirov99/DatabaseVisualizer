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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

public class specSceneController implements Initializable{
	@FXML TableView <Specialties> specs;
	@FXML TableColumn<Specialties,Integer> specIdColumn;
	@FXML TableColumn<Specialties,String> specDescColumn;
	@FXML TextField specDesc;
	
@Override
public void initialize(URL arg0, ResourceBundle arg1) {
	specIdColumn.setCellValueFactory(new PropertyValueFactory<>("specId"));
	specDescColumn.setCellValueFactory(new PropertyValueFactory<>("specDesc"));
	specs.setItems(getSpecs());
	specs.setEditable(true);
	specDescColumn.setCellFactory(TextFieldTableCell.forTableColumn());
}


public List<Specialties> findAllSpecs() {
	CriteriaBuilder cb = MainClass.getCurrentSessionfromConfig().getCriteriaBuilder();
    CriteriaQuery<Specialties> cq = cb.createQuery(Specialties.class);
    Root<Specialties> rootEntry = cq.from(Specialties.class);
    CriteriaQuery<Specialties> all = cq.select(rootEntry);
 
    TypedQuery<Specialties> allQuery = MainClass.getCurrentSessionfromConfig().createQuery(all);
    return allQuery.getResultList();    
}


public ObservableList<Specialties> getSpecs(){
	ObservableList<Specialties>specialties=FXCollections.observableArrayList();
	List<Specialties>spec=findAllSpecs();
	for(int i=0;i<spec.size();i++) {
		specialties.add(spec.get(i));
	}
	return specialties;
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

public void createSpecialty() {
	if(specDesc.getText().isBlank()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Create specialty");
		alert.setContentText("Text field is empty");
		alert.showAndWait();
	}else {
		boolean flag=false;
		List<Specialties>allSpecs=findAllSpecs();
		for(int i=0;i<allSpecs.size();i++) {
			if(allSpecs.get(i).getSpecDesc().equals(specDesc.getText())) {
				flag=true;
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Error");
				alert.setContentText("Specialty already exists");
				alert.showAndWait();
				specDesc.setText("");
			}
		}
		
		if(flag==false) {
			Session session1=MainClass.getCurrentSessionfromConfig();
			Specialties newSpec=new Specialties();
			newSpec.setSpecDesc(specDesc.getText().toString());
			try {
				
				session1.getSession();
				session1.beginTransaction();
				session1.save(newSpec);
				session1.getTransaction().commit();
				session1.close();
				specs.getItems().add(newSpec);
				specDesc.setText("");
				
				}catch(ConstraintViolationException e) {
					session1.getTransaction().rollback();
					System.out.println("error");
					specs.getItems().remove(newSpec);
				}
		}
	}
}

public void deleteSpec() {
	if(specs.getSelectionModel().isEmpty()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Delete specialty");
		alert.setContentText("No specialty selected");
		alert.showAndWait();
	}else {
		Session session1=MainClass.getCurrentSessionfromConfig();
		Specialties specialty=specs.getSelectionModel().getSelectedItem();
		specialty=session1.find(Specialties.class, specialty.getSpecId());
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete Specialty?", ButtonType.YES, ButtonType.NO);
	    ButtonType result = alert.showAndWait().orElse(ButtonType.YES);
	    if (ButtonType.YES.equals(result)) {
	    	if(specialty.getDoctorses().isEmpty()) {
	       try {
			
			session1.getSession();
			session1.beginTransaction();
			session1.delete(specialty);
			session1.getTransaction().commit();
			session1.close();
			specs.setItems(getSpecs());
			
			}catch(ConstraintViolationException e) {
				session1.getTransaction().rollback();
				System.out.println("error");
			
			}}else {
				Alert alert1 = new Alert(AlertType.WARNING);
				alert1.setTitle("Delete specialty");
				alert1.setContentText("There must be no doctors with this specialty in order to delete it");
				alert1.showAndWait();
			}
	    }
	}
}

public void editSpecDesc(CellEditEvent editCell) {
	
	if(editCell.getNewValue().toString().isBlank()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
		specs.setItems(getSpecs());
	}
	else {
	List<Specialties>allSpecs=findAllSpecs();
	boolean flag=false;
	for(int i=0;i<allSpecs.size();i++) {
		if(allSpecs.get(i).getSpecDesc().equals(editCell.getNewValue().toString())) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setContentText("Specialty already exists");
			alert.showAndWait();
			specs.setItems(getSpecs());
			flag=true;
		}
	}
	
	if(flag==false) {
		Session session=MainClass.getCurrentSessionfromConfig();
		Specialties spec=specs.getSelectionModel().getSelectedItem();
		spec=session.find(Specialties.class, spec.getSpecId());
		spec.setSpecDesc(editCell.getNewValue().toString());
		session.getSessionFactory().openSession();
		session.getSession();
		session.beginTransaction();
		session.saveOrUpdate(spec);
		session.getTransaction().commit();
		session.close();
		specs.setItems(getSpecs());
	}}
}

}
