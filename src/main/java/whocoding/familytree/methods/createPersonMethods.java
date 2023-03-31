package whocoding.familytree.methods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import whocoding.familytree.gui.dialogs.CancelDialog;
import whocoding.familytree.gui.dialogs.ExceptionDialog;
import whocoding.familytree.gui.dialogs.MyAlert;
import whocoding.familytree.gui.dialogs.MyAlert.MyAlertType;
import whocoding.familytree.gui.dialogs.MyChoiceDialog;
import whocoding.familytree.gui.dialogs.MyTextInputDialog;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import whocoding.familytree.GenelogicalTreeTool;

/**
 * 
 * Klasse um die Methoden zum Erstellen einer Person zu vereinfachen
 * 
 * @author MrWho97
 *
 */
public class createPersonMethods {

	// save the database connection
	private static Connection con = GenelogicalTreeTool.sql.getConnection();

	/*
	 * stores name, first name, gender, birthday, day of death and data of a person,
	 * which
	 * are set in the output of the dialogues
	 */
	private static String name, firstname, gender, birthday, deathday, data;

	// formatter for datepicker
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	/**
	 * set the name of a person
	 * 
	 * @return
	 */
	public static boolean setName() {
		MyTextInputDialog dialog = new MyTextInputDialog("Neue Person erstellen", "Geben Sie einen Nachnamen ein",
				"Nachname: ", "Nachname eintragen");

		String temp1 = dialog.getFromInputDialog((TextInputDialog) dialog.getDialog(), "Nachname eintragen");

		if (temp1 != null) {
			name = temp1;
			return setFirstname();
		} else {
			boolean temp = CancelDialog
					.cancelMethod("Möchten Sie das Erstellen der Person abbrechen? Dann klicken Sie Ok!");
			if (temp != true) {
				return setName();
			} else {
				return false;
			}

		}
	}

	/**
	 * set the firstname of a person
	 * 
	 * @return
	 */
	private static boolean setFirstname() {
		MyTextInputDialog dialog = new MyTextInputDialog("Neue Person erstellen", "Geben Sie einen Vornamen ein",
				"Vorname: ", "Vorname eintragen");

		String temp1 = dialog.getFromInputDialog((TextInputDialog) dialog.getDialog(), "Vorname eintragen");

		if (temp1 != null) {
			firstname = temp1;
			return setGender();
		} else {
			boolean temp = CancelDialog
					.cancelMethod("Möchten Sie das Erstellen der Person abbrechen? Dann klicken Sie Ok!");
			if (temp != true) {
				return setFirstname();
			} else {
				return false;
			}

		}
	}

	/**
	 * set the gender of a person
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static boolean setGender() {
		ArrayList<Object> choices = new ArrayList<Object>();
		choices.add("männlich");
		choices.add("weiblich");
		choices.add("divers");

		MyChoiceDialog dialog = new MyChoiceDialog("Neue Person erstellen", "Wählen Sie das Geschlecht aus",
				"Geschlecht: ", "Geschlecht", choices);

		// Traditional way to get the response value.
		Optional<Object> result = (Optional<Object>) dialog.getResult();
		if (result.isPresent()) {
			gender = (String) result.get();
			if (!gender.equals("Geschlecht")) {
				return setBirthday();
			} else {
				MyAlert dialog1 = new MyAlert(MyAlertType.Error, "Datenbankfehler",
						"Achtung es ist ein Fehler aufgetreten!", "Das Geschlecht kann nícht 'Geschlecht' sein!");
				Optional<ButtonType> result1 = (Optional<ButtonType>) dialog1.getResult();

				if (result1.get() == ButtonType.APPLY) {
					return setGender();
				} else {
					boolean temp1 = CancelDialog
							.cancelMethod("Möchten Sie das Erstellen der Person abbrechen? Dann klicken Sie Ok!");
					if (temp1 != true) {
						dialog1.getAlert().close();
						return setGender();
					} else {
						dialog1.getAlert().close();
						return false;
					}
				}
			}
		} else {
			boolean temp1 = CancelDialog.cancelMethod();
			if (temp1 != true) {
				dialog.getDialog().close();
				return setGender();
			} else {
				dialog.getDialog().close();
				return false;
			}
		}

	}

	/**
	 * set the birthday of a person
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static boolean setBirthday() {
		final Stage stage = new Stage();
		if (birthday == null) {
			birthday = "Leer";
		}
		MyAlert alert = new MyAlert(MyAlertType.Confirmation, "Person erstellen", "Geburtsdatum: " + birthday,
				"Wählen Sie eine Option aus.");

		ButtonType buttonTypeOne = new ButtonType("Geburtsdatum auswählen");
		ButtonType buttonTypeTwo = new ButtonType("Überspringen");
		ButtonType buttonTypeThree = new ButtonType("Ok", ButtonData.APPLY);
		ButtonType buttonTypeCancel = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);

		alert.getAlert().getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeCancel);

		Optional<ButtonType> result = (Optional<ButtonType>) alert.getResult();
		if (result.get() == buttonTypeOne) {

			final DatePicker datePicker = new DatePicker();
			datePicker.setValue(LocalDate.now());
			datePicker.setShowWeekNumbers(true);

			Button b = new Button("Ok");
			b.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent t) {
					birthday = datePicker.getValue().format(formatter);
					stage.close();
					return;
				}
			});

			Label l = new Label("Wählen Sie ein Datum!");

			FlowPane root = new FlowPane();
			root.setVgap(8);
			root.setHgap(4);

			root.getChildren().add(l);
			root.getChildren().add(datePicker);
			root.getChildren().add(b);

			root.setPadding(new Insets(10));

			stage.setTitle("Neue Person erstellen");
			Scene scene = new Scene(root, 300, 200);
			stage.setScene(scene);
			stage.showAndWait();
			return setBirthday();
		} else if (result.get() == buttonTypeTwo) {
			birthday = null;
			return setDeathday();
		} else if (result.get() == buttonTypeThree) {

			if (birthday != null && birthday != "Leer") {
				return setDeathday();
			} else {
				MyAlert dialog = new MyAlert(MyAlertType.Error, "Datenbankfehler",
						"Achtung es ist ein Fehler aufgetreten!", "Der Geburstag darf nicht 'Leer' sein!");
				Optional<ButtonType> result1 = (Optional<ButtonType>) dialog.getResult();

				if (result1.get() == ButtonType.APPLY) {
					return setBirthday();
				} else {
					boolean temp1 = CancelDialog
							.cancelMethod("Möchten Sie das Erstellen der Person abbrechen? Dann klicken Sie Ok!");
					if (temp1 != true) {
						stage.close();
						return setBirthday();
					} else {
						stage.close();
						return false;
					}
				}
			}

		} else {
			return false;
		}
	}

	/**
	 * set the deathday of a person
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static boolean setDeathday() {
		final Stage stage = new Stage();
		if (deathday == null) {
			deathday = "Leer";
		}
		final MyAlert alert = new MyAlert(MyAlertType.Confirmation, "Person erstellen", "Todesdatum: " + deathday,
				"Wählen Sie eine Option aus.");

		ButtonType buttonTypeOne = new ButtonType("Todesdatum auswählen");
		ButtonType buttonTypeTwo = new ButtonType("Überspringen");
		ButtonType buttonTypeThree = new ButtonType("Ok", ButtonData.APPLY);
		ButtonType buttonTypeCancel = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);

		alert.getAlert().getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeCancel);

		Optional<ButtonType> result = (Optional<ButtonType>) alert.getResult();
		if (result.get() == buttonTypeOne) {

			final DatePicker datePicker = new DatePicker();
			datePicker.setValue(LocalDate.now());
			datePicker.setShowWeekNumbers(true);

			Button b = new Button("Ok");
			b.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent t) {
					if (birthday != null) {
						LocalDate bday = LocalDate.parse(birthday, formatter);
						if (datePicker.getValue().isBefore(bday)) {
							MyAlert dialog = new MyAlert(MyAlertType.Error, "Datenbankfehler",
									"Achtung es ist ein Fehler aufgetreten!",
									"Der Todestag darf nicht vor dem Geburstag sein!");
							Optional<ButtonType> result1 = (Optional<ButtonType>) dialog.getResult();
							if (result1.get() == ButtonType.APPLY) {
								stage.close();
								setDeathday();
								return;
							} else {
								boolean temp1 = CancelDialog.cancelMethod(
										"Möchten Sie das Erstellen der Person abbrechen? Dann klicken Sie Ok!");
								if (temp1 != true) {
									stage.close();
									alert.getAlert().close();
									setDeathday();
									return;
								} else {
									stage.close();
									setDeathday();
									return;
								}
							}
						} else {
							deathday = datePicker.getValue().format(formatter);
							stage.close();
							// TODO: setDeathday();
							return;
						}

					} else {
						deathday = datePicker.getValue().format(formatter);
						stage.close();
						setDeathday();
						return;
					}
				}
			});

			Label l = new Label("Wählen Sie ein Datum!");

			FlowPane root = new FlowPane();
			root.setVgap(8);
			root.setHgap(4);

			root.getChildren().add(l);
			root.getChildren().add(datePicker);
			root.getChildren().add(b);

			root.setPadding(new Insets(10));

			stage.setTitle("Neue Person erstellen");
			Scene scene = new Scene(root, 300, 200);
			stage.setScene(scene);
			stage.showAndWait();
			return setDeathday();
		} else if (result.get() == buttonTypeTwo) {
			deathday = null;
			return setData();
		} else if (result.get() == buttonTypeThree) {

			if (deathday != null && deathday != "Leer") {
				return setData();
			} else {
				MyAlert dialog = new MyAlert(MyAlertType.Error, "Datenbankfehler",
						"Achtung es ist ein Fehler aufgetreten!", "Der Todestag darf nicht 'Leer' sein!");
				Optional<ButtonType> result1 = (Optional<ButtonType>) dialog.getResult();

				if (result1.get() == ButtonType.APPLY) {
					return setDeathday();
				} else {
					boolean temp1 = CancelDialog
							.cancelMethod("Möchten Sie das Erstellen der Person abbrechen? Dann klicken Sie Ok!");
					if (temp1 != true) {
						stage.close();
						return setDeathday();
					} else {
						stage.close();
						return false;
					}
				}
			}
		} else {
			return false;
		}
	}

	/**
	 * set the information for the person
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static boolean setData() {
		MyAlert alert = new MyAlert(MyAlertType.Information, "Person erstellen", "Informationen eintragen",
				"Hier können Sie Informationen zu der Person angeben.");

		ButtonType buttonTypeSkip = new ButtonType("Überspringen");
		ButtonType buttonTypeOK = new ButtonType("OK");
		ButtonType buttonTypeCancel = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);

		alert.getAlert().getButtonTypes().setAll(buttonTypeSkip, buttonTypeOK, buttonTypeCancel);

		Label label = new Label("Daten eingeben: ");

		TextArea textArea = new TextArea();
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		alert.getAlert().getDialogPane().setContent(textArea);

		Optional<ButtonType> result = (Optional<ButtonType>) alert.getResult();
		if (result.get() == buttonTypeSkip) {
			data = null;
			return finalDialog();
		} else if (result.get() == buttonTypeOK) {
			// ... user chose "Two"
			if (textArea.getText() == null || textArea.getText().isEmpty()) {
				boolean temp = CancelDialog
						.cancelMethod("Sie haben gar nichts eingegeben möchten Sie nochwas eingeben!");
				if (temp != true) {
					alert.getAlert().close();
					return setData();
				} else {
					data = null;
					alert.getAlert().close();
					return finalDialog();
				}
			} else {
				data = textArea.getText();
				return finalDialog();
			}

		} else {
			boolean temp = CancelDialog
					.cancelMethod("Möchten Sie das Erstellen der Person abbrechen? Dann klicken Sie Ok!");
			if (temp != true) {
				alert.getAlert().close();

				return setData();
			} else {
				alert.getAlert().close();
				return false;
			}
		}
	}

	/**
	 * confirm all information
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static boolean finalDialog() {
		String dialog = "Sie haben folgende Daten eingegeben: " + "\n" +
				"Name: " + name + "\n" +
				"Vorname: " + firstname + "\n" +
				"Geschlecht: " + gender + "\n";

		if (birthday != null) {
			dialog = dialog + "Geburtstag: " + birthday + "\n";
		}

		if (deathday != null) {
			dialog = dialog + "Todestag: " + deathday + "\n";
		}

		if (data != null) {
			dialog = dialog + "Mehr Informationen: " + data + "\n";
		}

		MyAlert alert = new MyAlert(MyAlertType.Information, "Achtung!",
				"Das Programm hat folgende Daten gesammelt - Bitte bestätigen Sie diese!", dialog);

		Optional<ButtonType> result = (Optional<ButtonType>) alert.getResult();

		if (result.get() == ButtonType.OK) {
			try {
				PreparedStatement ps = con.prepareStatement(
						"INSERT INTO person (P_UUID, name, firstname, gender, birthday, deathday, infos) VALUES (?, ?, ?, ? ,?, ?, ?)");
				ps.setString(1, UUID.randomUUID().toString());
				ps.setString(2, name);
				ps.setString(3, firstname);
				ps.setString(4, gender);
				ps.setString(5, birthday);
				ps.setString(6, deathday);
				if (data == null || data.isEmpty()) {
					ps.setString(7, "NULL");
				} else {
					ps.setString(7, data);
				}

				int i = ps.executeUpdate();

				if (i > 0) {
					MyAlert info = new MyAlert(MyAlertType.Information, "Person erstellt!", "Erfolg!",
							"Die Person wurde der Datenbank hinzugefügt!");
					info.showAlert();
					return true;

				} else {
					MyAlert dialog1 = new MyAlert(MyAlertType.Error, "Datenbankfehler",
							"Achtung es ist ein Fehler aufgetreten!", "Person konnte nicht eingetragen werden!");
					dialog1.showAlert();
					return false;
				}

			} catch (SQLException e) {
				ExceptionDialog exe = new ExceptionDialog("Datenbankfehler", "Achtung es ist ein Fehler aufgetreten!",
						"Person konnte nicht eingetragen werden!", e);
				exe.showAlert();
				return false;
			}

		}

		else {
			boolean temp = CancelDialog
					.cancelMethod("Möchten Sie das Erstellen der Person abbrechen? Dann klicken Sie Ok!");
			if (temp != true) {
				alert.getAlert().close();

				return finalDialog();
			} else {
				alert.getAlert().close();
				return false;
			}

		}

	}

}
