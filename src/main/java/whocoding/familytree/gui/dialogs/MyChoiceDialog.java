package whocoding.familytree.gui.dialogs;

import java.util.ArrayList;

import javafx.scene.control.ChoiceDialog;

/**
 * 
 * Klasse für den Auswahldialog
 * 
 * @author MrWho97
 *
 */
public class MyChoiceDialog extends MyDialog {

	/**
	 * 
	 * creates a new MyChoiceDialog object
	 * 
	 * @param title
	 * @param header
	 * @param content
	 * @param defaultChoice
	 * @param choices
	 */
	public MyChoiceDialog(String title, String header, String content, Object defaultChoice,
			ArrayList<Object> choices) {
		super(title, header, content);
		ChoiceDialog<?> dialog = new ChoiceDialog<Object>(defaultChoice, choices);
		super.setDialog(dialog);
	}

	/**
	 * 
	 * creates a new MyChoiceDialog object
	 * 
	 * @param header
	 * @param content
	 * @param defaultChoice
	 * @param choices
	 */
	public MyChoiceDialog(String header, String content, Object defaultChoice, ArrayList<Object> choices) {
		this("Auswahl Dialog", header, content, defaultChoice, choices);
	}

	/**
	 * 
	 * creates a new MyChoiceDialog object
	 * 
	 * @param content
	 * @param defaultChoice
	 * @param choices
	 */
	public MyChoiceDialog(String content, Object defaultChoice, ArrayList<Object> choices) {
		this("Auswahl Dialog", "Hier können Sie etwas auswählen", content, defaultChoice, choices);
	}

	/**
	 * 
	 * creates a new MyChoiceDialog object
	 * 
	 * @param defaultChoice
	 * @param choices
	 */
	public MyChoiceDialog(Object defaultChoice, ArrayList<Object> choices) {
		this("Auswahl Dialog", "Hier können Sie etwas auswählen", "Bitte treffen Sie eine Auswahl: ", defaultChoice,
				choices);
	}

}
