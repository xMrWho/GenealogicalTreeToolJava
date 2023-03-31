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
import whocoding.familytree.GenelogicalTreeTool;

/**
 * 
 * Klasse um die Methoden zur Beabrbeitung oder Löschung der Beziehungen einer
 * Person zu vereinfachen
 * 
 * @author MrWho97
 *
 */
public class relationShipMethods {

	// saves the database connection
	private static Connection con = GenelogicalTreeTool.sql.getConnection();

	/**
	 * creates a relationship between two persons with given information
	 * 
	 * @param partner1
	 * @param partner2
	 * @param infos
	 * @return
	 */
	public static Relationship createRelationship(Person partner1, Person partner2, String infos) {
		String sql = "";
		UUID uuid = UUID.randomUUID();

		if (infos == null) {
			infos = "NULL";
		}

		try {
			PreparedStatement ps;
			if (partner2 == null) {
				Relationship r1 = new Relationship(partner1);
				r1.setInfos(infos);
				r1.setUuid(uuid);
				sql = "INSERT INTO relationship (R_UUID, partner1, infos) VALUES (?, ?, ?)";
				ps = con.prepareStatement(sql);

				// System.err.println(r1.toString() + "\n Partner 2 = NULL");

				ps.setString(1, uuid.toString());
				ps.setString(2, partner1.getUuid().toString());

				ps.setString(3, infos);
				if (ps.executeUpdate() > 0) {
					return r1;
				} else {
					return null;
				}
			} else {
				sql = "INSERT INTO relationship (R_UUID, partner1, partner2, infos) VALUES (?, ?, ?, ?)";
				Relationship r2 = new Relationship(partner1, partner2);
				r2.setInfos(infos);
				r2.setUuid(uuid);
				ps = con.prepareStatement(sql);

				ps.setString(1, uuid.toString());
				ps.setString(2, partner1.getUuid().toString());

				ps.setString(3, partner2.getUuid().toString());
				ps.setString(4, infos);

				// System.err.println(r2.toString());
				if (ps.executeUpdate() > 0) {
					return r2;
				} else {
					return null;
				}

			}

		} catch (SQLException e) {
			ExceptionDialog err = new ExceptionDialog("Datenbankfehler", "Achtung es ist ein Fehler aufgetreten!",
					"Beziehung konnte nicht erstellt werden!", e);
			err.showAlert();
			return null;
		}

	}

	/**
	 * creates a relationship with one person where one of the partners is an
	 * unknown person with information
	 * 
	 * @param partner1
	 * @param infos
	 * @return
	 */
	public static Relationship createRelationship(Person partner1, String infos) {
		return createRelationship(partner1, null, infos);
	}

	/**
	 * get the relationship between two partner
	 * 
	 * @param partner01
	 * @param partner02
	 * @return
	 */
	public static Relationship getRelationship(Person partner01, Person partner02) {
		try {
			PreparedStatement ps0 = con
					.prepareStatement("SELECT * FROM relationship WHERE partner1 = ? AND partner2 = ?");
			ps0.setString(1, partner01.getUuid().toString());
			ps0.setString(2, partner02.getUuid().toString());

			ResultSet rs0 = ps0.executeQuery();
			if (rs0.next()) {
				String r_uuid = rs0.getString("R_UUID");
				String p1_uuid = rs0.getString("partner1");
				String p2_uuid = rs0.getString("partner2");

				String infos = rs0.getString("infos");

				Person partner1 = personMethods.getPerson(p1_uuid);
				Person partner2 = personMethods.getPerson(p2_uuid);

				Relationship rInDB = new Relationship(partner1, partner2);
				rInDB.setInfos(infos);
				rInDB.setUuid(UUID.fromString(r_uuid));

				if (!partner1.getRelationships().contains(rInDB)) {
					partner1.getRelationships().add(rInDB);
				}
				if (!partner2.getRelationships().contains(rInDB)) {
					partner2.getRelationships().add(rInDB);
				}
				return rInDB;

			} else {
				PreparedStatement ps1 = con
						.prepareStatement("SELECT * FROM relationship WHERE partner1 = ? AND partner2 = ?");
				ps1.setString(1, partner02.getUuid().toString());
				ps1.setString(2, partner01.getUuid().toString());
				ResultSet rs1 = ps1.executeQuery();
				if (rs1.next()) {
					String r_uuid = rs1.getString("R_UUID");
					String p1_uuid = rs1.getString("partner1");
					String p2_uuid = rs1.getString("partner2");

					String infos = rs1.getString("infos");

					Person partner1 = personMethods.getPerson(p1_uuid);
					Person partner2 = personMethods.getPerson(p2_uuid);

					Relationship rInDB = new Relationship(partner1, partner2);
					rInDB.setInfos(infos);
					rInDB.setUuid(UUID.fromString(r_uuid));

					if (!partner1.getRelationships().contains(rInDB)) {
						partner1.getRelationships().add(rInDB);
					}
					if (!partner2.getRelationships().contains(rInDB)) {
						partner2.getRelationships().add(rInDB);
					}
					return rInDB;

				} else {
					return null;
				}
			}
		} catch (SQLException e) {
			ExceptionDialog error = new ExceptionDialog("Datenbankfehler", "Achtung es ist ein Fehler aufgetreten!",
					"Beziehung konnte nicht ausgelesen werden", e);
			error.showAlert();
			return null;
		}

	}

	/**
	 * get the relationship with the r_uuid
	 * 
	 * @param r_uuid
	 * @return
	 */
	public static Relationship getRelationship(String r_uuid) {
		try {
			PreparedStatement ps0 = con.prepareStatement("SELECT * FROM relationship WHERE R_UUID = ?");
			ps0.setString(1, r_uuid);
			ResultSet rs1 = ps0.executeQuery();
			if (rs1.next()) {
				String p1_uuid = rs1.getString("partner1");
				String p2_uuid = rs1.getString("partner2");
				String infos = rs1.getString("infos");
				Relationship rInDB;
				if (p1_uuid == null || p1_uuid.equals("NULL")) {
					if (p2_uuid == null || p2_uuid.equals("NULL")) {
						return null;
					} else {
						Person p = personMethods.getPerson(p2_uuid);
						rInDB = new Relationship(p);
						rInDB.setUuid(UUID.fromString(r_uuid));
						rInDB.setInfos(infos);
						if (!p.getRelationships().contains(rInDB)) {
							p.getRelationships().add(rInDB);
						}
						return rInDB;
					}
				}

				else {
					if (p2_uuid == null || p2_uuid.equals("NULL")) {
						Person p = personMethods.getPerson(p1_uuid);
						rInDB = new Relationship(p);
						rInDB.setUuid(UUID.fromString(r_uuid));
						rInDB.setInfos(infos);
						if (!p.getRelationships().contains(rInDB)) {
							p.getRelationships().add(rInDB);
						}
						return rInDB;
					} else {
						Person p1 = personMethods.getPerson(p1_uuid);
						Person p2 = personMethods.getPerson(p2_uuid);
						rInDB = new Relationship(p1, p2);
						rInDB.setUuid(UUID.fromString(r_uuid));
						rInDB.setInfos(infos);
						if (!p1.getRelationships().contains(rInDB)) {
							p1.getRelationships().add(rInDB);
						}
						if (!p2.getRelationships().contains(rInDB)) {
							p2.getRelationships().add(rInDB);
						}
						return rInDB;
					}
				}
			} else {
				return null;
			}
		} catch (SQLException e) {
			ExceptionDialog err = new ExceptionDialog("Datenbankfehler", "Achtung es ist ein Fehler aufgetreten!",
					"Beziehung konnte nicht ausgelesen werden", e);
			err.showAlert();
			return null;
		}
	}

	/**
	 * get all relationships of a person
	 * 
	 * @param p
	 * @return
	 */
	public static ArrayList<Relationship> getRelationships(Person p) {
		ArrayList<Relationship> temp = new ArrayList<Relationship>();
		try {
			PreparedStatement ps0 = con.prepareStatement("SELECT R_UUID FROM relationship WHERE partner1 = ?");
			ps0.setString(1, p.getUuid().toString());

			ResultSet rs0 = ps0.executeQuery();

			while (rs0.next()) {
				Relationship r = getRelationship(rs0.getString("R_UUID"));
				temp.add(r);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			PreparedStatement ps0 = con.prepareStatement("SELECT R_UUID FROM relationship WHERE partner2 = ?");
			ps0.setString(1, p.getUuid().toString());

			ResultSet rs0 = ps0.executeQuery();

			while (rs0.next()) {
				Relationship r = getRelationship(rs0.getString("R_UUID"));
				temp.add(r);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return temp;
	}

	/**
	 * is a relation exits with the r_uuid
	 * 
	 * @param R_UUID
	 * @return
	 */
	public static boolean relationExists(String R_UUID) {
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("SELECT * FROM relationship WHERE R_UUID = ?");
			ps.setString(1, R_UUID);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			ExceptionDialog err = new ExceptionDialog("Datenbankfehler", "Achtung es ist ein Fehler aufgetreten!",
					"Beziehung konnte nicht gefunden werden!", e);
			err.showAlert();
			return false;
		}

	}

	/**
	 * if a relation between two partners exits
	 * 
	 * @param partner1
	 * @param partner2
	 * @return
	 */
	public static boolean relationExists(Person partner1, Person partner2) {
		try {
			PreparedStatement ps0 = con
					.prepareStatement("SELECT R_UUID FROM relationship WHERE partner1 = ? AND partner2 = ?");
			ps0.setString(1, partner1.getUuid().toString());
			ps0.setString(2, partner2.getUuid().toString());

			ResultSet rs0 = ps0.executeQuery();

			if (rs0.next()) {
				return true;
			} else {
				PreparedStatement ps1 = con
						.prepareStatement("SELECT R_UUID FROM relationship WHERE partner1 = ? AND partner2 = ?");
				ps1.setString(1, partner2.getUuid().toString());
				ps1.setString(2, partner1.getUuid().toString());

				ResultSet rs1 = ps1.executeQuery();

				if (rs1.next()) {
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			ExceptionDialog err = new ExceptionDialog("Datenbankfehler", "Achtung es ist ein Fehler aufgetreten!",
					"Beziehung konnte nicht gefunden werden!", e);
			err.showAlert();
			return false;
		}

	}

	/**
	 * adds a child to the relationship
	 * 
	 * @param r_uuid
	 * @param p_uuid
	 * @return
	 */
	public static boolean addChild(String r_uuid, String p_uuid) {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO children (R_UUID, P_UUID) VALUES (?, ?)");
			ps.setString(1, r_uuid);
			ps.setString(2, p_uuid);

			if (ps.executeUpdate() > 0) {
				Relationship r = getRelationship(r_uuid);
				Person child = personMethods.getPerson(p_uuid);
				if (r.getPartner0() != null && r.getPartner1() != null) {

					r.getPartner0().getRelationship(r_uuid).addChild(child);
					r.getPartner1().getRelationship(r_uuid).addChild(child);
					return true;
				} else if (r.getPartner0() != null && r.getPartner1() == null) {
					r.getPartner0().getRelationship(r_uuid).addChild(child);
					return true;
				} else if (r.getPartner0() == null && r.getPartner1() != null) {
					r.getPartner1().getRelationship(r_uuid).addChild(child);
					return true;
				} else {
					return false;
				}

			} else {
				return false;
			}

		} catch (SQLException e) {
			ExceptionDialog err = new ExceptionDialog("Datenbankfehler", "Achtung es ist ein Fehler aufgetreten!",
					"Kind konnte nicht zu der Beziehung hinzugefügt werden!", e);
			err.showAlert();
			return false;
		}
	}

	/**
	 * 
	 * remove a child from a relationship
	 * 
	 * @param r_uuid
	 * @param p_uuid
	 * @return
	 */
	public static boolean removeChild(String r_uuid, String p_uuid) {
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM children WHERE R_UUID = ? AND P_UUID = ?");
			ps.setString(1, r_uuid);
			ps.setString(2, p_uuid);

			if (ps.executeUpdate() > 0) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			ExceptionDialog err = new ExceptionDialog("Datenbankfehler", "Achtung es ist ein Fehler aufgetreten!",
					"Kind konnte nicht aus der Beziehung gelöscht werden!", e);
			err.showAlert();
			return false;
		}
	}

	/**
	 * remove all children from the relationship
	 * 
	 * @param r_uuid
	 * @return
	 */
	public static boolean removeChildren(String r_uuid) {
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM children WHERE R_UUID = ?");
			ps.setString(1, r_uuid);

			if (ps.executeUpdate() > 0) {
				return true;
			} else {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * delete a relationship
	 * 
	 * @param r_uuid
	 * @return
	 */
	public static boolean delRelationship(String r_uuid) {
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("DELETE FROM relationship WHERE R_UUID = ?");
			ps.setString(1, r_uuid);
			if (ps.executeUpdate() > 0) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			ExceptionDialog err = new ExceptionDialog("Datenbankfehler", "Achtung es ist ein Fehler aufgetreten!",
					"Konnte die Beziehung nicht löschen!", e);
			err.showAlert();
			return false;
		}
	}

	/**
	 * Get all children from a relationship
	 * 
	 * @param r_uuid --> relationship uuid
	 * @return
	 */
	public static ArrayList<Person> getChildren(String r_uuid) {
		// TODO Auto-generated method stub
		ArrayList<Person> temp = new ArrayList<Person>();
		try {
			PreparedStatement ps0 = con.prepareStatement("SELECT * FROM children WHERE R_UUID = ?");
			ps0.setString(1, r_uuid);

			ResultSet rs = ps0.executeQuery();

			while (rs.next()) {
				String p_uuid = rs.getString("P_UUID");

				Person p = personMethods.getPerson(p_uuid);
				temp.add(p);
			}

			return temp;
		} catch (SQLException e) {
			ExceptionDialog err = new ExceptionDialog("Datenbankfehler", "Achtung es ist ein Fehler aufgetreten!",
					"Konnte die Kinder der Beziehung nicht auslesen", e);
			err.showAlert();
			return null;
		}
	}

	/**
	 * If the relationship only has one partner
	 * 
	 * @param r_uuid
	 * @return
	 */
	public static boolean hasOnePartner(String r_uuid) {
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("SELECT * FROM relationship WHERE R_UUID = ?");
			ps.setString(1, r_uuid);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				String partner1 = rs.getString("partner1");
				String partner2 = rs.getString("partner2");
				if (partner1 == null && partner2 == null) {
					return false;
				}

				if (partner1 == null || partner1.isEmpty() || partner1.equals("NULL")) {
					return true;
				}

				else if (partner2 == null || partner2.isEmpty() || partner2.equals("NULL")) {
					return true;
				} else {
					return false;
				}

			} else {
				return false;
			}
		} catch (SQLException e) {
			ExceptionDialog err = new ExceptionDialog("Datenbankfehler", "Achtung es ist ein Fehler aufgetreten!",
					"Beziehung konnte nicht gefunden werden!", e);
			err.showAlert();
			return false;
		}
	}
}
