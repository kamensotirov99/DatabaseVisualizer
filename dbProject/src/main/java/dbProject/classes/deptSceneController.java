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

public class deptSceneController implements Initializable{
	@FXML TableView <Departments> depts;
	@FXML TableColumn<Departments,Integer> deptIdColumn;
	@FXML TableColumn<Departments,String> deptDescColumn;
	@FXML TextField deptDesc;

	
	
@Override
public void initialize(URL arg0, ResourceBundle arg1) {
	deptIdColumn.setCellValueFactory(new PropertyValueFactory<>("deptId"));
	deptDescColumn.setCellValueFactory(new PropertyValueFactory<>("deptDesc"));
	depts.setItems(getDepts());
	depts.setEditable(true);
	deptDescColumn.setCellFactory(TextFieldTableCell.forTableColumn());
}	


public List<Departments> findAllDepts() {
	CriteriaBuilder cb = MainClass.getCurrentSessionfromConfig().getCriteriaBuilder();
    CriteriaQuery<Departments> cq = cb.createQuery(Departments.class);
    Root<Departments> rootEntry = cq.from(Departments.class);
    CriteriaQuery<Departments> all = cq.select(rootEntry);
 
    TypedQuery<Departments> allQuery = MainClass.getCurrentSessionfromConfig().createQuery(all);
    return allQuery.getResultList();    
}


public ObservableList<Departments> getDepts(){
	ObservableList<Departments>departments=FXCollections.observableArrayList();
	List<Departments>dept=findAllDepts();
	for(int i=0;i<dept.size();i++) {
		departments.add(dept.get(i));
	}
	return departments;
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

public void createDepartment() {
	if(deptDesc.getText().isBlank()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Create department");
		alert.setContentText("Text field is empty");
		alert.showAndWait();
	}else {
		boolean flag=false;
		List<Departments>allDepts=findAllDepts();
		for(int i=0;i<allDepts.size();i++) {
			if(allDepts.get(i).getDeptDesc().equals(deptDesc.getText())) {
				flag=true;
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Error");
				alert.setContentText("Department already exists");
				alert.showAndWait();
				deptDesc.setText("");
			}
		}
		
		if(flag==false) {
			Session session1=MainClass.getCurrentSessionfromConfig();
			Departments newDept=new Departments();
			newDept.setDeptDesc(deptDesc.getText().toString());
			try {
				
				session1.getSession();
				session1.beginTransaction();
				session1.save(newDept);
				session1.getTransaction().commit();
				session1.close();
				depts.getItems().add(newDept);
				deptDesc.setText("");
				
				}catch(ConstraintViolationException e) {
					session1.getTransaction().rollback();
					System.out.println("error");
					depts.getItems().remove(newDept);
				}
		}
	}
}

public void deleteDept() {
	if(depts.getSelectionModel().isEmpty()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Delete department");
		alert.setContentText("No department selected");
		alert.showAndWait();
	}else {
		Session session1=MainClass.getCurrentSessionfromConfig();
		Departments department=depts.getSelectionModel().getSelectedItem();
		department=session1.find(Departments.class, department.getDeptId());
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete Department?", ButtonType.YES, ButtonType.NO);
	    ButtonType result = alert.showAndWait().orElse(ButtonType.YES);
	    if (ButtonType.YES.equals(result)) {
	    	if(department.getDoctorses().isEmpty()) {
	       try {
			
			session1.getSession();
			session1.beginTransaction();
			session1.delete(department);
			session1.getTransaction().commit();
			session1.close();
			depts.setItems(getDepts());
			
			}catch(ConstraintViolationException e) {
				session1.getTransaction().rollback();
				System.out.println("error");
			
			}
	    }else {
			Alert alert1 = new Alert(AlertType.WARNING);
			alert1.setTitle("Delete department");
			alert1.setContentText("There must be no doctors in this dept. in order to delete it");
			alert1.showAndWait();
		}
	    }
	}
}

public void editDeptDesc(CellEditEvent editCell) {
	if(editCell.getNewValue().toString().isBlank()) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setContentText("Field is empty");
		alert.showAndWait();
		depts.setItems(getDepts());
	}
	else {
	List<Departments>allDepts=findAllDepts();
	boolean flag=false;
	for(int i=0;i<allDepts.size();i++) {
		if(allDepts.get(i).getDeptDesc().equals(editCell.getNewValue().toString())) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setContentText("Department already exists");
			alert.showAndWait();
			depts.setItems(getDepts());
			flag=true;
		}
	}
	
	if(flag==false) {
		Session session=MainClass.getCurrentSessionfromConfig();
		Departments dept=depts.getSelectionModel().getSelectedItem();
		dept=session.find(Departments.class, dept.getDeptId());
		dept.setDeptDesc(editCell.getNewValue().toString());
		session.getSessionFactory().openSession();
		session.getSession();
		session.beginTransaction();
		session.saveOrUpdate(dept);
		session.getTransaction().commit();
		session.close();
		depts.setItems(getDepts());
	}
}}


}
