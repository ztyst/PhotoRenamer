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
import java.util.HashMap;
import java.util.logging.*;

/**
 * NamesKeeper is saving and loading all names that a file has had.
 *
 */
public class NamesKeeper {
	private final static Logger logger = Logger.getLogger(NamesKeeper.class.getName());
	private final static Handler consoleHandler = new ConsoleHandler();

	/** A mapping of file's unchanged name to all names it has had. */
	public HashMap<String, ArrayList<String>> activitylist = new HashMap<String, ArrayList<String>>();

	/**
	 * Creates a new empty NamesKeeper.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public NamesKeeper(String filePath) throws ClassNotFoundException, IOException {

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
	 * Get the name map from the file at path filePath.
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

			// deserialize the Map
			activitylist = (HashMap<String, ArrayList<String>>) input.readObject();
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "cannot read from input", e);
		}
	}

	/**
	 * Writes the activitylist to file at filePath.
	 * 
	 * @param filePath
	 *            the file to write the records to
	 * @throws IOException
	 */
	public void saveToFile(String filePath) throws IOException {
		OutputStream file = new FileOutputStream(filePath);
		OutputStream buffer = new BufferedOutputStream(file);
		ObjectOutput output = new ObjectOutputStream(buffer);

		// Serialize the activitylist
		output.writeObject(activitylist);
		output.close();
	}

}
