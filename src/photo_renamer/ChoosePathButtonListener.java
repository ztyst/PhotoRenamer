package photo_renamer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Observer design pattern:
 * 
 * This class is served as an observer class. When it receives
 * notification from PhotoRenamer, it's going to update the contents .
 */

/**
 * The listener for the button to choose a directory to save the log information
 * and save the master tag list and each files' name history. This is where most
 * of the work is done.
 */
public class ChoosePathButtonListener implements ActionListener {

	/** The window the button is in. */
	private JFrame mainFrame;
	/** The file chooser to use when the user clicks. */
	private JFileChooser fileChooser;
	/** The button for the user to choose a directory showing the images */
	private JButton btnChooseDir;
	/**
	 * The button for the user to choose a directory to be the location saving the log information
	 * and save the master tag list and each files' name history.
	 */
	private JButton btnChoosePath;
	/** The absolute path for the files */
	public static String saveAP = "";

	/**
	 * An action listener for window mainFrame, using fileChooser to choose a
	 * directory to be the location saving the log information and the master tag list and each
	 * files' name history.
	 * 
	 * @param mainframe
	 *            the main window
	 * @param fileChooser
	 *            the file chooser to use
	 * @param btnChooseDir
	 *            The button for the user to delete a tag
	 * @param btnChoosePath
	 *            The button for the user to choose a directory to save the log
	 *            information and save the master tag list and each files' name
	 *            history.
	 */
	public ChoosePathButtonListener(JFrame mainframe, JFileChooser fileChooser, JButton btnChooseDir,
			JButton btnChoosePath) {
		this.mainFrame = mainframe;
		this.fileChooser = fileChooser;
		this.btnChooseDir = btnChooseDir;
		this.btnChoosePath = btnChoosePath;

	}

	/**
	 * Handle the user clicking on the choose path button.
	 *
	 * @param e
	 *            the event object
	 */
	public void actionPerformed(ActionEvent e) {
		int returnVal = fileChooser.showOpenDialog(mainFrame.getContentPane());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			saveAP = file.getAbsolutePath();
			btnChooseDir.setEnabled(true);
			btnChoosePath.setEnabled(false);

		} else {
			JOptionPane.showMessageDialog(mainFrame, "Please Select A Directory Again", "Warning",
					JOptionPane.WARNING_MESSAGE);
		}

	}

}
