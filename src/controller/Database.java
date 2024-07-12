package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/*
 * Name: Emma Shi 
 * Application Frame
 * Date: June 14th, 2024
 * Teacher: Mr. Fernandes
 * Course Code: ICS4U1
 */
// This class is used to connect to the database
// I realized that I was writing a lot of repetitive code. So, I created this controller to improve the efficiency of this process
// but didn't get around to implementing it for all of the methods.
// Reference Youtube Video: https://www.youtube.com/watch?v=_Zg4tZoFdx0
public class Database {

	private static final String URL = "jdbc:mysql://localhost:3306/UserProjectsDB";
	private static final String USER = "root";
	private static final String PASSWORD = "Emmashi1!";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	public static void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (conn != null)
				conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
