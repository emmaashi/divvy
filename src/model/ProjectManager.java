package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
/*
 * Name: Emma Shi 
 * Project Manager object
 * Date: June 14th, 2024
 * Teacher: Mr. Fernandes
 * Course Code: ICS4U1
 */

// Template class for a Project Manager that extends the User object
public class ProjectManager extends User {

	// Inherit all the properties from the user
	public ProjectManager(String username, String password, String first_name, String last_name, String email,
			String instagram, String discord, ArrayList<Project> projects, LinkedList<Task> tasks) {
		super(username, password, first_name, last_name, email, instagram, discord, projects, tasks);
	}

	// Utility methods that a project manager can use
	// Method to assign tasks
	public void assignTask(String taskID, String username) {
		try (Connection connection = getConnection()) {
			// First, get the user ID for the username
			int userID = getUserIDByUsername(connection, username);
			if (userID != -1) {
				// REFERENCE: https://www.sqltutorial.org/sql-update/
				// Assign the task to the user
				String sql = "UPDATE Tasks SET userID = ? WHERE taskID = ?";
				try (PreparedStatement statement = connection.prepareStatement(sql)) {
					statement.setInt(1, userID);
					statement.setString(2, taskID);
					statement.executeUpdate();
					System.out.println("Task assigned successfully.");
				}
			} else {
				System.out.println("User not found: " + username);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	// Method to add a user to a project
	public void addUserToProject(String projectID, String username) {
		try (Connection connection = getConnection()) {
			// First, get the user ID for the username
			int userID = getUserIDByUsername(connection, username);
			if (userID != -1) {
				// REFERNCE: https://www.w3schools.com/sql/sql_insert.asp
				String sql = "INSERT INTO ProjectMembers (projectID, userID) VALUES (?, ?)";
				try (PreparedStatement statement = connection.prepareStatement(sql)) {
					statement.setString(1, projectID);
					statement.setInt(2, userID);
					statement.executeUpdate();
				}
			} else {
				System.out.println("User not found: " + username);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	// Helper method to get user ID by username
	private int getUserIDByUsername(Connection connection, String username) throws SQLException {
		String sql = "SELECT userID FROM Users WHERE username = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, username);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt("userID");
				} else {
					return -1; // User not found
				}
			}
		}
	}

	// Method to get a connection to the database
	private Connection getConnection() throws SQLException {
		String url = "jdbc:mysql://localhost:3306/ProjectManagementDB";
		String user = "root";
		String password = "Emmashi1!";
		return DriverManager.getConnection(url, user, password);
	}
}
