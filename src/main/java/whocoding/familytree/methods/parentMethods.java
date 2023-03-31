package whocoding.familytree.methods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import whocoding.familytree.classes.Person;
import whocoding.familytree.classes.Relationship;

import whocoding.familytree.gui.dialogs.ExceptionDialog;
import whocoding.familytree.gui.dialogs.MyAlert;
import whocoding.familytree.gui.dialogs.MyAlert.MyAlertType;

import whocoding.familytree.GenelogicalTreeTool;

/**
 * 
 * Klasse um die Methoden zur Festlegung/Bearbeitung der Eltern zu vereinfachen
 * 
 * @author MrWho97
 *
 */
public class parentMethods {

	// save the database connection
	private static Connection con = GenelogicalTreeTool.sql.getConnection();

	/**
	 * if child is in table
	 * 
	 * @param p_uuid
	 * @return
	 */
	public static boolean childIsInTable(String p_uuid) {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM children WHERE P_UUID = ?");
			ps.setString(1, p_uuid);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * 
	 * get relation_uuid where the relation contains the child
	 * 
	 * @param p_uuid
	 * @return
	 */
	public static String getRelationUUIDFromChild(String p_uuid) {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM children WHERE P_UUID = ?");
			ps.setString(1, p_uuid);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getString("R_UUID");
			} else {
				return null;
			}

		} catch (SQLException e) {
			return null;
		}
	}

	/**
	 * get parents of a person
	 * 
	 * @param p
	 * @return
	 */
	public static ArrayList<Person> getParents(Person p) {
		ArrayList<Person> temp = new ArrayList<Person>();
		if (childIsInTable(p.getUuid().toString())) {
			String r_uuid = getRelationUUIDFromChild(p.getUuid().toString());
			try {
				PreparedStatement ps1 = con.prepareStatement("SELECT * FROM relationship WHERE R_UUID = ?");
				ps1.setString(1, r_uuid);

				ResultSet rs = ps1.executeQuery();
				if (rs.next()) {
					if (rs.getString("partner1") == null || rs.getString("partner1").equals("NULL")) {
					} else {
						Person p1 = personMethods.getPerson(rs.getString("partner1"));
						temp.add(p1);
					}
					if (rs.getString("partner2") == null || rs.getString("partner2").equals("NULL")) {

					} else {
						Person p2 = personMethods.getPerson(rs.getString("partner2"));
						temp.add(p2);
					}
					return temp;
				} else {
					return temp;
				}

			} catch (SQLException e) {
				ExceptionDialog err = new ExceptionDialog("Datenbankfehler", "Achtung es ist ein Fehler aufgetreten!",
						"Eltern kontnen nicht ausgelesen werden", e);
				err.showAlert();
				e.printStackTrace();
				return null;
			}
		} else {
			return temp;
		}

	}

	/**
	 * set parent of a person when two parents exists
	 * 
	 * @param child
	 * @param parentOLD
	 * @param partner
	 * @param parentNEW
	 * @return
	 */
	public static boolean setParent(Person child, Person parentOLD, Person partner, Person parentNEW) {
		Relationship r = relationShipMethods.getRelationship(parentOLD, partner);
		if (relationShipMethods.removeChild(r.getUuid().toString(), child.getUuid().toString())) {
			if (relationShipMethods.relationExists(parentNEW, partner)) {
				Relationship r1 = relationShipMethods.getRelationship(parentNEW, partner);
				return relationShipMethods.addChild(r1.getUuid().toString(), child.getUuid().toString());
			} else {
				Relationship r1 = relationShipMethods.createRelationship(parentNEW, partner, null);
				if (r1 != null) {
					return relationShipMethods.addChild(r1.getUuid().toString(), child.getUuid().toString());
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	/**
	 * set first parent of a child when no parents exits
	 * 
	 * @param child
	 * @param parent
	 * @return
	 */
	public static boolean addFirstParentToChild(Person child, Person parent) {
		Relationship r1 = relationShipMethods.createRelationship(parent, null, null);

		return relationShipMethods.addChild(r1.getUuid().toString(), child.getUuid().toString());
	}

	/**
	 * addPatrent to the child
	 * 
	 * @param current
	 * @param parent01
	 * @param newParent
	 * @return
	 */
	public static boolean addParent(Person current, Person parent01, Person newParent) {
		if (relationShipMethods.relationExists(parent01, newParent)) {
			String r_uuid = getRelationUUIDFromChild(current.getUuid().toString());
			if (relationShipMethods.removeChild(r_uuid, current.getUuid().toString())) {
				if (relationShipMethods.delRelationship(r_uuid)) {
					Relationship r1 = relationShipMethods.getRelationship(parent01, newParent);
					return relationShipMethods.addChild(r1.getUuid().toString(), current.getUuid().toString());
				} else {
					MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
							"Konnte die Beziehung nicht entfernen!");
					err.showAlert();
					return false;
				}
			} else {
				MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
						"Konnte das Kind aus der Beziehung nicht entfernen!");
				err.showAlert();
				return false;
			}

		} else {
			String p_uuid = current.getUuid().toString();
			String r_uuid = getRelationUUIDFromChild(p_uuid);
			// System.err.println(p_uuid);
			if (relationShipMethods.removeChild(r_uuid, p_uuid)) {
				if (relationShipMethods.delRelationship(r_uuid)) {
					Relationship r1 = relationShipMethods.createRelationship(parent01, newParent, null);
					return relationShipMethods.addChild(r1.getUuid().toString(), p_uuid);
				} else {
					MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
							"Konnte die Beziehung nicht entfernen!");
					err.showAlert();
					return false;
				}
			} else {
				MyAlert err = new MyAlert(MyAlertType.Error, "Fehler!", "Achtung es ist ein Fehler aufgetreten!",
						"Konnte das Kind nicht aus der Beziehung entfernen!");
				err.showAlert();
				return false;
			}

		}
	}

	/**
	 * 
	 * Delete Person in children table
	 * 
	 * @param p_uuid
	 * @return true --> if person could be deleted
	 */
	public static boolean delChild(String p_uuid) {

		String sql = "DELETE FROM children WHERE P_UUID = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, p_uuid);

			int i = ps.executeUpdate();

			if (i > 0) {
				return true;
			} else {
				return true;
			}

		} catch (SQLException e) {
			ExceptionDialog exe = new ExceptionDialog("Datenbankfehler", "Achtung es ist ein Fehler aufgetreten!",
					"Das Kind konnte nicht gelöscht werden!");
			exe.showAlert();
			return false;
		}

	}

}
