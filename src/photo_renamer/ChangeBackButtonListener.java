package photo_renamer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;

/**
 * Observer design pattern:
 * 
 * This class is served as an observer class. When it receives
 * notification from PhotoRenamer, it's going to update the contents.
 */

/**
 * The listener for the button to change the name back. This is where most of
 * the work is done.
 */
public class ChangeBackButtonListener implements ActionListener {

	/** The combo box for showing all names the file had before */
	private JComboBox<String> nameComboBox;
	/** The combo box for showing the currently existing tags of the file */
	private JComboBox<String> tagComboBox;
	/** The list of showing all files's names under the chosen directory */
	private JList<String> imageList;
	/** The button for the user to delete a tag */
	private JButton btnDelete;

	/**
	 * An action listener for allowing the user to delete a certain tag of the
	 * file, updating the latest tags and name history the file currently has,
	 * and updating the current name the file has.
	 * 
	 * @param nameComboBox
	 *            The combo box for showing all names the file had
	 * @param tagComboBox
	 *            The combo box for showing all tags the file currently has
	 * @param imageList
	 *            The area of showing a list of all files' names
	 * @param btnDelete
	 *            The button for the user to delete a tag
	 */
	public ChangeBackButtonListener(JComboBox<String> nameComboBox, JComboBox<String> tagComboBox,
			JList<String> imageList, JButton btnDelete) {
		this.nameComboBox = nameComboBox;
		this.tagComboBox = tagComboBox;
		this.imageList = imageList;
		this.btnDelete = btnDelete;

	}

	/**
	 * Handle the user clicking on the change back button.
	 * 
	 * @param e
	 *            the event object
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		int index = PhotoRenamer.imagemodel.indexOf(PhotoRenamer.pr.getImage().getName());
		String newName = (String) nameComboBox.getSelectedItem();
		int nameindex = PhotoRenamer.pr.getImage().getListOfNames().indexOf(newName);
		PhotoRenamer.pr.changeNameBack(PhotoRenamer.pr.getImage().getListOfNames(), nameindex);

		// Updating the list of showing all files name once
		// one of the file's name in the list has been changed
		DefaultListModel<String> newNameModel = new DefaultListModel<String>();
		newNameModel = PhotoRenamer.imagemodel;
		newNameModel.setElementAt(PhotoRenamer.pr.getImage().getName(), index);
		imageList.setModel(newNameModel);

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

		// Updating the nameComboBox
		nameComboBox.removeAllItems();
		for (String name : PhotoRenamer.pr.getImage().getListOfNames()) {
			nameComboBox.addItem(name);
		}
	}

}
