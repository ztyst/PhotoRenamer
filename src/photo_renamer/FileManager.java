package photo_renamer;

import java.io.File;



/**
 * The File manager for the program to choose a directory. This is where showing
 * the list of images is done.
 */
public class FileManager{
	
	
	/**
	 * Build the tree of nodes rooted at file in the file system; note curr is
	 * the FileNode corresponding to file, so this only adds nodes for children
	 * of file to the tree. Precondition: file represents a directory.
	 * 
	 * @param file
	 *            the file or directory we are building
	 * @param curr
	 *            the node representing file
	 */
	public static void buildTree(File file, FileNode curr) {
		for (File file1 : file.listFiles()) {
			if (file1.isDirectory()) {
				FileNode curr1 = new FileNode(file1.getName(), curr, FileType.DIRECTORY);
				curr.addChild(file1.getName(), curr1);
				//buildTree(file1, curr1);
			} else {
				curr.addChild(file1.getName(), new FileNode(file1.getName(), curr, FileType.FILE));
			}

		}

	}

	/**
	 * Build a string buffer representation of the only image files' contents of
	 * the tree rooted at n, prepending each file name in each line.
	 *
	 * @param fileNode
	 *            the root of the subtree
	 * @param contents
	 *            the string to display
	 */
	public static void buildDirectoryContents(FileNode fileNode, StringBuffer contents) {

		// Check whether the fileNode is a directory or an image file.
		if (!fileNode.isDirectory() && (fileNode.getName().endsWith(".jpg") | fileNode.getName().endsWith(".jpeg")
				| fileNode.getName().endsWith(".png") | fileNode.getName().endsWith(".PNG")
				| fileNode.getName().endsWith(".JPG"))) {
			contents.append(fileNode.getName());
			contents.append("\n");
		}

		// Recursively call buildDirectoryContents in order to get all image
		// files under a specific directory
		if (fileNode.isDirectory()) {
			for (FileNode filenode1 : fileNode.getChildren()) {
				buildDirectoryContents(filenode1, contents);
			}
		}
	}

}
