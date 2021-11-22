package splat.lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.Math;

public class Lexer {
	//add a field which holds onto the file object to be tokenized
	private File f;
	private List<Token> tokens;
	private int line_num=0;
	private String[] keywords = {"program","begin","end","is","while","do",
						"if","then","else","print","print_line","return",
						"void","Integer","Boolean","String",
						"true","false"
						};
	private String[] operators = {"<",">","=","==","<=",">=","+","-","*","/","%",
								":",":=","and","or","not"};
	private String[] symbols = {";","(",")","\"",","};
	
	public Lexer(File file){
		f = file;
		tokens = new ArrayList<Token>();
	}

	private Boolean isSymbol(String ch)
	{
		for (int i=0; i<symbols.length; i++)
		{
			if (ch.equals(symbols[i])) return true;
		}
		return false;
	}

	private Boolean isKeyword(String lex)
	{
		for (int i=0; i<keywords.length; i++)
		{
			if (lex.equals(keywords[i])) return true;
		}
		return false;
	}
	
	private Boolean isOperator(String op)
	{
		for (int i=0; i<operators.length; i++)
		{
			if (op.equals(operators[i])) return true;
		}
		return false;
	}

	private Boolean isLabelChar(char ch)
	{
		return ch=='_' || Character.isLetterOrDigit(ch);
	}

	private Boolean isValidSymbol(String str)
	{
		for (int i=0; i< operators.length;i++)
		{
			if (str.equals(operators[i])) return true;
		}
		for (int i=0; i < symbols.length;i++)
		{
			if (str.equals(symbols[i])) return true;
		}
		return false;
	}

	public List<Token> tokenize() throws LexException {
		// TODO Auto-generated method stub
		// reading the file character by character, for tracking both the line and column
		try{
		Scanner myReader = new Scanner(this.f);

		while (myReader.hasNextLine()) 
		{
			String line = myReader.nextLine();
			this.line_num++;
			int column = 0;
			String currentLexeme = "";
			Token.Type_ tokenType;

			// processing the line starts here
			while(column < line.length()){
				// remove whitespace
				while (column<line.length() && Character.isWhitespace(line.charAt(column))){
					column ++;
				}
				if (column >= line.length())
				{
					break;
				}
				char currentChar = line.charAt(column);
				
				if (Character.isLetter(currentChar)  || currentChar=='_')
				{
					// read until hits the non-alphanumeric or the end of line
					int index = column+1;
					while ( index < line.length() && isLabelChar(line.charAt(index)))
					{
						index = index+1;
					}
					currentLexeme = line.substring(column,index);
					tokenType = Token.Type_.IDENTIFIER;
					// check if keyword
					if (isKeyword(currentLexeme))
					{
						tokenType = Token.Type_.KEYWORD;
					}
					// check if "and", "or" or "not" operator
					else if(isOperator(currentLexeme))
					{
						tokenType = Token.Type_.OPERATOR;
					}
					tokens.add(new Token(currentLexeme,this.line_num,column,tokenType));
					// update column
					column = index;
				}

				else if (Character.isDigit(currentChar))
				{
					int index = column+1;
					// read until hits non-digit or the end of line
					while(index < line.length() && Character.isDigit(line.charAt(index)))
					{
						index = index+1;
					}
					currentChar = line.charAt(Math.min(index,line.length() - 1));
					// test whether the next char is alphabetic. If so, throw error
					if(Character.isLetter(currentChar) || currentChar=='_')
					{
						throw new LexException("bad identifier"+currentChar, this.line_num, column);
					}
					currentLexeme = line.substring(column,index);
					tokenType = Token.Type_.NUMBER;
					tokens.add(new Token(currentLexeme,this.line_num,column,tokenType));
					column = index;
				}
				// here comes the processing of non-alphanumeric characters
				else {
					int index = column;
					// read until the symbol makes sense, i.e. valid. Eager implementation
					while (index < line.length())
					{
						if ( isValidSymbol( line.substring(column, index+1)) )
						{
							index = index + 1;
						}else{
							break;
						}
					}
					currentLexeme = line.substring(column, index);
					tokenType = Token.Type_.SYMBOL;
					// next goes checking the validity of the lexeme
					if (currentLexeme.length() == 0 || currentLexeme.equals("="))
					{
						throw new LexException("unknown symbol1", this.line_num, column);
					}

					// check whether an operator
					if (isOperator(currentLexeme))
					{
						tokenType = Token.Type_.OPERATOR;
					}
					// check if symbol
					else if(isSymbol(currentLexeme))
					{
						// check if the opening quotes, i.e. string literal
						if(currentLexeme.equals("\""))
						{
							//processing the string literal
							while (index < line.length() && line.charAt(index)!='\"')
							{
								if (line.charAt(index) == '\\')
								{
									throw new LexException("backslashes are prohibited",
											 this.line_num, index);
								}
								index = index + 1;
							}
							// if hits the end of line and no double quote exist then throw error
							if (index == line.length()){
								throw new LexException("unclosed string literal",
														this.line_num, column);	
							}
							currentLexeme = line.substring(column, index);
							tokenType = Token.Type_.StringLiteral;
							//since the index at this point is at the closing doulbe quote
							// we should update index by 1 to process the next characters
							index = index+1;
						}
						else
						{
							tokenType = Token.Type_.SYMBOL;
						}
					}
					else {// it should never reach this point
						throw new LexException("unknown symbol", this.line_num, column);
					}

					tokens.add(new Token(currentLexeme,this.line_num,column,tokenType));
					//update column
					column = index;
				}
			}

		}

		myReader.close();
		} catch (FileNotFoundException ex){
			System.out.println("file not found");
		}
		return tokens;
	}

}
