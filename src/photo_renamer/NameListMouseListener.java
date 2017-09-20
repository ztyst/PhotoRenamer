package photo_renamer;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * Observer design pattern:
 * 
 * This class is served as an observer class. When it receives
 * notification from PhotoRenamer, it's going to update the contents.
 */

/**
 * The listener for the mouse to choose an image. This is where most of the work
 * is done.
 */
public class NameListMouseListener implements MouseListener {

	/** The image that the user selected */
	private ImageFile imageSelected;
	/** The button for the user to add a tag */
	private JButton btnAddTag;
	/** The area to let the user input the tag or tags they'd like to add */
	private JTextField textField;
	/**
	 * The scroll panel allowing the user to view the master tag list by
	 * scrolling up and down
	 */
	private JScrollPane tagSP;
	/** The button for the user to delete a tag */
	private JButton btnDelete;
	/** The combo box for showing the currently existing tags of the file */
	private JComboBox<String> tagComboBox;
	/** The combo box for showing all names the file had before */
	private JComboBox<String> nameComboBox;
	/** The button for the user to change the name back */
	private JButton btnChangeBack;
	/** The label for displaying the selected image */
	private JLabel lblDisplayImage;

	/**
	 * An mouse listener to understand which file the user has clicked on and
	 * selected.
	 * 
	 * @param btnAddTag
	 *            The button for the user to add a tag
	 * @param textField
	 *            The area to let the user input the tag or tags they'd like to
	 *            add
	 * @param tagSP
	 *            The scroll panel allowing the user to view the master tag list
	 *            by scrolling up and down
	 * @param btnDelete
	 *            The button for the user to delete a tag
	 * @param tagComboBox
	 *            The combo box for showing the currently existing tags of the
	 *            file
	 * @param nameComboBox
	 *            The combo box for showing all names the file had before
	 * @param btnChangeBack
	 *            The button for the user to change the name back
	 * @param lblDisplayImage
	 *            The label for displaying the selected image
	 */
	public NameListMouseListener(JButton btnAddTag, JTextField textField, JScrollPane tagSP, JButton btnDelete,
			JComboBox<String> tagComboBox, JComboBox<String> nameComboBox, JButton btnChangeBack,
			JLabel lblDisplayImage) {
		this.btnAddTag = btnAddTag;
		this.textField = textField;
		this.tagSP = tagSP;
		this.btnDelete = btnDelete;
		this.tagComboBox = tagComboBox;
		this.nameComboBox = nameComboBox;
		this.btnChangeBack = btnChangeBack;
		this.lblDisplayImage = lblDisplayImage;
	}

	/**
	 * Handle the user clicking on the scroll panel to select the image.
	 * 
	 * @param e
	 *            the event object
	 */
	public void mouseClicked(MouseEvent mouseEvent) {
		JList<String> theList = (JList<String>) mouseEvent.getSource();
		if (mouseEvent.getClickCount() == 1) {
			int index = theList.locationToIndex(mouseEvent.getPoint());
			if (index >= 0) {
				Object o = theList.getModel().getElementAt(index);
				imageSelected = new ImageFile(ChooseDirButtonListener.fileAP + ImageFile.separator + o.toString());
				PhotoRenamer.pr = new Renamer(imageSelected);
				btnAddTag.setEnabled(true);
				textField.setEditable(true);
				tagSP.setEnabled(true);

				if (!imageSelected.getListOfTags().isEmpty()) {
					btnDelete.setEnabled(true);
					tagComboBox.setEnabled(true);
				} else {
					btnDelete.setEnabled(false);
					tagComboBox.setEnabled(false);
				}

				if (imageSelected.getListOfNames().size() > 1) {
					nameComboBox.setEnabled(true);
					btnChangeBack.setEnabled(true);
				} else {
					nameComboBox.setEnabled(false);
					btnChangeBack.setEnabled(false);
				}

				// Generate all the tags that the selected file has
				PhotoRenamer.tagmodel.clear();
				for (String tag : PhotoRenamer.pr.getListOfAllTags()) {
					PhotoRenamer.tagmodel.addElement(tag);
				}

				// Generate the tagComboBox to get the latest tags
				// for the selected image
				tagComboBox.removeAllItems();
				for (String tag : PhotoRenamer.pr.getImage().getListOfTags()) {
					tagComboBox.addItem(tag);
				}

				// Generate the nameComboBox which contains all the
				// names the file has had before
				nameComboBox.removeAllItems();
				for (String name : PhotoRenamer.pr.getImage().getListOfNames()) {
					nameComboBox.addItem(name);
				}

				try {
					Image image = ImageIO.read(imageSelected).getScaledInstance(224, 97, Image.SCALE_FAST);
					lblDisplayImage.setIcon(new ImageIcon(image));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
