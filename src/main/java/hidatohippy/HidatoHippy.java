package hidatohippy;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.model.impl.WeightedNode;

/**
 * Solution to Code Golf challenge using A* Search algorithm.
 * 
 * Code Golf challenge:
 * http://codegolf.stackexchange.com/questions/54251/we-love
 * -our-weird-puzzles-us-brits
 * 
 * Useful references... http://rosettacode.org/wiki/Hidato
 * http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html
 * 
 * Puzzle generator... http://hidoku-solver.appspot.com/#1
 * 
 * Java search algorithm library "Hipster": http://www.hipster4j.org/
 * 
 * @author Gareth S.
 */

public class HidatoHippy {

	public static void main(String[] args) {

		try {

			if (args.length != 1) {
				displayUsage();
				return;
			}
			String fileName = args[0];
			HidatoBoard board = readBoard(fileName);

//			String folder = "src/main/resources/sampleinput/";
//			HidatoBoard board = readBoard(folder + "SampleInput1.txt");
//			HidatoBoard board = readBoard(folder + "SampleInput2.txt");
//			HidatoBoard board = readBoard(folder + "SampleInput3_impossible.txt");
//			HidatoBoard board = readBoard(folder + "SampleInput4.txt");
//			HidatoBoard board = readBoard(folder + "SampleInput5.txt");
//			HidatoBoard board = readBoard(folder + "SampleInput6.txt");

			System.out.println("Original...");
			System.out.println(board.boardAsString());

			Algorithm<HidatoAction, HidatoBoard, WeightedNode<HidatoAction, HidatoBoard, Double>>.SearchResult result = HidatoBoardSearcher
					.search(board);

			System.out.println("Finished...");
			HidatoBoard endState = result.getGoalNode().state();
			if (endState.connectedToLastNumber()) {
				System.out.println("Solution found!");
				System.out.println(endState.boardAsString());
				System.out.println("Additional details...");
				System.out.println(result);
			} else {
				System.out.println("Solution not found!");
				System.exit(1);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println();
			e.printStackTrace();
			System.exit(2);
		}
	}

	private static HidatoBoard readBoard(String fileName) {
		List<String> stringList = null;
		try {
			Path filePath = new File(fileName).toPath();
			stringList = Files.readAllLines(filePath, Charset.defaultCharset());
		} catch (IOException e) {
			System.out.println("Unable to read input file: " + fileName);
			throw new RuntimeException(e);
		}
		return new HidatoBoard(stringList.toArray(new String[] {}));
	}

	public static void displayUsage() {
		System.out.println("java HidatoHippy <input file>");
	}
}