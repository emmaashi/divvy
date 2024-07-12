package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import model.User;

/*
 * Name: Emma Shi 
 * Contact Panel
 * Date: June 14th, 2024
 * Teacher: Mr. Fernandes
 * Course Code: ICS4U1
 */

// This panel displays all the user's 
public class ContactPanel extends JPanel implements ActionListener {
	// Fields
	static JPanel contactPanel = new JPanel();
	JLabel contactLabel = new JLabel((new ImageIcon("images/Contact.png")));

	// Text areas
	JTextArea emailTextArea = new JTextArea();
	JTextArea instagramTextArea = new JTextArea();
	JTextArea discordTextArea = new JTextArea();
	JTextArea firstNameTextArea = new JTextArea();
	JTextArea lastNameTextArea = new JTextArea();

	// Buttons
	JButton saveButton = new JButton();
	JLabel usernameLabel = new JLabel();

	// User credentials
	private String username;
	private String password;

	// Constructor method
	public ContactPanel(User user) {
		username = user.getUsername();
		password = user.getPassword();
		setupContact();
		panelSetup();
		loadData(); // Load existing data if any
	}

	// Constructor method
	public ContactPanel(String username, String password) {
		this.username = username;
		this.password = password;
		setupContact();
		panelSetup();
		loadData(); // Load existing data if any
	}

	// This method sets up the contact information for a user
	private void setupContact() {
		contactPanel.add(contactLabel);
		addTextArea(emailTextArea, 349, 280, 490, 54, contactLabel);
		addTextArea(instagramTextArea, 349, 422, 490, 54, contactLabel);
		addTextArea(discordTextArea, 349, 563, 490, 54, contactLabel);
		addTextArea(firstNameTextArea, 1009, 423, 320, 37, contactLabel);
		addTextArea(lastNameTextArea, 1009, 513, 320, 54, contactLabel);
		addTransparentButton(saveButton, 1005, 673, 131, 51, contactLabel);

		// Add the user's username as a label
		usernameLabel.setBounds(1009, 577, 320, 54);
		usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 17));
		contactLabel.add(usernameLabel);

		// Load current username and set it to the label
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/UserProjectsDB", "root",
				"Emmashi1!")) {
			String sql = "SELECT username FROM users WHERE username = ? AND password = ?";
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setString(1, username);
				statement.setString(2, password);
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						String currentUsername = resultSet.getString("username");
						usernameLabel.setText(currentUsername);
					}
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading username.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Helper method to add text areas
	public void addTextArea(JTextArea textArea, int x, int y, int width, int height, JLabel label) {
		textArea.setBounds(x, y, width, height);
		textArea.setOpaque(false);
		if (textArea == firstNameTextArea || textArea == lastNameTextArea) {
			textArea.setFont(new Font("SansSerif", Font.PLAIN, 17));
		} else {
			textArea.setFont(new Font("SansSerif", Font.PLAIN, 22));
		}
		label.add(textArea);
	}

	// Helper method to add a transparent button onto a label
	public void addTransparentButton(JButton button, int x, int y, int width, int height, JLabel label) {
		button.setBounds(x, y, width, height);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setFocusable(false);
		button.addActionListener(this);
		label.add(button);
	}

	// Setup the panel
	private void panelSetup() {
		// First panel to be displayed
		add(contactPanel);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == saveButton) {
			saveContactInfo();
		}
	}

	// Method to save contact info to the database
	private void saveContactInfo() {
		String email = emailTextArea.getText().trim();
		String instagram = instagramTextArea.getText().trim();
		String discord = discordTextArea.getText().trim();
		String firstName = firstNameTextArea.getText().trim();
		String lastName = lastNameTextArea.getText().trim();

		// Connect to the SQL database
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/UserProjectsDB", "root",
				"Emmashi1!")) {
			String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, instagram = ?, discord = ?, performance_ranking = ? WHERE username = ?";
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setString(1, firstName);
				statement.setString(2, lastName);
				statement.setString(3, email);
				statement.setString(4, instagram);
				statement.setString(5, discord);
				statement.setString(6, "GOOD");
				statement.setString(7, username);
				statement.executeUpdate();
				JOptionPane.showMessageDialog(this, "Contact information saved successfully.", "Success",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error saving contact information.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Load the data from the SQL database to reflect in the UI
	private void loadData() {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/UserProjectsDB", "root",
				"Emmashi1!")) {
			String sql = "SELECT first_name, last_name, email, instagram, discord FROM users WHERE username = ? AND password = ?";
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setString(1, username);
				statement.setString(2, password);
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						firstNameTextArea.setText(resultSet.getString("first_name"));
						lastNameTextArea.setText(resultSet.getString("last_name"));
						// Check if email, instagram, and discord are not null before setting the text
						String email = resultSet.getString("email");
						if (email != null) {
							emailTextArea.setText(email);
						}
						String instagram = resultSet.getString("instagram");
						if (instagram != null) {
							instagramTextArea.setText(instagram);
						}
						String discord = resultSet.getString("discord");
						if (discord != null) {
							discordTextArea.setText(discord);
						}
					} else {
						JOptionPane.showMessageDialog(this,
								"No contact information found for the given username and password.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading contact information.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
