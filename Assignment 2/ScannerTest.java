/**
 * /**
 * JUunit tests for the Scanner for the class project in COP5556 Programming Language Principles 
 * at the University of Florida, Fall 2017.
 * 
 * This software is solely for the educational benefit of students 
 * enrolled in the course during the Fall 2017 semester.  
 * 
 * This software, and any software derived from it,  may not be shared with others or posted to public web sites,
 * either during the course or afterwards.
 * 
 *  @Beverly A. Sanders, 2017
 */

package cop5556fa17;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556fa17.Scanner.LexicalException;
import cop5556fa17.Scanner.Token;

import static cop5556fa17.Scanner.Kind.*;

public class ScannerTest {

	//set Junit to be able to catch exceptions
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	
	//To make it easy to print objects and turn this output on and off
	static final boolean doPrint = true;
	private void show(Object input) {
		if (doPrint) {
			System.out.println(input.toString());
		}
	}

	/**
	 *Retrieves the next token and checks that it is an EOF token. 
	 *Also checks that this was the last token.
	 *
	 * @param scanner
	 * @return the Token that was retrieved
	 */
	
	Token checkNextIsEOF(Scanner scanner) {
		Scanner.Token token = scanner.nextToken();
		assertEquals(Scanner.Kind.EOF, token.kind);
		assertFalse(scanner.hasTokens());
		return token;
	}


	/**
	 * Retrieves the next token and checks that its kind, position, length, line, and position in line
	 * match the given parameters.
	 * 
	 * @param scanner
	 * @param kind
	 * @param pos
	 * @param length
	 * @param line
	 * @param pos_in_line
	 * @return  the Token that was retrieved
	 */
	Token checkNext(Scanner scanner, Scanner.Kind kind, int pos, int length, int line, int pos_in_line) {
		Token t = scanner.nextToken();
		assertEquals(scanner.new Token(kind, pos, length, line, pos_in_line), t);
		return t;
	}

	/**
	 * Retrieves the next token and checks that its kind and length match the given
	 * parameters.  The position, line, and position in line are ignored.
	 * 
	 * @param scanner
	 * @param kind
	 * @param length
	 * @return  the Token that was retrieved
	 */
	Token check(Scanner scanner, Scanner.Kind kind, int length) {
		Token t = scanner.nextToken();
		assertEquals(kind, t.kind);
		assertEquals(length, t.length);
		return t;
	}

	/**
	 * Simple test case with a (legal) empty program
	 *   
	 * @throws LexicalException
	 */
	@Test
	public void testEmpty() throws LexicalException {
		String input = "";  //The input is the empty string.  This is legal
		show(input);        //Display the input 
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		checkNextIsEOF(scanner);  //Check that the only token is the EOF token.
	}	
	
	@Test
	public void testOneSemi() throws LexicalException {
		String input = ";";  //The input is the empty string.  This is legal
		show(input);        //Display the input 
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		checkNext(scanner, SEMI, 0, 1, 1, 1);
		checkNextIsEOF(scanner);  //Check that the only token is the EOF token.
	}
	
	@Test
	public void testSemiCarrageReturnNewLine() throws LexicalException {
		String input = ";;\r\n;;";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, SEMI, 0, 1, 1, 1);
		checkNext(scanner, SEMI, 1, 1, 1, 2);
		checkNext(scanner, SEMI, 4, 1, 2, 1);
		checkNext(scanner, SEMI, 5, 1, 2, 2);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testWhiteSpace() throws LexicalException {
		String input = "     ";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testingOperators1() throws LexicalException {
		String input = "++<<=-><-";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_PLUS, 0, 1, 1, 1);
		checkNext(scanner, OP_PLUS, 1, 1, 1, 2);
		checkNext(scanner, OP_LT, 2, 1, 1, 3);
		checkNext(scanner, OP_LE, 3, 2, 1, 4);
		checkNext(scanner, OP_RARROW, 5, 2, 1, 6);
		checkNext(scanner, OP_LARROW, 7, 2, 1, 8);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testSqBraces() throws LexicalException {
		String input = " [[ , ] ]] \n";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, LSQUARE, 1, 1, 1, 2);
		checkNext(scanner, LSQUARE, 2, 1, 1, 3);
		checkNext(scanner, COMMA, 4, 1, 1, 5);
		checkNext(scanner, RSQUARE, 6, 1, 1, 7);
		checkNext(scanner, RSQUARE, 8, 1, 1, 9);
		checkNext(scanner, RSQUARE, 9, 1, 1, 10);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testingOperators2() throws LexicalException {
		String input = "+|+<<=-\t\n/> <->";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_PLUS, 0, 1, 1, 1);
		checkNext(scanner, OP_OR, 1, 1, 1, 2);
		checkNext(scanner, OP_PLUS, 2, 1, 1, 3);
		checkNext(scanner, OP_LT, 3, 1, 1, 4);
		checkNext(scanner, OP_LE, 4, 2, 1, 5);
		checkNext(scanner, OP_MINUS, 6, 1, 1, 7);
		checkNext(scanner, OP_DIV, 9, 1, 2, 1);
		checkNext(scanner, OP_GT, 10, 1, 2, 2);
		checkNext(scanner, OP_LARROW, 12, 2, 2, 4);
		checkNext(scanner, OP_GT, 14, 1, 2, 6);
		checkNextIsEOF(scanner);
	}	
	
	@Test
	public void testingOperators3() throws LexicalException {
		String input = "\n/ //++\n/";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_DIV, 1, 1, 2, 1);
		checkNext(scanner, OP_DIV, 8, 1, 3, 1);	
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testBooleanLiteral1() throws LexicalException {
		String input = "\ntrue\t n/";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, BOOLEAN_LITERAL, 1, 4, 2, 1);
		checkNext(scanner, IDENTIFIER, 7, 1, 2, 7);	
		checkNext(scanner, OP_DIV, 8, 1, 2, 8);	
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testBooleanLiteral2() throws LexicalException {
		String input = "\nfalse/false+true-\r\n\tfalse\t n/";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, BOOLEAN_LITERAL, 1, 5, 2, 1);
		checkNext(scanner, OP_DIV, 6, 1, 2, 6);	
		checkNext(scanner, BOOLEAN_LITERAL, 7, 5, 2, 7);
		checkNext(scanner, OP_PLUS, 12, 1, 2, 12);	
		checkNext(scanner, BOOLEAN_LITERAL, 13, 4, 2, 13);
		checkNext(scanner, OP_MINUS, 17, 1, 2, 17);	
		checkNext(scanner, BOOLEAN_LITERAL, 21, 5, 3, 2);
		checkNext(scanner, IDENTIFIER, 28, 1, 3, 9);	
		checkNext(scanner, OP_DIV, 29, 1, 3, 10);	
		checkNextIsEOF(scanner);
	}
	
	@Test(expected = LexicalException.class)
    public void testStringLiteral2() throws LexicalException {
    	String input = "\"abc\\k\\t\"";
        show(input);
        Scanner scanner = new Scanner(input).scan();
        show(scanner);
       
    }
	
	@Test
    public void testStringLiteral3() throws LexicalException {
    	String input = "\"abc\"\"\"";
        show(input);
        Scanner scanner = new Scanner(input).scan();
        show(scanner);
        checkNext(scanner,STRING_LITERAL, 0, 5, 1, 1);
        checkNext(scanner,STRING_LITERAL, 5, 2, 1, 6);
        checkNextIsEOF(scanner);
    }
	
	@Test
    public void testStringLiteral1() throws LexicalException {
		
		String input = "abc\r\ndef";
        show(input);
        Scanner sc = new Scanner(input).scan();
        show(sc);
        checkNext(sc, IDENTIFIER,0,3,1,1);
        checkNext(sc, IDENTIFIER,5,3,2,1);
        checkNextIsEOF(sc);  //Check that the only token is the EOF token.
    }
	
	@Test
	public void testIntegerLiteral() throws LexicalException {
		String input = "\n100 000";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, INTEGER_LITERAL, 1, 3, 2, 1);	
		checkNext(scanner, INTEGER_LITERAL, 5, 1, 2, 5);
		checkNext(scanner, INTEGER_LITERAL, 6, 1, 2, 6);
		checkNext(scanner, INTEGER_LITERAL, 7, 1, 2, 7);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testKeyword1() throws LexicalException {
		String input = "\n-x\r\n\thello\thi";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_MINUS, 1, 1, 2, 1);	
		checkNext(scanner, KW_x, 2, 1, 2, 2);
		checkNext(scanner, IDENTIFIER, 6, 5, 3, 2);
		checkNext(scanner, IDENTIFIER, 12, 2, 3, 8);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testBraces() throws LexicalException {
		String input = "[(\n\f()]";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, LSQUARE, 0, 1, 1, 1);
		checkNext(scanner, LPAREN, 1, 1, 1, 2);
		checkNext(scanner, LPAREN, 4, 1, 2, 2);
		checkNext(scanner, RPAREN, 5, 1, 2, 3);
		checkNext(scanner, RSQUARE, 6, 1, 2, 4);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testComma() throws LexicalException {
		String input = " , ; ,  ,";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, COMMA, 1, 1, 1, 2);
		checkNext(scanner, SEMI, 3, 1, 1, 4);
		checkNext(scanner, COMMA, 5, 1, 1, 6);
		checkNext(scanner, COMMA, 8, 1, 1, 9);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testWhiteSpaces() throws LexicalException {
		String input = "int ab==\n5;()";  //The input is the empty string.  This is legal
		show(input);        //Display the input 
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		checkNext(scanner, KW_int, 0, 3, 1, 1);
		checkNext(scanner, IDENTIFIER, 4, 2, 1, 5);
		checkNext(scanner, OP_EQ, 6, 2, 1, 7);
		checkNext(scanner, INTEGER_LITERAL, 9, 1, 2, 1);
		checkNext(scanner, SEMI, 10, 1, 2, 2);
		checkNext(scanner, LPAREN, 11, 1, 2, 3);
		checkNext(scanner, RPAREN, 12, 1, 2, 4);
		checkNextIsEOF(scanner);  //Check that the only token is the EOF token.
	}
	
	@Test
	public void testDigit() throws LexicalException {
		String input = "11234756987";  //The input is the empty string.  This is legal
		show(input);        //Display the input 
		thrown.expect(LexicalException.class);
		try {
			new Scanner(input).scan();  //Create a Scanner and initialize it
		}
		catch (LexicalException e) {
			show(e);
			assertEquals(11,e.getPos());
			throw e;
		}		
	}
	
	/**
	 * Test illustrating how to put a new line in the input program and how to
	 * check content of tokens.
	 * 
	 * Because we are using a Java String literal for input, we use \n for the
	 * end of line character. (We should also be able to handle \n, \r, and \r\n
	 * properly.)
	 * 
	 * Note that if we were reading the input from a file, as we will want to do 
	 * later, the end of line character would be inserted by the text editor.
	 * Showing the input will let you check your input is what you think it is.
	 * 
	 * @throws LexicalException
	 */
	@Test
	public void testSemi() throws LexicalException {
		String input = ";;\n;;";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, SEMI, 0, 1, 1, 1);
		checkNext(scanner, SEMI, 1, 1, 1, 2);
		checkNext(scanner, SEMI, 3, 1, 2, 1);
		checkNext(scanner, SEMI, 4, 1, 2, 2);
		checkNextIsEOF(scanner);
	}
	
	/**
	 * This example shows how to test that your scanner is behaving when the
	 * input is illegal.  In this case, we are giving it a String literal
	 * that is missing the closing ".  
	 * 
	 * Note that the outer pair of quotation marks delineate the String literal
	 * in this test program that provides the input to our Scanner.  The quotation
	 * mark that is actually included in the input must be escaped, \".
	 * 
	 * The example shows catching the exception that is thrown by the scanner,
	 * looking at it, and checking its contents before rethrowing it.  If caught
	 * but not rethrown, then JUnit won't get the exception and the test will fail.  
	 * 
	 * The test will work without putting the try-catch block around 
	 * new Scanner(input).scan(); but then you won't be able to check 
	 * or display the thrown exception.
	 * 
	 * @throws LexicalException
	 */
	@Test
	public void failUnclosedStringLiteral() throws LexicalException {
		String input = "\" greetings  ";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //
			show(e);
			assertEquals(13,e.getPos());
			throw e;
		}
	}


}
