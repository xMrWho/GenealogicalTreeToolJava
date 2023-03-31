package whocoding.familytree.gui.dialogs;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * 
 * Der Dialog, der aufgerufen wird, wenn das Programm einen Ausnahmefall
 * feststellt
 * 
 * @author MrWho97
 *
 */
public class ExceptionDialog extends MyAlert {

	private Exception exception;

	/**
	 * creates an ExceptionDialog
	 * with an exception
	 * 
	 * @param title
	 * @param header
	 * @param content
	 * @param exception
	 */
	public ExceptionDialog(String title, String header, String content, Exception exception) {
		super(MyAlertType.Error, title, header, content);
		this.exception = exception;
	}

	/**
	 * creates an ExceptionDialog
	 * without an exception
	 * 
	 * @param title
	 * @param header
	 * @param content
	 */
	public ExceptionDialog(String title, String header, String content) {
		this(title, header, content, null);
	}

	/**
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(Exception exception) {
		this.exception = exception;
	}

	/**
	 * show the ExeptionDialog
	 */
	@Override
	public void showAlert() {
		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("Fehlermeldung: ");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);
		// Set expandable Exception into the dialog pane.
		super.getAlert().getDialogPane().setExpandableContent(expContent);

		super.showAlert();
	}

}
