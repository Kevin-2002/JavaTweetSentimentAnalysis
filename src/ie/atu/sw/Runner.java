package ie.atu.sw;

/**
 * This class is for the execution for this program
 */
public class Runner {

	/**
	 * main method used for running the applications making method calls e.c.t.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// main scope variables
		boolean exitOrCompute;
		// make a new instance of the menu class
		Menu menu = new Menu();
		ProcessSentimentVT virtualThreadsInstance;
		OutputResultsToFile analysisPrinter;

		// loop for running the entire program
		do {
			// print the menu
			exitOrCompute = menu.RunMenu();

			// check weather the program is being exited or if the virtual thread comparison
			// for lexicon and resource is needed instead
			// process the data and do the sentiment analysis if true is true
			if (exitOrCompute == true) {
				// data processing with virtual threads
				virtualThreadsInstance = new ProcessSentimentVT(menu.getInputFilePath(), menu.getFileLine(),
						menu.getLexiconFilePath());
				virtualThreadsInstance.go();

				// now that the sentiment has been calculated save analysis into file
				analysisPrinter = new OutputResultsToFile(menu.getOutputFilePath(), menu.getInputFilePath(),
						menu.getLexiconFilePath(), virtualThreadsInstance.getTweetSpecified(),
						virtualThreadsInstance.getFinalSentimentValue());
				analysisPrinter.runOutputResults();
			}
			// call the virtual thread class to dynamically do the comparison given the
			// paths from the menuInput.txt file
		} while (exitOrCompute == true);
		System.out.println("leaving");
	}
}