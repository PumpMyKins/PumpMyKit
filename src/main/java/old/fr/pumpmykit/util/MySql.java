package old.fr.pumpmykit.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySql {

	private Connection conn;
	private MySQLCredentials credentials;

	public MySql(MySQLCredentials credentials) {
		this.credentials = credentials;
	}

	public boolean isConnected() throws SQLException {
		return conn != null && !conn.isClosed();
	}

	public void openConnection() throws SQLException, ClassNotFoundException {
		if (!isConnected()) {
			Class.forName("org.mariadb.jdbc.Driver");
			this.conn = DriverManager.getConnection(
					"jdbc:mysql://" + this.credentials.getHost() + ":" + this.credentials.getPort() + "/" + this.credentials.getDatabase() + "?autoReconnect=true",
					this.credentials.getUsername(), this.credentials.getPassword());
		}
	}

	public void closeConnection() throws SQLException {
		this.conn.close();
	}

	public ResultSet sendQuery(String query) throws Exception {
		if (isConnected()) {

			Statement st = this.conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			return rs;

		}
		throw new Exception("SQL connection closed !");
	}

	public void sendUpdate(String update) throws Exception {
		if (isConnected()) {

			Statement statement = this.conn.createStatement();	//create statement
			statement.executeUpdate(update);	//send statement

			// close
			statement.close();

		}else {
			throw new Exception("SQL connection closed !");
		}

	}

	public static class MySQLCredentials {

		private String host = "";
		private int port;
		private String username = "";
		private String password = "";
		private String database = "";

		public MySQLCredentials(String host, int port, String username, String password, String database) {
			this.host = host;
			this.port = port;
			this.username = username;
			this.password = password;
			this.database = database;
		}

		public String getHost() {
			return host;
		}

		public String getDatabase() {
			return database;
		}

		public String getPassword() {
			return password;
		}

		public int getPort() {
			return port;
		}

		public String getUsername() {
			return username;
		}

		public void setDatabase(String database) {
			this.database = database;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public void setUsername(String username) {
			this.username = username;
		}
	}

}