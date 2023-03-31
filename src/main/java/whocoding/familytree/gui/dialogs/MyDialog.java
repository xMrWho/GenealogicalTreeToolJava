package whocoding.familytree.gui.dialogs;

import java.util.Optional;

import javafx.scene.control.Dialog;

/**
 * 
 * Oberklasse für den MyChoiceDialog und MyTextInputDialog
 * 
 * vereinfacht die beiden Klassen
 * 
 * @author MrWho97
 *
 */
public class MyDialog {

	// save the title, header and content of the dialog
	private String title, header, content;

	// save the dialog
	private Dialog<?> dialog;

	/**
	 * 
	 * @param title
	 * @param header
	 * @param content
	 */
	public MyDialog(String title, String header, String content) {
		this.title = title;
		this.header = header;
		this.content = content;
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
	 * 
	 * @return the dialog
	 */
	public Dialog<?> getDialog() {
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(content);
		return dialog;
	}

	/**
	 * 
	 * @param dialog the dialog to set
	 */
	public void setDialog(Dialog<?> dialog) {
		this.dialog = dialog;
	}

	/**
	 * 
	 * @return the alert and wait for the response value.
	 */
	public Optional<?> getResult() {
		Optional<?> result = getDialog().showAndWait();
		return result;
	}

	/**
	 * Show the Dialog
	 */
	public void showDialog() {
		getDialog().show();
	}

}
