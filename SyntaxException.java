/**Author: Kevin Abrahams
 * Date: 01-27-2021
 * Description: SyntaxException - Provide a class causess an exception to be thrown if there is a syntax error.
 */

public class SyntaxException extends Exception {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1812533093666009581L;

	/**
	 * Constructor used to set error message
	 *
	 * @param message - Error message
	 */
	public SyntaxException(String message) {
		super(message);
	}
}
