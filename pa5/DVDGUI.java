package pa5;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 *  This class is an implementation of DVDUserInterface
 *  that uses JOptionPane to display the menu of command choices.
 */

public class DVDGUI implements DVDUserInterface {

	 private DVDCollection dvdlist;

	 public DVDGUI(DVDCollection dl)
	 {
		 dvdlist = dl;
	 }

	 public void processCommands()
	 {
		 String[] commands = {"Show all DVDs",
				 	"Add DVD",
				 	"Get DVDs By Rating",
				 	"Get Total Running Time",
				 	"Upcoming Releases",
				 	"Exit and Save"};

		 int choice;
		 ImageIcon im = new ImageIcon("netflix.png");
		 JPanel jp = new JPanel();
		 jp.setLayout(new GridLayout(0,1));
		 JButton[] buttons = {new JButton(commands[0]), new JButton(commands[1]), new JButton(commands[2]),
				 new JButton(commands[3]), new JButton(commands[4]), new JButton(commands[5])};

		 buttons[0].addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e)
		      {
				 doGetDVDs();
		      }
		 });
		 buttons[1].addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e)
		      {
				 doAddOrModifyDVD();
		      }
		 });
		 buttons[2].addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e)
		      {
				 doGetDVDsByRating();
		      }
		 });
		 buttons[3].addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e)
		      {
				 doGetTotalRunningTime();
		      }
		 });
		 buttons[4].addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e)
		      {
				 doUpcomingReleases();
		      }
		 });
		 buttons[5].addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e)
		      {
				 doSave();
				 System.exit(0);
		      }
		 });

		 jp.add(buttons[0]);
		 jp.add(buttons[1]);
		 jp.add(buttons[2]);
		 jp.add(buttons[3]);
		 jp.add(buttons[4]);
		 jp.add(buttons[5]);



		 do {
			 choice = JOptionPane.showOptionDialog(null,
					 jp,
					 "DVD Collection",
					 JOptionPane.DEFAULT_OPTION,
					 JOptionPane.INFORMATION_MESSAGE,
					 im,
					 new Object[]{},
					 null);

		 } while (choice != commands.length-1);
		 System.exit(0);
	 }

	private void doGetDVDs() {
		String[] titles = new String[dvdlist.getNumDvds()];

		for (int i = 0; i < dvdlist.getNumDvds(); i++)
		{
			titles[i] = dvdlist.getDvdArray()[i].getTitle();
		}

		JComboBox titlesList = new JComboBox(titles);

		int r = JOptionPane.showConfirmDialog(null, titlesList, "DVD Collection",
                JOptionPane.OK_CANCEL_OPTION);
		if (r == JOptionPane.CANCEL_OPTION) return;

		int tindex = titlesList.getSelectedIndex();

		String title = String.format("%s%nRated %s%nRuntime %s mins", dvdlist.getDvdArray()[tindex].getTitle()
					, dvdlist.getDvdArray()[tindex].getRating(), dvdlist.getDvdArray()[tindex].getRunningTime());

		String[] options = {"Modify", "Delete", "OK"};
		ImageIcon icon = new ImageIcon();

		if (dvdlist.getDvdArray()[tindex].getTitle().equalsIgnoreCase("The Shining"))
		{
			icon = new ImageIcon("The-Shining-icon.png");
		} else if (dvdlist.getDvdArray()[tindex].getTitle().equalsIgnoreCase("A Clockwork Orange"))
		{
			icon = new ImageIcon("A-Clockwork-Orange-icon.png");
		} else if (dvdlist.getDvdArray()[tindex].getTitle().equalsIgnoreCase("Terminator 2"))
		{
			icon = new ImageIcon("t2.jpeg");
		}

        int response = JOptionPane.showOptionDialog(null,
        		title,
        		dvdlist.getDvdArray()[tindex].getTitle(),
        		JOptionPane.YES_NO_OPTION,
                JOptionPane.DEFAULT_OPTION,
                icon,
                options,
                options[options.length - 1]
                );

        if (response == 0)
        {
        	JTextField t1 = new JTextField(15);
        	JTextField t2 = new JTextField(15);
        	JTextField t3 = new JTextField(15);

        	t1.setText(dvdlist.getDvdArray()[tindex].getTitle());
        	JPanel p = new JPanel(new GridLayout(0, 2));
        	p.add(new JLabel("Title: "));
        	t1.setEditable(false);
        	p.add(t1);
        	//p.add(Box.createHorizontalStrut(15));
        	p.add(new JLabel("Rating: "));
        	p.add(t2);
        	t2.setText(dvdlist.getDvdArray()[tindex].getRating());
        	p.add(new JLabel("Runtime: "));
        	p.add(t3);
        	t3.setText(String.valueOf(dvdlist.getDvdArray()[tindex].getRunningTime()));

        	String[] saveOrCancel = {"Save", "Cancel"};
            int pChoice = JOptionPane.showOptionDialog(null,
            		p,
            		dvdlist.getDvdArray()[tindex].getTitle(),
            		JOptionPane.YES_NO_OPTION,
                    JOptionPane.DEFAULT_OPTION,
                    null,
                    saveOrCancel,
                    saveOrCancel[saveOrCancel.length - 1]
                    );

            if (pChoice == 0)
            {
            	try {
            		if (!t2.getText().matches("PG|PG-13|R")) throw new NumberFormatException();
            		dvdlist.getDvdArray()[tindex].setRating(t2.getText());
        		} catch (NumberFormatException nfe) {
        			JOptionPane.showMessageDialog(null, "Rating must be PG, PG-13 or R.\nNo changes made.\nTry again.", "DVD Collection",
                            JOptionPane.QUESTION_MESSAGE);
        			return;
        		}
            	try {
            		dvdlist.getDvdArray()[tindex].setRunningTime(Integer.parseInt(t3.getText()));
        		} catch (NumberFormatException nfe) {
        			JOptionPane.showMessageDialog(null, "Running time must be a valid integer.\nNo changes made.\nTry again.", "DVD Collection",
                            JOptionPane.QUESTION_MESSAGE);
        			return;
        		}

            	doSave();

            	JOptionPane.showMessageDialog(null, "Changes saved.", "DVD Collection",
                        JOptionPane.QUESTION_MESSAGE);
            }

        }

        if (response == 1)
        {
        	int reply = JOptionPane.showConfirmDialog(null, "Are you sure?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        	if (reply == 0)
        	{
        		dvdlist.removeDVD(dvdlist.getDvdArray()[tindex].getTitle());
        	}
        }

	}

	private void doAddOrModifyDVD() {

		// Request the title
		String title = JOptionPane.showInputDialog("Enter title");
		if (title == null) {
			return;		// dialog was cancelled
		}
		title = title.toUpperCase();

		// Request the rating
		String rating = JOptionPane.showInputDialog("Enter rating for " + title);
		if (!rating.matches("PG|PG-13|R"))
		{
			JOptionPane.showMessageDialog(null, "Rating must be PG, PG-13 or R.\nNo changes made.", "DVD Collection",
                    JOptionPane.QUESTION_MESSAGE);
			return;
		}
		if (rating == null) {
			return;		// dialog was cancelled
		}
		rating = rating.toUpperCase();

		// Request the running time
		String time = JOptionPane.showInputDialog("Enter running time for " + title);
		if (time == null) {
		}

                // Add or modify the DVD (assuming the rating and time are valid
                dvdlist.addOrModifyDVD(title, rating, time);

	}

	private void doRemoveDVD() {

		// Request the title
		String title = JOptionPane.showInputDialog("Enter title");
		if (title == null) {
			return;		// dialog was cancelled
		}
		title = title.toUpperCase();

                // Remove the matching DVD if found
                dvdlist.removeDVD(title);

	}

	private void doGetDVDsByRating() {

		// Request the rating
		String rating = JOptionPane.showInputDialog("Enter rating");
		if (rating == null) {
			return;		// dialog was cancelled
		}
		rating = rating.toUpperCase();

                String results = dvdlist.getDVDsByRating(rating);

                JOptionPane.showMessageDialog(null, results, "DVD Collection",
                        JOptionPane.QUESTION_MESSAGE);

	}

        private void doGetTotalRunningTime() {

                int total = dvdlist.getTotalRunningTime();
                JOptionPane.showMessageDialog(null, total + " minutes total", "DVD Collection",
                        JOptionPane.QUESTION_MESSAGE);

        }

	private void doSave() {

		dvdlist.save();
	}

	private void doUpcomingReleases() {
		JPanel jp = new JPanel();
		ImageIcon image = new ImageIcon("pa5_images/aotd.jpg");
		JLabel label = new JLabel("", image, JLabel.CENTER);
		jp.add(label);

		int jo = JOptionPane.showOptionDialog(null,
				 jp,
				 "DVD Collection",
				 JOptionPane.DEFAULT_OPTION,
				 JOptionPane.INFORMATION_MESSAGE,
				 null,
				 null,
				 null);
	}

}
