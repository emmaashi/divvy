package view;

//Imports
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.User;

/*
 * Name: Emma Shi
 * Date: June 14th, 2024
 * Course Code: ICS4U1
 * Teacher: Mr. Fernandes
 * Performance Panel
 */

// This panel displays the calculated user performance ranking
public class PerformancePanel extends JPanel {
	JLabel performanceLabel = new JLabel(new ImageIcon("images/Performance.png"));
	JLabel performanceRatingLabel = new JLabel();

	// Constructor method
	public PerformancePanel(User user) {
		panelSetup(user);

	}

	// This method sets up the panel
	private void panelSetup(User user) {
		add(performanceLabel);

		// Get the rating of the user if it exists (assuming they do not have a newly
		// opened account)
		if (user.getPerformanceRating() != null) {
			performanceRatingLabel.setText(user.getPerformanceRating());

		} else {
			performanceRatingLabel.setText("NEEDS IMPROVEMENT");
		}
		performanceRatingLabel.setFont(new Font("SansSerif", Font.PLAIN, 17));
		performanceLabel.add(performanceRatingLabel);

	}
}
