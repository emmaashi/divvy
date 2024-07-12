package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import model.Project;
import model.User;

/*
 * Name: Emma Shi 
 * Project  Panel
 * Date: June 14th, 2024
 * Teacher: Mr. Fernandes
 * Course Code: ICS4U1
 */

// This class allows the user to create a new project and access a prexisting one
public class ProjectPanel extends JPanel implements ActionListener {
    // Fields for project creation
    static JPanel projectPanel = new JPanel();
    JLabel projectLabel = new JLabel(new ImageIcon("images/ProjectSelectionFrame.png"));
    JLabel uniqueProjectID = new JLabel();
    JTextArea projectNameTA = new JTextArea();
    JTextArea projectDescriptionTA = new JTextArea();
    JTextArea projectDeadlineTA = new JTextArea(); // TextArea to input the deadline
    JTextArea joinProjectTA = new JTextArea();
    JButton saveButton = new JButton();
    private boolean isProjectSelected = false;

    // Fields for existing project
    public String username;
    public String password;
    public int userID; // User ID for the current user

    // Constructor method
    public ProjectPanel(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        panelSetup();
        frameSetup();
    }

    // This method sets up the panel
    private void panelSetup() {
        projectPanel.add(projectLabel);
        addTransparentButton(saveButton, 1151, 201, 136, 47, projectLabel);
        addTextArea(projectNameTA, 463, 210, 582, 29, projectLabel);
        addTextArea(projectDescriptionTA, 344, 301, 953, 109, projectLabel);
        addTextArea(projectDeadlineTA, 1125, 435, 158, 29, projectLabel);
        addTextArea(joinProjectTA, 832, 563, 494, 29, projectLabel);
        uniqueProjectID.setBounds(688, 431, 582, 29);
        projectLabel.add(uniqueProjectID);
        uniqueProjectID.setText(Project.generateProjectID());
        uniqueProjectID.setFont(new Font("SansSerif", Font.BOLD, 20));
    }

    // This is a helper method that adds a transparent button top of a label
    private void addTransparentButton(JButton button, int x, int y, int width, int height, JLabel label) {
        button.setBounds(x, y, width, height);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.addActionListener(this);
        label.add(button);
    }

    // Helper method to add text areas
    public void addTextArea(JTextArea textArea, int x, int y, int width, int height, JLabel label) {
        textArea.setBounds(x, y, width, height);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 17));
        // Set a border for the JTextArea
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        textArea.setBorder(border);
        label.add(textArea);
    }

    // This method sets up the frame for the project panel
    private void frameSetup() {
        // First panel to be displayed
        add(projectPanel);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            // Create a new Project instance and save to the database
            String projectName = projectNameTA.getText();
            String projectDescription = projectDescriptionTA.getText();
            String projectDeadline = projectDeadlineTA.getText();
            Date deadline = Date.valueOf(LocalDate.parse(projectDeadline)); // Convert string to Date
            Project project = new Project(projectName, projectDescription, deadline, userID); // Pass userID as managerID
            project.saveToDatabase();
            JOptionPane.showMessageDialog(this, "Project saving is successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Check if the project ID entered in joinProjectTA exists
            String projectID = joinProjectTA.getText().trim();
            if (!projectID.isEmpty()) {
                joinProject(projectID);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid project ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to join an existing project - UI not updated
    private void joinProject(String projectID) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/UserProjectsDB", "root", "Emmashi1!")) {
            // Fetch the existing users for the project
            String fetchSql = "SELECT user_ids FROM projects WHERE project_id = ?";
            try (PreparedStatement fetchStatement = connection.prepareStatement(fetchSql)) {
                fetchStatement.setString(1, projectID);
                try (ResultSet resultSet = fetchStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String userIds = resultSet.getString("user_ids");
                        if (userIds == null || userIds.isEmpty()) {
                            userIds = this.username;
                        } else {
                            userIds += "," + this.username;
                        }
                        // Update the project with the new user
                        String updateSql = "UPDATE projects SET user_ids = ? WHERE project_id = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                            updateStatement.setString(1, userIds);
                            updateStatement.setString(2, projectID);
                            updateStatement.executeUpdate();
                            JOptionPane.showMessageDialog(this, "Successfully joined the project.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Project ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error joining project.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
