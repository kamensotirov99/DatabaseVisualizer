package dbProject.classes;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.math.NumberUtils;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;





public class docSceneController implements Initializable{
	@FXML TableView<Doctors> docs;
	@FXML TableColumn<Doctors,Integer> docIdColumn;
	@FXML TableColumn<Doctors,String> docFnameColumn;
	@FXML TableColumn<Doctors,String> docLnameColumn;
	@FXML TableColumn<Doctors,String> docPhonenumColumn;
	@FXML TableColumn<Doctors,Integer> docSpecColumn;
	@FXML TableColumn<Doctors,Integer> docDeptColumn;
	@FXML TextField docFname;
	@FXML TextField docLname;
	@FXML TextField docPhoneNum;
	@FXML ChoiceBox<Integer> docSpec;
	@FXML ChoiceBox<Integer> docDept;
	@FXML TextField searchField;
	
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

@Override
public void initialize(URL arg0, ResourceBundle arg1) {
	docIdColumn.setCellValueFactory(new PropertyValueFactory<>("docId"));
	docFnameColumn.setCellValueFactory(new PropertyValueFactory<>("docFname"));
	docLnameColumn.setCellValueFactory(new PropertyValueFactory<>("docLname"));
	docPhonenumColumn.setCellValueFactory(new PropertyValueFactory<>("docPhonenum"));
	docSpecColumn.setCellValueFactory(new PropertyValueFactory<>("specialties"));
	docDeptColumn.setCellValueFactory(new PropertyValueFactory<>("departments"));
	docs.setItems(getDocs());
	initSpecs();
	initDepts();
	docs.setEditable(true);
	docFnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
	docLnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
	docPhonenumColumn.setCellFactory(TextFieldTableCell.forTableColumn());
	docSpecColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
	docDeptColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
}
	

public List<Doctors> findAllDoctors() {
	CriteriaBuilder cb = MainClass.getCurrentSessionfromConfig().getCriteriaBuilder();
    CriteriaQuery<Doctors> cq = cb.createQuery(Doctors.class);
    Root<Doctors> rootEntry = cq.from(Doctors.class);
    CriteriaQuery<Doctors> all = cq.select(rootEntry);
 
    TypedQuery<Doctors> allQuery = MainClass.getCurrentSessionfromConfig().createQuery(all);
    return allQuery.getResultList();    
}

public ObservableList<Doctors> getDocs(){
	ObservableList<Doctors>doctors=FXCollections.observableArrayList();
	List<Doctors>doc=findAllDoctors();
	for(int i=0;i<doc.size();i++) {
		doctors.add(doc.get(i));
	}
	return doctors;
}

public void deleteDoctor() {
	if(docs.getSelectionModel().isEmpty()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Delete doctor");
		alert.setContentText("No doctor selected");
		alert.showAndWait();
	}
	else {
	Session session1=MainClass.getCurrentSessionfromConfig();
	Doctors doctor=docs.getSelectionModel().getSelectedItem();
	doctor=session1.find(Doctors.class, doctor.getDocId());
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete Doctor?", ButtonType.YES, ButtonType.NO);
    ButtonType result = alert.showAndWait().orElse(ButtonType.YES);
    if (ButtonType.YES.equals(result)) {
       try {
		
		session1.getSession();
		session1.beginTransaction();
		session1.delete(doctor);
		session1.getTransaction().commit();
		session1.close();
		docs.setItems(getDocs());
		
		}catch(ConstraintViolationException e) {
			session1.getTransaction().rollback();
			System.out.println("error");
			//docs.getItems().add(doctor);
		}
    }
    }
	
}

public void createDoctor() {
	
	if(docFname.getText().isBlank()||docLname.getText().isBlank()||docPhoneNum.getText().isBlank()||docSpec.getSelectionModel().isEmpty()||docDept.getSelectionModel().isEmpty()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Empty field encountered");
		alert.showAndWait();
	}else if(docPhoneNum.getText().length()!=10) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Incorrect or incomplete phone number");
		alert.showAndWait();
	}else {
		boolean flag=false;
		List<Doctors>allDocs=findAllDoctors();
		for(int i=0;i<allDocs.size();i++) {
			if(allDocs.get(i).getDocPhonenum().equals(docPhoneNum.getText())) {
				flag=true;
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Error");
				alert.setContentText("Phone number must be unique");
				alert.showAndWait();
				docPhoneNum.setText("");
			}
		}
		if(flag==false) {
			Session session1=MainClass.getCurrentSessionfromConfig();
			Doctors newDoc=new Doctors();
			newDoc.setDocFname(docFname.getText().toString());
			newDoc.setDocLname(docLname.getText().toString());
			newDoc.setDocPhonenum(docPhoneNum.getText().toString());
			List<Specialties>allSpecs=findAllSpecs();
			for(int i=0;i<allSpecs.size();i++) {
				if(allSpecs.get(i).getSpecId()==docSpec.getValue()) {
					Specialties a=allSpecs.get(i);
					a=session1.find(Specialties.class,a.getSpecId());
					newDoc.setSpecialties(a);
					a.getDoctorses().add(newDoc);
				}
			}
			
			List<Departments>allDepts=findAllDepts();
			
			for(int i=0;i<allDepts.size();i++) {
				if(allDepts.get(i).getDeptId()==docDept.getValue()) {
					Departments b=allDepts.get(i);
					b=session1.find(Departments.class, allDepts.get(i).getDeptId());
					newDoc.setDepartments(b);
					b.addDoctor(newDoc);
				}
			}
			
			
			try {
				
				session1.getSession();
				session1.beginTransaction();
				session1.save(newDoc);
				session1.getTransaction().commit();
				session1.close();
				docs.getItems().add(newDoc);
				docFname.setText("");
				docLname.setText("");
				docPhoneNum.setText("");
				docSpec.setValue(null);
				docDept.setValue(null);
				
				}catch(ConstraintViolationException e) {
					session1.getTransaction().rollback();
					System.out.println("error");
					docs.getItems().remove(newDoc);
				}
		}
		
	}
	
}

public List<Specialties> findAllSpecs() {
	CriteriaBuilder cb = MainClass.getCurrentSessionfromConfig().getCriteriaBuilder();
    CriteriaQuery<Specialties> cq = cb.createQuery(Specialties.class);
    Root<Specialties> rootEntry = cq.from(Specialties.class);
    CriteriaQuery<Specialties> all = cq.select(rootEntry);
 
    TypedQuery<Specialties> allQuery = MainClass.getCurrentSessionfromConfig().createQuery(all);
    return allQuery.getResultList();    
}

public void initSpecs() {
	List<Specialties>spec=findAllSpecs();
	for(int i=0;i<spec.size();i++) {
		docSpec.getItems().add(spec.get(i).getSpecId());
	}
	
}

public List<Departments> findAllDepts() {
	CriteriaBuilder cb = MainClass.getCurrentSessionfromConfig().getCriteriaBuilder();
    CriteriaQuery<Departments> cq = cb.createQuery(Departments.class);
    Root<Departments> rootEntry = cq.from(Departments.class);
    CriteriaQuery<Departments> all = cq.select(rootEntry);
 
    TypedQuery<Departments> allQuery = MainClass.getCurrentSessionfromConfig().createQuery(all);
    return allQuery.getResultList();    
}

public void initDepts() {
	List<Departments>dept=findAllDepts();
	for(int i=0;i<dept.size();i++) {
		docDept.getItems().add(dept.get(i).getDeptId());
	}
}

public void numCheck(KeyEvent event) {
	if ((event.getCharacter().matches("[a-z]"))||(event.getCharacter().matches("[A-Z]"))||(event.getCharacter().matches("-"))||(event.getCharacter().matches("/")))
    {
        event.consume();
        docPhoneNum.backward();
        docPhoneNum.deleteNextChar();

    }
}

public void editDocFname(CellEditEvent editCell) {
	if(editCell.getNewValue().toString().isBlank()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
		docs.setItems(getDocs());
	}
	else {
		Session session=MainClass.getCurrentSessionfromConfig();
		Doctors doc=docs.getSelectionModel().getSelectedItem();
		doc=session.find(Doctors.class, doc.getDocId());
		doc.setDocFname(editCell.getNewValue().toString());
		session.getSessionFactory().openSession();
		session.getSession();
		session.beginTransaction();
		session.saveOrUpdate(doc);
		session.getTransaction().commit();
		session.close();
		docs.setItems(getDocs());
		
	}
}

public void editDocLname(CellEditEvent editCell) {
	if(editCell.getNewValue().toString().isBlank()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
		docs.setItems(getDocs());
	}
	else {
		Session session=MainClass.getCurrentSessionfromConfig();
		Doctors doc=docs.getSelectionModel().getSelectedItem();
		doc=session.find(Doctors.class, doc.getDocId());
		doc.setDocLname(editCell.getNewValue().toString());
		session.getSessionFactory().openSession();
		session.getSession();
		session.beginTransaction();
		session.saveOrUpdate(doc);
		session.getTransaction().commit();
		session.close();
		docs.setItems(getDocs());
		
	}
}

public void editDocPhoneNum(CellEditEvent editCell) {
	
	
	
	if(editCell.getNewValue().toString().isBlank()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
		docs.setItems(getDocs());
	}
	else  if(editCell.getNewValue().toString().length()!=10){
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Phone number must be 10 characters");
		alert.showAndWait();
		docs.setItems(getDocs());
		
	}
	else
	{
		boolean flag=false;
		List<Doctors>allDocs=findAllDoctors();
		for(int i=0;i<allDocs.size();i++) {
			if(allDocs.get(i).getDocPhonenum().equals(editCell.getNewValue().toString())) {
				flag=true;
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Error");
				alert.setContentText("Phone number must be unique");
				alert.showAndWait();
			}
			}
		if(flag==false) {
		Session session=MainClass.getCurrentSessionfromConfig();
		Doctors doc=docs.getSelectionModel().getSelectedItem();
		doc=session.find(Doctors.class, doc.getDocId());
		doc.setDocPhonenum(editCell.getNewValue().toString());
		session.getSessionFactory().openSession();
		session.getSession();
		session.beginTransaction();
		session.saveOrUpdate(doc);
		session.getTransaction().commit();
		session.close();
		docs.setItems(getDocs());
	}}
}

public void editDocSpec(CellEditEvent editCell) {
	if(editCell.getNewValue()==null) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
		docs.setItems(getDocs());
	}
	
	else {
		Session session=MainClass.getCurrentSessionfromConfig();
		Doctors doc=docs.getSelectionModel().getSelectedItem();
		doc=session.find(Doctors.class, doc.getDocId());
		List<Specialties>allSpecs=findAllSpecs();
		boolean flag=false;
		for(int i=0;i<allSpecs.size();i++) {
			if(allSpecs.get(i).getSpecId()==(int)editCell.getNewValue()) {
				Specialties a=allSpecs.get(i);
				a=session.find(Specialties.class,a.getSpecId());
				doc.getSpecs().getDoctorses().remove(doc);
				doc.setSpecialties(a);
				a.getDoctorses().add(doc);
				flag=true;
				session.getSessionFactory().openSession();
				session.getSession();
				session.beginTransaction();
				session.saveOrUpdate(doc);
				session.getTransaction().commit();
				session.close();
				docs.setItems(getDocs());
			}
		}
		if(flag==false) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setContentText("Invalid Id");
			alert.showAndWait();
			docs.setItems(getDocs());
		}
	}
}

public void test() {
	List<Specialties>allSpecs=findAllSpecs();

	for(int i=0;i<allSpecs.size();i++) {
		System.out.println(allSpecs.get(i).getSpecId());
		Iterator itr = allSpecs.get(i).getDoctorses().iterator();
		while (itr.hasNext()) {
            System.out.println(itr.next());
        }
	}
}

public void editDocDept(CellEditEvent editCell) {
	if(editCell.getNewValue()==null) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
		docs.setItems(getDocs());
	}
	
	else {
		Session session=MainClass.getCurrentSessionfromConfig();
		Doctors doc=docs.getSelectionModel().getSelectedItem();
		doc=session.find(Doctors.class, doc.getDocId());
		List<Departments>allDepts=findAllDepts();
		boolean flag=false;
		for(int i=0;i<allDepts.size();i++) {
			if(allDepts.get(i).getDeptId()==(int)editCell.getNewValue()) {
				Departments a=allDepts.get(i);
				a=session.find(Departments.class,a.getDeptId());
				doc.getDepts().getDoctorses().remove(doc);
				doc.setDepartments(a);
				a.getDoctorses().add(doc);
				flag=true;
				session.getSessionFactory().openSession();
				session.getSession();
				session.beginTransaction();
				session.saveOrUpdate(doc);
				session.getTransaction().commit();
				session.close();
				docs.setItems(getDocs());
			}
		}
		if(flag==false) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setContentText("Invalid Id");
			alert.showAndWait();
			docs.setItems(getDocs());
		}
	}
}

public void searchBySpec() {
	if(searchField.getText().isBlank()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
	}else {
		List<Doctors>allDocs=findAllDoctors();
		ObservableList<Doctors>doctors=FXCollections.observableArrayList();
		for(int i=0;i<allDocs.size();i++) {
			if(searchField.getText().equals(String.valueOf(allDocs.get(i).getSpecs().getSpecId()))) {
				doctors.add(allDocs.get(i));
			}
		}
		if(doctors.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Oops");
			alert.setContentText("No doctors were found");
			alert.showAndWait();
			searchField.setText("");
		}else {
			docs.setItems(doctors);
			searchField.setText("");
		}
	}
}

public void resetTable() {
	docs.setItems(getDocs());
}

public void searchByDept() {
	if(searchField.getText().isBlank()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
	}else {
		List<Doctors>allDocs=findAllDoctors();
		ObservableList<Doctors>doctors=FXCollections.observableArrayList();
		for(int i=0;i<allDocs.size();i++) {
			if(searchField.getText().equals(String.valueOf(allDocs.get(i).getDepts().getDeptId()))) {
				doctors.add(allDocs.get(i));
			}
		}
		if(doctors.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Oops");
			alert.setContentText("No doctors were found");
			alert.showAndWait();
			searchField.setText("");
		}else {
			docs.setItems(doctors);
			searchField.setText("");
		}
	}
}

}
