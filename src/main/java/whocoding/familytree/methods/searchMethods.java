package whocoding.familytree.methods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import whocoding.familytree.classes.Person;
import whocoding.familytree.gui.dialogs.ExceptionDialog;
import whocoding.familytree.gui.dialogs.MyAlert;
import whocoding.familytree.gui.dialogs.MyAlert.MyAlertType;
import whocoding.familytree.GenelogicalTreeTool;

/**
 * Klasse um die Methode zur Suche einer Person zu vereinfachen
 * 
 * @author MrWho97
 *
 */
public class searchMethods {

	// save the database connection
	private static Connection con = GenelogicalTreeTool.sql.getConnection();

	/**
	 * Search a person with the given parameters
	 * 
	 * @param firstname --> firstname of a person
	 * @param lastname  --> lastname of a person
	 * @return
	 */
	@SuppressWarnings("null")
	public static ArrayList<Person> searchPerson(String firstname, String lastname) {
		ArrayList<Person> temp = new ArrayList<Person>();
		if ((firstname != null || !firstname.isEmpty()) && (lastname != null || lastname.isEmpty())) {
			String sql = "SELECT * FROM person WHERE firstname LIKE '%" + firstname + "%' AND name LIKE '%" + lastname
					+ "%'";
			try {
				PreparedStatement ps = con.prepareStatement(sql);

				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					Person p = personMethods.createPersonFromDatabase(rs.getString("P_UUID"), rs.getString("name"),
							rs.getString("firstname"),
							rs.getString("gender"), rs.getString("birthday"), rs.getString("deathday"),
							rs.getString("infos"));
					temp.add(p);
				}
				return temp;

			} catch (SQLException e) {
				ExceptionDialog exe = new ExceptionDialog("Fehler", "Achtung es ist ein Fehler aufgetreten!",
						"Datenbankfehler!", e);

				exe.showAlert();
				return null;
			}
		}
		if ((firstname == null || firstname.isEmpty()) && (lastname != null || !lastname.isEmpty())) {
			String sql = "SELECT * FROM person WHERE name LIKE '%" + lastname + "%'";
			try {
				PreparedStatement ps = con.prepareStatement(sql);

				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					Person p = personMethods.createPersonFromDatabase(rs.getString("P_UUID"), rs.getString("name"),
							rs.getString("firstname"),
							rs.getString("gender"), rs.getString("birthday"), rs.getString("deathday"),
							rs.getString("infos"));
					temp.add(p);
				}
				return temp;

			} catch (SQLException e) {
				ExceptionDialog exe = new ExceptionDialog("Fehler", "Achtung es ist ein Fehler aufgetreten!",
						"Datenbankfehler!", e);

				exe.showAlert();
				return null;
			}
		}
		if ((firstname != null || !firstname.isEmpty()) && (lastname == null || lastname.isEmpty())) {
			String sql = "SELECT * FROM person WHERE firstname LIKE '%" + firstname + "%'";
			try {
				PreparedStatement ps = con.prepareStatement(sql);

				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					Person p = personMethods.createPersonFromDatabase(rs.getString("P_UUID"), rs.getString("name"),
							rs.getString("firstname"),
							rs.getString("gender"), rs.getString("birthday"), rs.getString("deathday"),
							rs.getString("infos"));
					temp.add(p);
				}
				return temp;

			} catch (SQLException e) {
				ExceptionDialog exe = new ExceptionDialog("Fehler", "Achtung es ist ein Fehler aufgetreten!",
						"Datenbankfehler!", e);

				exe.showAlert();
				return null;
			}
		} else {
			MyAlert err = new MyAlert(MyAlertType.Error, "Fehler", "Achtung es ist ein Fehler aufgetreten!",
					"Fehler beim Lesen der Eingaben!");
			err.showAlert();
			return null;
		}
	}

}
