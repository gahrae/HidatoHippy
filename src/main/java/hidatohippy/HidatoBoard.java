package hidatohippy;

import java.awt.Point;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HidatoBoard {

	public static final int NUMBER_PLACEHOLDER = -1;
	public static final int WALL = -2;

	private static final String DISPLAY_NUMBER_PLACEHOLDER = "--";
	private static final String DISPLAY_WALL = "XX";

	Point location;
	int[][] board;

	public HidatoBoard(String[] boardConfiguration) {
		setup(boardConfiguration);
	}

	public HidatoBoard(HidatoBoard hidatoBoard) {
		this.location = new Point(hidatoBoard.getLocation());
		this.board = copyArray(hidatoBoard.board);
	}

	protected static int[][] copyArray(int[][] array) {
		int[][] newArray = new int[array.length][];
		for (int i = 0; i < array.length; i++)
			newArray[i] = Arrays.copyOf(array[i], array[i].length);
		return newArray;
	}

	public void setup(String[] input) {
		String[][] puzzle = new String[input.length][];
		for (int i = 0; i < input.length; i++)
			puzzle[i] = input[i].split(" ");

		// Note: Boards are equal in width and height
		int nRows = puzzle.length;
		int nCols = puzzle[0].length;

		board = new int[nRows][nCols];

		for (int r = 0; r < nRows; r++) {
			String[] row = puzzle[r];
			for (int c = 0; c < nCols; c++) {
				String cell = row[c];
				switch (cell) {
				case DISPLAY_NUMBER_PLACEHOLDER:
					board[r][c] = NUMBER_PLACEHOLDER;
					break;
				case DISPLAY_WALL:
					board[r][c] = WALL;
					break;
				default:
					int val = Integer.parseInt(cell);
					setCellValue(new Point(c, r), val);
					if (val == 1)
						location = new Point(c, r);
				}
			}
		}
	}

	public void move(HidatoAction direction) {
		if (!canMove(direction)) {
			throw new IllegalArgumentException("Unable to move in direction: "
					+ direction);
		}

		int nextValue = getCellValue(location) + 1;
		location = direction.move(location);
		setCellValue(location, nextValue);
	}

	public boolean canMove(HidatoAction direction) {
		Point newLocation = direction.move(location);
		if (newLocation.x < 0 || newLocation.x > getWidth() - 1
				|| newLocation.y < 0 || newLocation.y > getHeight() - 1) {
			return false;
		}

		return getCellValue(newLocation) == NUMBER_PLACEHOLDER
				|| getCellValue(newLocation) == getCellValue(location) + 1;
	}

	/**
	 * The next largest visible number from the current cell.
	 * 
	 * Is the current cell value plus one when no numbers are visible. Is null
	 * when no next number exists.
	 * 
	 * @return The next largest number
	 */
	public Integer getNextNumber() {
		int cellValue = getCellValue(location);
		int nextLargest = Integer.MAX_VALUE;
		int largest = Integer.MIN_VALUE;
		boolean hasEmptyCell = false;

		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board[0].length; x++) {
				Point cell = new Point(x,y);
				
				int currentCellValue = getCellValue(cell);

				if (currentCellValue > largest) {
					largest = currentCellValue;
				}

				if (currentCellValue == NUMBER_PLACEHOLDER) {
					hasEmptyCell = true;
				}

				if (currentCellValue > cellValue && currentCellValue < nextLargest
						&& currentCellValue <= largest) {
					nextLargest = currentCellValue;
				}
			}
		}

		boolean noNextValue = !hasEmptyCell && cellValue == largest;
		if (noNextValue) {
			return null;
		}

		if (nextLargest != Integer.MAX_VALUE) {
			return nextLargest;
		}

		return cellValue + 1;
	}

	public Integer getDistanceTo(Integer next) {
		Optional<Point> optionalNextLocation = cellStream().filter(
				cell -> getCellValue(cell) == next).findFirst();
		if (!optionalNextLocation.isPresent()) {
			return null;
		}
		Point nextLocation = optionalNextLocation.get();
		int maxXDistance = Math.abs((location.x - nextLocation.x));
		int maxYDistance = Math.abs((location.y - nextLocation.y));
		return (maxXDistance > maxYDistance) ? maxXDistance : maxYDistance;
	}

	public void setCellValue(Point cell, int value) {
		board[cell.y][cell.x] = value;
	}

	public int getCellValue(Point cell) {
		return board[cell.y][cell.x];
	}

	public String boardAsString() {
		String display = "";
		for (int[] row : board) {
			for (int c : row) {
				if (c == WALL)
					display += DISPLAY_WALL + " ";
				else
					display += String.format(c > 0 ? "%2d "
							: DISPLAY_NUMBER_PLACEHOLDER + " ", c);
			}
			display += System.lineSeparator();
		}
		return display;
	}

	/**
	 * A Stream of all points on the board. Includes walls.
	 **/
	public Stream<Point> cellStream() {
		return pointStream(board);
	}

	/**
	 * A Stream of points around the given point. Does not include walls and is
	 * inside of board boundaries.
	 **/
	public Stream<Point> deltaCellStream(Point cell) {
		return pointStream(cell).filter(
				point -> point.x >= 0 && point.x < getWidth() && point.y >= 0
						&& point.y < getHeight()).filter(
				point -> getCellValue(point) != WALL);
	}

	/**
	 * The last number is connected to when it is the only cell that is not
	 * connected to one higher than it.
	 * 
	 * @return true when connected to last number and false otherwise.
	 */
	public boolean connectedToLastNumber() {
		return cellStream().filter(
				cell -> getCellValue(cell) != WALL && !isConnectedToNext(cell))
				.count() == 1;
	}

	/**
	 * A cell is connected when it is next to another cell that has a value one
	 * higher than it.
	 * 
	 * @param cell
	 *            location to examine.
	 * @return return true when connected to next, and false otherwise.
	 */
	public boolean isConnectedToNext(Point cell) {
		int cellValue = getCellValue(cell);
		int nextValue = cellValue + 1;
		return cellValue != NUMBER_PLACEHOLDER
				&& deltaCellStream(cell).anyMatch(
						delta -> getCellValue(delta) == nextValue);
	}

	/**
	 * There are duplicate numbers on the board when more than one occurrence of
	 * the number is visible.
	 * 
	 * @return true when duplicate numbers, and false otherwise.
	 */
	public boolean hasDuplicateNumbers() {
		return cellStream().map(cell -> getCellValue(cell))
				.filter(value -> value != NUMBER_PLACEHOLDER && value != WALL)
				.collect(Collectors.groupingBy(value -> value)).entrySet()
				.stream().anyMatch(e -> e.getValue().size() > 1);
	}

	/**
	 * A board has isolated numbers when one or more of the cells is isolated.
	 * 
	 * @see HidatoBoard#isIsolated(Point)
	 * @return true if board contains isolated numbers, and false otherwise.
	 */
	public boolean hasIsolatedNumbers() {
		return cellStream().anyMatch(this::isIsolated);
	}

	/**
	 * A cell is isolated when it:
	 * <ul>
	 * <li>Is not a wall, and
	 * <li>Is not next to a cell that has a value one higher than it, and</li>
	 * <li>Is not touching an empty cell</li>
	 * </ul>
	 * 
	 * @param cell
	 *            location to examine
	 * @return true when isolated, and false otherwise.
	 */
	public boolean isIsolated(Point cell) {
		return getCellValue(cell) != WALL && isSurrounded(cell)
				&& !isConnectedToNext(cell);
	}

	/**
	 * A cell is surrounded when it is not connected to an empty cell.
	 * 
	 * @param cell
	 *            location to examine
	 * @return true when cell is surrounded and false otherwise.
	 **/
	public boolean isSurrounded(Point cell) {
		return deltaCellStream(cell).noneMatch(
				delta -> getCellValue(delta) == NUMBER_PLACEHOLDER);
	}

	public int[][] getBoard() {
		return board;
	}

	public int getHeight() {
		return board.length;
	}

	public int getWidth() {
		return board[0].length;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public String toString() {
		return String.format("(%d,%d)=%d", location.x, location.y,
				getCellValue(location));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(board);
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HidatoBoard other = (HidatoBoard) obj;
		if (!Arrays.deepEquals(board, other.board))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}

	public static Stream<Point> pointStream(int[][] board) {
		return IntStream
				.rangeClosed(0, board.length - 1)
				.mapToObj(
						y -> {
							return (Stream<Point>) IntStream.rangeClosed(0,
									board[y].length - 1).mapToObj(x -> {
								return new Point(x, y);
							});
						}).flatMap(innerStream -> innerStream);
	}

	public static Stream<Point> pointStream(Point origin) {
		return IntStream
				.rangeClosed(-1, 1)
				.mapToObj(
						y -> (Stream<Point>) IntStream.rangeClosed(-1, 1)
								.mapToObj(
										x -> new Point(origin.x + x, origin.y
												+ y)))
				.flatMap(innerStream -> innerStream);
	}
}
