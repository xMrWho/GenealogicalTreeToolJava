package whocoding.familytree;

/**
 * JavaFX App
 */

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import whocoding.familytree.classes.*;
import whocoding.familytree.gui.dialogs.ExceptionDialog;
import whocoding.familytree.gui.dialogs.MyAlert;
import whocoding.familytree.gui.dialogs.MyAlert.MyAlertType;
import whocoding.familytree.methods.*;
import whocoding.familytree.sql.*;
import javafx.application.*;
import javafx.stage.Stage;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * 
 * Hauptklasse, welche das Programm lädt
 * 
 * @author MrWho97
 * 
 */

public class GenelogicalTreeTool extends Application {

	public static MySQL sql;

	private static FXMLLoader loader;

	public static ArrayList<Person> persons = new ArrayList<Person>();

	public static boolean initSQL() {
		sql = new MySQL("localhost", "3306", "root", "");
		try {
			sql.openConnection();

			if (sql.isConnected()) {
				return DatabaseMethods.initDatabase();
			} else {
				return false;
			}

		} catch (ClassNotFoundException e) {
			ExceptionDialog exe = new ExceptionDialog("Fehler", "Achtung es ist ein Fehler aufgetreten!",
					"Die Treiberklasse wurde nicht gefunden!", e);

			exe.showAlert();
			return false;
		} catch (SQLException e) {
			ExceptionDialog exe = new ExceptionDialog("Fehler", "Achtung es ist ein Fehler aufgetreten!",
					"Datenbankfehler!", e);

			exe.showAlert();
			return false;
		}

	}

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage stage) {
		if (initSQL()) {
			personMethods.initPersonsFromDatabase();

			loader = new FXMLLoader(getClass().getResource("/mainView.fxml"));

			Parent root;

			try {
				root = loader.load();

				Scene scene = new Scene(root);

				stage.setScene(scene);

				stage.setTitle("STAMMBAUMTOOL");
				stage.show();
			} catch (IOException e) {
				ExceptionDialog exe = new ExceptionDialog("Fehler", "Achtung es ist ein Fehler aufgetreten!",
						"Der Hauptbildschirm konnte nicht geladen werden!", e);
				exe.showAlert();
			}

		} else {
			MyAlert dialog = new MyAlert(MyAlertType.Error, "Datenbankfehler", "Achtung es ist ein Fehler aufgetreten!",
					"Die Datenbank konnte nicht erreicht werden!");
			dialog.showAlert();
		}

	}

}
