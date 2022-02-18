package dbProject.classes;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

public class trSceneController implements Initializable{
	@FXML TableView <Treatments> treats;
	@FXML TableColumn<Treatments,Integer> trIdColumn;
	@FXML TableColumn<Treatments,Integer> patIdColumn;
	@FXML TableColumn<Treatments,Integer> docIdColumn;
	@FXML TableColumn<Treatments,LocalDate> stDateColumn;
	@FXML TableColumn<Treatments,LocalDate> endDateColumn;
	@FXML TableColumn<Treatments,Integer> amountDueColumn;
	@FXML TableColumn<Treatments,Integer> diagIdColumn;
	@FXML ChoiceBox<Integer> trPats;
	@FXML ChoiceBox<Integer> trDocs;
	@FXML ChoiceBox<Integer> trDiags;
	@FXML TextField amountDueField;
	@FXML DatePicker stDate;
	@FXML DatePicker endDate;
	@FXML TextField searchField;
	
@Override
public void initialize(URL arg0, ResourceBundle arg1) {
	trIdColumn.setCellValueFactory(new PropertyValueFactory<>("trId"));
	patIdColumn.setCellValueFactory(new PropertyValueFactory<>("patients"));
	docIdColumn.setCellValueFactory(new PropertyValueFactory<>("doctors"));
	stDateColumn.setCellValueFactory(new PropertyValueFactory<>("stDate"));
	endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
	amountDueColumn.setCellValueFactory(new PropertyValueFactory<>("amountDue"));
	diagIdColumn.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
	treats.setItems(getTreatments());
	initPatients();
	initDoctors();
	initDiags();
	treats.setEditable(true);
	patIdColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
	docIdColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
	stDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
	endDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
	amountDueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
	diagIdColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
}


public List<Treatments> findAllTreatments() {
	CriteriaBuilder cb = MainClass.getCurrentSessionfromConfig().getCriteriaBuilder();
    CriteriaQuery<Treatments> cq = cb.createQuery(Treatments.class);
    Root<Treatments> rootEntry = cq.from(Treatments.class);
    CriteriaQuery<Treatments> all = cq.select(rootEntry);
 
    TypedQuery<Treatments> allQuery = MainClass.getCurrentSessionfromConfig().createQuery(all);
    return allQuery.getResultList();    
}

public ObservableList<Treatments> getTreatments(){
	ObservableList<Treatments>treatments=FXCollections.observableArrayList();
	List<Treatments>tr=findAllTreatments();
	for(int i=0;i<tr.size();i++) {
		treatments.add(tr.get(i));
	}
	return treatments;
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

public List<Doctors> findAllDoctors() {
	CriteriaBuilder cb = MainClass.getCurrentSessionfromConfig().getCriteriaBuilder();
    CriteriaQuery<Doctors> cq = cb.createQuery(Doctors.class);
    Root<Doctors> rootEntry = cq.from(Doctors.class);
    CriteriaQuery<Doctors> all = cq.select(rootEntry);
 
    TypedQuery<Doctors> allQuery = MainClass.getCurrentSessionfromConfig().createQuery(all);
    return allQuery.getResultList();    
}

public List<Diagnosis> findAllDiags() {
	CriteriaBuilder cb = MainClass.getCurrentSessionfromConfig().getCriteriaBuilder();
    CriteriaQuery<Diagnosis> cq = cb.createQuery(Diagnosis.class);
    Root<Diagnosis> rootEntry = cq.from(Diagnosis.class);
    CriteriaQuery<Diagnosis> all = cq.select(rootEntry);
 
    TypedQuery<Diagnosis> allQuery = MainClass.getCurrentSessionfromConfig().createQuery(all);
    return allQuery.getResultList();    
}

public void initPatients() {
	List<Patients>pat=findAllPatients();
	for(int i=0;i<pat.size();i++) {
		trPats.getItems().add(pat.get(i).getPatId());
	}
	
}

public void initDoctors() {
	List<Doctors>doc=findAllDoctors();
	for(int i=0;i<doc.size();i++) {
		trDocs.getItems().add(doc.get(i).getDocId());
	}
}

public void initDiags() {
	List<Diagnosis>diag=findAllDiags();
	for(int i=0;i<diag.size();i++) {
		trDiags.getItems().add(diag.get(i).getDiagId());
	}
	
}

public void createTreatment() {
	
	if(amountDueField.getText().isBlank()||trPats.getSelectionModel().isEmpty()||trDocs.getSelectionModel().isEmpty()||trDiags.getSelectionModel().isEmpty()||stDate.getValue().equals(null)||endDate.getValue().equals(null)) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Empty field encountered");
		alert.showAndWait();
	}else if(stDate.getValue().compareTo(endDate.getValue())>0) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Starting date occurs after end date");
		alert.showAndWait();
	}else {

		
			Session session1=MainClass.getCurrentSessionfromConfig();
			Treatments newTr=new Treatments();
			newTr.setStDate(stDate.getValue());
			newTr.setEndDate(endDate.getValue());
			newTr.setAmountDue(Integer.parseInt(amountDueField.getText()));
			
			
			List<Patients>allPats=findAllPatients();
			for(int i=0;i<allPats.size();i++) {
				if(allPats.get(i).getPatId()==trPats.getValue()) {
					Patients a=allPats.get(i);
					a=session1.find(Patients.class,a.getPatId());
					newTr.setPatients(a);
					a.getTreatmentses().add(newTr);
				}
			}
			
			List<Doctors>allDocs=findAllDoctors();
			
			for(int i=0;i<allDocs.size();i++) {
				if(allDocs.get(i).getDocId()==trDocs.getValue()) {
					Doctors b=allDocs.get(i);
					b=session1.find(Doctors.class, allDocs.get(i).getDocId());
					newTr.setDoctors(b);
					b.getTreatmentses().add(newTr);
				}
			}
			
			List<Diagnosis>allDiags=findAllDiags();
			for(int i=0;i<allDiags.size();i++) {
				if(allDiags.get(i).getDiagId()==trDiags.getValue()) {
					Diagnosis a=allDiags.get(i);
					a=session1.find(Diagnosis.class,a.getDiagId());
					newTr.setDiagnosis(a);
					a.getTreatmentses().add(newTr);
				}
			}
			
			
			try {
				
				session1.getSession();
				session1.beginTransaction();
				session1.save(newTr);
				session1.getTransaction().commit();
				session1.close();
				treats.getItems().add(newTr);
				trPats.setValue(null);
				trDocs.setValue(null);
				trDiags.setValue(null);
				amountDueField.setText("");
				stDate.setValue(null);
				endDate.setValue(null);
				
				}catch(ConstraintViolationException e) {
					session1.getTransaction().rollback();
					System.out.println("error");
					treats.getItems().remove(newTr);
				}
		
		
	}
	
}


public void deleteTreatment() {
	if(treats.getSelectionModel().isEmpty()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Delete treatment");
		alert.setContentText("No treatment selected");
		alert.showAndWait();
	}
	else {
	Session session1=MainClass.getCurrentSessionfromConfig();
	Treatments tr=treats.getSelectionModel().getSelectedItem();
	tr=session1.find(Treatments.class, tr.getTrId());
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete Treatment?", ButtonType.YES, ButtonType.NO);
    ButtonType result = alert.showAndWait().orElse(ButtonType.YES);
    if (ButtonType.YES.equals(result)) {
       try {
		
		session1.getSession();
		session1.beginTransaction();
		session1.delete(tr);
		session1.getTransaction().commit();
		session1.close();
		treats.setItems(getTreatments());
		
		}catch(ConstraintViolationException e) {
			session1.getTransaction().rollback();
			System.out.println("error");
			
		}
    }
    }
	
}

public void editAmountDue(CellEditEvent editCell) {
	if(editCell.getNewValue().toString().isBlank()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
		treats.setItems(getTreatments());
	}
	else {
		Session session=MainClass.getCurrentSessionfromConfig();
		Treatments tr=treats.getSelectionModel().getSelectedItem();
		tr=session.find(Treatments.class, tr.getTrId());
		tr.setAmountDue((int)editCell.getNewValue());
		session.getSessionFactory().openSession();
		session.getSession();
		session.beginTransaction();
		session.saveOrUpdate(tr);
		session.getTransaction().commit();
		session.close();
		treats.setItems(getTreatments());
		
	}
}

public void editStDate(CellEditEvent editCell) {
	
	
	
	if(editCell.getNewValue()==null) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
		treats.setItems(getTreatments());
	}
	else
	{	LocalDate cellDate=LocalDate.parse(editCell.getNewValue().toString());
		Session session=MainClass.getCurrentSessionfromConfig();
		Treatments tr=treats.getSelectionModel().getSelectedItem();
		tr=session.find(Treatments.class, tr.getTrId());
		
		if(cellDate.compareTo(tr.getEndDate())>0) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Starting date should not occur after the end date");
		alert.showAndWait();
		treats.setItems(getTreatments());
		}
		
		else {
		tr.setStDate(LocalDate.parse(editCell.getNewValue().toString()));
		session.getSessionFactory().openSession();
		session.getSession();
		session.beginTransaction();
		session.saveOrUpdate(tr);
		session.getTransaction().commit();
		session.close();
		treats.setItems(getTreatments());
		}
		
	}
}

public void editEndDate(CellEditEvent editCell) {
	
	
	
	if(editCell.getNewValue()==null) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
		treats.setItems(getTreatments());
	}
	else
	{	LocalDate cellDate=LocalDate.parse(editCell.getNewValue().toString());
		Session session=MainClass.getCurrentSessionfromConfig();
		Treatments tr=treats.getSelectionModel().getSelectedItem();
		tr=session.find(Treatments.class, tr.getTrId());
		
		if(cellDate.compareTo(tr.getEndDate())<0) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Starting date should not occur before the end date");
		alert.showAndWait();
		treats.setItems(getTreatments());
		}
		
		else {
		tr.setEndDate(LocalDate.parse(editCell.getNewValue().toString()));
		session.getSessionFactory().openSession();
		session.getSession();
		session.beginTransaction();
		session.saveOrUpdate(tr);
		session.getTransaction().commit();
		session.close();
		treats.setItems(getTreatments());
		}
		
	}
}

public void editTrPat(CellEditEvent editCell) {
	if(editCell.getNewValue()==null) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
		treats.setItems(getTreatments());
	}
	
	else {
		Session session=MainClass.getCurrentSessionfromConfig();
		Treatments tr=treats.getSelectionModel().getSelectedItem();
		tr=session.find(Treatments.class, tr.getTrId());
		List<Patients>allPats=findAllPatients();
		boolean flag=false;
		for(int i=0;i<allPats.size();i++) {
			if(allPats.get(i).getPatId()==(int)editCell.getNewValue()) {
				Patients a=allPats.get(i);
				a=session.find(Patients.class,a.getPatId());
				tr.getPatient().getTreatmentses().remove(tr);
				tr.setPatients(a);
				a.getTreatmentses().add(tr);
				flag=true;
				session.getSessionFactory().openSession();
				session.getSession();
				session.beginTransaction();
				session.saveOrUpdate(tr);
				session.getTransaction().commit();
				session.close();
				treats.setItems(getTreatments());
			}
		}
		if(flag==false) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setContentText("Invalid Id");
			alert.showAndWait();
			treats.setItems(getTreatments());
		}
	}
}

public void editTrDoc(CellEditEvent editCell) {
	if(editCell.getNewValue()==null) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
		treats.setItems(getTreatments());
	}
	
	else {
		Session session=MainClass.getCurrentSessionfromConfig();
		Treatments tr=treats.getSelectionModel().getSelectedItem();
		tr=session.find(Treatments.class, tr.getTrId());
		List<Doctors>allDocs=findAllDoctors();
		boolean flag=false;
		for(int i=0;i<allDocs.size();i++) {
			if(allDocs.get(i).getDocId()==(int)editCell.getNewValue()) {
				Doctors a=allDocs.get(i);
				a=session.find(Doctors.class,a.getDocId());
				tr.getDoctor().getTreatmentses().remove(tr);
				tr.setDoctors(a);
				a.getTreatmentses().add(tr);
				flag=true;
				session.getSessionFactory().openSession();
				session.getSession();
				session.beginTransaction();
				session.saveOrUpdate(tr);
				session.getTransaction().commit();
				session.close();
				treats.setItems(getTreatments());
			}
		}
		if(flag==false) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setContentText("Invalid Id");
			alert.showAndWait();
			treats.setItems(getTreatments());
		}
	}
}

public void editTrDiagnosis(CellEditEvent editCell) {
	if(editCell.getNewValue()==null) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
		treats.setItems(getTreatments());
	}
	
	else {
		Session session=MainClass.getCurrentSessionfromConfig();
		Treatments tr=treats.getSelectionModel().getSelectedItem();
		tr=session.find(Treatments.class, tr.getTrId());
		List<Diagnosis>allDiags=findAllDiags();
		boolean flag=false;
		for(int i=0;i<allDiags.size();i++) {
			if(allDiags.get(i).getDiagId()==(int)editCell.getNewValue()) {
				Diagnosis a=allDiags.get(i);
				a=session.find(Diagnosis.class,a.getDiagId());
				tr.getDiag().getTreatmentses().remove(tr);
				tr.setDiagnosis(a);
				a.getTreatmentses().add(tr);
				flag=true;
				session.getSessionFactory().openSession();
				session.getSession();
				session.beginTransaction();
				session.saveOrUpdate(tr);
				session.getTransaction().commit();
				session.close();
				treats.setItems(getTreatments());
			}
		}
		if(flag==false) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setContentText("Invalid Id");
			alert.showAndWait();
			treats.setItems(getTreatments());
		}
	}
}

public void resetTable() {
	treats.setItems(getTreatments());
}

public void searchByPatient() {
	if(searchField.getText().isBlank()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
	}else {
		List<Treatments>allTreats=findAllTreatments();
		ObservableList<Treatments>treatments=FXCollections.observableArrayList();
		for(int i=0;i<allTreats.size();i++) {
			if(searchField.getText().equals(String.valueOf(allTreats.get(i).getPatient().getPatId()))) {
				treatments.add(allTreats.get(i));
			}
		}
		if(treatments.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Oops");
			alert.setContentText("Patient not found");
			alert.showAndWait();
			searchField.setText("");
		}else {
			treats.setItems(treatments);
			searchField.setText("");
		}
	}
}

public void searchByDiagnosis() {
	if(searchField.getText().isBlank()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
	}else {
		List<Treatments>allTreats=findAllTreatments();
		ObservableList<Treatments>treatments=FXCollections.observableArrayList();
		for(int i=0;i<allTreats.size();i++) {
			if(searchField.getText().equals(String.valueOf(allTreats.get(i).getDiag().getDiagId()))) {
				treatments.add(allTreats.get(i));
			}
		}
		if(treatments.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Oops");
			alert.setContentText("Diagnosis not found");
			alert.showAndWait();
			searchField.setText("");
		}else {
			treats.setItems(treatments);
			searchField.setText("");
		}
	}
}

public void searchByDoctor() {
	if(searchField.getText().isBlank()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
	}else {
		List<Treatments>allTreats=findAllTreatments();
		ObservableList<Treatments>treatments=FXCollections.observableArrayList();
		for(int i=0;i<allTreats.size();i++) {
			if(searchField.getText().equals(String.valueOf(allTreats.get(i).getDoctor().getDocId()))) {
				treatments.add(allTreats.get(i));
			}
		}
		if(treatments.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Oops");
			alert.setContentText("Doctor not found");
			alert.showAndWait();
			searchField.setText("");
		}else {
			treats.setItems(treatments);
			searchField.setText("");
		}
	}
}

}
