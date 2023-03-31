package whocoding.familytree.gui.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import whocoding.familytree.classes.*;
import whocoding.familytree.gui.dialogs.MyAlert;
import whocoding.familytree.gui.dialogs.MyAlert.MyAlertType;
import whocoding.familytree.GenelogicalTreeTool;
import whocoding.familytree.methods.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * 
 * Regler für die Benutzeroberfläche zur Personenauswahl
 * 
 * @author MrWho97
 *
 */
@SuppressWarnings("restriction")
public class PersonChooserDialogController {

	// save the class the controller was called from
	private PersonSettableClass ctrlCalledFrom;

	// save the selected person
	private Person current;

	@FXML // fx:id="personView"
	private ListView<Person> personView; // Value injected by FXMLLoader

	@FXML // fx:id="firstNameSearch"
	private TextField firstNameSearch; // Value injected by FXMLLoader

	@FXML // fx:id="lastNameSearch"
	private TextField lastNameSearch; // Value injected by FXMLLoader

	public PersonChooserDialogController(PersonSettableClass ctrlCalledFrom) {
		this.ctrlCalledFrom = ctrlCalledFrom;
	}

	/**
	 * If user clicks on the choosePerson button
	 * 
	 * @param event
	 */
	@FXML
	void choosePerson(ActionEvent event) {
		if (current == null) {
			MyAlert err = new MyAlert(MyAlertType.Error, "Fehler", "Es ist ein Fehler aufgetreten!",
					"Es wurde keine Person ausgewählt");
			err.showAlert();
		} else {
			ctrlCalledFrom.setPerson(current);

			Stage s = (Stage) personView.getScene().getWindow();
			s.close();
		}
	}

	/**
	 * If user clicks on the searchPerson button
	 * 
	 * @param event
	 */
	@FXML
	void searchPerson(ActionEvent event) {

		ArrayList<Person> list = searchMethods.searchPerson(firstNameSearch.getText(), lastNameSearch.getText());

		personView.getItems().clear();

		personView.setItems(FXCollections.observableArrayList(list));
		setStuff();
	}

	/**
	 * set the items for the list view
	 */
	private void setItems() {
		ArrayList<Person> temp = new ArrayList<Person>();
		PreparedStatement ps;
		try {
			ps = GenelogicalTreeTool.sql.getConnection().prepareStatement("SELECT * FROM person");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Person p = personMethods.getPerson(rs.getString("P_UUID"));
				temp.add(p);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		personView.setItems(FXCollections.observableArrayList(temp));
	}

	/**
	 * setCellFactory and selectedItemProperty() for the listview
	 */
	private void setStuff() {
		personView.setCellFactory(new Callback<ListView<Person>, ListCell<Person>>() {
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

		personView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Person>() {

			public void changed(ObservableValue<? extends Person> observable, Person oldValue, Person newValue) {
				current = newValue;

			}
		});
	}

	/**
	 * initialize the controller
	 */
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert personView != null : "fx:id=\"personView\" was not injected: check your FXML file 'personChooser.fxml'.";
		assert firstNameSearch != null
				: "fx:id=\"firstNameSearch\" was not injected: check your FXML file 'personChooser.fxml'.";
		assert lastNameSearch != null
				: "fx:id=\"lastNameSearch\" was not injected: check your FXML file 'personChooser.fxml'.";
		setItems();
		setStuff();
	}

}