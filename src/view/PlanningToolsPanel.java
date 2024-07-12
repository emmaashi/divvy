package view;

import java.awt.Color;
import java.awt.Component;
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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import model.User;

/*
 * Name: Emma Shi 
 * Planning Tools Panel
 * Date: June 14th, 2024
 * Teacher: Mr. Fernandes
 * Course Code: ICS4U1
 */

// This class is the PlanningToolsFrame
public class PlanningToolsPanel extends JPanel implements ActionListener {

	// Fields
	private JTable table;
	private DefaultTableModel tableModel;
	private String username; // Username for the current user

	static JPanel planningPanel = new JPanel(null); // Use null layout for absolute positioning
	JPanel projectSelectionPanel = new JPanel();
	JButton addTaskButton = new JButton();
	JButton saveButton = new JButton();
	JLabel planningLabel = new JLabel(new ImageIcon("images/Planning.png"));

	// Constructor method
	public PlanningToolsPanel(User user) {
		createTable();
		addBackground();
		frameSetup();
		loadTasks(); // Load tasks when the frame is initialized
	}
	
	// Constructor method
	public PlanningToolsPanel(String username) {
		createTable();
		addBackground();
		frameSetup();
		loadTasks(); // Load tasks when the frame is initialized
	}


	private void createTable() {

		// Set up the table model and table
		tableModel = new DefaultTableModel(new String[] { "Task", "Description", "Assigned To" }, 0);
		table = new JTable(tableModel);
		table.setFont(new Font("SansSerif", Font.PLAIN, 16));
		table.setRowHeight(30);

		// Set custom cell renderer to add a black outline around each row
		DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
			Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				((JComponent) c).setBorder(blackBorder);
				return c;
			}
		};

		table.setDefaultRenderer(Object.class, cellRenderer);

		// Set up the scroll pane
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(269, 267, 1030, 460); // Position scrollPane within planningPanel
		planningPanel.add(scrollPane);

		addTransparentButton(addTaskButton, 258, 138, 170, 45, planningLabel);
		addTransparentButton(saveButton, 1190, 138, 136, 45, planningLabel);
	}

	// Add the labels to the background
	private void addBackground() {
		planningLabel.setBounds(0, 0, 1440, 900);
		planningPanel.add(planningLabel);
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

	private void frameSetup() {
		// First panel to be displayed
		add(planningPanel);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addTaskButton) {
			tableModel.addRow(new Object[] { "", "", "" });
		} else if (e.getSource() == saveButton) {
			saveTasks();
		}
	}

	// Load tasks from the database
	private void loadTasks() {
		try (Connection connection = getConnection()) {
			String sql = "SELECT task, description, assigned_to FROM tasks WHERE username = ?";
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setString(1, username);
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						String task = resultSet.getString("task");
						String description = resultSet.getString("description");
						String assignedTo = resultSet.getString("assigned_to");
						tableModel.addRow(new Object[] { task, description, assignedTo });
					}
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	// Save tasks to the database
	private void saveTasks() {
		try (Connection connection = getConnection()) {
			String deleteSql = "DELETE FROM tasks WHERE username = ?";
			try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
				deleteStatement.setString(1, username);
				deleteStatement.executeUpdate();
			}

			String insertSql = "INSERT INTO tasks (username, task, description, assigned_to) VALUES (?, ?, ?, ?)";
			try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
				for (int i = 0; i < tableModel.getRowCount(); i++) {
					String task = (String) tableModel.getValueAt(i, 0);
					String description = (String) tableModel.getValueAt(i, 1);
					String assignedTo = (String) tableModel.getValueAt(i, 2);
					insertStatement.setString(1, username);
					insertStatement.setString(2, task);
					insertStatement.setString(3, description);
					insertStatement.setString(4, assignedTo);
					insertStatement.executeUpdate();
				}
			}
			JOptionPane.showMessageDialog(this, "Tasks saved successfully!", "Success",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error saving tasks.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Method to get a connection to the database
	private Connection getConnection() throws SQLException {
		String url = "jdbc:mysql://localhost:3306/UserProjectsDB";
		String user = "root";
		String password = "Emmashi1!";
		return DriverManager.getConnection(url, user, password);
	}
}
