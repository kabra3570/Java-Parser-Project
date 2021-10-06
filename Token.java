/**Author: Kevin Abrahams
 * Date: 01-27-2021
 * Description: Token - Provide a enum which stores a collection of items representing valid entries or values in the grammar. These are legitimate 
 * values which designate possible entries in the input file based on the syntactic structure set forth. This program largely facilitates
 * the conversion of the input to the respective GUI, helping to exonerate issues which may arise in the program if the file contains unforeseen input.  
 */

/**
 * Enum for token
 *
 * @author Kevin
 */
public enum Token {
	BUTTON, COLON, COMMA, EOF, END, FLOW, GRID, GROUP, LABEL, LAYOUT, OPENING_ROUND_BRACE, NUMBER, PANEL, PERIOD, RADIO,
	CLOSING_ROUND_BRACE, SEMICOLON, STRING, TEXTFIELD, WINDOW
};
