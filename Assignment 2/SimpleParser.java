package cop5556fa17;



import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import cop5556fa17.Scanner.Kind;
import cop5556fa17.Scanner.Token;
import cop5556fa17.SimpleParser.SyntaxException;

import static cop5556fa17.Scanner.Kind.*;

public class SimpleParser {
	
	Set<Kind> hashSet = new HashSet<>();
	
	HashMap<String, Set<Kind>> first = new HashMap<String,Set<Kind>>();
	
	@SuppressWarnings("serial")
	public class SyntaxException extends Exception {
		Token t;

		public SyntaxException(Token t, String message) {
			super(message);
			this.t = t;
		}

	}


	Scanner scanner;
	Token t;

	SimpleParser(Scanner scanner) {
		this.scanner = scanner;
		t = scanner.nextToken();
		
		hashSet.add(Kind.KW_boolean);
		hashSet.add(KW_int);
		first.put("variableDeclaration", hashSet);
		first.put("varType", hashSet);
		
		hashSet = new HashSet<>();
		hashSet.add(KW_image);
		first.put("imageDeclaration", hashSet);
		
		hashSet = new HashSet<>();
		hashSet.add(KW_url);
		hashSet.add(KW_file);
		first.put("sourceSinkDeclaration", hashSet);
		first.put("sourceSinkType", hashSet);
		
		hashSet = new HashSet<>();
		hashSet.addAll(first.get("variableDeclaration"));
		hashSet.addAll(first.get("imageDeclaration"));
		hashSet.addAll(first.get("sourceSinkDeclaration"));
		first.put("declaration", hashSet);
		
		hashSet = new HashSet<>();
		hashSet.add(STRING_LITERAL);
		hashSet.add(OP_AT);
		hashSet.add(IDENTIFIER);
		first.put("source", hashSet);
		
		hashSet = new HashSet<>();
		hashSet.add(IDENTIFIER);
		first.put("statement", hashSet);
		
		hashSet = new HashSet<>();
		hashSet.add(OP_LARROW);
		first.put("imageInStatement", hashSet);
		
		hashSet = new HashSet<>();
		hashSet.add(OP_RARROW);
		first.put("imageOutStatement", hashSet);
		
		hashSet = new HashSet<>();
		hashSet.add(LSQUARE);
		hashSet.add(OP_ASSIGN);
		first.put("assignmentStatement", hashSet);
		
		hashSet = new HashSet<>();
		hashSet.add(LSQUARE);
		first.put("lhs", hashSet);
		first.put("lhsSelector", hashSet);
		
		hashSet = new HashSet<>();
		hashSet.add(IDENTIFIER);
		hashSet.add(KW_SCREEN);
		first.put("sink", hashSet);
		
		hashSet = new HashSet<>();
		
		hashSet.add(KW_sin);
		hashSet.add(KW_cos);
		hashSet.add(KW_atan);
		hashSet.add(KW_abs);
		hashSet.add(KW_cart_x);
		hashSet.add(KW_cart_y);
		hashSet.add(KW_polar_a);
		hashSet.add(KW_polar_r);
		
		first.put("functionApplication", hashSet);
		first.put("functionName", hashSet);
		
		hashSet.add(INTEGER_LITERAL);
		hashSet.add(LPAREN);
		hashSet.add(BOOLEAN_LITERAL);
		first.put("primary", hashSet);
		
		hashSet = new HashSet<>();
		hashSet.add(IDENTIFIER);
		first.put("identOrPixelSelectorExpression", hashSet);
		
		hashSet = new HashSet<>();
		hashSet.add(KW_x);
		first.put("xySelector", hashSet);
		
		hashSet = new HashSet<>();
		hashSet.add(KW_r);
		first.put("raSelector", hashSet);
		
		hashSet = new HashSet<>();
		
		hashSet.add(OP_EXCL);
		hashSet.add(KW_x);
		hashSet.add(KW_y);
		hashSet.add(KW_r);
		hashSet.add(KW_a);
		hashSet.add(KW_X);
		hashSet.add(KW_Y);
		hashSet.add(KW_Z);
		hashSet.add(KW_A);
		hashSet.add(KW_R);
		hashSet.add(KW_DEF_X);
		hashSet.add(KW_DEF_Y);
		hashSet.addAll(first.get("primary"));
		hashSet.addAll(first.get("identOrPixelSelectorExpression"));
		first.put("unaryExpressionNotPlusMinus", hashSet);
		
		hashSet = new HashSet<>();
		hashSet.add(OP_PLUS);
		hashSet.add(OP_MINUS);
		hashSet.addAll(first.get("unaryExpressionNotPlusMinus"));
		first.put("unaryExpression", hashSet);
		first.put("addExpression", hashSet);
		first.put("andExpression", hashSet);
		first.put("eqExpression", hashSet);
		first.put("orExpression", hashSet);
		first.put("multExpression", hashSet);
		first.put("relExpression", hashSet);
		first.put("expression", hashSet);
		first.put("selector", hashSet);
		
		hashSet = new HashSet<>();
		hashSet.add(OP_Q);
		first.put("expressionTail", hashSet);
	}

	/**
	 * Main method called by compiler to parser input.
	 * Checks for EOF
	 * 
	 * @throws SyntaxException
	 */
	public void parse() throws SyntaxException {
		program();
		matchEOF();
	}
	

	/**
	 * Program ::=  IDENTIFIER   ( Declaration SEMI | Statement SEMI )*   
	 * 
	 * Program is start symbol of our grammar.
	 * 
	 * @throws SyntaxException
	 */
	void program() throws SyntaxException {
		//TODO  implement this
		match(IDENTIFIER);
		
		while(first.get("declaration").contains(t.kind) || first.get("statement").contains(t.kind)) {
			if(first.get("declaration").contains(t.kind)) {
				declaration();
			}
			else {
				statement();
			}
			match(SEMI);
		}
		
	}

	
	public void statement() throws SyntaxException {
		// TODO Auto-generated method stub
		match(IDENTIFIER);
		if(first.get("assignmentStatement").contains(t.kind)) {
			assignmentStatement();
		} else if(first.get("imageOutStatement").contains(t.kind)) {
			imageOutStatement();
		} else {
			imageInStatement();
		}
	}

	public void imageInStatement() throws SyntaxException {
		// TODO Auto-generated method stub
		
		match(OP_LARROW);
		source();
	}

	public void imageOutStatement() throws SyntaxException {
		// TODO Auto-generated method stub
		
		match(OP_RARROW);
		sink();
	}

	public void sink() throws SyntaxException {
		// TODO Auto-generated method stub
		if(t.kind == IDENTIFIER) {
			match(IDENTIFIER);
		} else {
			match(KW_SCREEN);
		}
	}

	public void assignmentStatement() throws SyntaxException {
		// TODO Auto-generated method stub
		lhs();
		match(OP_ASSIGN);
		expression();
	}

	public void lhs() throws SyntaxException {
		// TODO Auto-generated method stub
		
		
		if(t.kind == LSQUARE) {
			match(LSQUARE);
			lhsSelector();
			match(RSQUARE);
		}				
	}

	public void lhsSelector() throws SyntaxException {
		// TODO Auto-generated method stub
		match(LSQUARE);
		if(first.get("xySelector").contains(t.kind)) {
			xySelector();
		} else {
			raSelector();
		}
		match(RSQUARE);
	}

	public void raSelector() throws SyntaxException {
		// TODO Auto-generated method stub
		match(KW_r);
		match(COMMA);
		match(KW_A);
	}

	public void xySelector() throws SyntaxException {
		// TODO Auto-generated method stub
		match(KW_x);
		match(COMMA);
		match(KW_y);
	}

	public void source() throws SyntaxException {
		// TODO Auto-generated method stub
		if(t.kind == STRING_LITERAL) {
			match(STRING_LITERAL);
		} else if(t.kind == OP_AT) {
			match(OP_AT);
			expression();
		} else {
			match(IDENTIFIER);
		}
	}

	public void declaration() throws SyntaxException {
		// TODO Auto-generated method stub
		if(first.get("variableDeclaration").contains(t.kind)) {
			variableDeclaration();
		}else if(first.get("imageDeclaration").contains(t.kind)) {
			imageDeclaration();
		}else if(first.get("sourceSinkDeclaration").contains(t.kind)) {
			sourceSinkDeclaration();
		}else {
			//error
			throw new SyntaxException(t, "no declaration found");
		}	
	}

	public void sourceSinkDeclaration() throws SyntaxException {
		// TODO Auto-generated method stub
		sourceSinkType();
		match(IDENTIFIER);
		match(OP_ASSIGN);
		source();
	}

	public void sourceSinkType() throws SyntaxException {
		// TODO Auto-generated method stub
		if(t.kind == KW_url) {
			match(KW_url);
		}
		else
		match(KW_file);
	}

	public void imageDeclaration() throws SyntaxException {
		// TODO Auto-generated method stub
		match(KW_image);
		if(t.kind == LSQUARE) {
			match(LSQUARE);
			expression();
			match(COMMA);
			expression();
			match(RSQUARE);
		}
		match(IDENTIFIER);
		if(t.kind == OP_LARROW) {
			match(OP_LARROW);
			source();
		}
	}

	public void variableDeclaration() throws SyntaxException {
		// TODO Auto-generated method stub
		varType();
		match(IDENTIFIER);
		if(t.kind == OP_ASSIGN) {
			match(OP_ASSIGN);
			expression();
		}
	}

	public void varType() throws SyntaxException {
		// TODO Auto-generated method stub
		if(t.kind == KW_int) {
			match(KW_int);
		} else if(t.kind == KW_boolean) {
			match(KW_boolean);
		} else {
			//error
			throw new SyntaxException(t, "int or boolean expected");
		}
	}

	public void match(Kind kind) throws SyntaxException {
		// TODO Auto-generated method stub
		if(t.kind == kind) {
			consume();
		} else {
			System.out.println(t.pos);
			System.out.println(kind);
			System.out.println(t.kind);
			throw new SyntaxException(t, "Syntax error");
		}
	}

	public void consume() {
		// TODO Auto-generated method stub
		t = scanner.nextToken();
	}

	/**
	 * Expression ::=  OrExpression  OP_Q  Expression OP_COLON Expression    | OrExpression
	 * 
	 * Our test cases may invoke this routine directly to support incremental development.
	 * 
	 * @throws SyntaxException
	 */
	void expression() throws SyntaxException {
		//TODO implement this.
		orExpression();
		expressionTail();
	}


	public void expressionTail() throws SyntaxException {
		// TODO Auto-generated method stub
		if(t.kind == OP_Q) {
			match(OP_Q);
			expression();
			match(OP_COLON);
			expression();
		}
	}

	public void orExpression() throws SyntaxException {
		// TODO Auto-generated method stub
		andExpression();
		while(t.kind == OP_OR) {
			match(OP_OR);
			andExpression();
		}
	}

	public void andExpression() throws SyntaxException {
		// TODO Auto-generated method stub
		eqExpression();
		while(t.kind == OP_AND) {
			match(OP_AND);
			eqExpression();
		}
	}

	public void eqExpression() throws SyntaxException {
		// TODO Auto-generated method stub
		relExpression();
		while(t.kind == OP_EQ || t.kind == OP_NEQ) {
			if(t.kind == OP_EQ) {
				match(OP_EQ);
			}else if(t.kind == OP_NEQ) {
				match(OP_NEQ);
			}else {
				//error
				throw new SyntaxException(t, "equal or not equal expected");
			}
			relExpression();
		}
	}

	public void relExpression() throws SyntaxException {
		// TODO Auto-generated method stub
		addExpression();
		while(t.kind == OP_LT || t.kind == OP_GT || t.kind == OP_LE || t.kind == OP_GE) {
			if(t.kind == OP_LT) {
				match(OP_LT);
			}else if(t.kind == OP_GT) {
				match(OP_GT);
			}else if(t.kind == OP_LE) {
				match(OP_LE);
			}else if(t.kind == OP_GE) {
				match(OP_GE);
			}
			else {
				//error
				throw new SyntaxException(t, "< or > or <= or >= expected");
			}
			addExpression();
		}
	}

	public void addExpression() throws SyntaxException {
		// TODO Auto-generated method stub
		multExpression();
		while(t.kind == OP_PLUS || t.kind == OP_MINUS) {
			if(t.kind == OP_PLUS) {
				match(OP_PLUS);
			}else if(t.kind == OP_MINUS) {
				match(OP_MINUS);
			}else {
				//error
				throw new SyntaxException(t, "+ or - expected");
			}
			multExpression();
		}
	}

	public void multExpression() throws SyntaxException {
		// TODO Auto-generated method stub
		unaryExpression();
		while(t.kind == OP_TIMES || t.kind == OP_DIV || t.kind == OP_MOD) {
			if(t.kind == OP_TIMES) {
				match(OP_TIMES);
			}else if(t.kind == OP_DIV) {
				match(OP_DIV);
			}else if(t.kind == OP_MOD) {
				match(OP_MOD);
			}
			else {
				//error
				throw new SyntaxException(t, "* or / or % expected");
			}
			unaryExpression();
		}
	}

	public void unaryExpression() throws SyntaxException {
		// TODO Auto-generated method stub
		if(t.kind == OP_PLUS) {
			match(OP_PLUS);
			unaryExpression();
		} else if(t.kind == OP_MINUS) {
			match(OP_MINUS);
			unaryExpression();
		} else {
			unaryExpressionNotPlusMinus();
		}
	}

	public void unaryExpressionNotPlusMinus() throws SyntaxException {
		// TODO Auto-generated method stub
		if(t.kind == OP_EXCL) {
			match(OP_EXCL);
			unaryExpression();
		} else if(first.get("primary").contains(t.kind)) {
			primary();
		} else if(first.get("identOrPixelSelectorExpression").contains(t.kind)) {
			identOrPixelSelectorExpression();
		} else if(t.kind == KW_x) {
			match(KW_x);
		} else if(t.kind == KW_y) {
			match(KW_y);
		} else if(t.kind == KW_r) {
			match(KW_r);
		} else if(t.kind == KW_a) {
			match(KW_a);
		} else if(t.kind == KW_X) {
			match(KW_X);
		} else if(t.kind == KW_Y) {
			match(KW_Y);
		} else if(t.kind == KW_Z) {
			match(KW_Z);
		} else if(t.kind == KW_A) {
			match(KW_A);
		} else if(t.kind == KW_R) {
			match(KW_R);
		} else if(t.kind == KW_DEF_X) {
			match(KW_DEF_X);
		} else if(t.kind == KW_DEF_Y) {
			match(KW_DEF_Y);
		} else {
			//error
			throw new SyntaxException(t, "unary operator expected");
		}
	}

	public void identOrPixelSelectorExpression() throws SyntaxException {
		// TODO Auto-generated method stub
		match(IDENTIFIER);
		if(t.kind == LSQUARE) {
			match(LSQUARE);
			selector();
			match(RSQUARE);
		}
	}

	public void selector() throws SyntaxException {
		// TODO Auto-generated method stub
		expression();
		match(COMMA);
		expression();
	}

	public void primary() throws SyntaxException {
		// TODO Auto-generated method stub
		if(t.kind == INTEGER_LITERAL ) {
			match(INTEGER_LITERAL);
		} else if(t.kind == LPAREN) {
			match(LPAREN);
			expression();
			match(RPAREN);
		} else if(first.get("functionApplication").contains(t.kind)) {
			functionApplication();
		} else {
			match(BOOLEAN_LITERAL);
		}
	}

	public void functionApplication() throws SyntaxException {
		// TODO Auto-generated method stub
		functionName();
		if(t.kind == LPAREN) {
		  	match(LPAREN);
		  	expression();
		  	match(RPAREN);
		} else {
			match(LSQUARE);
			selector();
			match(RSQUARE);
		}
	}

	public void functionName() throws SyntaxException {
		// TODO Auto-generated method stub
		if(t.kind == KW_sin) {
			match(KW_sin);
		} else if(t.kind == KW_cos) {
			match(KW_cos);
		} else if(t.kind == KW_atan) {
			match(KW_atan);
		} else if(t.kind == KW_abs) {
			match(KW_abs);
		} else if(t.kind == KW_cart_x) {
			match(KW_cart_x);
		} else if(t.kind == KW_cart_y) {
			match(KW_cart_y);
		} else if(t.kind == KW_polar_a) {
			match(KW_polar_a);
		} else if(t.kind == KW_polar_r) {
			match(KW_polar_r);
		} else {
			throw new SyntaxException(t, "error in functionName");
		}
	}

	/**
	 * Only for check at end of program. Does not "consume" EOF so no attempt to get
	 * nonexistent next Token.
	 * 
	 * @return
	 * @throws SyntaxException
	 */
	public Token matchEOF() throws SyntaxException {
		if (t.kind == EOF) {
			return t;
		}
		String message =  "Expected EOL at " + t.line + ":" + t.pos_in_line;
		throw new SyntaxException(t, message);
	}
}
