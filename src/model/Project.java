package model;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Name: Emma Shi 
 * Project object
 * Date: June 14th, 2024
 * Teacher: Mr. Fernandes
 * Course Code: ICS4U1
 */

// This class the template class for a project. It contains a utility method that generates a unique project ID for each project
public class Project {

    // Fields
    private String projectID;
    private String projectName;
    private String description;
    private Date deadline;
    private int managerID;  // New field to store the manager's ID

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ID_LENGTH = 10;
    private static final SecureRandom random = new SecureRandom();

    // Constructor method
    public Project(String projectName, String description, Date deadline, int managerID) {
        this.projectID = generateProjectID();
        this.projectName = projectName;
        this.description = description;
        this.deadline = deadline;
        this.managerID = managerID; // Initialize manager ID
    }

    // Getters and Setters
    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public int getManagerID() {
        return managerID;
    }

    public void setManagerID(int managerID) {
        this.managerID = managerID;
    }

    // This method generates a unique ten-character project ID
    public static String generateProjectID() {
        StringBuilder projectID = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            projectID.append(CHARACTERS.charAt(index));
        }
        return projectID.toString();
    }

    // Save the project to database
    public void saveToDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/UserProjectsDB", "root", "Emmashi1!")) {
        	// REFERENCE: https://stackoverflow.com/questions/17317198/how-to-save-an-sql-query-into-a-table
            String sql = "INSERT INTO projects (name, description, deadline, project_id, user_ids) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, projectName);
                statement.setString(2, description);
                statement.setDate(3, deadline);
                statement.setString(4, projectID);
                statement.setString(5, ""); // Initially, no users are assigned to the project
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // REFERENCE: https://www.javatpoint.com/example-to-connect-to-the-mysql-database
    // Method to get a connection to the database
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/UserProjectsDB";
        String user = "root";
        String password = "Emmashi1!";
        return DriverManager.getConnection(url, user, password);
    }
}
