/**Author: Kevin Abrahams
 * Date: 01-27-2021
 * Description: LexicalAnalyzer - Provide a class which helps analyze and tokenize the contents of the input file selected by the user
 * which stores the contents of the GUI to be created. This class helps facilitate the conversion of the input file to java code. 
 */
import java.io.*;

/**
 * Implementation of lexical analyzer
 *
 * @author Kevin
 */
public class LexicalAnalyzer {

	private final StreamTokenizer tokenizer;// Stream Tokenizer to tokenize the file
	private final char punctuations[] = { ',', ':', ';', '.', '(', ')' };// Supported functuation symbols
	private final Token[] punctuationTokens = { Token.COMMA, Token.COLON, Token.SEMICOLON, Token.PERIOD,
			Token.OPENING_ROUND_BRACE, Token.CLOSING_ROUND_BRACE };// Tokens for punctuation symbols

	/**
	 * Constructor to tokenize the input file
	 *
	 * @param fileName- Input file name
	 * @throws FileNotFoundException - Exception if file is not found
	 */
	public LexicalAnalyzer(String fileName) throws FileNotFoundException {
		tokenizer = new StreamTokenizer(new FileReader(fileName));// Tokenize the input file
		tokenizer.ordinaryChar('.');// Ordinary Symbol
		tokenizer.quoteChar('"');// Quote Symbol
	}

	/**
	 * Get the next token type type
	 *
	 * @return Next token
	 * @throws SyntaxException
	 * @throws IOException
	 */
	public Token getNextTokenType() throws SyntaxException, IOException {
		int nextToken = tokenizer.nextToken();
		switch (nextToken) {
		case StreamTokenizer.TT_NUMBER:
			return Token.NUMBER;// Return Number type
		case StreamTokenizer.TT_WORD:
			for (Token token : Token.values()) {
				if (token.name().equals(tokenizer.sval.toUpperCase())) {
					return token;
				}
			}
			throw new SyntaxException(getLineNumber() + " Invalid token " + getToken());
		case StreamTokenizer.TT_EOF:
			return Token.EOF;// End of file token
		case '"':
			return Token.STRING;// String token
		default:
			for (int i = 0; i < punctuations.length; i++) {
				if (punctuations[i] == nextToken) {
					return punctuationTokens[i];// Return punctuation mark
				}
			}
			break;
		}
		return Token.EOF;
	}

	/**
	 * Get the current nextToken
	 *
	 * @return Current nextToken
	 */
	public String getToken() {
		return tokenizer.sval;
	}

	/**
	 * Get the current value
	 *
	 * @return current value
	 */
	public double getValue() {
		return tokenizer.nval;
	}

	// Returns the current line of the input file
	/**
	 * The the current nextToken line number
	 *
	 * @return current nextToken line number
	 */
	public int getLineNumber() {
		return tokenizer.lineno();
	}
}
