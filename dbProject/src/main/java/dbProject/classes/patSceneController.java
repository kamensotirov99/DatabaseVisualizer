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

public class patSceneController	implements Initializable {
	@FXML TableView <Patients> pats;
	@FXML TableColumn<Patients,Integer> patIdColumn;
	@FXML TableColumn<Patients,String> patFnameColumn;
	@FXML TableColumn<Patients,String> patLnameColumn;
	@FXML TableColumn<Patients,String> patAddressColumn;
	@FXML TextField patFname;
	@FXML TextField patLname;
	@FXML TextField patAddress;
	
@Override
public void initialize(URL arg0, ResourceBundle arg1) {
	patIdColumn.setCellValueFactory(new PropertyValueFactory<>("patId"));
	patFnameColumn.setCellValueFactory(new PropertyValueFactory<>("patFname"));
	patLnameColumn.setCellValueFactory(new PropertyValueFactory<>("patLname"));
	patAddressColumn.setCellValueFactory(new PropertyValueFactory<>("patAddress"));
	pats.setItems(getPats());
	pats.setEditable(true);
	patFnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
	patLnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
	patAddressColumn.setCellFactory(TextFieldTableCell.forTableColumn());
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

public List<Patients> findAllPatients() {
	CriteriaBuilder cb = MainClass.getCurrentSessionfromConfig().getCriteriaBuilder();
    CriteriaQuery<Patients> cq = cb.createQuery(Patients.class);
    Root<Patients> rootEntry = cq.from(Patients.class);
    CriteriaQuery<Patients> all = cq.select(rootEntry);
 
    TypedQuery<Patients> allQuery = MainClass.getCurrentSessionfromConfig().createQuery(all);
    return allQuery.getResultList();    
}

public ObservableList<Patients> getPats(){
	ObservableList<Patients>patients=FXCollections.observableArrayList();
	List<Patients>pat=findAllPatients();
	for(int i=0;i<pat.size();i++) {
		patients.add(pat.get(i));
	}
	return patients;
}


public void createPatient() {
	if(patFname.getText().isBlank()||patLname.getText().isBlank()||patAddress.getText().isBlank()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Create patient");
		alert.setContentText("Empty text field found");
		alert.showAndWait();
	}else {
		
			Session session1=MainClass.getCurrentSessionfromConfig();
			Patients newPat=new Patients();
			newPat.setPatFname(patFname.getText().toString());
			newPat.setPatLname(patLname.getText().toString());
			newPat.setPatAddress(patAddress.getText().toString());
			try {
				
				session1.getSession();
				session1.beginTransaction();
				session1.save(newPat);
				session1.getTransaction().commit();
				session1.close();
				pats.getItems().add(newPat);
				patFname.setText("");
				patLname.setText("");
				patAddress.setText("");
				
				}catch(ConstraintViolationException e) {
					session1.getTransaction().rollback();
					System.out.println("error");
					pats.getItems().remove(newPat);
				}
		}
	}


public void deletePat() {
	if(pats.getSelectionModel().isEmpty()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Delete patient");
		alert.setContentText("No patient selected");
		alert.showAndWait();
	}else {
		Session session1=MainClass.getCurrentSessionfromConfig();
		Patients patient=pats.getSelectionModel().getSelectedItem();
		patient=session1.find(Patients.class, patient.getPatId());
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete Patient?", ButtonType.YES, ButtonType.NO);
	    ButtonType result = alert.showAndWait().orElse(ButtonType.YES);
	    if (ButtonType.YES.equals(result)) {
	    	if(patient.getTreatmentses().isEmpty()) {
	       try {
			
			session1.getSession();
			session1.beginTransaction();
			session1.delete(patient);
			session1.getTransaction().commit();
			session1.close();
			pats.setItems(getPats());
			
			}catch(ConstraintViolationException e) {
				session1.getTransaction().rollback();
				System.out.println("error");
			
			}}else {
				Alert alert1 = new Alert(AlertType.WARNING);
				alert1.setTitle("Delete patient");
				alert1.setContentText("There must be no treatment with this patient in order to delete it");
				alert1.showAndWait();
			}
	    }
	}
}

public void editPatFname(CellEditEvent editCell) {
	if(editCell.getNewValue().toString().isBlank()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
		pats.setItems(getPats());
	}
	else {
		Session session=MainClass.getCurrentSessionfromConfig();
		Patients pat=pats.getSelectionModel().getSelectedItem();
		pat=session.find(Patients.class, pat.getPatId());
		pat.setPatFname(editCell.getNewValue().toString());
		session.getSessionFactory().openSession();
		session.getSession();
		session.beginTransaction();
		session.saveOrUpdate(pat);
		session.getTransaction().commit();
		session.close();
		pats.setItems(getPats());
		
	}
}

public void editPatLname(CellEditEvent editCell) {
	if(editCell.getNewValue().toString().isBlank()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
		pats.setItems(getPats());
	}
	else {
		Session session=MainClass.getCurrentSessionfromConfig();
		Patients pat=pats.getSelectionModel().getSelectedItem();
		pat=session.find(Patients.class, pat.getPatId());
		pat.setPatLname(editCell.getNewValue().toString());
		session.getSessionFactory().openSession();
		session.getSession();
		session.beginTransaction();
		session.saveOrUpdate(pat);
		session.getTransaction().commit();
		session.close();
		pats.setItems(getPats());
		
	}
}

public void editPatAddress(CellEditEvent editCell) {
	if(editCell.getNewValue().toString().isBlank()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
		pats.setItems(getPats());
	}
	else {
		Session session=MainClass.getCurrentSessionfromConfig();
		Patients pat=pats.getSelectionModel().getSelectedItem();
		pat=session.find(Patients.class, pat.getPatId());
		pat.setPatAddress(editCell.getNewValue().toString());
		session.getSessionFactory().openSession();
		session.getSession();
		session.beginTransaction();
		session.saveOrUpdate(pat);
		session.getTransaction().commit();
		session.close();
		pats.setItems(getPats());
		
	}
}

}
