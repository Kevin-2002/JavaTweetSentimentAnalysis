package ie.atu.sw;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.StructuredTaskScope;

/**
 * Sentiment Analysis done here using the thread safe concurrent resources and
 * virtual threads
 */
public class ProcessSentimentVT {

	// class variables
	String inputFilePath;
	int tweetLine;
	String lexiconFilePath;
	// variables that need gets and sets to send to output file
	String tweetSpecified;
	double finalSentimentValue;

	// store lexicon words and sentiment values in a map
	ConcurrentHashMap<String, Double> lexicon = new ConcurrentHashMap<>();

	// store all tweets
	ConcurrentHashMap<Integer, String> allTweets = new ConcurrentHashMap<>();

	// store a chosen tweet
	ConcurrentLinkedDeque<String> tweetWordSet = new ConcurrentLinkedDeque<String>();

	// store the values of the words sentiment in here
	ConcurrentLinkedDeque<Double> tweetSentimentValues = new ConcurrentLinkedDeque<Double>();

	/**
	 * This is the constructor for ProcessSentimentVT.java it takes the 3 file paths
	 * entered from the user and stores them into variables that have gets and sets
	 * 
	 * @param inputFilePath   this contains a path to a file filled with words the
	 *                        user will want to calculate the sentiment value of
	 * @param tweetLine       this contains a path to a file that will contain the
	 *                        results and analysis report calculated
	 * @param lexiconFilePath this contains the path of the desired lexicon file the
	 *                        user would like to read from
	 */
	public ProcessSentimentVT(String inputFilePath, int tweetLine, String lexiconFilePath) {
		this.inputFilePath = inputFilePath;
		this.tweetLine = tweetLine;
		this.lexiconFilePath = lexiconFilePath;
	}

	/**
	 * Generally this method job is to get the lexicon to read each line in as a
	 * stream to ensure smooth reading in and for each line it forks to ensure
	 * concurrent processing of the lines then it call the
	 * isolateWordAndValueFromLexicon(line); which will then process each line.
	 * 
	 * @throws Throwable in case the try clause fails
	 */
	public void readLexicon() throws Throwable {
		try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
			Files.lines(Paths.get(getLexiconFilePath())).forEach(line -> {
				scope.fork(() -> {
					isolateWordAndValueFromLexicon(line);
					return null;//to prevent an error when forking
				});
			});
			scope.join();// join all of the forks from each element
			scope.throwIfFailed(e -> e);
		}
	}

	/**
	 * make a method to single out the word before the comma and read in every value
	 * after the comma with no whitespace
	 * 
	 * @param line is a line of text from the lexicon
	 */
	public void isolateWordAndValueFromLexicon(String line) {
		// split the line at the comma and separate the two sides into an array
		String[] parts = line.split(",");

		// Ensure that the line has both word and sentiment
		if (parts.length == 2) {
			// assign word and number to a variable without the whiteSpace
			String word = parts[0].trim();
			String sentimentValue = parts[1].trim();

			// Add word and sentiment to the ConcurrentHashMap
			lexicon.put(word, Double.parseDouble(sentimentValue));

		} else {
			// When there is an error in lexicon format print error
			System.out.println("Invalid line format: " + line);
		}
	}

	/**
	 * This method reads the file that contains the stuff the user wants to be analyzed
	 * 
	 * @throws scope.throwIfFailed(e -> e); happens if for join fails
	 */
	public void readInputFile() throws Throwable {
		// method variables

		// read the input text into a set

		try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
			Files.lines(Paths.get(getInputFilePath())).forEach(line -> {
				scope.fork(() -> {
					// .put method calculating an index for each
					storeEachTweetLine(line);
					return null;
				});
			});
			scope.join();// join all of the forks from each element
			scope.throwIfFailed(e -> e);

			// use tweetLine from menu to get the words of that specified line and add them
			// to tweetWordSet
			if (allTweets.containsKey(getTweetLine())) {
				// pass through the tweet sentence to be processed
				isolateTweetWordByWord(allTweets.get(getTweetLine()));

				// set the line to send to the output to file class
				setTweetSpecified(allTweets.get(getTweetLine()));
			}

			// tells the user why nothing is there
			else {
				System.out.println("The file didn't contain a line of text at line: " + getTweetLine());
			}
			System.out.println("This is your tweet\n" + getTweetSpecified());
		}
	}

	/**
	 * This takes the whole line and puts it into the allTweets map with an index as it's key
	 * so when we need to check which line the user has entered we can obtain it easily
	 * 
	 * @param line a line from the file the user wanted analyzed
	 */
	public void storeEachTweetLine(String line) {
		// Get the current size of the map for indexing purposes as I couldn't find a
		// thread-safe concurrent ArrayList
		int currentIndex = allTweets.size();

		// take the lines as they are then add them to the list with the index
		// I do cannot have the same line variable so it is just called text
		Arrays.stream(line.split(".*\\n.*")).forEach(text -> allTweets.put(currentIndex, text));
	}

	/**
	 * separates each word from the line and adds each word into tweetWordSet as
	 * separate records to make calculation possible
	 * 
	 * @param line needs the line of text to isolate words from
	 */
	public void isolateTweetWordByWord(String line) {
		Arrays.stream(line.split("\\s+")).forEach(word -> tweetWordSet.add(word));
	}

	/**
	 * this method gets each word and calls another method that gets its sentiment
	 * value, then it adds it to a data structure then it calls a method that then
	 * uses this to calculate the average of these sentiment values and prints it
	 * for the user
	 * 
	 * @throws Throwable
	 */
	public void calculateSentimentValue() throws Throwable {
		try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
			tweetWordSet.forEach((word) -> {
				scope.fork(() -> {
					tweetSentimentValues.add(processWord(word));
					return null;
				});
			});

			scope.join();// join all of the forks from each element
			scope.throwIfFailed(e -> e);
			System.out.println("The sentiment value of the tweet was: " + getAverageSentimentValue());
		}
	}

	/**
	 * this is the finding of a given word sentiment value from the lexicon map
	 * 
	 * @param word needs the word to check if it is in the lexicon
	 * @return 0 when the value is not in the lexicon, or return a matched value
	 */
	public double processWord(String word) {
		// when the word is not found it makes the double null prevent the error here
		if (lexicon.get(word) == null) {
			return 0;
		}

		// if the word has value in the lexicon return it
		return lexicon.get(word);
	}

	/**
	 * This averages all of the values in the tweetSentimentValues collection 
	 * 
	 * @return the average sentiment
	 */
	public double getAverageSentimentValue() {
		// method variables
		double total = 0;
		int totalAmountOfValues = 0, i = 0;

		// get total amount of values
		for (Double sentimentValue : tweetSentimentValues) {
			totalAmountOfValues++;
		}

		for (Double sentimentValue : tweetSentimentValues) {
			i++;// iterate as soon as it opens so the checks run correctly

			// not the last value
			if (totalAmountOfValues > i) {
				total += sentimentValue;// add all the values
			}

			// on the last number
			else {
				total += sentimentValue;// add last value and average by the number of results
				total = total / totalAmountOfValues;// divide the sum of total by number of values to average
			}
		}

		// set the result in order to give it to the output file
		setFinalSentimentValue(total);

		// send result back to method call
		return total;
	}

	/**
	 * called in the runner classes' main method this .go() method makes virtual threads
	 * and forks them and kick-starts the process of the calculating and data reading concurrently
	 * @throws Exception
	 */
	public void go() throws Exception {
		var tReadLexicon = Thread.ofVirtual().unstarted(() -> {
			try {
				readLexicon();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		});
		tReadLexicon.start();
		tReadLexicon.join();

		var tReadInputFile = Thread.ofVirtual().unstarted(() -> {
			try {
				readInputFile();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		});

		tReadInputFile.start();
		tReadInputFile.join();

		var task = ForkJoinPool.commonPool() // Not the same FJP used by virtual threads
				.submit(() -> {
					// get output
					try {
						calculateSentimentValue();
					} catch (Throwable e) {
						e.printStackTrace();
					}
				});
		task.join();
	}

	public String getInputFilePath() {
		return inputFilePath;
	}

	public void setInputFilePath(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	public int getTweetLine() {
		return tweetLine;
	}

	public void setTweetLine(int tweetLine) {
		this.tweetLine = tweetLine;
	}

	public String getLexiconFilePath() {
		return lexiconFilePath;
	}

	public void setLexiconFilePath(String lexiconFilePath) {
		this.lexiconFilePath = lexiconFilePath;
	}

	public String getTweetSpecified() {
		return tweetSpecified;
	}

	public void setTweetSpecified(String tweetSpecified) {
		this.tweetSpecified = tweetSpecified;
	}

	public double getFinalSentimentValue() {
		return finalSentimentValue;
	}

	public void setFinalSentimentValue(double finalSentimentValue) {
		this.finalSentimentValue = finalSentimentValue;
	}

}
