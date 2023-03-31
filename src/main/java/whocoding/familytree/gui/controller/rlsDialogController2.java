package whocoding.familytree.gui.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import whocoding.familytree.GenelogicalTreeTool;
import whocoding.familytree.methods.parentMethods;
import whocoding.familytree.methods.personMethods;
import whocoding.familytree.methods.relationShipMethods;

import whocoding.familytree.classes.*;
import whocoding.familytree.gui.dialogs.ExceptionDialog;
import whocoding.familytree.gui.dialogs.MyAlert;
import whocoding.familytree.gui.dialogs.MyAlert.MyAlertType;

/**
 * 
 * Regler für die zweite Oberfläche zum Editieren der Beziehungen einer Person
 * 
 * Hier kann eine vorher ausgewählte Beziehung bearbeitet werden
 * 
 * Dabei besteht die Möglichkeit Kinder zu der Beziehung hinzuzufügen
 * 
 * @author MrWho97
 *
 */
@SuppressWarnings("restriction")
public class rlsDialogController2 extends PersonSettableClass {

	@FXML // fx:id="removeChildBtn"
	private Button removeChildBtn; // Value injected by FXMLLoader

	@FXML // fx:id="editChildBtn"
	private Button editChildBtn; // Value injected by FXMLLoader

	@FXML // fx:id="addPartnerBtn"
	private Button addPartnerBtn; // Value injected by FXMLLoader

	@FXML // fx:id="partner2"
	private TextField partner2; // Value injected by FXMLLoader

	@FXML // fx:id="partner1"
	private TextField partner1; // Value injected by FXMLLoader

	@FXML // fx:id="children"
	private ListView<Person> children; // Value injected by FXMLLoader

	@FXML // fx:id="infos"
	private TextArea infos; // Value injected by FXMLLoader

	@FXML // fx:id="btnSaveInfos"
	private Button btnSaveInfos; // Value injected by FXMLLoader

	// save the current relationship
	private Relationship relation;

	// save the rlsDialogController1 for reloading the list
	private rlsDialogController1 ctrl1;

	// save the partners of the relationship and the selected child in the listview
	private Person partner01, partner02, choosenChild;

	// save the method that is selected from the event
	private String method = "";

	// save the database connection
	private static Connection con = GenelogicalTreeTool.sql.getConnection();

	/**
	 * Adds a child to the relationship
	 * 
	 * @param old_rel_uuid
	 * @param c_uuid
	 */
	private void addChildToRelation(String old_rel_uuid, String c_uuid) {

		String p_uuid1 = partner01.getUuid().toString();
		String p_uuid2 = partner02.getUuid().toString();
		if (p_uuid1.equals(c_uuid)) {
			MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
					"Die Person ist ein Partner der Beziehung!");
			err.showAlert();
			super.setPerson(null);
			return;
		}

		if (p_uuid2.equals(c_uuid)) {
			MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
					"Die Person ist ein Partner der Beziehung!");
			err.showAlert();
			super.setPerson(null);
			return;
		}

		ArrayList<Person> childern0 = relationShipMethods.getChildren(relation.getUuid().toString());
		for (Person pp : childern0) {
			if (getPerson().getUuid().equals(pp.getUuid())) {
				MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
						"Die Person ist bereits ein Kind der Beziehung!");
				err.showAlert();
				super.setPerson(null);
				return;
			}
		}

		ArrayList<Person> childern1 = personMethods.getDescendantsFromPerson(partner01.getUuid().toString());
		for (Person pp : childern1) {
			if (getPerson().getUuid().equals(pp.getUuid())) {
				MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
						"Die Person ist bereits ein Nachfahre von " + partner1.toString());
				err.showAlert();
				super.setPerson(null);
				return;
			}
		}

		ArrayList<Person> childern2 = personMethods.getDescendantsFromPerson(partner02.getUuid().toString());
		for (Person pp : childern2) {
			if (getPerson().getUuid().equals(pp.getUuid())) {
				MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
						"Die Person ist bereits ein Nachfahre von " + partner2.toString());
				err.showAlert();
				super.setPerson(null);
				return;
			}
		}

		if (old_rel_uuid != null) {
			if (relationShipMethods.removeChild(old_rel_uuid, c_uuid)) {
				if (relationShipMethods.delRelationship(old_rel_uuid)) {
					if (relationShipMethods.addChild(relation.getUuid().toString(), c_uuid)) {
						setItems();
						setStuff();
						MyAlert err = new MyAlert(MyAlertType.Information, "Information", "Erfolg!",
								"Das Kind konnte erfolgreich zur Beziehung hinzugefügt werden!");
						err.showAlert();
						return;

					} else {
						MyAlert err = new MyAlert(MyAlertType.Error, "Fehler", "Achtung es ist ein Fehler aufgetreten!",
								"Das Kind konnte nicht zur Beziehung hinzugefügt werden!");
						err.showAlert();
						return;
					}
				} else {
					MyAlert err = new MyAlert(MyAlertType.Error, "Fehler", "Achtung es ist ein Fehler aufgetreten!",
							"Die Beziehung konnte nicht entfernt werden!");
					err.showAlert();
					return;
				}
			} else {
				MyAlert err = new MyAlert(MyAlertType.Error, "Fehler", "Achtung es ist ein Fehler aufgetreten!",
						"Kind konnte nicht von der Beziehung entfernt werden!");
				err.showAlert();
				return;
			}
		} else {
			if (relationShipMethods.addChild(relation.getUuid().toString(), c_uuid)) {
				setItems();
				setStuff();
				MyAlert err = new MyAlert(MyAlertType.Information, "Information", "Erfolg!",
						"Das Kind konnte erfolgreich zur Beziehung hinzugefügt werden!");
				err.showAlert();
				return;

			} else {
				MyAlert err = new MyAlert(MyAlertType.Error, "Fehler", "Achtung es ist ein Fehler aufgetreten!",
						"Das Kind konnte nicht zur Beziehung hinzugefügt werden!");
				err.showAlert();
				return;
			}
		}
	}

	/**
	 * @param p --> the person to set
	 */
	@Override
	public void setPerson(Person p) {
		super.setPerson(p);
		String current_r_uuid = relation.getUuid().toString();

		if (method.equals("addChild")) {

			if (partner01 != null && partner02 != null) {
				HashMap<UUID, UUID> map = new HashMap<UUID, UUID>();
				try {
					PreparedStatement ps = con.prepareStatement("SELECT * FROM children");

					ResultSet rs = ps.executeQuery();

					while (rs.next()) {
						String r_uuid = rs.getString("R_UUID");
						String p_uuid = rs.getString("P_UUID");

						map.put(UUID.fromString(p_uuid), UUID.fromString(r_uuid));

					}

					if (map.containsKey(p.getUuid())) {
						UUID rel_uuid = map.get(p.getUuid());

						boolean test = relationShipMethods.hasOnePartner(rel_uuid.toString());
						Relationship rel = relationShipMethods.getRelationship(rel_uuid.toString());
						if (test) {
							if (rel.getPartner0() != null) {
								if (partner01.getUuid().equals(rel.getPartner0().getUuid())) {

									addChildToRelation(rel_uuid.toString(), p.getUuid().toString());
								} else if (partner02.getUuid().equals(rel.getPartner0().getUuid())) {
									addChildToRelation(rel_uuid.toString(), p.getUuid().toString());
								} else {
									MyAlert err = new MyAlert(MyAlertType.Error, "Fehler",
											"Achtung es ist ein Fehler aufgetreten!",
											"Die Person ist ein Kind von " + rel.toString());
									err.showAlert();

								}

							} else if (rel.getPartner1() != null) {
								if (partner01.getUuid().equals(rel.getPartner1().getUuid())) {

									addChildToRelation(rel_uuid.toString(), p.getUuid().toString());
								} else if (partner02.getUuid().equals(rel.getPartner1().getUuid())) {
									addChildToRelation(rel_uuid.toString(), p.getUuid().toString());
								} else {
									MyAlert err = new MyAlert(MyAlertType.Error, "Fehler",
											"Achtung es ist ein Fehler aufgetreten!",
											"Die Person ist ein Kind von " + rel.toString());
									err.showAlert();

								}
							} else {
								MyAlert err = new MyAlert(MyAlertType.Error, "Fehler",
										"Achtung es ist ein Fehler aufgetreten!",
										"Wie kann das sein? Beide Partner der Beziehung sind leer? \n Der Fehler kann durch den Systemadministrator behoben werden!");
								err.showAlert();
							}

						} else {
							MyAlert err = new MyAlert(MyAlertType.Error, "Fehler",
									"Achtung es ist ein Fehler aufgetreten!",
									p.toString() + " ist bereits ein Kind von "
											+ rel.toString());
							err.showAlert();
						}

					} else {
						addChildToRelation(null, p.getUuid().toString());
					}

				} catch (SQLException e) {
					ExceptionDialog exe = new ExceptionDialog("Datenbankfehler",
							"Achtung es ist ein Fehler aufgetreten!", "Konnte keine Kinder finden!", e);
					exe.showAlert();
					super.setPerson(null);
					return;
				}

			} else {

				MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
						"Um ein Kind hinzuzufügen müssen beide Partner gesetzt sein!");
				err.showAlert();
			}
		}
		if (method.equals("setPartner1")) {
			if (relationShipMethods.relationExists(partner02, getPerson())) {
				MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
						"Diese Beziehung gibt es bereits!");
				err.showAlert();
				super.setPerson(null);
				return;
			} else {
				ArrayList<Person> childern0 = personMethods.getDescendantsFromPerson(partner02.getUuid().toString());

				for (Person pp : childern0) {

					if (getPerson().getUuid().equals(pp.getUuid())) {
						MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!",
								"Achtung es ist ein Fehler aufgetreten!",
								"Ein Nachfahre der ausgewählten Person kann nicht sein eigener Vorfahre sein!");
						err.showAlert();
						super.setPerson(null);
						return;
					}
				}

				ArrayList<Person> children = new ArrayList<Person>();
				try {
					PreparedStatement ps = con.prepareStatement("SELECT * FROM children WHERE R_UUID = ?");
					ps.setString(1, current_r_uuid);

					ResultSet rs = ps.executeQuery();

					while (rs.next()) {
						Person tempPerson = personMethods.getPerson(rs.getString("P_UUID"));
						children.add(tempPerson);
						relationShipMethods.removeChild(current_r_uuid, tempPerson.getUuid().toString());
					}
				} catch (SQLException e) {
					ExceptionDialog err = new ExceptionDialog("Datenbankfehler",
							"Achtung es ist ein Fehler aufgetreten!", "Es konnten keine Kinder gefunden werden", e);
					err.showAlert();
					super.setPerson(null);
					return;
				}

				Relationship rTemp = relationShipMethods.createRelationship(partner02, getPerson(), null);
				for (Person person : children) {
					rTemp.addChild(person);

				}

				if (relationShipMethods.delRelationship(current_r_uuid)) {
					if (!rTemp.getChildren().isEmpty()) {
						for (Person child : rTemp.getChildren()) {
							relationShipMethods.addChild(rTemp.getUuid().toString(), child.getUuid().toString());
						}
					}

					relation = rTemp;

					partner01 = getPerson();
					partner1.setText(getPerson().toString());

					MyAlert err = new MyAlert(MyAlertType.Information, "Information", "Erfolg!",
							"Der Partner wurde erfolgreich gesetzt!");
					err.showAlert();

					if (ctrl1 != null) {
						ctrl1.setItems();
						ctrl1.setStuff();
					}

					super.setPerson(null);
					return;
				} else {
					MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
							"Der Partner konnte nicht gesetzt werden!");
					err.showAlert();
					super.setPerson(null);
					return;
				}

			}
		}
		if (method.equals("setPartner2")) {
			if (relationShipMethods.relationExists(partner01, getPerson())) {
				MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
						"Diese Beziehung gibt es bereits!");
				err.showAlert();
				super.setPerson(null);
				return;
			} else {

				ArrayList<Person> childern0 = personMethods.getDescendantsFromPerson(partner01.getUuid().toString());

				for (Person pp : childern0) {

					if (getPerson().getUuid().equals(pp.getUuid())) {
						MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!",
								"Achtung es ist ein Fehler aufgetreten!",
								"Ein Nachfahre der ausgewählten Person kann nicht sein eigener Vorfahre sein!");
						err.showAlert();
						super.setPerson(null);
						return;
					}
				}

				ArrayList<Person> children = new ArrayList<Person>();
				try {
					PreparedStatement ps = con.prepareStatement("SELECT * FROM children WHERE R_UUID = ?");
					ps.setString(1, current_r_uuid);

					ResultSet rs = ps.executeQuery();

					while (rs.next()) {
						Person tempPerson = personMethods.getPerson(rs.getString("P_UUID"));

						children.add(tempPerson);
						relationShipMethods.removeChild(current_r_uuid, tempPerson.getUuid().toString());
					}
				} catch (SQLException e) {
					ExceptionDialog err = new ExceptionDialog("Datenbankfehler",
							"Achtung es ist ein Fehler aufgetreten!", "Es konnten keine Kinder gefunden werden", e);
					err.showAlert();
					super.setPerson(null);
					return;
				}

				Relationship rTemp = relationShipMethods.createRelationship(partner01, getPerson(), null);
				for (Person person : children) {
					rTemp.addChild(person);
				}

				if (relationShipMethods.delRelationship(current_r_uuid)) {

					if (!rTemp.getChildren().isEmpty()) {
						for (Person child : rTemp.getChildren()) {
							relationShipMethods.addChild(rTemp.getUuid().toString(), child.getUuid().toString());
						}
					}

					relation = rTemp;
					partner02 = getPerson();
					partner2.setText(getPerson().toString());
					MyAlert err = new MyAlert(MyAlertType.Information, "Information", "Erfolg!",
							"Der Partner wurde erfolgreich gesetzt!");
					err.showAlert();

					if (ctrl1 != null) {
						ctrl1.setItems();
						ctrl1.setStuff();
					}

					super.setPerson(null);
					return;
				} else {
					MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
							"Der Partner konnte nicht gesetzt werden!");
					err.showAlert();
					super.setPerson(null);
					return;
				}

			}
		}
	}

	/**
	 * init the controller with the relation
	 * 
	 * @param relation
	 */
	public rlsDialogController2(rlsDialogController1 ctrl, Relationship relation) {
		this.relation = relation;
		this.ctrl1 = ctrl;
	}

	/**
	 * adds a child to the relationship
	 * 
	 * @param event
	 */
	@FXML
	void addChild(ActionEvent event) {
		method = "addChild";
		choosePerson();
		return;
	}

	/**
	 * if user clicks on the addPartner button
	 * 
	 * @param event
	 */
	@FXML
	void addPartner(ActionEvent event) {
		if (partner01 == null && partner02 == null) {
			MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
					"Wie sind Sie hierher gekommen? \n Dieser Fehler ist unmöglich, da mindestens ein Partner vorhanden sein muss!");
			err.showAlert();
			return;
		}
		if (partner01 == null && partner02 != null) {
			method = "setPartner1";
			choosePerson();
			return;
		}
		if (partner01 != null && partner02 == null) {
			method = "setPartner2";
			choosePerson();
			return;
		} else {
			MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
					"Wie sind Sie hierher gekommen? \n Dieser Fehler ist unmöglich, da mindestens ein Partner vorhanden sein muss!");
			err.showAlert();
			return;
		}
	}

	/**
	 * if user clicks the editChild button
	 * 
	 * @param event
	 */
	@FXML
	void editChild(ActionEvent event) {
		MyAlert info = new MyAlert(MyAlertType.Information, "Information", "Achtung!",
				"Um das Kind zu bearbeiten müssen Sie das Kind auswählen. \n Später kann es aber hier bearbeitet werden!");
		info.showAlert();
	}

	/**
	 * if user clicks the removeChild button
	 * 
	 * @param event
	 */
	@FXML
	void removeChild(ActionEvent event) {
		Person p = choosenChild;
		boolean bool = relationShipMethods.removeChild(relation.getUuid().toString(), p.getUuid().toString());

		if (bool) {
			MyAlert info = new MyAlert(MyAlertType.Information, "Information", "Erfolg!",
					"Das Kind wurde aus der Beziehung entfernt!");
			info.showAlert();
			setItems();
			setStuff();
			return;
		} else {
			MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
					"Das Kind konnte nicht aus der Beziehung entfernt werden!");
			err.showAlert();
			setItems();
			setStuff();
			return;
		}
	}

	/**
	 * if user clicks the saveInfos button
	 * 
	 * @param event
	 */
	@FXML
	void saveInfos(ActionEvent event) {
		if (infos.getText().isEmpty()) {
			MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
					"Es gibt keine neuen Informationen!");
			err.showAlert();
			return;
		} else {
			try {
				PreparedStatement ps = con.prepareStatement("UPDATE relationship SET infos = ? WHERE R_UUID = ?");

				ps.setString(1, infos.getText());
				ps.setString(2, relation.getUuid().toString());

				int i = ps.executeUpdate();
				if (i > 0) {
					MyAlert info = new MyAlert(MyAlertType.Information, "Information", "Erfolg!",
							"Neue Informationen konnten erfolgreich gespeichert werden!");
					info.showAlert();
					return;
				} else {
					MyAlert err = new MyAlert(MyAlertType.Error, "Datenbankfehler",
							"Achtung es ist ein Fehler aufgetreten!",
							"Informationen konnten nicht gespeichert werden!");
					err.showAlert();
					return;
				}
			} catch (SQLException e) {
				ExceptionDialog err = new ExceptionDialog("Datenbankfehler", "Achtung es ist ein Fehler aufgetreten!",
						"Informationen konnten nicht gespeichert werden!", e);
				err.showAlert();
				return;
			}
		}

	}

	/**
	 * setItems for the children listview
	 */
	private void setItems() {
		ArrayList<Person> temp = new ArrayList<Person>();

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM children WHERE R_UUID = ?");
			ps.setString(1, relation.getUuid().toString());

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Person p = personMethods.getPerson(rs.getString("P_UUID"));
				temp.add(p);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		children.setItems(FXCollections.observableArrayList(temp));
	}

	/**
	 * setCellFactory and selectedItemProperty() for the listview
	 */
	private void setStuff() {
		children.setCellFactory(new Callback<ListView<Person>, ListCell<Person>>() {
			public ListCell<Person> call(ListView<Person> p) {
				ListCell<Person> cell = new ListCell<Person>() {
					@Override
					protected void updateItem(Person t, boolean bln) {
						super.updateItem(t, bln);
						if (t != null) {
							setText(t.getFirstname() + " " + t.getName());
						}
					}
				};

				return cell;
			}
		});

		children.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Person>() {

			public void changed(ObservableValue<? extends Person> observable, Person oldValue, Person newValue) {
				// Your action here
				choosenChild = newValue;
				removeChildBtn.setDisable(false);
				editChildBtn.setDisable(false);
			}
		});
	}

	/**
	 * initialize the controller
	 */
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert removeChildBtn != null
				: "fx:id=\"removeChildBtn\" was not injected: check your FXML file 'relationShipDialog2.fxml'.";
		assert editChildBtn != null
				: "fx:id=\"editChildBtn\" was not injected: check your FXML file 'relationShipDialog2.fxml'.";
		assert addPartnerBtn != null
				: "fx:id=\"addPartnerBtn\" was not injected: check your FXML file 'relationShipDialog2.fxml'.";
		assert partner2 != null
				: "fx:id=\"partner2\" was not injected: check your FXML file 'relationShipDialog2.fxml'.";
		assert partner1 != null
				: "fx:id=\"partner1\" was not injected: check your FXML file 'relationShipDialog2.fxml'.";
		assert children != null
				: "fx:id=\"children\" was not injected: check your FXML file 'relationShipDialog2.fxml'.";
		assert infos != null : "fx:id=\"infos\" was not injected: check your FXML file 'relationShipDialog2.fxml'.";
		assert btnSaveInfos != null
				: "fx:id=\"btnSaveInfos\" was not injected: check your FXML file 'relationShipDialog2.fxml'.";

		if (relation.getPartner0() != null) {
			partner01 = relation.getPartner0();
			partner1.setText(partner01.toString());
		}
		if (relation.getPartner1() != null) {
			partner02 = relation.getPartner1();
			partner2.setText(partner02.toString());
		}
		if (relation.getPartner0() != null && relation.getPartner1() != null) {
			addPartnerBtn.setDisable(true);
		}
		if (relation.getInfos() == null || relation.getInfos().equals("NULL")) {
			infos.setText("");
		} else {
			String info = relation.getInfos();
			infos.setText(info);
		}

		setItems();
		setStuff();

	}

	/**
	 * choosePerson event
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

}