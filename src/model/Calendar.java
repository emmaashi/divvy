package model;

// Imports
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 * Name: Emma Shi 
 * Calendar object
 * Date: June 14th, 2024
 * Teacher: Mr. Fernandes
 * Course Code: ICS4U1
 */

// This class creates the calendar that the JPanel is being updated to
public class Calendar extends JPanel implements ActionListener {

	// Fields
	private LocalDate currentDate;
	private JLabel monthYearLabel;
	private JTable calendarTable;
	private DefaultTableModel tableModel;
	private Map<LocalDate, List<Task>> tasks;
	private JButton simulateButton;

	// Constructor Method
	public Calendar() {
		currentDate = LocalDate.now();
		tasks = new HashMap<>();
		initializeUI();
	}

	// Utility methods
	private void initializeUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(618, 368)); // Set size of the panel

		monthYearLabel = new JLabel();
		monthYearLabel.setHorizontalAlignment(SwingConstants.CENTER);
		monthYearLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		
		//Create all the components of the calendar locally so that changes can be reflected
		JButton previousMonthButton = new JButton("<");
		previousMonthButton.setActionCommand("previousMonth");
		previousMonthButton.addActionListener(this);

		JButton nextMonthButton = new JButton(">");
		nextMonthButton.setActionCommand("nextMonth");
		nextMonthButton.addActionListener(this);

		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BorderLayout());
		headerPanel.add(previousMonthButton, BorderLayout.WEST);
		headerPanel.add(monthYearLabel, BorderLayout.CENTER);
		headerPanel.add(nextMonthButton, BorderLayout.EAST);

		String[] columnNames = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
		tableModel = new DefaultTableModel(columnNames, 0);
		calendarTable = new JTable(tableModel);
		calendarTable.setRowHeight(90); // Increase row height for better spacing
		calendarTable.setRowSelectionAllowed(true);
		calendarTable.setCellSelectionEnabled(true);

		calendarTable.setDefaultRenderer(Object.class, new CalendarCellRenderer());
		// Implement mouse clicked functionality
		calendarTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = calendarTable.rowAtPoint(evt.getPoint());
				int col = calendarTable.columnAtPoint(evt.getPoint());
				if (row >= 0 && col >= 0) {
					String day = (String) calendarTable.getValueAt(row, col);
					if (day != null && !day.isEmpty()) {
						LocalDate date = YearMonth.from(currentDate).atDay(Integer.parseInt(day));
						showTasksForDate(date);
					}
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(calendarTable);
		scrollPane.setPreferredSize(new Dimension(600, 250)); // Set preferred size for scroll pane
		add(headerPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);

		JButton addButton = new JButton("Add Task");
		addButton.setActionCommand("addTask");
		addButton.addActionListener(this);

		simulateButton = new JButton("Simulate Day");
		simulateButton.setActionCommand("simulateDay");
		simulateButton.addActionListener(this);

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(1, 2));
		controlPanel.add(addButton);
		controlPanel.add(simulateButton);

		add(controlPanel, BorderLayout.SOUTH);

		updateCalendar();
	}

	// This method updates the UI of the calendar
	private void updateCalendar() {
		DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
		monthYearLabel.setText(currentDate.format(monthYearFormatter));

		tableModel.setRowCount(0);
		YearMonth yearMonth = YearMonth.from(currentDate);
		LocalDate firstOfMonth = yearMonth.atDay(1);
		int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; // to start from Sunday

		int daysInMonth = yearMonth.lengthOfMonth();
		int rows = (dayOfWeek + daysInMonth + 6) / 7; // to determine the number of rows needed

		String[][] calendarData = new String[rows][7];

		int day = 1;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < 7; j++) {
				if (i == 0 && j < dayOfWeek || day > daysInMonth) {
					calendarData[i][j] = "";
				} else {
					calendarData[i][j] = String.valueOf(day++);
				}
			}
		}

		for (String[] rowData : calendarData) {
			tableModel.addRow(rowData);
		}
	}

	// This method adds a task to the calendar
	private void addTask() {
		String day = JOptionPane.showInputDialog(this, "Enter the day of the task:");
		if (day != null && !day.isEmpty()) {
			LocalDate date = YearMonth.from(currentDate).atDay(Integer.parseInt(day));
			String title = JOptionPane.showInputDialog(this, "Enter the task title:");
			String description = JOptionPane.showInputDialog(this, "Enter the task description:");

			String[] options = { "Red", "Orange", "Green" };
			int priorityOption = JOptionPane.showOptionDialog(this, "Select the priority of the task:", "Task Priority",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

			if (priorityOption == JOptionPane.CLOSED_OPTION) {
				return; // Exit if no priority is selected
			}

			String priority = options[priorityOption];

			if (title != null && !title.isEmpty()) {
				tasks.computeIfAbsent(date, k -> new ArrayList<>()).add(new Task(title, description, priority, date));
				JOptionPane.showMessageDialog(this, "Task added successfully.");
				updateCalendar();
			}
		}
	}

	// Displays the tasks for a certain date
	private void showTasksForDate(LocalDate date) {
		List<Task> taskList = tasks.get(date);
		if (taskList != null && !taskList.isEmpty()) {
			JPanel taskPanel = new JPanel(new BorderLayout());
			JTextArea taskDescriptions = new JTextArea();
			taskDescriptions.setEditable(false);
			StringBuilder descriptionBuilder = new StringBuilder();
			// Iterate through the list of tasks and append any changes
			for (Task task : taskList) {
				descriptionBuilder.append(task.getTitle()).append(" (").append(task.getPriority()).append("): ")
						.append(task.getDescription()).append("\n");
			}
			taskDescriptions.setText(descriptionBuilder.toString());
			taskPanel.add(new JScrollPane(taskDescriptions), BorderLayout.CENTER);

			// Implement an action listener for this button
			JButton deleteButton = new JButton("Delete Task");
			deleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String taskTitleToDelete = JOptionPane.showInputDialog(taskPanel,
							"Enter the title of the task to delete:");
					// Verify that the task exists
					if (taskTitleToDelete != null && !taskTitleToDelete.trim().isEmpty()) {
						boolean removed = taskList
								.removeIf(task -> task.getTitle().equalsIgnoreCase(taskTitleToDelete.trim()));
						if (removed) {
							JOptionPane.showMessageDialog(taskPanel, "Task deleted successfully.");
							if (taskList.isEmpty()) {
								tasks.remove(date);
							}
							updateCalendar();
						} else {
							JOptionPane.showMessageDialog(taskPanel, "Task not found.");
						}
					}
				}
			});
			taskPanel.add(deleteButton, BorderLayout.SOUTH);

			JOptionPane.showMessageDialog(this, taskPanel, "Tasks for " + date, JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "No tasks for this day.");
		}
	}

	private void simulateDay() {
		currentDate = currentDate.plusDays(1);
		updateCalendar();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		// Switch statement to get the command
		switch (command) {
		case "previousMonth":
			currentDate = currentDate.minusMonths(1);
			updateCalendar();
			break;
		case "nextMonth":
			currentDate = currentDate.plusMonths(1);
			updateCalendar();
			break;
		case "addTask":
			addTask();
			break;
		case "simulateDay":
			simulateDay();
			break;
		}
	}


	// Renders the colour of the calnedar depending on if the date has passed or not
	private class CalendarCellRenderer extends DefaultTableCellRenderer {
		private final Color lightGreen = new Color(185, 228, 170);
		private final Color redColor = new Color(255, 180, 180); // Red color for overdue tasks

		// REFERENCE: https://stackoverflow.com/questions/44048090/jtable-how-to-get-the-cell-as-a-component-for-a-given-position
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setVerticalAlignment(SwingConstants.TOP);

			if (value != null && !value.toString().isEmpty()) {
				int day = Integer.parseInt(value.toString());
				LocalDate dateToCheck = YearMonth.from(currentDate).atDay(day);
				StringBuilder cellText = new StringBuilder("<html>" + day);

				if (tasks.containsKey(dateToCheck)) {
					for (Task task : tasks.get(dateToCheck)) {
						String color;
						// Check if task is overdue
						if (task.getDueDate().isBefore(currentDate)) {
							color = "red"; // Set to red if overdue
						} else {
							switch (task.getPriority()) {
							case "Red":
								color = "red";
								break;
							case "Orange":
								color = "orange";
								break;
							case "Green":
								color = "green";
								break;
							default:
								color = "black";
							}
						}
						cellText.append("<br><font color='").append(color).append("'>&#9679;</font> ")
								.append(task.getTitle());
					}
				}
				cellText.append("</html>");
				setText(cellText.toString());

				// Set background color based on current date and overdue status
				if (dateToCheck.equals(currentDate)) {
					cell.setBackground(lightGreen); // Current date
				} else if (tasks.containsKey(dateToCheck)
						&& tasks.get(dateToCheck).stream().anyMatch(task -> task.getDueDate().isBefore(currentDate))) {
					cell.setBackground(redColor); // Overdue tasks
				} else {
					cell.setBackground(Color.WHITE); // Normal background color
				}

				// Bold font if there are tasks for this date
				if (tasks.containsKey(dateToCheck)) {
					cell.setFont(cell.getFont().deriveFont(Font.BOLD));
				}
			} else {
				cell.setBackground(Color.WHITE);
			}

			return cell;
		}
	}
}
