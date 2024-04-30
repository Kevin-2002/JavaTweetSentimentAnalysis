package ie.atu.sw;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 
 * <b>This class is used for menu related things such as:</b>
 * 
 * the menu for the user -(1) Specify a text file for analysis, -(2) Specify a
 * path for a text resource to analyze for example tweets aka twitter url(not
 * literally a url in this case), -(3)Save the output to a specified file, -(4)
 * Enter lexicon file path that will be used, -(5) Display the results of the
 * lexicon comparison to the resource and output the result to the file if the
 * user has specified the output file location, -(6) quit
 * 
 * 
 * @author Kevin McShane
 * @version JavaSE-21 developed in eclipse version 2023-12
 */

public class Menu {

	// class scope variables
	Scanner inputReader = new Scanner(System.in);
	public String inputFilePath;
	public String outputFilePath;
	public String lexiconFilePath;
	public int fileLine;

	/**
	 * default constructor
	 */
	public Menu() {

	}

	/**
	 * This method call all of the functions for menu to make code in runner neat
	 * 
	 * developers note: when I made userChoice 1,3,4 I did not understand what the
	 * url meant gathering from context now i understand it means to specify a tweet
	 * or tweets for the sentiment processing class to know what to do as Scanner is
	 * not threadSafe. so programming choice 2 came after I had made the sentiment
	 * processor at the time of doing 1,3,4 I forgot that talking to multiple
	 * classes would be made simple and secure with the use of a private variable
	 * and public gets with a private sets, however this is a lot to reactor so I
	 * will leave it the way it is due to time.
	 * 
	 * @return when return false the program was exited, when return true the
	 *         program calls the calculation in main
	 */
	public boolean RunMenu() {
		boolean quitMenu = false, validInputFilePath = false, validLexiconPath = false, validURL = false;
		int userChoice, url = 0;

		// do-while loop until user quits
		do {
			// print menu for user
			printMenu();

			// prompt the user
			System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
			System.out.println("Select Option [1-4 or 6 before you select 5]>");
			userChoice = inputReader.nextInt();

			if (userChoice == 1) {
				// return the file path
				inputFilePath = configureInputTxtFileLocation();

				// check to see if a valid filePath was returned
				// if empty string it is invalid
				if (inputFilePath.length() > 0) {
					validInputFilePath = true;
				} else {
					validInputFilePath = false;
				}
			} else if (userChoice == 2) {
				url = specifyURL();

				if (url != 0) {
					setFileLine(url);
					validURL = true;
				}
			} else if (userChoice == 3) {
				// return the file path
				outputFilePath = configureOutputTxtFileLocation();
			} else if (userChoice == 4) {
				// return the file path
				lexiconFilePath = configureLexiconPath();

				// check validity
				if (lexiconFilePath.length() > 0) {
					validLexiconPath = true;
				} else {
					validLexiconPath = false;
				}

			} else if (userChoice == 5) {
				if (validInputFilePath == true && validURL == true && validLexiconPath == true) {
					System.out.println("valid file paths");

					// return to the main and make the request
					return true;

				} else {
					System.out.println("Invalid file paths:");
					System.out.println("NOTE: YOU MUST ENSURE THAT YOU RUN OPTIONS 1, 2 AND 4 FIRST");
				}

			} else if (userChoice == 6) {
				// quit the menu
				System.out.println("closing application");
				quitMenu = true;
			} else {
				System.out.println("Invalid option, try again");
			}
		} while (quitMenu == false);

		return false;
	}

	/**
	 * This method prints menu for the user
	 */
	public void printMenu() {
		// You should put the following code into a menu or Menu class
		System.out.println(ConsoleColour.BLUE);
		System.out.println("************************************************************");
		System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
		System.out.println("*                                                          *");
		System.out.println("*             Virtual Threaded Sentiment Analyser          *");
		System.out.println("*                                                          *");
		System.out.println("************************************************************");
		System.out.println("(1) Specify a Text File");
		System.out.println("(2) Specify a URL (Note: this means which line in the file you want analysis for)");
		System.out.println("(3) Specify an Output File (default: ./out.txt)");
		System.out.println("(4) Configure Lexicons");
		System.out.println("(5) Execute, Analyse and Report");
		System.out.println("(6) Quit");
	}

	/**
	 * This method allows the user to configure the text file location for the text
	 * file they want to read for sentiment analysis
	 * 
	 * @return true if file resource exists, false if not
	 */
	public String configureInputTxtFileLocation() {
		// method scope variables
		String fileName;
		String line;

		// prompt user to declare a txt file
		System.out.println("Search for a text file");

		fileName = inputReader.nextLine();// consume the whitespace
		fileName = inputReader.nextLine();// read the user input

		// specify file to use
		File selectedFile = new File(fileName);

		// because a scanner is being used a fileNotFoundException can happen so
		// surround try with catch
		try {
			System.out.println("Your file contents are:");
			// Use scanner to read from the file
			Scanner fileToReadFrom = new Scanner(selectedFile);

			// read the entire file in the while loop line by line
			while (fileToReadFrom.hasNext()) {
				// read file
				line = fileToReadFrom.nextLine();

				// text
				System.out.println(line);
			}

			// close the reader
			fileToReadFrom.close();

			return fileName;

		} catch (FileNotFoundException e) {
			// tell user what happened
			System.out.println("No text file found at the path:" + fileName);
			System.out.println("Please ensure the path is correct");
			// exit the method gracefully to not break user interaction with the menu
			return "";
		}
	}

	/**
	 * This method asks the user to specify which line of text they want to use from their file
	 * @return the line they chose
	 */
	public int specifyURL() {
		// method variables
		int choice;

		System.out.println("What line in the archive of tweets would you like to get the sentiment of:");
		choice = inputReader.nextInt();

		return choice;
	}

	/**
	 * This method is for specifying where you would like to save the results
	 * received from the sentiment analysis function
	 * 
	 * @return a valid file path or return empty String
	 */
	public String configureOutputTxtFileLocation() {
		// method scope variables
		String fileName;

		// prompt user to declare a txt file
		System.out.println("Enter the path of the file you would like to save results to");

		fileName = inputReader.nextLine();// consume the whitespace
		fileName = inputReader.nextLine();// read the user input

		System.out.println("File path for save configured successfully");

		// if invalid it has to return something so return an empty string
		return fileName;
	}

	/**
	 * Method to get the file path for the lexicon
	 * 
	 * @return returns a path for the lexicon if found if not an empty string is
	 *         passed
	 */
	public String configureLexiconPath() {
		// method scope variables
		String fileName;

		// prompt user to declare a txt file
		System.out.println("Enter the path of the lexicon you would like to use");

		fileName = inputReader.nextLine();// consume the whitespace
		fileName = inputReader.nextLine();// read the user input

		// specify file to use
		File selectedFile = new File(fileName);

		if (selectedFile.exists()) {
			System.out.println("Lexicon path configured successfully");
			return fileName;
		} else {
			System.out.println("No file found please try again");
		}

		return "";
	}

	/**
	 * getter for inputfilePath variable
	 * 
	 * @return
	 */
	public String getInputFilePath() {
		return inputFilePath;
	}

	/**
	 * setter for inputfilePath variable
	 * 
	 * @param inputFilePath
	 */
	public void setInputFilePath(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	/**
	 * 
	 * @return
	 */
	public String getOutputFilePath() {
		return outputFilePath;
	}

	/**
	 * 
	 * @param outputFilePath
	 */
	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}

	/**
	 * 
	 * @return
	 */
	public String getLexiconFilePath() {
		return lexiconFilePath;
	}

	/**
	 * 
	 * @param lexiconFilePath
	 */
	public void setLexiconFilePath(String lexiconFilePath) {
		this.lexiconFilePath = lexiconFilePath;
	}

	/**
	 * 
	 * @return
	 */
	public int getFileLine() {
		return fileLine;
	}

	/**
	 * 
	 * @param fileLine
	 */
	public void setFileLine(int fileLine) {
		this.fileLine = fileLine;
	}
}
