package view;

// Imports
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import controller.MenuTabHelper;
import model.User;
/*
 * Name: Emma Shi
 * Date: June 14th, 2024
 * Course Code: ICS4U1
 * Teacher: Mr. Fernandes
 * Final Summative Project
 */

//This frame is responsible for onboarding the user to the home page. It contains both the login and account creation logic.
public class OnboardFrame extends JFrame implements ActionListener {

	// GUI Fields
	// Panels
	JPanel onboardPanel = new JPanel();
	JPanel loginPanel = new JPanel();
	JPanel createAccountPanel = new JPanel();

	// Background screen labels
	JLabel onboardLabel = new JLabel(new ImageIcon("images/Onboarding.png"));
	JLabel loginLabel = new JLabel(new ImageIcon("images/Login.png"));
	JLabel createAccountLabel = new JLabel(new ImageIcon("images/CreateAccount.png"));

	// Text areas
	JTextArea usernameTextArea = new JTextArea();
	JTextArea passwordTextArea = new JTextArea();
	JTextArea newPasswordTextArea = new JTextArea();
	JTextArea firstNameTextArea = new JTextArea();
	JTextArea lastNameTextArea = new JTextArea();

	// Buttons
	JButton getStartedButton = new JButton();
	JButton signInButton = new JButton();
	JButton loginButton = new JButton();
	JButton createAccountButton = new JButton();
	JButton backButton = new JButton();

	// For the action performed method to determine which screen is the current
	// screen
	boolean loginScreen = false;

	// Constructor method
	public OnboardFrame() {
		onboardSetup();
		frameSetup();
	}

	// This method sets up the initial screen users will see
	private void onboardSetup() {
		onboardPanel.add(onboardLabel);
		addTextArea(usernameTextArea, 384, 573, 469, 54, onboardLabel);
		addTransparentButton(getStartedButton, 859, 563, 177, 54, onboardLabel);
		addTransparentButton(signInButton, 569, 633, 68, 26, onboardLabel);
	}

	// This method sets up the panel for the user to create their account
	private void createAccountSetup() {
		createAccountPanel.add(createAccountLabel);
		addTextArea(firstNameTextArea, 487, 390, 465, 54, createAccountLabel);
		addTextArea(lastNameTextArea, 486, 495, 465, 54, createAccountLabel);
		addTextArea(newPasswordTextArea, 487, 597, 465, 54, createAccountLabel);
		addTransparentButton(createAccountButton, 811, 671, 136, 51, createAccountLabel);
		addTransparentButton(backButton, 71, 76, 13, 30, createAccountLabel);
		add(createAccountPanel);
	}

	// This method sets up the panel for the user to login to their existing account
	private void loginSetup() {
		loginPanel.add(loginLabel);
		addTextArea(passwordTextArea, 493, 531, 465, 54, loginLabel);
		addTransparentButton(loginButton, 819, 605, 136, 51, loginLabel);
		addTransparentButton(backButton, 71, 76, 13, 30, loginLabel);
		add(loginPanel);
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

	// Helper method to add text areas
	public void addTextArea(JTextArea textArea, int x, int y, int width, int height, JLabel label) {
		textArea.setBounds(x, y, width, height);
		textArea.setOpaque(false);
		textArea.setFont(new Font("SansSerif", Font.PLAIN, 22));
		label.add(textArea);
	}

	// This method sets up the current frame
	private void frameSetup() {
		// First panel to be displayed
		add(onboardPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height);
		setResizable(false);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// If the user does not have account, allow them to create a username
		if (event.getSource() == getStartedButton) {
			String username = usernameTextArea.getText().trim();
			// Error checking
			if (username.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please enter a username.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			} else if (username.contains(" ")) {
				JOptionPane.showMessageDialog(this, "Username cannot contain spaces.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			} else if (username.length() < 7) {
				JOptionPane.showMessageDialog(this, "Username needs to be at least 7 characters.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			} else if (User.isUsernameExists(username)) {
				JOptionPane.showMessageDialog(this, "Username already exists. Please choose another.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			onboardPanel.setVisible(false); // set the visibility of the panel to false to allow toggle
			createAccountPanel.setVisible(true);
			createAccountSetup();
		}
		// If the user has an existing account
		else if (event.getSource() == signInButton) {
			String username = usernameTextArea.getText().trim();
			if (User.isUsernameExists(username)) {
				onboardPanel.setVisible(false);
				loginPanel.setVisible(true);
				loginSetup();
			} else {
				JOptionPane.showMessageDialog(this, "User does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (event.getSource() == loginButton) {
			String username = usernameTextArea.getText().trim();
			String password = passwordTextArea.getText().trim();
			User user = new User(username, password);
			if (user.authenticate()) {
				JOptionPane.showMessageDialog(this, "Login successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
			
				new MainFrame(username, password);
			} else {
				JOptionPane.showMessageDialog(this, "Incorrect password.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (event.getSource() == createAccountButton) {
			String firstName = firstNameTextArea.getText().trim();
			String lastName = lastNameTextArea.getText().trim();
			String password = newPasswordTextArea.getText().trim();
			String username = usernameTextArea.getText().trim();
			// Error checking
			if (firstName.isEmpty() || lastName.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			// Create a new instance of a user
			User newUser = new User(username, password, firstName, lastName);
			if (newUser.create()) {
				JOptionPane.showMessageDialog(this, "Account created successfully.", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				
				new MainFrame(username, password);
			} else {
				JOptionPane.showMessageDialog(this, "Failed to create account.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (event.getSource() == backButton) {
			createAccountPanel.setVisible(false);
			loginPanel.setVisible(false);
			onboardPanel.setVisible(true);
		}
	}
}
