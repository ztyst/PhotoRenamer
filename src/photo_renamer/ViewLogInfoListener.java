package photo_renamer;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Observer design pattern:
 * 
 * This class is served as an observer class. When it receives
 * notification from PhotoRenamer, it's going to update the contents.
 */

/**
 * The listener for the button to show the file containing all log information.
 * This is where most of the work is done.
 */
public class ViewLogInfoListener implements ActionListener {

	/** The window the button is in. */
	private JFrame mainframe;

	/**
	 * An action listener for popping up the file which contains all log
	 * information
	 * 
	 * @param mainframe
	 *            The window the button is in.
	 */
	public ViewLogInfoListener(JFrame mainframe) {
		this.mainframe = mainframe;
	}

	/**
	 * Handle the user clicking on the change back button.
	 * 
	 * @param e
	 *            the event object
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		File logHistory = new File(ChoosePathButtonListener.saveAP + File.separator + "loglist.txt");
		if (logHistory.exists()) {
			try {
				Desktop.getDesktop().open(logHistory);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(mainframe, "The File Is Not Existed or The Location Of File Is Not Selected ",
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

}
