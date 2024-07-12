package controller;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
/*
 * Name: Emma Shi 
 * MenuTabHelper
 * Date: June 14th, 2024
 * Teacher: Mr. Fernandes
 * Course Code: ICS4U1
 * Description: facilitates navigation between panels without creating a new frame each time. 
 */

// This class facilitates navigation between different panels. 
public class MenuTabHelper extends JPanel implements ActionListener {

	// Fields
    public String username;
    public String password;
    public JButton homeButton = new JButton();
    public JButton projectButton = new JButton();
    public JButton planningButton = new JButton();
    public JButton performanceButton = new JButton();
    public JButton contactButton = new JButton();
    private JPanel cardPanel;
    private CardLayout cardLayout;

    
    // Constructor method
    public MenuTabHelper(String username, String password, JPanel cardPanel, CardLayout cardLayout) {
        this.username = username;
        this.password = password;
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;

        setLayout(null); // Use null layout for absolute positioning
        setOpaque(false); // Make the panel transparent
        addButtons(); // Add buttons to the panel
    }

    // This method adds all the buttons to the panel
    private void addButtons() {
        addTransparentButton(homeButton, 0, 69, 195, 44);
        addTransparentButton(projectButton, 0, 116, 195, 44);
        addTransparentButton(planningButton, 0, 168, 195, 44);
        addTransparentButton(performanceButton, 0, 308, 195, 44);
        addTransparentButton(contactButton, 0, 358, 195, 44);
    }

    // Helper method to make transparent
    private void addTransparentButton(JButton button, int x, int y, int width, int height) {
        button.setBounds(x, y, width, height);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.addActionListener(this);
        add(button); // Add button to the panel
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == homeButton) {
            cardLayout.show(cardPanel, "home");
        } else if (e.getSource() == projectButton) {
            cardLayout.show(cardPanel, "project");
        } else if (e.getSource() == planningButton) {
            cardLayout.show(cardPanel, "planning");
        } else if (e.getSource() == performanceButton) {
            cardLayout.show(cardPanel, "performance");
        } else if (e.getSource() == contactButton) {
            cardLayout.show(cardPanel, "contact");
        }
    }
}
