package whocoding.familytree.gui.dialogs;

import java.util.Optional;

import whocoding.familytree.gui.dialogs.MyAlert.MyAlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

/**
 * 
 * Klasse für den Texteingabedialog
 * 
 * @author MrWho97
 *
 */
public class MyTextInputDialog extends MyDialog {
	/**
	 * 
	 * @param title
	 * @param header
	 * @param content
	 * @param defaultValue
	 */
	public MyTextInputDialog(String title, String header, String content, String defaultValue) {
		super(title, header, content);
		TextInputDialog temp = new TextInputDialog(defaultValue);
		super.setDialog(temp);

	}

	/**
	 * 
	 * @param header
	 * @param content
	 * @param defaultValue
	 */
	public MyTextInputDialog(String header, String content, String defaultValue) {
		this("Eingabe-Dialog", header, content, defaultValue);
	}

	/**
	 * 
	 * @param content
	 * @param defaultValue
	 */
	public MyTextInputDialog(String content, String defaultValue) {
		this("Eingabe-Dialog", "Hier wird eine Eingabe erwartet", content, defaultValue);
	}

	/**
	 * 
	 * @param defaultValue
	 */
	public MyTextInputDialog(String defaultValue) {
		this("Eingabe-Dialog", "Hier wird eine Eingabe erwartet", "Eingabe: ", defaultValue);
	}

	/**
	 * 
	 */
	public MyTextInputDialog() {
		this("Eingabe-Dialog", "Hier wird eine Eingabe erwartet", "Eingabe: ", "");
	}

	/**
	 * 
	 * @param dialog       --> textinputdialog
	 * @param defaultInput --> default input
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getFromInputDialog(TextInputDialog dialog, String defaultInput) {
		String temp = "";
		Optional<String> result = (Optional<String>) super.getResult();
		if (result.isPresent()) {
			temp = result.get();

			if (temp.equals(defaultInput)) {
				MyAlert err = new MyAlert(MyAlertType.Error, "Fehler", "Achtung es ist ein Fehler aufgetreten!",
						"Der eingegebene Wert darf nicht '" + defaultInput + "' sein!");
				Optional<ButtonType> result1 = (Optional<ButtonType>) err.getResult();
				if (result1.get() == ButtonType.APPLY) {
					dialog.close();
					return getFromInputDialog(dialog, defaultInput);
				} else {
					boolean temp1 = CancelDialog.cancelMethod();
					if (temp1 != true) {
						dialog.close();
						return getFromInputDialog(dialog, defaultInput);
					} else {
						dialog.close();
						return null;
					}
				}
			} else {
				return temp;
			}
		} else {
			return null;
		}

	}

}
