package fr.pumpmykins.kit.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL {

	private String host = "";
	private int port = 3306;
	private String username = "root";
	private String password = "";
	private String database = "";
	private Connection conn;

	public MySQL(String host, int port, String username, String password, String database) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.database = database;
	}

	public MySQL(MySQLCredentials credentials) {
		this.host = credentials.getHost();
		this.port = credentials.getPort();
		this.username = credentials.getUsername();
		this.password = credentials.getPassword();
		this.database = credentials.getDatabase();

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

	public boolean isConnected() {
		try {
			if(this.conn != null && !(this.conn.isClosed()))
				return true;
			else
				return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void openConnection() {
		if (!isConnected()) {
			try {
				
				this.conn = DriverManager.getConnection(
						"jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true",
						this.username, this.password);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeConnection() {
		if (isConnected()) {
			if(!this.host.isEmpty() && !this.username.isEmpty() && !this.database.isEmpty()) {
				try {
					
					this.conn.close();
				
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
			}
		}
	}

	public void refreshConnection() throws SQLException {
		
		if(this.conn.isClosed()) {
			
			this.conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true",
						this.username, this.password);
		} else {
			
			this.conn.close();
			
			refreshConnection();
		}
	}
	
	public ResultSet getResult(String query) {
		if (isConnected()) {
			try {
				PreparedStatement pst = this.conn.prepareStatement(query);
				return pst.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void update(String query) {

		if(isConnected()) {

			try {

				PreparedStatement pst = conn.prepareStatement(query);
				pst.executeUpdate();
				
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}
	
	
}