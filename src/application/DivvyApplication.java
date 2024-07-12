package application;

// Imports
import view.OnboardFrame;
/*
 * Name: Emma Shi 
 * Application Frame
 * Date: June 14th, 2024
 * Teacher: Mr. Fernandes
 * Course Code: ICS4U1
 * Description: This is a project management tool that aims to streamline the process of group management during projects. It allows users to create accounts
 * to see all the active projects they have, the tasks they need to do, contact information of members of their group, and provides planning tools to ease 
 * the entire process. 
 * Major Skills: MySQL Database, OOP Concepts, Lists, Map, ArrayLists, Methods, Algorithms, Java Swing Components, Counter-controlled loops, 
 * Selection Statements, Random function, LinkedList, Components, DefaultCellRenderer
 * Areas of Concern: 
 * - User is able to add and remove tasks to their calendar but it is not saved to the SQL database by referencing the username
 * - User is able to create a project that is added as a sub-table for their account. However, the UI is not updated for re-accessing/joining a pre-existing project 
 * - Column is created in SQL database for users to join a project if they enter the correct projectID, but ran out of time for implementing 
 * the UI changes for it
 * - Select project button doesn't do anything on the main page
 * - Planning tool navigates to a blank panel. If you want to see what it would look like, run the PlanningToolsFrame
 */

// This is the application class that runs the program
public class DivvyApplication {

	// Main method
	public static void main(String[] args) {
		new OnboardFrame();
	}
}
