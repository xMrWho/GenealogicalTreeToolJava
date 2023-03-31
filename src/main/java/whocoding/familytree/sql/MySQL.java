package whocoding.familytree.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * MySQL-Klasse, um die Verbindung zu einer MySQL-Datenbank herzustellen
 * 
 * @author MrWho97
 *
 */
public class MySQL extends Database {
	private final String user, database, password,
			port, hostname;

	/**
	 * 
	 * @param hostname
	 * @param port
	 * @param database
	 * @param username
	 * @param password
	 */
	public MySQL(String hostname, String port, String database,
			String username, String password) {
		this.hostname = hostname;
		this.port = port;
		this.database = database;
		this.user = username;
		this.password = password;
	}

	/**
	 * 
	 * @param hostname
	 * @param port
	 * @param username
	 * @param password
	 */
	public MySQL(String hostname, String port,
			String username, String password) {
		this.hostname = hostname;
		this.port = port;
		this.database = null;
		this.user = username;
		this.password = password;
	}

	/**
	 * 
	 * Opens the Connection
	 */
	@Override
	public Connection openConnection() throws SQLException,
			ClassNotFoundException {
		if (checkConnection()) {
			return connection;
		}

		String connectionURL = "jdbc:mysql://"
				+ this.hostname + ":" + this.port;
		if (database != null) {
			connectionURL = connectionURL + "/" + this.database;
		}

		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection(connectionURL,
				this.user, this.password);
		return connection;
	}

	// isConnected
	public boolean isConnected() {
		return (connection == null ? false : true);
	}

}
