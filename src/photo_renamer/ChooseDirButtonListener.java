package photo_renamer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * Observer design pattern:
 * 
 * This class is served as an observer class. When it receives
 * notification from PhotoRenamer, it's going to update the contents.
 */

/**
 * The listener for the button to choose the directory to show the images. This
 * is where most of the work is done.
 */
public class ChooseDirButtonListener implements ActionListener {

	/** The window the button is in. */
	private JFrame mainFrame;
	/** The area to let the user input the tag or tags they'd like to add */
	private JTextField textField;
	/** The combo box for showing the currently existing tags of the file */
	private JComboBox<String> tagComboBox;
	/** The combo box for showing all names the file had before */
	private JComboBox<String> nameComboBox;
	/** The button for the user to delete a tag */
	private JButton btnDelete;
	/** The button for the user to change the file's name back */
	private JButton btnChangeBack;
	/** The button for the user to add the tags */
	private JButton btnAddTag;
	/** The file chooser to use when the user clicks. */
	private JFileChooser fileChooser;
	/**
	 * The label to display the image the user selected under the chosen
	 * directory.
	 */
	private JLabel lblDisplayImage;
	/** The label for the full path to the chosen directory. */
	private JLabel lblSelectDir;
	/**
	 * The scroll panel allowing the user scrolling up and down to select the
	 * image
	 */
	private JScrollPane imageSP;
	/**
	 * The scroll panel allowing the user scrolling up and down to select the
	 * tag or tags
	 */
	private JScrollPane tagSP;
	/** the absolute path of the selected file */
	public static String fileAP;

	/**
	 * An action listener for window mainFrame, displaying a file path on
	 * dirLabel, using fileChooser to choose a file, updating the latest tags
	 * and name history the file currently has, and updating the current name
	 * the file has.
	 * 
	 * @param mainFrame
	 *            the main window
	 * @param textField
	 *            The area to let the user input the tag or tags they'd like to
	 *            add
	 * @param tagComboBox
	 *            The combo box for showing all names the file had before
	 * @param nameComboBox
	 *            The combo box for showing all names the file had before
	 * @param btnDelete
	 *            The button for the user to delete a tag
	 * @param btnChangeBack
	 *            The button for the user to change the file's name back
	 * @param fileChooser
	 *            the file chooser to use
	 * @param imageSP
	 *            The scroll panel allowing the user scrolling up and down to
	 *            select the tag or tags
	 * @param tagSP
	 *            The scroll panel allowing the user scrolling up and down to
	 *            select the tag or tags
	 * @param btnAddTag
	 *            The button for the user to add a tag
	 * @param lblDisplayImage
	 *            The label to display the image the user selected under the
	 *            chosen directory.
	 * @param lblSelectDir
	 *            the label for the directory path
	 */

	public ChooseDirButtonListener(JFrame mainFrame, JTextField textField, JComboBox<String> tagComboBox,
			JComboBox<String> nameComboBox, JButton btnDelete, JButton btnChangeBack, JFileChooser fileChooser,
			JScrollPane imageSP, JScrollPane tagSP, JButton btnAddTag, JLabel lblDisplayImage, JLabel lblSelectDir) {
		this.mainFrame = mainFrame;
		this.textField = textField;
		this.nameComboBox = nameComboBox;
		this.tagComboBox = tagComboBox;
		this.btnChangeBack = btnChangeBack;
		this.btnDelete = btnDelete;
		this.lblDisplayImage = lblDisplayImage;
		this.lblSelectDir = lblSelectDir;
		this.btnAddTag = btnAddTag;
		this.fileChooser = fileChooser;
		this.imageSP = imageSP;
		this.tagSP = tagSP;
	}

	/**
	 * Handle the user clicking on the open button.
	 *
	 * @param e
	 *            the event object
	 */
	public void actionPerformed(ActionEvent e) {

		int returnVal = fileChooser.showOpenDialog(mainFrame.getContentPane());
		lblDisplayImage.setText("");

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			fileAP = file.getAbsolutePath();
			if (file.exists()) {
				lblSelectDir.setText("Selected Directory " + fileAP);
				FileNode fileTree = new FileNode(file.getName(), null, FileType.DIRECTORY);
				FileManager.buildTree(file, fileTree);

				// Generate the list of images
				StringBuffer contents = new StringBuffer();
				FileManager.buildDirectoryContents(fileTree, contents);
				String[] temp = contents.toString().split("\n");
				if (temp[0].contains(".")) {
					int index = 0;
					PhotoRenamer.imagemodel.clear();
					for (String item : temp) {
						PhotoRenamer.imagemodel.insertElementAt(item, index);
						index++;
					}
					imageSP.setEnabled(true);
				} else {
					PhotoRenamer.imagemodel.clear();
					lblSelectDir.setText("No Image in the Directory. Choose Again");
				}

				PhotoRenamer.tagmodel.clear();
				lblDisplayImage.setIcon(null);
				imageSP.setEnabled(false);
				tagSP.setEnabled(false);
				btnChangeBack.setEnabled(false);
				btnAddTag.setEnabled(false);
				btnDelete.setEnabled(false);
				tagComboBox.setEnabled(false);
				tagComboBox.removeAllItems();
				nameComboBox.setEnabled(false);
				nameComboBox.removeAllItems();
				textField.setEditable(false);

			}
		} else {
			lblSelectDir.setText("No Path Selected");

		}

	}
}
