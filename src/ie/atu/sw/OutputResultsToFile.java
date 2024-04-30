package ie.atu.sw;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class is only for writing all the necessary details to a file
 */
public class OutputResultsToFile {
	// class variables
	String path;
	String twitterPath;
	String lexiconPath;
	String tweet;
	double sentimentValue;

	/**
	 * default constructor to accept parameters when an instance is made
	 * 
	 * @param path           :the path of the file we write to
	 * @param twitterPath    :the path of the file resource the text that was
	 *                       analyzed was in
	 * @param lexiconPath    :the path of the lexicon the sentiment value
	 *                       calculations were based on
	 * @param tweet          :the line of words that were analyzed
	 * @param sentimentValue :the sentiment value of the line
	 */
	public OutputResultsToFile(String path, String twitterPath, String lexiconPath, String tweet,
			double sentimentValue) {
		this.path = path;
		this.twitterPath = twitterPath;
		this.lexiconPath = lexiconPath;
		this.tweet = tweet;
		this.sentimentValue = sentimentValue;
	}

	/**
	 * This method serves as a method call to get the ball rolling from the runner
	 * class
	 * 
	 * @throws IOException because checkIfPathIsNull() has throws IOException
	 */
	public void runOutputResults() throws IOException {

		System.out.println("Saving all of the below details to a file");
		System.out.println("Output file (if null then ./out.txt): " + getPath());
		System.out.println("The path of the file analyzed: " + getTwitterPath());
		System.out.println("The path of the given lexicon: " + getLexiconPath());
		System.out.println("The line analyzed: " + getTweet());
		System.out.println("The total sentiment value: " + getSentimentValue());

		checkIfPathIsNull();
	}

	/**
	 * This method checks whether it should use the default file path if the user
	 * hasn't specified one
	 * 
	 * @throws IOException because saveResultsToFile(String path) has throws
	 *                     IOException
	 */
	public void checkIfPathIsNull() throws IOException {
		if (getPath() != null) {
			// pass in the path
			saveResultsToFile(getPath());
		} else {
			// use default path to save to
			saveResultsToFile("./out.txt");
		}
	}

	/**
	 * This does the file writing now that the prerequisites are out of the way
	 * 
	 * @param path the path we write to
	 * @throws IOException this method uses FileWriter and PrintWriter
	 */
	public void saveResultsToFile(String path) throws IOException {
		FileWriter fileWriter = new FileWriter(path, true);// turn on append mode

		PrintWriter fileWriterWithPrintMethods = new PrintWriter(fileWriter);// give access to print methods

		//separate any previous contents in the file clearly to make it an easier read
		fileWriterWithPrintMethods.println("\n-------------------------------------------------------------");
		
		// do the file write
		fileWriterWithPrintMethods.println("ANALYSIS DETAILS:");
		fileWriterWithPrintMethods.println("Resource paths:");
		fileWriterWithPrintMethods.println("File path being compared:" + getTwitterPath());
		fileWriterWithPrintMethods.println("Lexicon path:" + getLexiconPath());
		fileWriterWithPrintMethods.println("Line submitted for analysis:" + getTweet());
		fileWriterWithPrintMethods.println(
				"Sentiment value for this line (NOTE: the higher the value the more positive the statement was in sentimental value):"
						+ getSentimentValue());

		// update file and recapture resources
		fileWriter.close();
	}

	/**
	 * 
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 
	 * @return
	 */
	public String getTwitterPath() {
		return twitterPath;
	}

	/**
	 * 
	 * @param twitterPath
	 */
	public void setTwitterPath(String twitterPath) {
		this.twitterPath = twitterPath;
	}

	/**
	 * 
	 * @return
	 */
	public String getLexiconPath() {
		return lexiconPath;
	}

	/**
	 * 
	 * @param lexiconPath
	 */
	public void setLexiconPath(String lexiconPath) {
		this.lexiconPath = lexiconPath;
	}

	/**
	 * 
	 * @return
	 */
	public String getTweet() {
		return tweet;
	}

	/**
	 * 
	 * @param tweet
	 */
	public void setTweet(String tweet) {
		this.tweet = tweet;
	}

	/**
	 * 
	 * @return
	 */
	public double getSentimentValue() {
		return sentimentValue;
	}

	/**
	 * 
	 * @param sentimentValue
	 */
	public void setSentimentValue(double sentimentValue) {
		this.sentimentValue = sentimentValue;
	}

}
