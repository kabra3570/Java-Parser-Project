/**Author: Kevin Abrahams
 * Date: 01-27-2021
 * Description: Parser - This class is crucial to affirm and ensure that the user has correctly abided by the grammar language set forth 
 * in the program. This parser essentially embodies the syntatic structure that the input file must abide by, 
 * This parser class is responsible for parsing or reading the contents of the input file based on the grammar set forth.
 * This class is instrumental in the conversion of the contents of the input file into appropriately produced GUI.
 */
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Implementation of the parser
 *
 * @author Kevin
 */
public class Parser {

	// Lexer to check syntax
	private LexicalAnalyzer lexer;
	// Expected token
	private Token expectedToken;
	// Current token
	private Token currentToken;
	// The frame used to show the gui from parsed file
	private JFrame frame;
	// Title of the frame
	private String title;
	// Radio button group
	private ButtonGroup group;

	/**
	 * Parser constructor to set the lexer
	 *
	 * @param lexer - Lexer instance
	 */
	public Parser(LexicalAnalyzer lexer) {
		this.lexer = lexer;
	}

	/**
	 * Parse the given gui file
	 *
	 * @throws IOException
	 * @throws SyntaxException
	 */
	void parse() throws IOException, SyntaxException {
		currentToken = lexer.getNextTokenType();
		if (parseInput()) {
			frame.setVisible(true);
		} else {
			// Throw exception
			String message = lexer.getLineNumber() + " Expecting Token is " + expectedToken + " but found "
					+ currentToken;
			throw new SyntaxException(message);
		}
	}

	/**
	 * Evaluate the token
	 *
	 * @param token - Token to be evaluated
	 * @return True/false
	 * @throws IOException
	 * @throws SyntaxException
	 */
	private boolean validateToken(Token token) throws IOException, SyntaxException {
		if (currentToken == token) {
			switch (currentToken) {
			case BUTTON:
			case END:
			case FLOW:
			case GRID:
			case LABEL:
			case LAYOUT:
			case PANEL:
			case RADIO:
			case TEXTFIELD:
			case WINDOW:
			case COMMA:
			case COLON:
			case OPENING_ROUND_BRACE:
			case CLOSING_ROUND_BRACE:
				currentToken = lexer.getNextTokenType();// Get the next token type
				break;
			case GROUP:
				group = new ButtonGroup();
				currentToken = lexer.getNextTokenType();// Get the next token type
				break;
			case STRING:
				title = lexer.getToken();
				currentToken = lexer.getNextTokenType();// Get the next token type
				break;
			case SEMICOLON:
				currentToken = lexer.getNextTokenType();// Get the next token type
			}
			return true;
		} else {
			expectedToken = token;// Set the expected token
			return false;// Invalid token
		}
	}

	/**
	 * Parse the input
	 *
	 * @return @throws IOException
	 * @throws SyntaxException
	 */
	private boolean parseInput() throws IOException, SyntaxException {
		if (validateToken(Token.WINDOW)) {// Current token is window
			if (validateToken(Token.STRING)) {// Current token is tile
				// Create JFrame with fiven title
				frame = new JFrame(title);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				if (validateToken(Token.OPENING_ROUND_BRACE)) {// Next token should be opening round brace
					if (validateToken(Token.NUMBER)) {// Next token should be width of frame
						int width = (int) lexer.getValue();// Get the width of of window
						currentToken = lexer.getNextTokenType();// Get next token
						if (validateToken(Token.COMMA)) {// Next token should be
							if (validateToken(Token.NUMBER)) {// Next token should be height of frame
								int height = (int) lexer.getValue();
								currentToken = lexer.getNextTokenType();// Get next token
								if (validateToken(Token.CLOSING_ROUND_BRACE)) {// It should be closing open braces
									frame.setSize(width, height);// Set the size of the window
									JPanel mainPanel = new JPanel();
									mainPanel.setSize(width, height);
									frame.add(mainPanel);
									// Parse layout of the panel
									if (parseLayout(mainPanel)) {
										// Parse gui widgets
										if (parseWidgets(mainPanel)) {
											// Parse the token
											if (validateToken(Token.END)) {
												return validateToken(Token.PERIOD);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Parse the layout of the mainPanel
	 *
	 * @param mainPanel - Main panel
	 * @return True/false
	 * @throws IOException
	 * @throws SyntaxException
	 */
	private boolean parseLayout(JPanel mainPanel) throws IOException, SyntaxException {
		if (validateToken(Token.LAYOUT)) {// Valid token should be layout
			if (parseLayoutType(mainPanel)) {// Parse the type of layout
				return validateToken(Token.COLON);// Next token should be colon
			}
		}
		return false;
	}

	/**
	 * Parse the type of layout of main panel
	 *
	 * @param mainPanel - Main panel
	 * @return True/False
	 * @throws IOException
	 * @throws SyntaxException
	 */
	private boolean parseLayoutType(JPanel mainPanel) throws IOException, SyntaxException {
		boolean isValid = false;
		if (validateToken(Token.FLOW)) {// Set flow layout
			mainPanel.setLayout(new FlowLayout());
			isValid = true;
		} else if (validateToken(Token.GRID)) {// Set the grid layput
			if (validateToken(Token.OPENING_ROUND_BRACE)) {// Token should be opening round brace
				if (validateToken(Token.NUMBER)) {// Get the number of rows in grid layout
					int rows = (int) lexer.getValue();
					currentToken = lexer.getNextTokenType();
					if (validateToken(Token.COMMA)) {
						if (validateToken(Token.NUMBER)) {// Get the number of columns in grid layout
							int cols = (int) lexer.getValue();
							currentToken = lexer.getNextTokenType();
							if (validateToken(Token.CLOSING_ROUND_BRACE)) {// If it is closing brase then set grid
																			// layout
								mainPanel.setLayout(new GridLayout(rows, cols));
								return true;
							} else if (validateToken(Token.COMMA)) {
								if (validateToken(Token.NUMBER)) {// Next token should be horizontal gap in grid layout
									int hgap = (int) lexer.getValue();
									currentToken = lexer.getNextTokenType();
									if (validateToken(Token.COMMA)) {
										if (validateToken(Token.NUMBER)) {// Next token should be vertical gap in grid
																			// layout
											int vgap = (int) lexer.getValue();
											currentToken = lexer.getNextTokenType();
											if (validateToken(Token.CLOSING_ROUND_BRACE)) {
												mainPanel.setLayout(new GridLayout(rows, cols, hgap, vgap));// Set h
																											// grid
																											// layout
												isValid = true;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return isValid;
	}

	/**
	 * Parse the GUI Widgets
	 *
	 * @param mainPanel - Main panel of the GUI
	 * @return True/False
	 * @throws IOException
	 * @throws SyntaxException
	 */
	private boolean parseWidgets(JPanel mainPanel) throws IOException, SyntaxException {
		if (parseWidget(mainPanel)) {
			// Recursively check the widgets
			if (parseWidgets(mainPanel)) {
				return true;
			}
			return true;
		}
		return false;
	}

	/**
	 * Parse the widget
	 *
	 * @param mainPanel - Main panel
	 * @return True/False
	 * @throws IOException
	 * @throws SyntaxException
	 */
	private boolean parseWidget(JPanel mainPanel) throws IOException, SyntaxException {
		if (validateToken(Token.BUTTON)) {// Parse the button
			if (validateToken(Token.STRING)) {
				if (validateToken(Token.SEMICOLON)) {
					mainPanel.add(new JButton(title));
					return true;
				}
			}
		} else if (validateToken(Token.GROUP)) {// Parse the radio buttons
			if (parseRadioButtons(mainPanel)) {
				return endWidget();
			}
		} else if (validateToken(Token.LABEL)) {// Parse the Label
			if (validateToken(Token.STRING)) {
				if (validateToken(Token.SEMICOLON)) {
					mainPanel.add(new JLabel(title));
					return true;
				}
			}
		} else if (validateToken(Token.PANEL)) {// Parse the Panel
			mainPanel.add(mainPanel = new JPanel());
			if (parseLayout(mainPanel)) {
				if (parseWidgets(mainPanel)) {
					return endWidget();
				}
			}
		} else if (validateToken(Token.TEXTFIELD)) {// Parse the text field
			if (validateToken(Token.NUMBER)) {
				int value = (int) lexer.getValue();
				currentToken = lexer.getNextTokenType();
				if (validateToken(Token.SEMICOLON)) {
					mainPanel.add(new JTextField(value));
					return true;
				}
			}
		}
		return false;
	}

	// Parse End Widget
	private boolean endWidget() throws IOException, SyntaxException {
		if (validateToken(Token.END)) {
			return validateToken(Token.SEMICOLON);
		}
		return false;
	}

	/**
	 * Parse the Radio buttons
	 *
	 * @param mainPanel - Main Panel
	 * @return True/False
	 * @throws IOException
	 * @throws SyntaxException
	 */
	private boolean parseRadioButtons(JPanel mainPanel) throws IOException, SyntaxException {
		if (parseRadioButton(mainPanel)) {
			if (parseRadioButtons(mainPanel)) {
				return true;
			}
			return true;
		}
		return false;
	}

	/**
	 * Parse the radio buttons
	 *
	 * @param mainPanel
	 * @return True/False
	 * @throws IOException
	 * @throws SyntaxException
	 */
	private boolean parseRadioButton(JPanel mainPanel) throws IOException, SyntaxException {
		if (validateToken(Token.RADIO)) {
			if (validateToken(Token.STRING)) {
				if (validateToken(Token.SEMICOLON)) {
					JRadioButton rButton = new JRadioButton(title);
					mainPanel.add(rButton);
					group.add(rButton);
					return true;
				}
			}
		}
		return false;
	}
}
