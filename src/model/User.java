package model;

//Imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import controller.Database;

/*
 * Name: Emma Shi 
 * User obejct
 * Date: June 14th, 2024
 * Teacher: Mr. Fernandes
 * Course Code: ICS4U1
 */

// This is the tempalte class for a User
public class User {
	// Fields
	public String username;
	public String password;
	public String first_name;
	public String last_name;
	public String email;
	public String instagram;
	public String discord;
	public ArrayList<Project> projects;
	public LinkedList<Task> tasks;
	public int tasksCompletedOnTime;
	public int tasksLateOrUncompleted;
	public String performanceRating;

	// Constructors
	public User(String username, String password, String first_name, String last_name, String email, String instagram,
			String discord, ArrayList<Project> projects, LinkedList<Task> tasks) {
		this.username = username;
		this.password = password;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.instagram = instagram;
		this.discord = discord;
		this.projects = projects;
		this.tasks = tasks;
		this.tasksCompletedOnTime = 0;
		this.tasksLateOrUncompleted = 0;
		this.performanceRating = "NEEDS IMPROVEMENT";
	}

	public User(String username, String password, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.first_name = firstName;
		this.last_name = lastName;
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	// Getters and Setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInstagram() {
		return instagram;
	}

	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}

	public String getDiscord() {
		return discord;
	}

	public void setDiscord(String discord) {
		this.discord = discord;
	}

	public ArrayList<Project> getProjects() {
		return projects;
	}

	public void setProjects(ArrayList<Project> projects) {
		this.projects = projects;
	}

	public LinkedList<Task> getTasks() {
		return tasks;
	}

	public void setTasks(LinkedList<Task> tasks) {
		this.tasks = tasks;
	}

	public int getTasksCompletedOnTime() {
		return tasksCompletedOnTime;
	}

	public void incrementTasksCompletedOnTime() {
		this.tasksCompletedOnTime++;
		updatePerformanceRating();
	}

	public int getTasksLateOrUncompleted() {
		return tasksLateOrUncompleted;
	}

	public void incrementTasksLateOrUncompleted() {
		this.tasksLateOrUncompleted++;
		updatePerformanceRating();
	}

	public String getPerformanceRating() {
		return performanceRating;
	}

	 // Performance rating algorithm
    private void updatePerformanceRating() {
        int totalTasks = tasksCompletedOnTime + tasksLateOrUncompleted;
        if (totalTasks == 0) {
            this.performanceRating = "NEEDS IMPROVEMENT";
        } else {
            double performance = (double) tasksCompletedOnTime / totalTasks;
            if (performance >= 0.9) {
                this.performanceRating = "EXCELLENT";
            } else if (performance >= 0.75) {
                this.performanceRating = "GOOD";
            } else if (performance >= 0.5) {
                this.performanceRating = "SATISFACTORY";
            } else {
                this.performanceRating = "NEEDS IMPROVEMENT";
            }
        }
    }

    // Database Methods
    public static boolean isUsernameExists(String username) {
        boolean usernameExists = false;
        try (Connection connection = getConnection()) {
            String checkSql = "SELECT COUNT(*) FROM Users WHERE username = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
                checkStatement.setString(1, username);
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        usernameExists = count > 0;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return usernameExists;
    }

    // Authenticate the user
    public boolean authenticate() {
        boolean passwordCorrect = false;
        try (Connection connection = getConnection()) {
            String checkSql = "SELECT password FROM Users WHERE username = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
                checkStatement.setString(1, this.username);
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String storedPassword = resultSet.getString("password");
                        passwordCorrect = storedPassword.equals(this.password);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return passwordCorrect;
    }

    // Create a new user
    public boolean create() {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO Users (username, password, first_name, last_name) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, this.username);
                statement.setString(2, this.password);
                statement.setString(3, this.first_name);
                statement.setString(4, this.last_name);
                int rowsInserted = statement.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Add project to user's projects
    public boolean addProject(Project project) {
        String sql = "INSERT INTO ProjectMembers (projectID, username) VALUES (?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, project.getProjectID());
            pstmt.setString(2, this.username);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get connection to database
    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/UserProjectsDB";
        String user = "root";
        String password = "Emmashi1!";
        return DriverManager.getConnection(url, user, password);
    }

    // Mark task as complete
    public void markTaskAsComplete(Task task) {
        task.setComplete(true);
        incrementTasksCompletedOnTime();
        // Update the task in the database or other persistent storage if necessary
    }

    // Handle overdue tasks
    public void handleOverdueTasks(LocalDate currentDate) {
        for (Task task : tasks) {
            if (!task.isComplete() && task.getDueDate().isBefore(currentDate)) {
                incrementTasksLateOrUncompleted();
            }
        }
    }
}
