package pa5;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class DVDCollection {

	// Data fields

	/** The current number of DVDs in the array */
	private int numdvds;

	/** The array to contain the DVDs */
	private DVD[] dvdArray;

	/** The name of the data file that contains dvd data */
	private String sourceName;

	/** Boolean flag to indicate whether the DVD collection was
	    modified since it was last saved. */
	private boolean modified;

	/**
	 *  Constructs an empty directory as an array
	 *  with an initial capacity of 7. When we try to
	 *  insert into a full array, we will double the size of
	 *  the array first.
	 */
	public DVDCollection() {
		numdvds = 0;
		dvdArray = new DVD[7];
	}

	public String toString() {
		// Return a string containing all the DVDs in the
		// order they are stored in the array along with
		// the values for numdvds and the length of the array.
		// See homework instructions for proper format.
		String printCollection = String.format("numdvds = %d%ndvdArray.length = %d%n", numdvds, dvdArray.length);

		for (int i = 0; i < numdvds; i++)
		{
			printCollection += "dvdArray[" + i + "] = ";
			printCollection += dvdArray[i].getTitle() + "/" + dvdArray[i].getRating() + "/" + dvdArray[i].getRunningTime();
			printCollection += "\n";
		}

		return printCollection;
	}

	public void addOrModifyDVD(String title, String rating, String runningTime) {
		// NOTE: Be careful. Running time is a string here
		// since the user might enter non-digits when prompted.
		// If the array is full and a new DVD needs to be added,
		// double the size of the array first.
		//Validate input
		try {
			if (!rating.matches("PG|PG-13|R")) throw new NumberFormatException();
			int k = Integer.parseInt(runningTime);
		} catch (NumberFormatException nfe) {
			System.out.println("\nError: " + nfe);
			System.out.println("Please enter an integer running time and/or proper rating (PG, PG-13 or R)");
			System.out.println("No changes made.\n");
			return;
		}

		//First check if DVD is already in collection
		//Modify if it is
		boolean duplicate = false;
		for (int i = 0; i < numdvds; i++)
		{
			if (dvdArray[i].getTitle().equals(title))
			{


				JOptionPane.showMessageDialog(null, "Title already exists");
				duplicate = true;
				break;
				//dvdArray[i].setRating(rating);
				//dvdArray[i].setRunningTime(Integer.parseInt(runningTime));
			}
		}
		if (duplicate) return;

		//If the DVD collection is full, double its size
		if (numdvds >= dvdArray.length)
		{
			DVD[] tempArr = new DVD[dvdArray.length * 2];
			for (int i = 0; i < numdvds; i++)
			{
				tempArr[i] = dvdArray[i];
			}
			dvdArray = tempArr;
		}

		//Add the DVD to collection
		//Make sure collection array is not full
		//Then sort it
		if (numdvds < dvdArray.length)
		{
			DVD temp = new DVD(title, rating, Integer.parseInt(runningTime));
			dvdArray[numdvds] = temp;
			numdvds++;

			//Bubble sort collection into ABC
			//This algorithm has O(n^2) time efficiency
			for (int i = 0; i < numdvds - 1; i++)
			{
				for (int j = 0; j < numdvds - i - 1; j++)
				{
					if (dvdArray[j].getTitle().compareToIgnoreCase(dvdArray[j + 1].getTitle()) > 0)
					{
						DVD temp2 = dvdArray[j];
						dvdArray[j] = dvdArray[j + 1];
						dvdArray[j + 1] = temp2;
					}
				}
			}
		}
	}

	public void removeDVD(String title) {
		for (int i = 0; i < numdvds; i++)
		{
			if (dvdArray[i].getTitle().equals(title))
			{
				if (numdvds == 1)
				{
					DVD[] temp = new DVD[dvdArray.length];
					dvdArray = temp;
					numdvds--;
				} else {
					if (i == (numdvds - 1))
					{
						DVD[] temp = new DVD[dvdArray.length];
						for (int j = 0; j < numdvds-1; j++)
						{
							temp[j] = dvdArray[j];
						}
						dvdArray = temp;
					} else {
						System.arraycopy(dvdArray, i + 1, dvdArray, i, numdvds - 1 - i);
					}

					numdvds--;
					break;
				}
			}
		}
	}

	public String getDVDsByRating(String rating) {
		String ratingString = "";

		for (int i = 0; i < numdvds; i++)
		{
			if (dvdArray[i].getRating().compareToIgnoreCase(rating) == 0)
			{
				//ratingString += "dvdArray[" + i + "] = ";
				ratingString += dvdArray[i].getTitle() + "/" + dvdArray[i].getRating() + "/" + dvdArray[i].getRunningTime();
				ratingString += "\n";
			}
		}
		if (ratingString == "") ratingString = "No results found.";

		return ratingString;	// STUB: Remove this line.
	}

	public int getTotalRunningTime() {
		int runningTime = 0;

		for (int i = 0; i < numdvds; i++)
		{
			runningTime += dvdArray[i].getRunningTime();
		}

		return runningTime;
	}


	public void loadData(String filename) {
		sourceName = filename;

		try {
			File theFile = new File(filename);
			Scanner input = new Scanner(theFile);
			String[] sarray = {};

			while (input.hasNext())
			{
				sarray = input.nextLine().split(",");
			    addOrModifyDVD(sarray[0], sarray[1], sarray[2]);
			}

			input.close();
		} catch (FileNotFoundException e) {
			//System.out.println("\nError: " + e);
			//System.out.println("Creating output file " + sourceName + "\n");
			JOptionPane.showMessageDialog(null, "No such file or directory exists.\nCreating new file \"" + filename + "\""
					, "DVD Collection",
                    JOptionPane.QUESTION_MESSAGE);
		}

	}

	public void save() {
		try {
			PrintWriter output = new PrintWriter(new FileOutputStream(getSourceName(), false));

			for (int i = 0; i < numdvds; i++)
			{
				String s = String.format("%s,%s,%d", dvdArray[i].getTitle(), dvdArray[i].getRating(), dvdArray[i].getRunningTime());
				output.println(s);
			}

			output.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
	}

	// Additional private helper methods go here:
	String getSourceName() { return sourceName; }
	int getNumDvds() { return numdvds; }
	DVD[] getDvdArray() { return dvdArray; }

}