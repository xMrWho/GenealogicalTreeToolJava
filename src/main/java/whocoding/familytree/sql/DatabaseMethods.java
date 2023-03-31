package whocoding.familytree.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import whocoding.familytree.GenelogicalTreeTool;

/**
 * 
 * Klasse, welche die Methoden zur Erstellung der Datenbank
 * und Tabellen realiesiert, wenn diese noch nicht vorhanden sind
 * 
 * ansonsten wird hier festgelegt welche Datenbank vom Prorgamm benutzt wird
 * 
 * @author MrWho97
 *
 */
public class DatabaseMethods {
	// saves the database connection
	static Connection con = GenelogicalTreeTool.sql.getConnection();

	/**
	 * 
	 * inits the database
	 * 
	 * @return false --> if a sql exception occurs
	 * @return useDatabase() --> if not
	 */
	public static boolean initDatabase() {
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("CREATE DATABASE IF NOT EXISTS genelogicaltool");
			if (ps.executeUpdate() > 0) {
				return useDatabase();
			} else {
				return useDatabase();
				// System.err.println("FAILED CREATE DATABASE STATEMENT");
			}
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * 
	 * use the database
	 * 
	 * @return false --> if a sql exception occurs
	 * @return initPersonTable() --> if not
	 */
	private static boolean useDatabase() {
		try {
			con.setCatalog("genelogicaltool");
			return initPersonTable();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 
	 * inits the person table
	 * 
	 * @return false --> if a sql exception occurs
	 * @return initRelationshipTable() --> if not
	 */
	private static boolean initPersonTable() {
		String sql = "CREATE TABLE IF NOT EXISTS person (P_UUID VARCHAR(255), name VARCHAR(255), firstname VARCHAR(255), "
				+ "gender VARCHAR(255), birthday VARCHAR(255), deathday VARCHAR(255), infos LONGTEXT, PRIMARY KEY (P_UUID)) ENGINE = InnoDB;";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			if (ps.executeUpdate() > 0) {
				return initRelationshipTable();
			} else {
				return initRelationshipTable();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 
	 * inits the relationship table
	 * 
	 * @return false --> if a sql exception occurs
	 * @return initChildrenTable() --> if not
	 */
	private static boolean initRelationshipTable() {
		String sql = "CREATE TABLE IF NOT EXISTS relationship (R_UUID VARCHAR(255), partner1 VARCHAR(255), partner2 VARCHAR(255), infos LONGTEXT, "
				+ "PRIMARY KEY (R_UUID), FOREIGN KEY (partner1) REFERENCES person(P_UUID), FOREIGN KEY (partner2) REFERENCES person(P_UUID)) ENGINE = InnoDB;";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			if (ps.executeUpdate() > 0) {
				return initChildrenTable();
			} else {
				return initChildrenTable();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * inits the children table
	 * 
	 * @return false --> if a sql exception occurs
	 * @return true --> if not
	 */
	private static boolean initChildrenTable() {
		String sql = "CREATE TABLE IF NOT EXISTS children (R_UUID VARCHAR(255), P_UUID VARCHAR(255), FOREIGN KEY (R_UUID) REFERENCES "
				+ "relationship(R_UUID), FOREIGN KEY (P_UUID) REFERENCES person(P_UUID)) ENGINE = InnoDB;";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
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

}
