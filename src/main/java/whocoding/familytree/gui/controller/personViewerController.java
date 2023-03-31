package whocoding.familytree.gui.controller;

import whocoding.familytree.classes.Person;
import whocoding.familytree.classes.PersonSettableClass;
import whocoding.familytree.gui.dialogs.ExceptionDialog;
import whocoding.familytree.gui.dialogs.MyAlert;
import whocoding.familytree.gui.dialogs.MyAlert.MyAlertType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import whocoding.familytree.GenelogicalTreeTool;

/**
 * 
 * Regler für die Benutzeroberfläche zum Anzeigen/Editieren einer Person
 * 
 * Hier ist es möglich gespeicherte Informationen, wie den Namen, den Vornamen,
 * das Geschlecht und
 * die restlichen Informationen über eine Person abzuändern
 * 
 * @author MrWho97
 *
 */
@SuppressWarnings("restriction")
public class personViewerController {

	private Person person;

	// save the class the controller was called from
	private PersonSettableClass ctrlCalledFrom;

	// formatter for the datepicker
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@FXML // fx:id="pictures"
	private Button pictures; // Value injected by FXMLLoader

	@FXML // fx:id="firstname"
	private TextArea firstname; // Value injected by FXMLLoader

	@FXML // fx:id="genderChooser"
	private ChoiceBox<String> genderChooser; // Value injected by FXMLLoader

	@FXML // fx:id="lastname"
	private TextArea lastname; // Value injected by FXMLLoader

	@FXML // fx:id="birthday"
	private DatePicker birthday; // Value injected by FXMLLoader

	@FXML // fx:id="deathday"
	private DatePicker deathday; // Value injected by FXMLLoader

	@FXML // fx:id="infoField"
	private TextArea infoField; // Value injected by FXMLLoader

	// Save the database conenction
	private Connection con = GenelogicalTreeTool.sql.getConnection();

	/**
	 * constructor --> creates a new personViewerController object
	 * 
	 * @param ctrlCalledFrom
	 * @param person
	 */
	public personViewerController(PersonSettableClass ctrlCalledFrom, Person person) {
		this.ctrlCalledFrom = ctrlCalledFrom;
		this.person = person;
	}

	/**
	 * user clicks the editRelationship button
	 * 
	 * @param event
	 */
	@FXML
	void editRelationShips(ActionEvent event) {
		Stage stage = new Stage();

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/relationShipDialog1.fxml"));
		Parent root;
		try {
			rlsDialogController1 ctrl = new rlsDialogController1(person);
			loader.setController(ctrl);
			root = loader.load();

			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Beziehungen bearbeiten");

			stage.show();

		} catch (IOException e) {

			ExceptionDialog a = new ExceptionDialog("Fehler", "Achtung es ist ein Fehler aufgetreten!",
					"Das Tool zum Anzeigen der Bearbeitung der Beziehungen konnte nicht geladen werden!", e);
			a.showAlert();
		}

	}

	/**
	 * user clicks the finish workover button
	 * 
	 * @param event
	 */
	@FXML
	void finishWorkover(ActionEvent event) {
		String newFN = firstname.getText();
		String newLN = lastname.getText();
		String newGender = genderChooser.getSelectionModel().getSelectedItem();

		String newBDay, newDDay;
		if (birthday.getValue() == null) {
			newBDay = null;
		} else {
			newBDay = birthday.getValue().format(formatter);
		}

		if (deathday.getValue() == null) {
			newDDay = null;
		} else {
			newDDay = deathday.getValue().format(formatter);
		}

		String newInfos = infoField.getText();

		try {
			PreparedStatement ps = con.prepareStatement(
					"UPDATE person SET name = ?, firstname = ?, gender = ?, birthday = ?, deathday = ?, infos = ? WHERE P_UUID = ?");

			if (newLN == null || newLN.isEmpty()) {
				ps.setString(1, "NULL");
			} else {
				ps.setString(1, newLN);
			}

			if (newFN == null || newFN.isEmpty()) {
				ps.setString(2, "NULL");
			} else {
				ps.setString(2, newFN);
			}
			if (newGender == null || newGender.isEmpty()) {
				ps.setString(3, "NULL");
			} else {
				ps.setString(3, newGender);
			}

			if (newBDay == null || newBDay.isEmpty()) {
				ps.setString(4, "NULL");
			} else {
				ps.setString(4, newBDay);
			}
			if (newDDay == null || newDDay.isEmpty()) {
				ps.setString(5, "NULL");
			} else {
				ps.setString(5, newDDay);
			}

			if (newInfos == null || newInfos.isEmpty()) {
				ps.setString(6, "NULL");
			} else {
				ps.setString(6, newInfos);
			}

			ps.setString(7, person.getUuid().toString());

			int i = ps.executeUpdate();

			if (i > 0) {
				person.setName(newLN);
				person.setFirstname(newFN);
				person.setBirthday(newBDay);
				person.setDeathday(newDDay);
				person.setData(newInfos);

				ctrlCalledFrom.setPerson(person);
				MyAlert dialog = new MyAlert(MyAlertType.Information, "Information", "Erfolg!",
						"Die Person wurde bearbeitet und gespeichert!");
				dialog.showAlert();

				Stage s = (Stage) birthday.getScene().getWindow();
				s.close();
			} else {
				MyAlert dialog = new MyAlert(MyAlertType.Error, "Datenbankfehler", "Es ist ein Fehler aufgetreten",
						"Das Programm konnte die neuen Informationen über die Person nicht speichern!");
				dialog.showAlert();
			}

		} catch (SQLException e) {
			ExceptionDialog exe = new ExceptionDialog("Fehler", "Datenbankfehler!",
					"Das Programm konnte die neuen Informationen über die Person nicht speichern!", e);

			exe.showAlert();
		}
	}

	/**
	 * user clicks the show pictures button
	 * 
	 * @param event
	 */
	@FXML
	void showPictureExplorer(ActionEvent event) {
		MyAlert dialog = new MyAlert(MyAlertType.Information, "Information", "Nur so nebenbei: ",
				"Diese Funktion kommt noch!");
		dialog.showAlert();
	}

	/**
	 * initialize the controller
	 */
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {

		assert pictures != null : "fx:id=\"pictures\" was not injected: check your FXML file 'PersonViewer.fxml'.";
		assert firstname != null : "fx:id=\"firstname\" was not injected: check your FXML file 'PersonViewer.fxml'.";
		assert genderChooser != null
				: "fx:id=\"genderChooser\" was not injected: check your FXML file 'PersonViewer.fxml'.";
		assert lastname != null : "fx:id=\"lastname\" was not injected: check your FXML file 'PersonViewer.fxml'.";
		assert birthday != null : "fx:id=\"birthday\" was not injected: check your FXML file 'PersonViewer.fxml'.";
		assert deathday != null : "fx:id=\"deathday\" was not injected: check your FXML file 'PersonViewer.fxml'.";
		assert infoField != null : "fx:id=\"infoField\" was not injected: check your FXML file 'PersonViewer.fxml'.";

		ArrayList<String> choices = new ArrayList<String>();
		choices.add("männlich");
		choices.add("weiblich");
		choices.add("divers");

		genderChooser.getItems().addAll(choices);

		initFields();

	}

	private void initFields() {
		if (person != null) {
			firstname.setText(person.getFirstname());
			lastname.setText(person.getName());

			if (person.getGender() == null || person.getGender().equals("NULL")) {

			} else {
				genderChooser.getSelectionModel().select(person.getGender());
			}
			if (person.getDeathday() == null || person.getDeathday().equals("NULL")) {

			} else {
				deathday.setValue(LocalDate.parse(person.getDeathday(), formatter));
			}

			if (person.getBirthday() == null || person.getBirthday().equals("NULL")) {

			} else {
				birthday.setValue(LocalDate.parse(person.getBirthday(), formatter));
			}

			birthday.setShowWeekNumbers(true);
			deathday.setShowWeekNumbers(true);

			if (person.getData() == null || person.getData().equals("NULL")) {
				infoField.setText(" ");
			} else {
				infoField.setText(person.getData());
			}

		}
	}
}
