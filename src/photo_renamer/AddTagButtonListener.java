package photo_renamer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTextField;

/**
 * Observer design pattern:
 * 
 * This class is served as an observer class. When it receives
 * notification from PhotoRenamer, it's going to update the contents.
 */

/**
 * The listener for the button to add the chosen tag or tags. This is where most
 * of the work is done.
 */
public class AddTagButtonListener implements ActionListener {

	/** The area to let the user input the tag or tags they'd like to add */
	private JTextField textField;
	/**
	 * The list of showing all tags that have been added which refers to the
	 * master tag list
	 */
	private JList<String> tagList;
	/** The list of showing all files's names under the chosen directory */
	private JList<String> imageList;
	/** The combo box for showing the currently existing tags of the file */
	private JComboBox<String> tagComboBox;
	/** The combo box for showing all names the file had before */
	private JComboBox<String> nameComboBox;
	/** The button for the user to delete a tag */
	private JButton btnDelete;
	/** The button for the user to change the file's name back */
	private JButton btnChangeBack;

	/**
	 * An action listener for allowing the user to add a tag or multiple tags,
	 * updating the combo box of showing all currently tags the file has,
	 * updating the combo box of the name history of the file and updating the
	 * current name the file has.
	 * 
	 * @param textField
	 *            The area to let the user input the tag or tags they'd like to
	 *            add
	 * @param tagList
	 *            The list of showing all tags that have been added which refers
	 *            to the master tag list
	 * @param imageList
	 *            The list of showing all files's names under the chosen
	 *            directory
	 * @param tagComboBox
	 *            The combo box for showing all names the file had before
	 * @param nameComboBox
	 *            The combo box for showing all names the file had before
	 * @param btnDelete
	 *            The button for the user to delete a tag
	 * @param btnChangeBack
	 *            The button for the user to change the file's name back
	 */
	public AddTagButtonListener(JTextField textField, JList<String> tagList, JList<String> imageList,
			JComboBox<String> tagComboBox, JComboBox<String> nameComboBox, JButton btnDelete, JButton btnChangeBack) {
		this.textField = textField;
		this.tagList = tagList;
		this.nameComboBox = nameComboBox;
		this.imageList = imageList;
		this.tagComboBox = tagComboBox;
		this.btnChangeBack = btnChangeBack;
		this.btnDelete = btnDelete;
	}

	/**
	 * Handle the user clicking on the add tag button.
	 *
	 * @param e
	 *            the event object
	 */
	public void actionPerformed(ActionEvent e) {
		int index = PhotoRenamer.imagemodel.indexOf(PhotoRenamer.pr.getImage().getName());
		if (!tagList.getSelectedValuesList().isEmpty()) {
			PhotoRenamer.pr.addMultipleTags(tagList.getSelectedValuesList());
		}
		if (!textField.getText().isEmpty()) {
			for (String tag : textField.getText().split(",")) {
				if (!tag.trim().equals("")) {
					PhotoRenamer.pr.addTag(tag);
				}
			}
		}

		DefaultListModel<String> newNameModel = new DefaultListModel<String>();
		newNameModel = PhotoRenamer.imagemodel;
		newNameModel.setElementAt(PhotoRenamer.pr.getImage().getName(), index);
		imageList.setModel(newNameModel);

		// Updating the list of showing all files name once
		// one of the file's name in the list has been changed
		DefaultListModel<String> newTagModel = new DefaultListModel<String>();
		for (String tag : PhotoRenamer.pr.getListOfAllTags()) {
			newTagModel.addElement(tag);
		}
		tagList.setModel(newTagModel);

		// Clear the text field
		textField.setText("");

		// Updating the tagComboBox and if the file has no tags, the combo box
		// for the tags and the delete tag button will be disabled.
		tagComboBox.removeAllItems();
		for (String tag : PhotoRenamer.pr.getImage().getListOfTags()) {
			tagComboBox.addItem(tag);
		}
		if (!PhotoRenamer.pr.getImage().getListOfTags().isEmpty()) {
			btnDelete.setEnabled(true);
			tagComboBox.setEnabled(true);
		} else {
			btnDelete.setEnabled(false);
			tagComboBox.setEnabled(false);
		}

		// Updating the nameComboBox and if the file's name history only
		// contains one name, the combo box
		// for the names and the change back tag button will be disabled.
		nameComboBox.removeAllItems();
		for (String name : PhotoRenamer.pr.getImage().getListOfNames()) {
			nameComboBox.addItem(name);
		}
		if (PhotoRenamer.pr.getImage().getListOfNames().size() > 1) {
			nameComboBox.setEnabled(true);
			btnChangeBack.setEnabled(true);
		} else {
			nameComboBox.setEnabled(false);
			btnChangeBack.setEnabled(false);
		}

	}
}