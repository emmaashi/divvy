package model;

import java.time.LocalDate;
/*
 * Name: Emma Shi 
 * Task object
 * Date: June 14th, 2024
 * Teacher: Mr. Fernandes
 * Course Code: ICS4U1
 */

// This is the template class for a task
public class Task {
	// Fields
	private String title;
	private String description;
	private String priority;
	private LocalDate dueDate;
	private boolean isComplete;

	// Constructor method
	public Task(String title, String description, String priority, LocalDate dueDate) {
		this.title = title;
		this.description = description;
		this.priority = priority;
		this.dueDate = dueDate;
		this.isComplete = false;
	}

	// Getters and Setters
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean complete) {
		isComplete = complete;
	}
}
