package pa5;
import java.util.*;
import javax.swing.*;

/**
 * 	Program to display and modify a simple DVD collection
 */

public class DVDManager {

	public static void main(String[] args) {

		DVDUserInterface dlInterface;
		DVDCollection dl = new DVDCollection();

		Scanner scan = new Scanner(System.in);

		String title = JOptionPane.showInputDialog(null, "Enter name of data file to load", "Select Database", JOptionPane.QUESTION_MESSAGE);
		String filename = title;
		dl.loadData(filename);

		dlInterface = new DVDGUI(dl);
		dlInterface.processCommands();

	}

}
