/**Author: Kevin Abrahams
 * Date: 01-27-2021
 * Description: Main - Provide a class which serves as the main class for the application. This class is the starting point of the program, causing
 * the JFileChooser which initially prompts the user for an input file to appear.
 */
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Main class of the application
 *
 * @author Kevin
 */
public class Main {

	/**
	 * Main method of the application
	 *
	 * @param args - Array of command line arguments
	 * @throws IOException
	 * @throws SyntaxException
	 */
	public static void main(String[] args) throws IOException, SyntaxException {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileFilter(new FileNameExtensionFilter("OnlyText File", "txt"));
		if (JFileChooser.APPROVE_OPTION == jfc.showOpenDialog(null)) {
			String filepath = jfc.getSelectedFile().getAbsolutePath();
			LexicalAnalyzer lexer = new LexicalAnalyzer(filepath);
			Parser parser = new Parser(lexer);
			parser.parse();
		} else {
			System.out.println("File is not selected");
			System.exit(1);
		}

	}
}
