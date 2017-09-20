package photo_renamer;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.JTextField;
import javax.swing.JComboBox;

/**
 * Observer design pattern:
 * 
 * The design of our GUI follows a observer design pattern,
 * where the PhototRenamer class is served as an observable class
 * and each button listener class is served as an observer class.
 * 
 * When a button from PhotoRenamer is clicked by the user, it will
 * automatically notify the button listener it belongs to. 
 * 
 * When that specific button listener received that notification 
 * it will perform the actions required.
 */

/**
 * MVC Design Pattern:
 * 
 * The design of our program follows MVC pattern. The model refers to ImageFile class,
 * the Controller refers to Renamer class which manages the imageFile objects, and the 
 * view refers to the PhotoRenamer class which creates the GUI for the program and 
 * display the information of the files. 
 */

/**
 * Create and show a photo renamer, which enables the user to select a image
 * file and rename it by adding, deleting tags or by changing to the name the
 * file had before.
 */
public class PhotoRenamer {

	private JFrame mainframe;
	static DefaultListModel<String> imagemodel = new DefaultListModel<String>();
	static DefaultListModel<String> tagmodel = new DefaultListModel<String>();
	private JTextField textField;
	static Renamer pr;

	/**
	 * Create the application PhotoRenamer.
	 */
	public PhotoRenamer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		// GUI

		mainframe = new JFrame("Photo Renamer");
		mainframe.setBounds(200, 200, 750, 400);
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JFileChooser filechooser = new JFileChooser();
		filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		mainframe.getContentPane().setLayout(null);

		// Buttons in the mainframe

		JButton btnViewLogInfo = new JButton("View Log Info");
		btnViewLogInfo.setBounds(542, 343, 117, 29);
		mainframe.getContentPane().add(btnViewLogInfo);

		JButton btnChooseDir = new JButton("Choose A Diretory");
		btnChooseDir.setBounds(58, 325, 159, 29);
		mainframe.getContentPane().add(btnChooseDir);
		btnChooseDir.setEnabled(false);

		JButton btnDelete = new JButton("Delete Tag");
		btnDelete.setBounds(551, 101, 108, 29);
		mainframe.getContentPane().add(btnDelete);
		btnDelete.setEnabled(false);

		JButton btnChangeBack = new JButton("Change Back");
		btnChangeBack.setBounds(551, 218, 108, 29);
		mainframe.getContentPane().add(btnChangeBack);
		btnChangeBack.setEnabled(false);

		JButton btnAddTag = new JButton("Add Tag");
		btnAddTag.setBounds(326, 325, 117, 29);
		mainframe.getContentPane().add(btnAddTag);
		btnAddTag.setEnabled(false);

		JButton btnChoosePath = new JButton("Choose");
		btnChoosePath.setBounds(542, 302, 117, 29);
		mainframe.getContentPane().add(btnChoosePath);

		// Labels in the mainframe

		JLabel lblSelectDir = new JLabel(" Select a directory");
		lblSelectDir.setBounds(17, 13, 535, 18);
		lblSelectDir.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		mainframe.getContentPane().add(lblSelectDir);

		JLabel lblDeleteTag = new JLabel("Delete Tag");
		lblDeleteTag.setBounds(572, 34, 67, 16);
		mainframe.getContentPane().add(lblDeleteTag);

		JLabel lblChangeNameBack = new JLabel("Change Name Back");
		lblChangeNameBack.setBounds(542, 142, 130, 29);
		mainframe.getContentPane().add(lblChangeNameBack);

		JLabel lblCurrentlyAvailableTags = new JLabel("Select one or more tags :");
		lblCurrentlyAvailableTags.setBounds(296, 47, 174, 16);
		mainframe.getContentPane().add(lblCurrentlyAvailableTags);

		JLabel lblPlease = new JLabel("Type the new tags here");
		lblPlease.setBounds(296, 270, 174, 20);
		mainframe.getContentPane().add(lblPlease);

		JLabel lblInstruction = new JLabel("Using ',' between each");
		lblInstruction.setBounds(296, 290, 174, 15);
		mainframe.getContentPane().add(lblInstruction);

		JLabel lblAddTags = new JLabel("Add Tags");
		lblAddTags.setBounds(296, 34, 61, 16);
		mainframe.getContentPane().add(lblAddTags);

		JLabel lblDisplayImage = new JLabel("");
		lblDisplayImage.setBounds(29, 41, 224, 97);
		mainframe.getContentPane().add(lblDisplayImage);

		// The JLists in the mainframe

		JList<String> imageList = new JList<>(imagemodel);
		imageList.setBounds(17, 150, 250, 175);
		mainframe.getContentPane().add(imageList);
		// Allow the user to choose one file each time
		imageList.setSelectionMode(0);

		JList<String> tagList = new JList<>(tagmodel);
		tagList.setBounds(296, 65, 174, 210);
		mainframe.getContentPane().add(tagList);

		// The Scroll panels in the mainframe
		JScrollPane imageSP = new JScrollPane(imageList);
		imageSP.setBounds(17, 150, 250, 175);
		mainframe.getContentPane().add(imageSP);
		imageSP.setEnabled(false);

		JScrollPane tagSP = new JScrollPane(tagList);
		tagSP.setBounds(296, 65, 174, 210);
		mainframe.getContentPane().add(tagSP);
		tagSP.setEnabled(false);

		textField = new JTextField();
		textField.setBounds(296, 303, 174, 26);
		textField.setEditable(false);
		mainframe.getContentPane().add(textField);
		textField.setColumns(10);

		// The Combo Boxes in the mainframe
		JComboBox<String> tagComboBox = new JComboBox<String>();
		tagComboBox.setBounds(484, 62, 260, 27);
		mainframe.getContentPane().add(tagComboBox);
		tagComboBox.setEditable(false);
		tagComboBox.setEnabled(false);

		JComboBox<String> nameComboBox = new JComboBox<String>();
		nameComboBox.setBounds(484, 170, 260, 27);
		mainframe.getContentPane().add(nameComboBox);
		nameComboBox.setEditable(false);
		nameComboBox.setEnabled(false);

		// ButtonListeners

		// The listener for choosePathButton.
		ActionListener btnChoosePathListener = new ChoosePathButtonListener(mainframe, filechooser, btnChooseDir,
				btnChoosePath);
		btnChoosePath.addActionListener(btnChoosePathListener);

		// The listener for addTagButton.
		ActionListener btnAddTagListener = new AddTagButtonListener(textField, tagList, imageList, tagComboBox,
				nameComboBox, btnDelete, btnChangeBack);
		btnAddTag.addActionListener(btnAddTagListener);

		// The listener for deleteTagButton.
		ActionListener btnDeleteListener = new DeleteTagButtonListener(tagComboBox, nameComboBox, imageList, btnDelete);
		btnDelete.addActionListener(btnDeleteListener);

		// The listener for changeNameBackButton.
		ActionListener btnChangeBackListener = new ChangeBackButtonListener(nameComboBox, tagComboBox, imageList,
				btnDelete);
		btnChangeBack.addActionListener(btnChangeBackListener);

		// The listener for chooseDirButton.
		ActionListener btnChooseDirListener = new ChooseDirButtonListener(mainframe, textField, tagComboBox,
				nameComboBox, btnDelete, btnChangeBack, filechooser, imageSP, tagSP, btnAddTag, lblDisplayImage,
				lblSelectDir);
		btnChooseDir.addActionListener(btnChooseDirListener);

		// The listener for clicking the mouse in the scroll panel contain the
		// files' name list
		MouseListener mouseListener = new NameListMouseListener(btnAddTag, textField, tagSP, btnDelete, tagComboBox,
				nameComboBox, btnChangeBack, lblDisplayImage);
		imageList.addMouseListener(mouseListener);

		// The listener for viewLogInfoButton.
		ActionListener btnViewLogInfoListener = new ViewLogInfoListener(mainframe);
		btnViewLogInfo.addActionListener(btnViewLogInfoListener);
	}

	/**
	 * Launch the application PhotoRenamer.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PhotoRenamer window = new PhotoRenamer();
					window.mainframe.setVisible(true);
					JOptionPane.showMessageDialog(window.mainframe,
							"Please select a Directory to store the log file.\nPress the choose button on the right corner to begin.",
							"Warning", JOptionPane.WARNING_MESSAGE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
