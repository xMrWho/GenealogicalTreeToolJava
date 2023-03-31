package whocoding.familytree.gui.controller;

import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.*;
import whocoding.familytree.methods.*;
import whocoding.familytree.classes.*;

import whocoding.familytree.gui.dialogs.MyAlert;
import whocoding.familytree.gui.dialogs.MyAlert.MyAlertType;

/**
 * 
 * Der Regler für das Tool zum Anzeigen von Stammbäumen
 * 
 * @author MrWho97
 *
 */
public class treeToolController {

	// save the person that was selected and the choosen person in the treeView
	private Person p, choosen;

	@FXML // fx:id="treeView"
	private TreeView<Person> treeView; // Value injected by FXMLLoader

	@FXML // fx:id="showInfoBtn"
	private Button showInfoBtn; // Value injected by FXMLLoader

	@FXML // fx:id="editBtn"
	private Button editBtn; // Value injected by FXMLLoader

	@FXML // fx:id="infoDialog"
	private TextArea infoDialog; // Value injected by FXMLLoader

	/**
	 * init the controller for the treeToolController
	 * 
	 * @param p
	 */
	public treeToolController(Person p) {
		this.p = p;
	}

	/**
	 * if user click the choosePerson button
	 * 
	 * @param event
	 */
	@FXML
	void choosePersonEvent(ActionEvent event) {
		Stage stage1 = (Stage) editBtn.getScene().getWindow();
		stage1.close();

		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/mainView.fxml"));
		Parent root;

		try {
			root = loader.load();

			Scene scene = new Scene(root);

			stage.setScene(scene);

			stage.setTitle("STAMMBAUMTOOL");
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * if user clicks the close program menu
	 * 
	 * @param event
	 */
	@FXML
	private void closeProgramEvent(ActionEvent event) {
		Stage stage = (Stage) editBtn.getScene().getWindow();
		stage.close();
	}

	/**
	 * if user clicks the createPerson menu
	 * 
	 * @param event
	 */
	@FXML
	private void createPersonEvent(ActionEvent event) {
		boolean b = createPersonMethods.setName();
		if (b) {
			MyAlert err = new MyAlert(MyAlertType.Information, "Information", "Erfolg",
					"Die Person wurde erfolgreich erstellt!");
			err.showAlert();
		} else {
			MyAlert err = new MyAlert(MyAlertType.Error, "Fehler", "Achtung es ist ein Fehler aufgetreten!",
					"Person wurde nicht angelegt!");
			err.showAlert();
		}
	}

	/**
	 * if user clicks the editPerson button
	 * 
	 * @param event
	 */
	@FXML
	void edit(ActionEvent event) {
		MyAlert err = new MyAlert(MyAlertType.Error, "Fehler", "Achtung es ist ein Fehler aufgetreten!",
				"Person kann hier im späteren Verlauf des Projektes nochmal bearbeitet werden! \n"
						+ "Aus zeitlichen Gründen wurde diese Funktion erstmal ausgelassen!");
		err.showAlert();
	}

	/**
	 * if user clicks the showInfo button
	 * 
	 * @param event
	 */
	@FXML
	void showInfo(ActionEvent event) {
		String lastname = choosen.getName();

		String firstname = choosen.getFirstname();

		String birthday = choosen.getBirthday();

		String deathday = choosen.getDeathday();

		int age = choosen.calculateAge();

		String info = "Nachname: " + lastname + "\n"
				+ "Vorname: " + firstname + "\n"
				+ "\n";

		if (birthday == null || birthday.equals("NULL")) {
			info = info + "Geburtstag: Unbekannt" + "\n";
			if (deathday == null || deathday.equals("NULL")) {
				info = info + "Todestag: Unbekannt" + "\n";
				info = info + "\n"
						+ "Alter: Unbekannt \n";
			} else {
				info = info + "Todestag: " + deathday;
				info = info + "\n"
						+ "Alter: Unbekannt \n";
			}
		} else {
			info = info + "Geburtstag: " + birthday;
			if (deathday == null || deathday.equals("NULL")) {
				info = info + "Todestag: Unbekannt" + "\n";

				info = info + "\n"
						+ "Alter: " + age + "\n";
			} else {
				info = info + "Todestag: " + deathday;

				info = info + "\n"
						+ "Alter: " + age + "\n";
			}
		}

		String data = choosen.getData();
		if (data == null || data.equals("NULL")) {
			info = info + "\n"
					+ "Weitere Informationen: " + "\n"
					+ "Keine weiteren Informationen vorhanden";
		} else {
			info = info + "\n"
					+ "Weitere Informationen: " + "\n"
					+ data;
		}

		infoDialog.setText(info);
	}

	/**
	 * initialize the controller
	 */
	@FXML // This method is called by the FXMLLoader when initialization is complete
	public void initialize() {
		assert treeView != null : "fx:id=\"treeView\" was not injected: check your FXML file 'treeView.fxml'.";
		assert infoDialog != null : "fx:id=\"infoDialog\" was not injected: check your FXML file 'treeView.fxml'.";
		assert showInfoBtn != null : "fx:id=\"showInfoBtn\" was not injected: check your FXML file 'treeView.fxml'.";
		assert editBtn != null : "fx:id=\"editBtn\" was not injected: check your FXML file 'treeView.fxml'.";

		if (p == null) {
			MyAlert err = new MyAlert(MyAlertType.Error, "Fehler", "Keinen Erfolg!",
					"Die ausgewählte Person ist nicht gesetzt!");
			err.showAlert();
		} else {
			TreeItem<Person> rootNode = new TreeItem<Person>(p);

			rootNode.setExpanded(true);
			addParents(rootNode);

			treeView.setRoot(rootNode);
			treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Person>>() {
				public void changed(ObservableValue<? extends TreeItem<Person>> observable, TreeItem<Person> oldValue,
						TreeItem<Person> newValue) {
					choosen = newValue.getValue();
					editBtn.setDisable(false);
					showInfoBtn.setDisable(false);
				}
			});
		}

	}

	/**
	 * add parents to the rootNode
	 * 
	 * @param node
	 */
	private void addParents(TreeItem<Person> node) {
		for (Person pTemp : parentMethods.getParents(node.getValue())) {
			TreeItem<Person> temp = new TreeItem<Person>(pTemp);
			node.getChildren().add(temp);
			addParents(temp);

		}

	}

}