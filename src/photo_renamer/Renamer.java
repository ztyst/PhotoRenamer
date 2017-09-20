package photo_renamer;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * MVC Design Pattern:
 * 
 * Renamer served as the controller class. When user input the information in the GUI, 
 * the program will use different methods in the Renamer class according 
 * to the input. Then the changed information will be updated to the PhotoRenamer class 
 * to show the user what is updated.
 */


/**
 * A tool designed for renaming an ImageFile. It allows the user to add/delete
 * tags of an ImageFile or change it back to an older name.
 * 
 * @see ImageFile
 */
public class Renamer {

	private ImageFile image;
	private static final Logger logger = Logger.getLogger(Renamer.class.getName());
	private static final String masterTagLoc = ChoosePathButtonListener.saveAP + File.separator + "taglist.txt";
	private static final String logHistoryLoc = ChoosePathButtonListener.saveAP + File.separator + "loglist.txt";
	private static final String nameDictLoc = ChoosePathButtonListener.saveAP + File.separator + "namelist.txt";
	protected TagsKeeper tagsKeeper;
	protected NamesKeeper namesKeeper;
	protected FileHandler fileHandler;

	/**
	 * Initializes a new Renamer with a given ImageFile. This will allow
	 * the Renamer to manage the name of that ImageFIle. Also create a file
	 * to save the log information if there is no such file, otherwise append
	 * the log information into the file
	 * 
	 * @param image
	 *            The ImageFile given by the user.
	 * @see ImageFile
	 */
	public Renamer(ImageFile image) {
		this.image = image;

		File logHis = new File(logHistoryLoc);
		if (!(logHis.exists())) {
			try {
				fileHandler = new FileHandler(logHistoryLoc);
				logger.addHandler(fileHandler);
				SimpleFormatter formatter = new SimpleFormatter();
				fileHandler.setFormatter(formatter);
			} catch (SecurityException | IOException e) {
				e.printStackTrace();
			}

		} else {
			try {
				fileHandler = new FileHandler(logHistoryLoc, true);
				logger.addHandler(fileHandler);
				SimpleFormatter formatter = new SimpleFormatter();
				fileHandler.setFormatter(formatter);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Returns the ImageFile managed by this Renamer.
	 * 
	 * @return The ImageFile managed by this Renamer.
	 * @see ImageFile
	 */
	public ImageFile getImage() {
		return image;
	}

	/**
	 * Reassigns the ImageFile managed by this Renamer to a new ImageFile
	 * given by the user.
	 * 
	 * @param image
	 *            The ImageFile given by the user for reassignment.
	 * @see ImageFile
	 */
	public void setImage(ImageFile image) {
		this.image = image;
	}

	/**
	 * Returns the list that contains all the tags that have been added so far.
	 * 
	 * @return The list that contains all the tags.
	 * @see TagsKeeper
	 */
	public List<String> getListOfAllTags() {
		try {
			this.tagsKeeper = new TagsKeeper(masterTagLoc);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return this.tagsKeeper.listOfAllTags;
	}

	/**
	 * This method is the core of Renamer.
	 * 
	 * Renames the ImageFile managed by this PhotoRenamer to the given string.
	 * This will create a new ImageFile located at the same path as the original
	 * ImageFile but named with the given new name, and then call the method
	 * renameTo(File) from the class ImageFile to change the path of the
	 * original ImageFile managed by this Renamer if the renaming process
	 * was successful. After all the above processes are done, reassign the
	 * original ImageFile to the new ImageFile.
	 * 
	 * 
	 * @param newName
	 *            The given new name for renaming.
	 * @see ImageFile
	 */
	public void renameTo(String newName) {
		
		// Create a new file which its name is the new name including the tag
		// and assign
		// the new file to the one we want to change.
		ImageFile imageWithNewName = new ImageFile(
				this.image.getParent() + ImageFile.separatorChar + newName + "." + this.image.getFormat());
		List<String> tagsFromNewName = imageWithNewName.getListOfTags();
		imageWithNewName.setListOfNames(this.image.getListOfNames());
		imageWithNewName.setListOfTags(tagsFromNewName);

		String nameWithoutFormat = this.image.getName().split("." + this.image.getFormat())[0];
		if (this.image.renameTo(imageWithNewName)) {
			logger.log(Level.INFO, nameWithoutFormat + " Successfully renamed to " + newName);
			if (!imageWithNewName.getListOfNames().contains(newName)) {
				imageWithNewName.appendNewName(newName);
			}
			this.setImage(imageWithNewName);

			// Write the name list into the file
			try {
				namesKeeper = new NamesKeeper(nameDictLoc);
				namesKeeper.activitylist.put(imageWithNewName.originalname,
						(ArrayList<String>) imageWithNewName.getListOfNames());
				namesKeeper.saveToFile(nameDictLoc);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}

		} else {
			logger.log(Level.INFO, nameWithoutFormat + " Fail to rename " + newName);
		}

	}

	/**
	 * This method is mainly served as a helper function for addTag(String) and
	 * addMultipleTags(String).
	 * 
	 * Renames the ImageFile managed by this Renamer. This will add the
	 * given tag to the ImageFile's file name by using the method
	 * Renamer.renameTo(String).
	 * 
	 * @param tagname
	 *            The tag given by the user to be added to the file name.
	 */
	public void renameIncludeTag(String tagname) {
		String nameWithoutFormat = this.image.getName().split("." + this.image.getFormat())[0];
		// Check whether the tag exists in the filename
		if (this.image.getListOfTags().contains(tagname)) {
			logger.log(Level.INFO,
					nameWithoutFormat + " Failed to add tag to the image, tag already existed. " + tagname);
		} else {
			this.renameTo(nameWithoutFormat + "@" + tagname);
		}
	}

	/**
	 * Renames the ImageFile managed by this Renamer with a new name that
	 * has the given tag deleted from the original name.
	 * 
	 * @param tagname
	 *            The tag given by the user to be deleted from the file name.
	 */
	public void deleteTag(String tagname) {
		this.image.removeTag(tagname);
		String newName = "";
		String[] list = this.image.getName().split("." + this.image.getFormat())[0].split("@");
		newName = list[0];
		String[] tempTagList = java.util.Arrays.copyOfRange(list, 1, list.length);
		for (String s : tempTagList) {
			if (!s.equals(tagname)) {
				if (this.image.getListOfTags().contains(s)) {
					newName += "@" + s;
				} else {
					newName += s;
				}
			}
		}

		this.renameTo(newName);
	}

	/**
	 * Renames the ImageFile managed by this Renamer with a new name that
	 * has the given tag added to the original name.
	 * 
	 * @param tagname
	 *            The tag given by the user to be added to the file name.
	 */
	public void addTag(String tagname) {
		try {
			tagsKeeper = new TagsKeeper(masterTagLoc);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		if (tagname.contains("@")) {
			String modifiedTag = "";
			for (String s : tagname.split("@")) {
				modifiedTag += s;
			}
			tagname = modifiedTag;
		}
		if (!tagname.isEmpty() && !tagname.equals(",")) {
			if (!tagsKeeper.listOfAllTags.contains(tagname)) {
				try {
					tagsKeeper.listOfAllTags.add(tagname);
					tagsKeeper.saveToFile(masterTagLoc);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			this.renameIncludeTag(tagname);
		}

	}

	/**
	 * Renames the ImageFile managed by this Renamer with a new name that
	 * has each tag from the given list of tags added to the original name.
	 * 
	 * @param tagList
	 *            The list of tags given by the user to be added to the file
	 *            name.
	 */
	public void addMultipleTags(List<String> tagList) {
		for (String t : tagList) {
			this.addTag(t);
		}
	}

	/**
	 * Renames the ImageFile managed by this Renamer by a selected used
	 * name of this ImageFile. The used name is selected by the given index of a
	 * name list of that ImageFile.
	 * 
	 * 
	 * @param nameList
	 *            The list of all the names that ImageFile has ever used.
	 * @param index
	 *            The index given by the user that indicates a certain name from
	 *            the nameList.
	 */
	public void changeNameBack(List<String> nameList, int index) {
		String selectedName = nameList.get(index);
		this.renameTo(selectedName);
	}

}