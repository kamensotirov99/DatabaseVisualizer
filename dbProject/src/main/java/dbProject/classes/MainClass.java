package dbProject.classes;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MainClass extends Application{

	public static void main(String[] args) {
		launch(args);

	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root=FXMLLoader.load(getClass().getResource("Starter_scene.fxml"));
		primaryStage.initStyle(StageStyle.UNIFIED);
		Scene scene=new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Hospital");
		primaryStage.setResizable(false);
		
		
		primaryStage.show();
	}
	public static Session getCurrentSessionfromConfig() {
		SessionFactory sessionFactory=new Configuration().configure().buildSessionFactory();
		Session session=sessionFactory.openSession();
		
		  return session;
	}
}
