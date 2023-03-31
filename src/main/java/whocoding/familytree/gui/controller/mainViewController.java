package whocoding.familytree.gui.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import whocoding.familytree.classes.Person;
import whocoding.familytree.classes.PersonSettableClass;

import whocoding.familytree.gui.dialogs.ExceptionDialog;
import whocoding.familytree.gui.dialogs.MyAlert;
import whocoding.familytree.gui.dialogs.MyAlert.MyAlertType;

import whocoding.familytree.methods.createPersonMethods;
import whocoding.familytree.methods.personMethods;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import whocoding.familytree.GenelogicalTreeTool;

/**
 * 
 * Regler für den Hauptbildschirm / das erste Fenster der Anwendung!
 * 
 * @author MrWho97
 *
 */
public class mainViewController extends PersonSettableClass {

	/**
	 * set a person on a choice
	 * 
	 * @param p --> the person to set
	 */
	@Override
	public void setPerson(Person p) {
		super.setPerson(p);
		showPersonBtn.setDisable(false);
		delPersonBtn.setDisable(false);
		showTreeBtn.setDisable(false);
		choosenPersonText.setText(p.getFirstname() + " " + p.getName());

	}

	@FXML // fx:id="gripPane"
	private GridPane gripPane; // Value injected by FXMLLoader

	@FXML // fx:id="choosenPersonText"
	private TextArea choosenPersonText; // Value injected by FXMLLoader

	@FXML // fx:id="choosePersonBtn"
	private Button choosePersonBtn; // Value injected by FXMLLoader

	@FXML // fx:id="showTreeBtn"
	private Button showTreeBtn; // Value injected by FXMLLoader

	@FXML // fx:id="delPersonBtn"
	private Button delPersonBtn; // Value injected by FXMLLoader

	@FXML // fx:id="showPersonBtn"
	private Button showPersonBtn; // Value injected by FXMLLoader

	/**
	 * If user clicks on the choosePerson button
	 * 
	 * @param event
	 */
	@FXML
	void choosePerson(ActionEvent event) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/personChooser.fxml"));

		Parent parent;
		try {

			PersonChooserDialogController dialogController = new PersonChooserDialogController(this);
			fxmlLoader.setController(dialogController);

			parent = fxmlLoader.load();

			Scene scene = new Scene(parent);

			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Wählen Sie eine Person aus");

			stage.show();
		} catch (IOException e) {
			ExceptionDialog a = new ExceptionDialog("Fehler", "Achtung es ist ein Fehler aufgetreten!",
					"Das Tool zur Auswahl einer Person konnte nicht geladen werden!", e);
			a.showAlert();
		}

	}

	/**
	 * If user clicks on the createPerson button
	 * 
	 * @param event
	 */
	@FXML
	void createPerson(ActionEvent event) {
		boolean b = createPersonMethods.setName();
		if (b) {
			MyAlert e = new MyAlert(MyAlertType.Information, "Information", "Erfolg",
					"Die Person wurde erfolgreich erstellt!");
			e.showAlert();

			if (choosePersonBtn.isDisabled()) {
				choosePersonBtn.setTooltip(null);
				choosePersonBtn.setDisable(false);
			}
		} else {
			MyAlert e = new MyAlert(MyAlertType.Error, "Fehler", "Keinen Erfolg!", "Die Person wurde nicht angelegt!");
			e.showAlert();
		}
	}

	/**
	 * /**
	 * If user clicks on the delPerson button
	 * 
	 * @param event
	 */
	@FXML
	void delPerson(ActionEvent event) {
		Person p = super.getPerson();
		boolean b = personMethods.delPerson(p.getUuid().toString());
		if (b == true) {
			MyAlert e = new MyAlert(MyAlertType.Information, "Information", "Erfolg!",
					"Die Person wurde erfolgreich gelöscht!");
			e.showAlert();
			super.setPerson(null);
			showPersonBtn.setDisable(true);
			delPersonBtn.setDisable(true);
			showTreeBtn.setDisable(true);
			choosenPersonText.setText("");

			checkForPersons();
			return;
		} else {
			MyAlert e = new MyAlert(MyAlertType.Error, "Fehler", "Keinen Erfolg!",
					"Die Person wurde nicht erfolgreich gelöscht!");
			e.showAlert();
			return;
		}
	}

	/**
	 * /**
	 * If user clicks on the showFamilyTree button
	 * 
	 * @param event
	 */
	@FXML
	void showFamilyTree(ActionEvent event) {
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/treeView.fxml"));
		Parent root;

		try {
			treeToolController ctrl = new treeToolController(super.getPerson());
			loader.setController(ctrl);

			root = loader.load();

			Scene scene = new Scene(root);

			stage.setScene(scene);

			stage.setTitle("STAMMBAUMTOOL");

			Stage s = (Stage) delPersonBtn.getScene().getWindow();
			s.close();

			stage.show();
		} catch (IOException e) {
			ExceptionDialog exe = new ExceptionDialog("Fehler", "Achtung es ist ein Fehler aufgetreten!",
					"Das Tool zum Anzeigen von Stammbäumen konnte nicht geladen werden", e);
			exe.showAlert();
		}
	}

	/**
	 * If user clickson the showPerson button
	 * 
	 * @param event
	 */
	@FXML
	void showPerson(ActionEvent event) {
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/personViewer.fxml"));
		Parent parent;
		try {
			personViewerController ctrl = new personViewerController(this, super.getPerson());
			loader.setController(ctrl);
			parent = loader.load();

			Scene scene = new Scene(parent);

			stage.setScene(scene);

			stage.setTitle("Person bearbeiten");

			stage.show();
		} catch (IOException e) {
			ExceptionDialog exe = new ExceptionDialog("Fehler", "Achtung es ist ein Fehler aufgetreten!",
					"Das Programm konnte das Tool zum Bearbeiten einer Person nicht laden", e);
			exe.showAlert();
		}
	}

	private boolean checkForPersons() {
		Connection con = GenelogicalTreeTool.sql.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM person");
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return true;
			} else {
				choosePersonBtn.setDisable(true);

				Tooltip tipp = new Tooltip();
				tipp.setText("Es sind keine Personen in der Datenbank");
				choosePersonBtn.setTooltip(tipp);
				return false;
			}

		} catch (SQLException e) {
			ExceptionDialog exe = new ExceptionDialog("Datenbankfehler", "Achtung es ist ein Fehler aufgetreten!",
					"Personen konnten nicht aus der Datenbank ausgelesen werden!", e);
			exe.showAlert();
			return false;
		}
	}

	/**
	 * initialize the controller
	 */
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert gripPane != null : "fx:id=\"gripPane\" was not injected: check your FXML file 'mainView.fxml'.";
		assert choosenPersonText != null
				: "fx:id=\"choosenPersonText\" was not injected: check your FXML file 'mainView.fxml'.";
		assert choosePersonBtn != null
				: "fx:id=\"choosePersonBtn\" was not injected: check your FXML file 'mainView.fxml'.";
		assert showTreeBtn != null : "fx:id=\"showTreeBtn\" was not injected: check your FXML file 'mainView.fxml'.";
		assert delPersonBtn != null : "fx:id=\"delPersonBtn\" was not injected: check your FXML file 'mainView.fxml'.";
		assert showPersonBtn != null
				: "fx:id=\"showPersonBtn\" was not injected: check your FXML file 'mainView.fxml'.";

		checkForPersons();

	}

}
