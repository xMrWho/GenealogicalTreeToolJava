package whocoding.familytree.gui.dialogs;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Klasse, um die Alertfenster zu vereinfachen
 * Die Alertfenster sind Fenster, die angezeigt werden wenn zum Beispiel ein
 * Fehler auftritt
 * 
 * @author MrWho97
 *
 */
public class MyAlert {
	/**
	 * 
	 * Die enums sind Variablentypen mit festgelegten Wertemöglichkeiten.
	 * Sie dienen der Lesbarkeit und der logischen Struktur des Codes
	 * 
	 * Mit diesen Aufzählung besteht die Möglichkeit vordefinierte Konstanten
	 * für die Variablen festzulegen
	 * 
	 * @enum Information --> Informationsdialog
	 * @enum Warning --> Warnungsdialog
	 * @enum Error --> Fehlerdialog
	 * @enum Confirmation --> Bestätigungsdialog
	 *
	 * @author MrWho97
	 *
	 */
	public enum MyAlertType {
		Information, Warning, Error, Confirmation;
	}

	// save the title, header and content of the alert
	private String title, header, content;

	// save the MyAlertType of the alert
	private MyAlertType type;

	// save the alert dialog
	private Alert alert;

	/**
	 * 
	 * @param type
	 * @param title
	 * @param header
	 * @param content
	 */
	public MyAlert(MyAlertType type, String title, String header, String content) {
		this.type = type;

		switch (type) {
			case Information: {
				this.alert = new Alert(AlertType.INFORMATION);
				if (title == null) {
					title = "Information";
				}
				if (header == null) {
					header = "Wir wollen Sie über folgende Tatsache Informieren:";
				}
				if (content == null) {
					content = "Ich habe eine tolle Nachricht für Sie!";
				}

				alert.setTitle(title);
				alert.setHeaderText(header);
				alert.setContentText(content);
				break;
			}

			case Warning: {
				this.alert = new Alert(AlertType.WARNING);
				if (title == null) {
					title = "Warnung";
				}
				if (header == null) {
					header = "Wir wollen Sie über folgende Tatsache warnen:";
				}
				if (content == null) {
					content = "Ich habe keine tolle Nachricht für Sie!";
				}

				alert.setTitle(title);
				alert.setHeaderText(header);
				alert.setContentText(content);
				break;
			}
			case Error: {
				this.alert = new Alert(AlertType.ERROR);
				if (title == null) {
					title = "Fehler";
				}
				if (header == null) {
					header = "Es ist ein Fehler aufgetreten!";
				}
				if (content == null) {
					content = "Fehler x030";
				}

				alert.setTitle(title);
				alert.setHeaderText(header);
				alert.setContentText(content);
				break;
			}
			case Confirmation: {
				this.alert = new Alert(AlertType.CONFIRMATION);
				if (title == null) {
					title = "Bestätigung";
				}
				if (header == null) {
					header = "Bitte bestätigen Sie ihre Tätigkeit!";
				}
				if (content == null) {
					content = "Möchten Sie dies wirklich machen?";
				}

				alert.setTitle(title);
				alert.setHeaderText(header);
				alert.setContentText(content);
				break;
			}

			default: {
				this.alert = new Alert(AlertType.NONE);
				if (title == null) {
					title = "Alert";
				}
				if (header == null) {
					header = "Hier wird ein Alert angezeigt!";
				}
				if (content == null) {
					content = "Das ist toll!";
				}

				alert.setTitle(title);
				alert.setHeaderText(header);
				alert.setContentText(content);
				break;
			}

		}

	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the type
	 */
	public MyAlertType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(MyAlertType type) {
		this.type = type;
	}

	/**
	 * @return the alert
	 */
	public Alert getAlert() {
		return alert;
	}

	/**
	 * @param alert the Alert to set
	 */
	public void setAlert(Alert alert) {
		this.alert = alert;
	}

	/**
	 * 
	 * @return the alert and wait for the response value.
	 */
	public Optional<?> getResult() {
		Optional<?> result = getAlert().showAndWait();
		return result;
	}

	/**
	 * Show the Alert
	 */
	public void showAlert() {
		getAlert().show();
	}

}
