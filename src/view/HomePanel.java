package view;

import java.awt.CardLayout;
// Imports
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.MenuTabHelper;
import model.Calendar;
import model.User;

/*
 * Name: Emma Shi
 * Date: June 14th, 2024
 * Course Code: ICS4U1
 * Teacher: Mr. Fernandes
 * Home Panel
 */

// This is the home page of the application
public class HomePanel extends JPanel implements ActionListener {

	// User fields
	private String username;
	private String password;

	// GUI Fields
	JLabel homeLabel = new JLabel(new ImageIcon("images/Home.png"));
	// Add calendar object that was created
	Calendar calendarApp = new Calendar();
	// Buttons
	JButton homeButton = new JButton();
	JButton newProjectButton = new JButton();
	JButton projectButton = new JButton();
	JButton deadlineButton = new JButton();
	JButton planningButton = new JButton();
	JButton performanceButton = new JButton();
	JButton contactButton = new JButton();
	MenuTabHelper menuTab;

	// Constructor method
	public HomePanel(User user) {
		this.menuTab = new MenuTabHelper(username, password, this, new CardLayout());
		setLayout(null); // Use absolute positioning
		homeSetup();
		panelSetup();
	}

	// This method sets up the main home page
	private void homeSetup() {
		homeLabel.setBounds(0, 0, 1440, 900); // Assuming full HD screen size
		add(homeLabel);

		newProjectButton.setBounds(1102, 396, 78, 68);
		addTransparentButton(homeButton, 29, 1239, 123, 12, homeLabel); // Example button setup
		// Adding the CalendarApp panel to homePanel
		calendarApp.setBounds(304, 325, 664, 409); // Set bounds to position it on the home screen
		homeLabel.add(homeButton);
		homeLabel.add(calendarApp);
		homeLabel.add(menuTab);
	}

	// This method sets up the panel
	private void panelSetup() {
		// First panel to be displayed
		setPreferredSize(new Dimension(1920, 1080)); // Set preferred size for the panel
	}

	private void addTransparentButton(JButton button, int x, int y, int width, int height, JLabel label) {
		button.setBounds(x, y, width, height);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setFocusable(false);
		button.addActionListener(this);
		label.add(button);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == newProjectButton) {
			new PlanningToolsFrame(username);
		}
	}
}
