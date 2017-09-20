package photo_renamer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.*;

public class TagsKeeper {

	// use logger to be the Logger Object
	private final static Logger logger = Logger.getLogger(TagsKeeper.class.getName());
	private final static Handler consoleHandler = new ConsoleHandler();

	/**
	 * An arraylist of multiple strings which record all the tags content that
	 * has been used once
	 */
	public ArrayList<String> listOfAllTags = new ArrayList<String>();

	/**
	 * Creates a new empty TagsKeeper to store all tags' content that has been
	 * used
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public TagsKeeper(String filePath) throws ClassNotFoundException, IOException {

		logger.setLevel(Level.ALL);
		consoleHandler.setLevel(Level.ALL);
		logger.addHandler(consoleHandler);

		// if the file exists, first read out the stored data from the file
		// otherwise create a new file
		// Reads serializable objects from file.
		File file = new File(filePath);
		// Check whether the file exists or whether it is an empty file
		if (file.exists() && (file.length() != 0)) {
			readFromFile(filePath);
		} else {
			file.createNewFile();
		}
	}

	/**
	 * Get the arraylist of all tags from the file at path filePath.
	 * 
	 * @param path
	 *            the path of the data file
	 * @throws ClassNotFoundException
	 *             if filePath is not a valid path
	 */
	public void readFromFile(String path) throws ClassNotFoundException {
		try {
			InputStream file = new FileInputStream(path);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);

			// Deserialize the listOfAllTags
			listOfAllTags = (ArrayList<String>) input.readObject();
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "cannot read from input", e);
		}
	}

	/**
	 * Save the arraylist of all tags to the file at path filePath.
	 * 
	 * @param filepath
	 *            the path of the data file
	 * @throws IOException
	 */
	public void saveToFile(String filePath) throws IOException {
		OutputStream file = new FileOutputStream(filePath);
		OutputStream buffer = new BufferedOutputStream(file);
		ObjectOutput output = new ObjectOutputStream(buffer);

		// Serialize the listOfAllTags
		output.writeObject(listOfAllTags);
		output.close();
	}

}
