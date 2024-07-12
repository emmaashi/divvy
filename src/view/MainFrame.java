package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Color;
import controller.MenuTabHelper;
import model.User;

// This is the main frame that contains all of the panels, allowing toggle.
public class MainFrame extends JFrame {

	// Fields
	private CardLayout cardLayout;
	private JPanel cardPanel;

	// Constructor method
	public MainFrame(String username, String password) {
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);

		// Create a user object
		User user = new User(username, password);

		// Initialize and add all panels
		cardPanel.add(new HomePanel(user), "home");
		cardPanel.add(new ProjectPanel(user), "project");
		cardPanel.add(new PlanningToolsPanel(user), "planning");
		cardPanel.add(new PerformancePanel(user), "performance");
		cardPanel.add(new ContactPanel(user), "contact");

		// Menu tab
		MenuTabHelper menuTabHelper = new MenuTabHelper(username, password, cardPanel, cardLayout);

		// Set background color of the cardPanel
		cardPanel.setBackground(Color.RED);

		setLayout(null); // Avoid using null layout; setLayout(null) should be avoided in most cases
		menuTabHelper.setBounds(0, 0, 196, 450);
		cardPanel.setBounds(0, 0, 1440, 900);

		add(menuTabHelper);
		add(cardPanel);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1440, 900);
		setResizable(false);
		setVisible(true);
	}

}
