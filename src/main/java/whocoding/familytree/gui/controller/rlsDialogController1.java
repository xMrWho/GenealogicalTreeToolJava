package whocoding.familytree.gui.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

import whocoding.familytree.GenelogicalTreeTool;

import whocoding.familytree.methods.*;

import whocoding.familytree.gui.dialogs.*;
import whocoding.familytree.gui.dialogs.MyAlert.MyAlertType;
import whocoding.familytree.classes.*;

/**
 * 
 * Regler für die erste Oberfläche zum Editieren der Beziehungen einer Person
 * 
 * Hier können die Eltern einer Person gesetzt oder editiert werden
 * 
 * Weiterhin besteht hier die Möglichkeit neue Beziehungen zu erzeugen oder
 * bestehende Beziehungen zu bearbeiten
 * 
 * @author MrWho97
 *
 */
@SuppressWarnings("restriction")
public class rlsDialogController1 extends PersonSettableClass {

	// save the database connection
	private Connection con = GenelogicalTreeTool.sql.getConnection();

	// save the person that is choosen and the parents of the person
	private Person current, parent01, parent02;

	// save the relationship, that is selected
	private Relationship choosenRelation;

	/**
	 * init the controller with the relation
	 * 
	 * @param person
	 */
	public rlsDialogController1(Person person) {
		this.current = person;
	}

	@FXML // fx:id="delRelationBtn"
	private Button delRelationBtn; // Value injected by FXMLLoader

	@FXML // fx:id="editRelationBtn"
	private Button editRelationBtn; // Value injected by FXMLLoader

	@FXML // fx:id="addParentBtn"
	private Button addParentBtn; // Value injected by FXMLLoader

	@FXML // fx:id="parent2"
	private TextField parent2; // Value injected by FXMLLoader

	@FXML // fx:id="parent1"
	private TextField parent1; // Value injected by FXMLLoader

	@FXML // fx:id="choosenPerson"
	private TextField choosenPerson; // Value injected by FXMLLoader

	@FXML // fx:id="relationships"
	private ListView<Relationship> relationships; // Value injected by FXMLLoader

	// save the method that is selected from the event
	String method = "";

	/**
	 * @param p --> the person to set
	 */
	@Override
	public void setPerson(Person p) {
		super.setPerson(p);

		if (super.getPerson().getUuid().equals(current.getUuid())) {

			MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
					"Die neu ausgewählte Person kann nicht die aktuell angezeigte Person sein!");
			err.showAlert();
		} else {
			if (method.equals("editParent1")) {
				ArrayList<Person> childern = personMethods.getDescendantsFromPerson(current.getUuid().toString());

				for (Person pp : childern) {

					if (getPerson().getUuid().equals(pp.getUuid())) {
						MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!",
								"Achtung es ist ein Fehler aufgetreten!",
								"Der Elternteil konnte nicht korrekt gesetzt werden, da die Person ein Nachfahre der ausgewählten Person ist!");
						err.showAlert();
						super.setPerson(null);
						return;
					}
				}

				if (parentMethods.setParent(current, parent01, parent02, getPerson())) {
					MyAlert err = new MyAlert(MyAlertType.Information, "Information", "Erfolg!",
							"Der Elternteil konnte korrekt gesetzt werden!");
					err.showAlert();
					parent01 = getPerson();
					parent1.setText(parent01.toString());
					super.setPerson(null);
					return;
				} else {
					MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
							"Der Elternteil konnte nicht korrekt gesetzt werden!");
					err.showAlert();
					super.setPerson(null);
					return;
				}
			}
			if (method.equals("editParent2")) {

				ArrayList<Person> childern = personMethods.getDescendantsFromPerson(current.getUuid().toString());

				for (Person pp : childern) {

					if (getPerson().getUuid().equals(pp.getUuid())) {
						MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!",
								"Achtung es ist ein Fehler aufgetreten!",
								"Der Elternteil konnte nicht korrekt gesetzt werden, da die Person ein Nachfahre der ausgewählten Person ist!");
						err.showAlert();
						super.setPerson(null);
						return;
					}
				}

				if (parentMethods.setParent(current, parent02, parent01, getPerson())) {
					MyAlert err = new MyAlert(MyAlertType.Information, "Information", "Erfolg!",
							"Der Elternteil konnte korrekt gesetzt werden!");
					err.showAlert();
					parent02 = getPerson();
					parent2.setText(parent02.toString());
					super.setPerson(null);
					return;
				} else {
					MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
							"Der Elternteil konnte nicht korrekt gesetzt werden!");
					err.showAlert();
					super.setPerson(null);
					return;
				}

			}
			if (method.equals("parent1")) {

				ArrayList<Person> childern = personMethods.getDescendantsFromPerson(current.getUuid().toString());

				for (Person pp : childern) {

					if (getPerson().getUuid().equals(pp.getUuid())) {
						MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!",
								"Achtung es ist ein Fehler aufgetreten!",
								"Der Elternteil konnte nicht korrekt gesetzt werden, da die Person ein Nachfahre der ausgewählten Person ist!");
						err.showAlert();
						super.setPerson(null);
						return;
					}
				}

				boolean b = parentMethods.addParent(current, parent02, getPerson());
				if (b) {
					MyAlert err = new MyAlert(MyAlertType.Information, "Information", "Erfolg!",
							"Der Elternteil konnte korrekt gesetzt werden!");
					err.showAlert();
					parent01 = p;
					parent1.setText(parent01.toString());
					super.setPerson(null);
					return;
				} else {
					MyAlert dialog = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
							"Der Elternteil konnte nicht korrekt gesetzt werden!");
					dialog.showAlert();
					super.setPerson(null);
					return;
				}
			}
			if (method.equals("parent2")) {

				ArrayList<Person> childern = personMethods.getDescendantsFromPerson(current.getUuid().toString());

				for (Person pp : childern) {

					if (getPerson().getUuid().equals(pp.getUuid())) {
						MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!",
								"Achtung es ist ein Fehler aufgetreten!",
								"Der Elternteil konnte nicht korrekt gesetzt werden, da die Person ein Nachfahre der ausgewählten Person ist!");
						err.showAlert();
						super.setPerson(null);
						return;
					}
				}

				boolean b = parentMethods.addParent(current, parent01, getPerson());
				if (b) {
					MyAlert info = new MyAlert(MyAlertType.Information, "Information", "Erfolg!",
							"Der Elternteil konnte korrekt gesetzt werden!");
					info.showAlert();
					;
					parent02 = p;
					parent2.setText(parent01.toString());
					super.setPerson(null);
					return;
				} else {
					MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
							"Der Elternteil konnte nicht korrekt gesetzt werden!");
					err.showAlert();
					super.setPerson(null);
					return;
				}
			}
			if (method.equals("addParent")) {

				ArrayList<Person> childern = personMethods.getDescendantsFromPerson(current.getUuid().toString());

				for (Person pp : childern) {

					if (getPerson().getUuid().equals(pp.getUuid())) {
						MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!",
								"Achtung es ist ein Fehler aufgetreten!",
								"Der Elternteil konnte nicht korrekt gesetzt werden, da die Person ein Nachfahre der ausgewählten Person ist!");
						err.showAlert();
						super.setPerson(null);
						return;
					}
				}

				boolean b = parentMethods.addFirstParentToChild(current, getPerson());
				if (b) {
					MyAlert info = new MyAlert(MyAlertType.Information, "Information", "Erfolg!",
							"Der Elternteil konnte korrekt gesetzt werden!");
					info.showAlert();
					;
					parent01 = p;
					parent1.setText(parent01.toString());
					super.setPerson(null);
					return;
				} else {
					MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
							"Der Elternteil konnte nicht korrekt gesetzt werden!");
					err.showAlert();
					super.setPerson(null);
					return;
				}
			}

			if (method.equals("addRelationship")) {
				Person choosen = getPerson();
				if (relationShipMethods.relationExists(current, choosen)) {
					MyAlert dialog = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
							"Diese Beziehung gibt es schon!");
					dialog.showAlert();
					super.setPerson(null);
					return;
				} else {
					Relationship r = relationShipMethods.createRelationship(current, choosen, null);
					if (r != null) {
						setItems();
						setStuff();

						MyAlert info = new MyAlert(MyAlertType.Information, "Information", "Erfolg!",
								"Die Beziehung wurde erfolgreich erstellt!");
						info.showAlert();
						super.setPerson(null);
						return;
					} else {
						MyAlert err = new MyAlert(MyAlertType.Information, "Information", "Datenbankfehler!",
								"Beziehung konnte nicht erstellt werden!");
						err.showAlert();
						return;
					}
				}

			}
		}
	}

	/**
	 * if user clicks the addParent button
	 * 
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void addParent(ActionEvent event) {
		if (parent01 != null && parent02 != null) {
			List<Person> arr = Arrays.asList(parent01, parent02);
			ArrayList<Object> temp1 = new ArrayList<Object>();
			temp1.addAll(arr);

			MyChoiceDialog dia = new MyChoiceDialog("Elternteil bearbeiten",
					"Hier können Sie einen Elternteil auswählen",
					"Wählen Sie einen Elternteil aus", "Elternteil auswählen", temp1);

			Optional<Object> result = (Optional<Object>) dia.getResult();

			if (result.isPresent()) {
				Object rs = result.get();
				if (rs.equals("Elternteil auswählen")) {
					MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
							"Keine gültige Eingabe!");

					err.showAlert();
					dia.getDialog().close();
				}
				if (rs.equals(parent01)) {
					method = "editParent1";
					choosePerson();
					return;
				}
				if (rs.equals(parent02)) {
					method = "editParent2";
					choosePerson();
					return;
				}
			}
		} else if (parent01 != null && parent02 == null) {
			method = "parent2";
			choosePerson();
			return;
		} else if (parent01 == null && parent02 != null) {
			method = "parent1";
			choosePerson();
			return;
		} else if (parent01 == null && parent02 == null) {
			method = "addParent";
			choosePerson();
			return;
		}

	}

	/**
	 * choosePerson event if the programm needs a person
	 */
	private void choosePerson() {
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
			ExceptionDialog a = new ExceptionDialog("Fehler!", "Achtung es ist ein Fehler aufgetreten!",
					"Tool zur Auswahl einer Person konnte nicht geladen werden!", e);
			a.showAlert();
		}
	}

	/**
	 * if user clicks the addRelationship button
	 * 
	 * @param event
	 */
	@FXML
	void addRelationship(ActionEvent event) {
		method = "addRelationship";
		choosePerson();
		return;
	}

	/**
	 * if user clicks the delRelationship button
	 * 
	 * @param event
	 */
	@FXML
	void delRelationship(ActionEvent event) {
		String r_uuid = choosenRelation.getUuid().toString();
		if (relationShipMethods.removeChildren(r_uuid)) {
			if (relationShipMethods.delRelationship(r_uuid)) {
				MyAlert info = new MyAlert(MyAlertType.Information, "Information", "Erfolg!",
						"Die Beziehung wurde erfolgreich gelöscht!");
				info.showAlert();
				relationships.getItems().clear();

				setItems();
				setStuff();

			} else {
				MyAlert err = new MyAlert(MyAlertType.Error, "Datenbankfehler",
						"Achtung es ist ein Fehler aufgetreten!", "Beziehung konnte nicht gelöscht werden!");
				err.showAlert();
			}
		} else {
			MyAlert err = new MyAlert(MyAlertType.Error, "Datenbankfehler", "Achtung es ist ein Fehler aufgetreten!",
					"Beziehung konnte nicht gelöscht werden!");
			err.showAlert();
		}
	}

	/**
	 * if user clicks the editRelationship button
	 * 
	 * @param event
	 */
	@FXML
	void editRelationship(ActionEvent event) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/relationShipDialog2.fxml"));
		Parent parent;
		Stage stage = new Stage();

		try {
			rlsDialogController2 ctrl = new rlsDialogController2(this, choosenRelation);
			fxmlLoader.setController(ctrl);

			parent = fxmlLoader.load();

			Scene scene = new Scene(parent);
			stage.setScene(scene);

			stage.setTitle("Beziehung bearbeiten");
			stage.show();
		} catch (IOException e) {
			ExceptionDialog a = new ExceptionDialog("Fehler!", "Achtung es ist ein Fehler aufgetreten!",
					"Das Programm konnte das Tool zum Bearbeiten der Beziehung nicht laden", e);
			a.showAlert();
			e.printStackTrace();
		}
	}

	/**
	 * set the items for the list view
	 */
	public void setItems() {
		ArrayList<Relationship> temp = new ArrayList<Relationship>();
		try {
			PreparedStatement ps0 = con.prepareStatement("SELECT * FROM relationship WHERE partner1 = ?");
			ps0.setString(1, current.getUuid().toString());
			ResultSet rs0 = ps0.executeQuery();
			while (rs0.next()) {
				if (rs0.getString("R_UUID") == null || rs0.getString("R_UUID").equals("NULL")) {
					System.err.println("rs1.getString('R_UUID') == null || rs1.getString('R_UUID').equals('NULL')");
				} else {
					Relationship temp0 = relationShipMethods.getRelationship(rs0.getString("R_UUID"));
					if (!temp.contains(temp0)) {
						temp.add(temp0);
					}
				}

			}

		} catch (SQLException e) {
			ExceptionDialog exe = new ExceptionDialog("Datenbankfehler", "Achtung es ist ein Fehler aufgetreten",
					"Beziehungen nicht gefunden!", e);
			exe.showAlert();
		}
		try {
			PreparedStatement ps1 = con.prepareStatement("SELECT * FROM relationship WHERE partner2 = ?");
			ps1.setString(1, current.getUuid().toString());
			ResultSet rs1 = ps1.executeQuery();
			while (rs1.next()) {
				if (rs1.getString("R_UUID") == null || rs1.getString("R_UUID").equals("NULL")) {
					System.err.println("rs1.getString('R_UUID') == null || rs1.getString('R_UUID').equals('NULL')");
				} else {
					Relationship temp0 = relationShipMethods.getRelationship(rs1.getString("R_UUID"));
					if (!temp.contains(temp0)) {
						temp.add(temp0);
					}

				}
			}
		} catch (SQLException e) {
			ExceptionDialog exe = new ExceptionDialog("Datenbankfehler", "Achtung es ist ein Fehler aufgetreten",
					"Beziehungen nicht gefunden!", e);
			exe.showAlert();
		}
		relationships.setItems(FXCollections.observableArrayList(temp));
	}

	/**
	 * setCellFactory and selectedItemProperty() for relationships listview
	 */
	public void setStuff() {
		relationships.setCellFactory(new Callback<ListView<Relationship>, ListCell<Relationship>>() {
			public ListCell<Relationship> call(ListView<Relationship> p) {
				ListCell<Relationship> cell = new ListCell<Relationship>() {
					@Override
					protected void updateItem(Relationship t, boolean bln) {
						super.updateItem(t, bln);
						if (t != null) {
							setText(t.toString(current));
						} else {
							// System.err.println("t == null");
						}

					}
				};

				return cell;
			}
		});

		relationships.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Relationship>() {

			public void changed(ObservableValue<? extends Relationship> observable, Relationship oldValue,
					Relationship newValue) {
				// Your action here
				choosenRelation = newValue;
				editRelationBtn.setDisable(false);
				delRelationBtn.setDisable(false);
			}
		});
	}

	/**
	 * initialize the controller
	 */
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert delRelationBtn != null
				: "fx:id=\"delRelationBtn\" was not injected: check your FXML file 'relationShipDialog1.fxml'.";
		assert editRelationBtn != null
				: "fx:id=\"editRelationBtn\" was not injected: check your FXML file 'relationShipDialog1.fxml'.";
		assert addParentBtn != null
				: "fx:id=\"addParentBtn\" was not injected: check your FXML file 'relationShipDialog1.fxml'.";
		assert parent2 != null : "fx:id=\"parent2\" was not injected: check your FXML file 'relationShipDialog1.fxml'.";
		assert parent1 != null : "fx:id=\"parent1\" was not injected: check your FXML file 'relationShipDialog1.fxml'.";
		assert choosenPerson != null
				: "fx:id=\"choosenPerson\" was not injected: check your FXML file 'relationShipDialog1.fxml'.";
		assert relationships != null
				: "fx:id=\"relationships\" was not injected: check your FXML file 'relationShipDialog1.fxml'.";

		choosenPerson.setText(current.toString());
		choosenPerson.setEditable(false);

		for (Person parent : parentMethods.getParents(current)) {
			// System.err.println(parent);
			if (parent01 == null) {
				parent01 = parent;
				parent1.setText(parent01.toString());
			} else if (parent02 == null) {
				parent02 = parent;
				parent2.setText(parent02.toString());
			}
		}

		setItems();

		setStuff();

	}

}