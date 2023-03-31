package whocoding.familytree.gui.dialogs;

import java.util.Optional;

import whocoding.familytree.gui.dialogs.MyAlert.MyAlertType;
import javafx.scene.control.ButtonType;

/**
 * 
 * Der Dialog, der aufgerufen wird, wenn der Benutzer etwas abricht
 * 
 * @author MrWho97
 *
 */
public class CancelDialog {

	/**
	 * cancelMethod --> Cancel Dialog
	 * 
	 * @param title
	 * @param header
	 * @param msg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static boolean myCancelMethod(String title, String header, String msg) {

		MyAlert err = new MyAlert(MyAlertType.Confirmation, title, header, msg);
		Optional<ButtonType> result1 = (Optional<ButtonType>) err.getResult();
		if (result1.get() == ButtonType.OK) {
			// ... user chose OK
			err.getAlert().close();
			return true;
		} else {
			// ... user chose CANCEL or closed the dialog
			err.getAlert().close();
			return false;

		}
	}

	/**
	 * no params
	 * 
	 * @return
	 */
	public static boolean cancelMethod() {
		return myCancelMethod("Bestätigung", "Sie sind dabei diese Aktion abzubrechen!",
				"Sind Sie sich wirklich sicher?");
	}

	/**
	 * cancelMethod --> Cancel Dialog
	 * 
	 * @param msg
	 * @return
	 */
	public static boolean cancelMethod(String msg) {
		return myCancelMethod("Bestätigung", "Sie sind dabei diese Aktion abzubrechen!", msg);
	}

	/**
	 * cancelMethod --> Cancel Dialog
	 * 
	 * @param header
	 * @param msg
	 * @return
	 */
	public static boolean cancelMethod(String header, String msg) {
		return myCancelMethod("Bestätigung", header, msg);
	}

	/**
	 * cancelMethod --> Cancel Dialog
	 * 
	 * @param title
	 * @param header
	 * @param msg
	 * @return
	 */
	public static boolean cancelMethod(String title, String header, String msg) {
		return myCancelMethod(title, header, msg);
	}

}
