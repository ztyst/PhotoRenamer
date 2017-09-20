/**
 * 
 */
package photo_renamer;

import java.util.*;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Provides unit test cases for TagsKeeper, check if it manages the tag list
 * file in the user's OS properly.
 * 
 * @author chenz123
 * @author zhaoti31
 *
 */
public class TagsKeeperTest {

	private static ImageFile image;
	private static File tagListFile;
	private static TagsKeeper tagsKeeper;
	private static Renamer renamer;

	// Please select your own image path and
	// directory path for your tag list file.

	// Select a image file with no tag attached on it.
	private static final String IMAGE_PATH = "C:/Users/Kyle/Desktop/5.23.png";
	// Make sure that the file called 'taglist.txt' doesn't exist 
	// in the selected directory.
	private static final String TAG_LIST_FILE_DIR = "C:/Users/Kyle/Desktop";

	/**
	 * Set the image file that can be applied for the test cases, it will also
	 * initiate a renamer to perform add/delete tag(s) to the assigned image
	 * file.
	 * 
	 * @throws java.io.IOException
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		ChoosePathButtonListener.saveAP = TAG_LIST_FILE_DIR;
		image = new ImageFile(IMAGE_PATH);
		renamer = new Renamer(image);
	}

	/**
	 * Create a new tag list text file in the user's OS that is used to store
	 * all the ever added tags. Also create a tagsKeeper assigned with the given
	 * tag list text file's file path.
	 * 
	 * @throws ClassNotFoundException
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws IOException, ClassNotFoundException {
		tagListFile = new File(ChoosePathButtonListener.saveAP + File.separator + "taglist.txt");
		tagsKeeper = new TagsKeeper(tagListFile.getAbsolutePath());
	}

	/**
	 * Delete the tag list test file every time a test case is done, and also
	 * rename the given image file back to it's original name.
	 * 
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws IOException {
		// Delete the txt file that contains the tag List,
		// to make sure it's empty when we run the test.
		tagListFile.delete();
		// Change the name back to it's original name.
		renamer.renameTo(image.originalname.split("." + image.getFormat())[0]);

	}

	/**
	 * Test if an empty string is added to the tag list file when the user
	 * attempt to add an empty string to an image file.
	 * 
	 * Note that An empty string is not supposed to be added to the tag list.
	 */
	@Test
	public void testIfEmptyStringIsAdded() {
		renamer.addTag("a");
		renamer.addTag("1");
		renamer.addTag("");
		try {
			tagsKeeper.readFromFile(tagListFile.getAbsolutePath());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		List<String> actual = tagsKeeper.listOfAllTags;
		List<String> expected = new ArrayList<String>();
		expected.add("a");
		expected.add("1");
		assertEquals("Empty string is not supposed to be added.", expected, actual);
	}

	/**
	 * Test if an IOException is thrown when an invalid file path is given to
	 * the constructor.
	 */
	@Test
	public void testIfIOExceptionIsThrownOnInvalidInput() {
		boolean exceptionThrown = false;
		try {
			tagsKeeper = new TagsKeeper("This/is/not/a/valid/path");
			fail("IOException is not thrown");
		} catch (IOException e) {
			exceptionThrown = true;
		} catch (ClassNotFoundException e) {
			fail("IOException is not thrown");
		}
		assertEquals(true, exceptionThrown);
	}

	/**
	 * Test if "@" can be added to the tag list file.
	 * 
	 * Note that "@" is not supposed to be added to the tag list.
	 */
	@Test
	public void testIfAtSignIsAdded() {
		renamer.addTag("a");
		renamer.addTag("1");
		renamer.addTag("!");
		renamer.addTag("@");
		renamer.addTag("#");
		renamer.addTag("$");
		renamer.addTag("%");
		renamer.addTag("^");
		renamer.addTag("&");
		try {
			tagsKeeper.readFromFile(tagListFile.getAbsolutePath());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		List<String> actual = tagsKeeper.listOfAllTags;
		List<String> expected = new ArrayList<String>();
		expected.add("a");
		expected.add("1");
		expected.add("!");
		expected.add("#");
		expected.add("$");
		expected.add("%");
		expected.add("^");
		expected.add("&");
		assertEquals("'@' is not supposed to be added.", expected, actual);
	}

	/**
	 * Test if the tagsKeeper can handle complex inputs.
	 * 
	 * Note that "@" and repeated tags are not supposed to be added to the tag
	 * list.
	 */
	@Test
	public void testAddLongTagMixedWithAtSignAndRepeatedTags() {
		renamer.addTag("a#$%I@@DFgdfg@vw87u65");
		renamer.addTag("1weijh");
		renamer.addTag("ijh");
		renamer.addTag("1we@@@ijh");
		try {
			tagsKeeper.readFromFile(tagListFile.getAbsolutePath());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		List<String> actual = tagsKeeper.listOfAllTags;
		List<String> expected = new ArrayList<String>();
		expected.add("a#$%IDFgdfgvw87u65");
		expected.add("1weijh");
		expected.add("ijh");

		assertEquals("'@' and repeted tags are not supposed to be added.", expected, actual);
	}

	/**
	 * Test if a deleted tag remains in the tag list file after deleted that tag
	 * from an image file.
	 * 
	 * Note that the deleted tag should remain in the tag list.
	 */
	@Test
	public void testIfDeletedTagsRemains() {
		renamer.addTag("a");
		renamer.addTag("b");
		renamer.addTag("c");
		renamer.addTag("d");
		renamer.deleteTag("a");
		renamer.deleteTag("b");
		renamer.deleteTag("c");
		renamer.deleteTag("d");
		// All ever added tags are supposed to be saved in the tag list,
		// even if they are deleted from the image file.
		try {
			tagsKeeper.readFromFile(tagListFile.getAbsolutePath());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		List<String> actual = tagsKeeper.listOfAllTags;
		List<String> expected = new ArrayList<String>();
		expected.add("a");
		expected.add("b");
		expected.add("c");
		expected.add("d");
		assertEquals("All ever added tags must be saved in the tag list file.", expected, actual);
	}

	/**
	 * Test if tags handled by tagsKeeper are read and save properly.
	 */
	@Test
	// These two methods are closely related to each other,
	// so we decided to test them together.
	public void testSaveToFileAndReadFromFile() {
		// The File is supposed to be updated during the process of addTag().
		renamer.addTag("a");
		renamer.addTag("b");
		renamer.addTag("c");
		renamer.addTag("d");
		// Read the File to update the listOfAllTags.
		try {
			tagsKeeper.readFromFile(tagListFile.getAbsolutePath());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		List<String> actual = tagsKeeper.listOfAllTags;
		List<String> expected = new ArrayList<String>();
		expected.add("a");
		expected.add("b");
		expected.add("c");
		expected.add("d");
		assertEquals("Did't save all the tags the user added or didn't read from file properly.", expected, actual);
	}

	/**
	 * Test if multiple currently existing tag can 
	 * be added to the tag list file.
	 */
	@Test
	public void testAddManyTagsWithManyCurrentlyExistingTags() {
		renamer.addTag("a");
		renamer.addTag("b");
		renamer.addTag("c");
		renamer.addTag("d");
		renamer.addTag("a");
		renamer.addTag("c");
		renamer.addTag("d");
		renamer.addTag("a");
		try {
			tagsKeeper.readFromFile(tagListFile.getAbsolutePath());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		List<String> actual = tagsKeeper.listOfAllTags;
		List<String> expected = new ArrayList<String>();
		expected.add("a");
		expected.add("b");
		expected.add("c");
		expected.add("d");
		assertEquals("A currently existing tag shouldn't be added again.", expected, actual);
	}

	/**
	 * Test if a currently existing tag can be added to the tag list file.
	 */
	@Test
	public void testAddManyTagsWithOneCurrentlyExistingTag() {
		renamer.addTag("a");
		renamer.addTag("b");
		renamer.addTag("c");
		renamer.addTag("d");
		renamer.addTag("a");
		try {
			tagsKeeper.readFromFile(tagListFile.getAbsolutePath());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		List<String> actual = tagsKeeper.listOfAllTags;
		List<String> expected = new ArrayList<String>();
		expected.add("a");
		expected.add("b");
		expected.add("c");
		expected.add("d");
		assertEquals("A currently existing tag shouldn't be added again.", expected, actual);
	}

}