package photo_renamer;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * MVC Design Pattern:
 * 
 * ImageFile class served as the model in this design pattern,
 * which contains the information of ImageFile objects 
 * 
 */

/**
 * A representation of an image file, keeps track of all the tags it has 
 * and all names it has ever used. This class is an extension of class File.
 * 
 * @see File
 */
public class ImageFile extends File {

	private static final long serialVersionUID = 1L;
	//the listOfNames contains all names that the file had before.
	private List<String> listOfNames = new ArrayList<String>();
	private List<String> listOfTags = new ArrayList<String>();
	private String format;
	// the name without any tag
	public String originalname;
	private NamesKeeper nh;
	private static final String namelist = ChoosePathButtonListener.saveAP + File.separator +  "namelist.txt";

	/**
	 * Creates a new ImageFile. When initiated, automatically generates
	 * lists of all the tags and all the used name that this ImageFile has.
	 * 
	 * @param pathname The pathname of the given image file in user's operating system.
	 */
	public ImageFile(String pathname) {
		super(pathname);
		
		// Get the originalname of the image file
		if (this.getName().contains("@")){
			this.originalname = this.getName().split("@")[0] + "." + this.format;
		}
		else {
			this.originalname = this.getName();
		}
		
		String[] splitedName = this.getName().split("\\.");
		this.format = splitedName[splitedName.length - 1];
		String nameWithoutFormat = this.getName().split("." + this.format)[0];
		listOfNames.add(nameWithoutFormat);
		
		// Save all the tags into the listOfTags.
		String[] tagsList = this.getName().split("@");
		for (int i = 1; i < tagsList.length; i++) {
			if (i == tagsList.length - 1) {
				this.listOfTags.add(tagsList[i].split("." + this.format)[0]);
			} else {
				this.listOfTags.add(tagsList[i]);
			}
		}
		
		// Generate the listOfNames which contains all names that the file had before.
		try {
			nh = new NamesKeeper(namelist);
			for (String key: nh.activitylist.keySet()){
				if (key.equals(this.originalname)){
					listOfNames = nh.activitylist.get(key);
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Returns a list representation of all the used names of this ImageFile.
	 * 
	 * @return A list that contains all the used names.
	 */
	public List<String> getListOfNames() {
		return listOfNames;
	}

	
	/**
	 * Reassigns the list that contains all the used name of this ImageFile 
	 * to a given name list.
	 * 
	 * @param listOfNames The given list of name for reassignment. 
	 */
	public void setListOfNames(List<String> listOfNames) {
		this.listOfNames = listOfNames;
	}

	
	/**
	 * Add the given name to the list that contains all the used name of this ImageFile.
	 * 
	 * @param newName the new name for appending.
	 */
	public void appendNewName(String newName){
		this.listOfNames.add(newName);
	}
	
	
	/**
	 * Returns a list representation of all the tags of this ImageFile.
	 * 
	 * @return A list that contains all tags of this ImageFile.
	 */
	public List<String> getListOfTags() {
		return listOfTags;
	}

	
	/**
	 * Reassigns the list that contains all the tags of this ImageFile 
	 * to a given tag list.
	 * 
	 * @param listOfTags The given list of tags for reassignment. 
	 */	
	public void setListOfTags(List<String> listOfTags) {
		this.listOfTags = listOfTags;
	}

	
	/**
	 * Remove the given tag from the list that contains all the tags of this ImageFile.
	 * 
	 * @param tagname the given tag for removing.
	 */
	public void removeTag(String tagname){
		this.listOfTags.remove(tagname);
	}
	
	
	
	/**
	 * Returns a string representation of the format of this ImageFile. 
	 * For example, the format of the ImgaeFile "photo.jpg" would be "jpg".
	 * 
	 * @return The image format of this ImageFile.
	 */	
	public String getFormat() {
		return format;
	}

	
	/**
	 * Overrides the toString() method from the superclass File.
	 * 
	 * Returns a string representation of this ImageFile, indicating the name 
	 * and location of this ImageFile in the System.
	 * 
	 * @return A string representation of this ImageFile.
	 */
	@Override
	public String toString() {
		return getName() + " at location: " + getParent();

	}


}
