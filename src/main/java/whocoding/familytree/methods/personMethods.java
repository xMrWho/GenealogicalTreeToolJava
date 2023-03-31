package whocoding.familytree.methods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import whocoding.familytree.classes.Person;
import whocoding.familytree.classes.Relationship;
import whocoding.familytree.gui.dialogs.ExceptionDialog;
import whocoding.familytree.gui.dialogs.MyAlert;
import whocoding.familytree.gui.dialogs.MyAlert.MyAlertType;
import whocoding.familytree.GenelogicalTreeTool;

/**
 * 
 * Klasse um die Methoden der Personenbeabreitung o. -löschung zu vereinfachen
 * 
 * @author MrWho97
 *
 */
public class personMethods {

	// save the database connection
	private static Connection con = GenelogicalTreeTool.sql.getConnection();

	/**
	 * 
	 * Creates a Person Object from the Person that is stored in the database
	 * 
	 * @param p_uuid    --> Universally Unique Identifier of the person
	 * @param name      --> Lastname
	 * @param firstname --> Firstname
	 * @param gender    --> Gender
	 * @param birthday  --> Birthday
	 * @param deathday  --> Deathday
	 * @param infos     --> Informations about the person
	 * @return
	 */
	public static Person createPersonFromDatabase(String p_uuid, String name, String firstname, String gender,
			String birthday, String deathday, String infos) {
		Person p = new Person();
		p.setUuid(UUID.fromString(p_uuid));
		p.setName(name);
		p.setFirstname(firstname);
		p.setGender(gender);
		p.setBirthday(birthday);
		p.setDeathday(deathday);
		int age = p.calculateAge();
		p.setAge(age);
		p.setData(infos);

		return p;
	}

	/**
	 * Deletes a person from the database
	 * 
	 * @param p_uuid --> the Universally Unique Identifier of the person
	 * @return
	 */
	public static boolean delPerson(String p_uuid) {

		if (parentMethods.delChild(p_uuid)) {

			ArrayList<Relationship> rs_list = relationShipMethods.getRelationships(personMethods.getPerson(p_uuid));

			for (Relationship rel : rs_list) {
				String r_uuid = rel.getUuid().toString();
				/*
				 * relation only has one partner --> the only partner must be the person
				 * relationship and children of it must be deleted
				 */
				if (relationShipMethods.hasOnePartner(r_uuid)) {
					if (relationShipMethods.removeChildren(r_uuid)) {
						if (relationShipMethods.delRelationship(r_uuid)) {
							continue;
						} else {
							return false;
						}
					} else {
						return false;
					}
				}
				/*
				 * update the relationship
				 */
				else {
					String partner1 = rel.getPartner0().getUuid().toString();
					String partner2 = rel.getPartner1().getUuid().toString();
					String temp = "";

					if (partner1.equals(p_uuid)) {
						temp = "partner1";
					}

					else if (partner2.equals(p_uuid)) {
						temp = "partner2";
					}

					try {
						PreparedStatement ps0 = con
								.prepareStatement("UPDATE relationship SET " + temp + " = ? WHERE R_UUID = ?");
						ps0.setString(1, null);
						ps0.setString(2, r_uuid);

						int i = ps0.executeUpdate();

						if (i > 0) {
							continue;
						} else {
							return false;
						}

					} catch (SQLException e) {
						ExceptionDialog exe = new ExceptionDialog("Datenbankfehler",
								"Achtung es ist ein Fehler aufgetreten!", "Beziehung konnte nicht geändert werden!", e);
						exe.showAlert();
						return false;

					}

				}
			}
			try {
				PreparedStatement ps1 = con.prepareStatement("DELETE FROM person WHERE P_UUID = ?");
				ps1.setString(1, p_uuid);

				if (ps1.executeUpdate() > 0) {
					return true;
				} else {
					return false;
				}

			} catch (SQLException e) {
				ExceptionDialog exe = new ExceptionDialog("Datenbankfehler", "Achtung es ist ein Fehler aufgetreten!",
						"Person konnte nicht gelöscht werden!", e);
				exe.showAlert();
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Get a person from the database
	 * 
	 * @param p_uuid --> the Universally Unique Identifier of the person
	 * @return
	 */
	public static Person getPerson(String p_uuid) {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM person WHERE P_UUID = ?");

			ps.setString(1, p_uuid);
			ResultSet rs_person = ps.executeQuery();

			if (rs_person.next()) {
				Person person = personMethods.createPersonFromDatabase(rs_person.getString("P_UUID"),
						rs_person.getString("name"), rs_person.getString("firstname"),
						rs_person.getString("gender"), rs_person.getString("birthday"), rs_person.getString("deathday"),
						rs_person.getString("infos"));

				if (!GenelogicalTreeTool.persons.contains(person)) {
					GenelogicalTreeTool.persons.add(person);
				}

				return person;

			} else {
				MyAlert err = new MyAlert(MyAlertType.Error, "Fehler", "Achtung es ist ein Fehler aufgetreten!",
						"Die Person konnte nicht aus der Datenbank gelesen werden!");
				err.showAlert();
				return null;
			}
		} catch (SQLException e) {
			ExceptionDialog exe = new ExceptionDialog("Fehler", "Achtung es ist ein Fehler aufgetreten!",
					"Datenbankfehler!", e);

			exe.showAlert();
			return null;
		}

	}

	/**
	 * Init the stored person in the database as objects
	 */
	public static void initPersonsFromDatabase() {

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM person");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Person p = personMethods.createPersonFromDatabase(rs.getString("P_UUID"), rs.getString("name"),
						rs.getString("firstname"),
						rs.getString("gender"), rs.getString("birthday"), rs.getString("deathday"),
						rs.getString("infos"));
				GenelogicalTreeTool.persons.add(p);
			}
		} catch (SQLException e1) {
			ExceptionDialog exe = new ExceptionDialog("Fehler", "Datenbank Fehler!",
					"Es konnten keine Personen initalisiert werden!", e1);
			exe.showAlert();
		}
	}

	/**
	 * Gets all Descendants of a person
	 * 
	 * @param p_uuid --> person uuid
	 * @return
	 */
	public static ArrayList<Person> getDescendantsFromPerson(String p_uuid) {
		ArrayList<Person> temp = new ArrayList<Person>();

		Person current = getPerson(p_uuid);

		ArrayList<Relationship> relations = relationShipMethods.getRelationships(current);

		for (Relationship r : relations) {
			ArrayList<Person> children = relationShipMethods.getChildren(r.getUuid().toString());
			for (Person child : children) {
				temp.addAll(getDescendantsFromPerson(child.getUuid().toString()));
				temp.add(child);
			}
		}

		return temp;
	}
}
