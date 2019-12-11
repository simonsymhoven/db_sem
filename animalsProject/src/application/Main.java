package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class Main extends Application {
	
	private static Scene scene;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			scene = new Scene(new FXMLLoader(Main.class.getResource("/fxml/mainGui.fxml")).load());
	        primaryStage.setResizable(false);
	        primaryStage.setTitle("Tier-Lexikon");
	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
