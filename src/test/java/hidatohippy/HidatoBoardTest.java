package hidatohippy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class HidatoBoardTest {

	@Test
	public void testCanMove() throws Exception {

		String[] board = new String[] { "1", };
		HidatoBoard smallestBoard = new HidatoBoard(board);
		for (HidatoAction action : HidatoAction.values()) {
			assertFalse(smallestBoard.canMove(action));
		}

		board = new String[] { "XX XX XX", "XX 01 XX", "XX XX XX" };
		HidatoBoard hidatoBoard = new HidatoBoard(board);
		for (HidatoAction action : HidatoAction.values()) {
			assertFalse(hidatoBoard.canMove(action));
		}

		board[1] = "XX 01 --";
		testCanMove(board, new HidatoAction[] { HidatoAction.MOVE_RIGHT });

		board[1] = "-- 01 XX";
		testCanMove(board, new HidatoAction[] { HidatoAction.MOVE_LEFT });
		board[1] = "XX 01 XX";

		board[0] = "XX -- XX";
		testCanMove(board, new HidatoAction[] { HidatoAction.MOVE_UP });
		board[0] = "XX XX XX";

		board[2] = "XX -- XX";
		testCanMove(board, new HidatoAction[] { HidatoAction.MOVE_DOWN });
		board[2] = "XX XX XX";

		board[0] = "XX XX --";
		testCanMove(board, new HidatoAction[] { HidatoAction.MOVE_UP_RIGHT });

		board[0] = "-- XX XX";
		testCanMove(board, new HidatoAction[] { HidatoAction.MOVE_UP_LEFT });
		board[0] = "XX XX XX";

		board[2] = "XX XX --";
		testCanMove(board, new HidatoAction[] { HidatoAction.MOVE_DOWN_RIGHT });

		board[2] = "-- XX XX";
		testCanMove(board, new HidatoAction[] { HidatoAction.MOVE_DOWN_LEFT });
		board[2] = "XX XX XX";

		board = new String[] { "-- -- --", "-- 01 --", "-- -- --" };
		testCanMove(board, HidatoAction.values());

	}

	private void testCanMove(String[] boardConfig, HidatoAction[] actions) {
		HidatoBoard hidatoBoard = new HidatoBoard(boardConfig);
		for (HidatoAction action : actions) {
			assertTrue(hidatoBoard.canMove(action));
		}
	}

	@Test
	public void testMove() throws Exception {
		String[] board = new String[] { "1", };
		HidatoBoard smallestBoard = new HidatoBoard(board);
		for (HidatoAction action : HidatoAction.values()) {
			boolean thrown = false;
			try {
				smallestBoard.move(action);
			} catch (IllegalMoveException e) {
				thrown = true;
			}
			assertTrue(
					"Expected exception when attempt to move into illegal area",
					thrown);
		}

		board = new String[] { "XX XX XX", "XX 01 XX", "XX XX XX", };
		HidatoBoard hidatoBoard = new HidatoBoard(board);
		for (HidatoAction action : HidatoAction.values()) {
			boolean thrown = false;
			try {
				hidatoBoard.move(action);
			} catch (IllegalMoveException e) {
				thrown = true;
			}
			assertTrue(
					"Expected exception when attempt move into illegal area",
					thrown);
		}

		board = new String[] { "XX -- -- XX", "-- XX -- 01", "-- XX -- --", "XX -- -- XX" };

		HidatoBoard movingAroundBaoard = new HidatoBoard(board);
		movingAroundBaoard.move(HidatoAction.MOVE_UP_LEFT);
		movingAroundBaoard.move(HidatoAction.MOVE_LEFT);
		movingAroundBaoard.move(HidatoAction.MOVE_DOWN_LEFT);
		movingAroundBaoard.move(HidatoAction.MOVE_DOWN);
		movingAroundBaoard.move(HidatoAction.MOVE_DOWN_RIGHT);
		movingAroundBaoard.move(HidatoAction.MOVE_RIGHT);
		movingAroundBaoard.move(HidatoAction.MOVE_UP_RIGHT);
		movingAroundBaoard.move(HidatoAction.MOVE_LEFT);
		movingAroundBaoard.move(HidatoAction.MOVE_UP);
		String expectedMovingAroundBaoard = 
				"XX  3  2 XX " + System.lineSeparator() +
				" 4 XX 10  1 " + System.lineSeparator() +
				" 5 XX  9  8 " + System.lineSeparator() +
				"XX  6  7 XX " + System.lineSeparator();

		assertEquals(expectedMovingAroundBaoard,
				movingAroundBaoard.boardAsString());

	}

	@Test
	public void testIsConnectedToNext() throws Exception {
		String[] board = new String[] { "1 XX XX", "XX XX XX", "2 XX XX" };
		HidatoBoard disconnectedBoard = new HidatoBoard(board);
		assertFalse(disconnectedBoard.isConnectedToNext(new Point(0, 0)));

		board = new String[] { "1 XX XX", "3 XX XX", "XX XX XX" };
		disconnectedBoard = new HidatoBoard(board);
		assertFalse(disconnectedBoard.isConnectedToNext(new Point(0, 0)));

		board = new String[] { "XX XX XX", "XX 1 XX", "2 XX XX" };
		HidatoBoard connectedBoard = new HidatoBoard(board);
		assertTrue(connectedBoard.isConnectedToNext(new Point(1, 1)));

		board[2] = "XX 2 XX";
		connectedBoard = new HidatoBoard(board);
		assertTrue(connectedBoard.isConnectedToNext(new Point(1, 1)));

		board[2] = "XX XX 2";
		connectedBoard = new HidatoBoard(board);
		assertTrue(connectedBoard.isConnectedToNext(new Point(1, 1)));
		board[2] = "XX XX XX";

		board[0] = "2 XX XX";
		connectedBoard = new HidatoBoard(board);
		assertTrue(connectedBoard.isConnectedToNext(new Point(1, 1)));

		board[0] = "XX 2 XX";
		connectedBoard = new HidatoBoard(board);
		assertTrue(connectedBoard.isConnectedToNext(new Point(1, 1)));

		board[0] = "XX XX 2";
		connectedBoard = new HidatoBoard(board);
		assertTrue(connectedBoard.isConnectedToNext(new Point(1, 1)));
		board[0] = "XX XX XX";

		board[1] = "2 1 XX";
		connectedBoard = new HidatoBoard(board);
		assertTrue(connectedBoard.isConnectedToNext(new Point(1, 1)));

		board[1] = "XX 1 2";
		connectedBoard = new HidatoBoard(board);
		assertTrue(connectedBoard.isConnectedToNext(new Point(1, 1)));

	}

	@Test
			public void testConnectedToLastNumber() throws Exception {
				assertFalse(new HidatoBoard(new String[] { "1 2 -- 3" })
						.connectedToLastNumber());
				assertFalse(new HidatoBoard(new String[] { "1 -- 2 3" })
						.connectedToLastNumber());
				assertFalse(new HidatoBoard(new String[] { "1 4 -- 2 3" })
						.connectedToLastNumber());
		
				assertTrue(new HidatoBoard(new String[] { "1 2 3 4" })
						.connectedToLastNumber());
				
				String[] largeBoard = {
						"XX XX XX XX 52 53 XX XX XX XX",
						"XX XX XX XX 54 51 XX XX XX XX",
						"XX XX 56 55 28 50 30 31 XX XX",
						"XX XX 26 27 21 29 49 32 XX XX",
						"XX 25 24 20 22 48 45 44 33 XX",
						"XX 13 19 23 47 46 41 43 34 XX",
						"14 12 11 18 04 02 40 42 35 37",
						"15 16 17 10 05 03 01 39 38 36",
						"XX XX XX XX 09 06 XX XX XX XX",
						"XX XX XX XX 07 08 XX XX XX XX"
				};
				assertTrue(new HidatoBoard(largeBoard).connectedToLastNumber());
				
			}

	@Test
	public void testGetNextNumber() throws Exception {
		HidatoBoard alreadyCompleteHidatoBoard = new HidatoBoard(new String[] { "1 2 3" });
		assertEquals(2, (int) alreadyCompleteHidatoBoard.getNextNumber());
		
		alreadyCompleteHidatoBoard.move(HidatoAction.MOVE_RIGHT);
		assertEquals(3, (int) alreadyCompleteHidatoBoard.getNextNumber());
		
		alreadyCompleteHidatoBoard.move(HidatoAction.MOVE_RIGHT);
		assertEquals(null, (Integer) alreadyCompleteHidatoBoard.getNextNumber());
		
		
		HidatoBoard spacedHidatoBoard = new HidatoBoard(new String[] { "1 -- 3" });
		assertEquals(3, (int) spacedHidatoBoard.getNextNumber());

		spacedHidatoBoard.move(HidatoAction.MOVE_RIGHT);
		assertEquals(3, (int) spacedHidatoBoard.getNextNumber());

		spacedHidatoBoard.move(HidatoAction.MOVE_RIGHT);
		assertEquals(null, spacedHidatoBoard.getNextNumber());
		
		
		HidatoBoard withLargestHidatoBoard = new HidatoBoard(new String[] { "1 -- 3 -- 5" });
		assertEquals(3, (int) withLargestHidatoBoard.getNextNumber());
		
		withLargestHidatoBoard.move(HidatoAction.MOVE_RIGHT);
		assertEquals(3, (int) withLargestHidatoBoard.getNextNumber());
		
		withLargestHidatoBoard.move(HidatoAction.MOVE_RIGHT);
		assertEquals(5, (int) withLargestHidatoBoard.getNextNumber());
		
		withLargestHidatoBoard.move(HidatoAction.MOVE_RIGHT);
		assertEquals(5, (int) withLargestHidatoBoard.getNextNumber());
		
		withLargestHidatoBoard.move(HidatoAction.MOVE_RIGHT);
		assertEquals(null, withLargestHidatoBoard.getNextNumber());
		
		
		HidatoBoard withoutLargestHidatoBoard = new HidatoBoard(new String[] { "1 -- 3 -- --" });
		assertEquals(3, (int) withoutLargestHidatoBoard.getNextNumber());
		
		withoutLargestHidatoBoard.move(HidatoAction.MOVE_RIGHT);
		assertEquals(3, (int) withoutLargestHidatoBoard.getNextNumber());
		
		withoutLargestHidatoBoard.move(HidatoAction.MOVE_RIGHT);
		assertEquals(4, (int) withoutLargestHidatoBoard.getNextNumber());
		
		withoutLargestHidatoBoard.move(HidatoAction.MOVE_RIGHT);
		assertEquals(5, (int) withoutLargestHidatoBoard.getNextNumber());
		
		withoutLargestHidatoBoard.move(HidatoAction.MOVE_RIGHT);
		assertEquals(null, withoutLargestHidatoBoard.getNextNumber());
		
		
	}

	@Test
	public void testGetLocation() throws Exception {
		Point startAtOne = new Point(0, 0);
		assertEquals(startAtOne,
				new HidatoBoard(new String[] { "1 -- --" }).getLocation());
		assertEquals(startAtOne,
				new HidatoBoard(new String[] { "1 2 --" }).getLocation());
		assertEquals(startAtOne,
				new HidatoBoard(new String[] { "1 2 3" }).getLocation());
	}

	@Test
	public void testGetBoardWidthAndHeight() throws Exception {
		HidatoBoard hidatoBoard = new HidatoBoard(new String[] { "1 -- --",
				"-- -- --" });

		assertEquals(2, hidatoBoard.getHeight());
		assertEquals(3, hidatoBoard.getWidth());
	}

	@Test
	public void testGetDistanceTo() throws Exception {
		HidatoBoard oneToNine = new HidatoBoard(new String[] { 
				"01 02 03 04 05", 
				"06 07 08 09 10", 
				"11 12 13 14 15",
				"16 17 18 19 20",
				"21 22 23 24 25"
		});
		assertEquals(0, (double) oneToNine.getDistanceTo(1), 0);
		
		assertEquals(1, (double) oneToNine.getDistanceTo(2), 0);
		assertEquals(1, (double) oneToNine.getDistanceTo(7), 0);
		assertEquals(1, (double) oneToNine.getDistanceTo(6), 0);

		assertEquals(2, (double) oneToNine.getDistanceTo(3), 0);
		assertEquals(2, (double) oneToNine.getDistanceTo(8), 0);
		assertEquals(2, (double) oneToNine.getDistanceTo(13), 0);
		assertEquals(2, (double) oneToNine.getDistanceTo(12), 0);
		assertEquals(2, (double) oneToNine.getDistanceTo(11), 0);
		
		assertEquals(3, (double) oneToNine.getDistanceTo(4), 0);
		assertEquals(3, (double) oneToNine.getDistanceTo(9), 0);
		assertEquals(3, (double) oneToNine.getDistanceTo(14), 0);
		assertEquals(3, (double) oneToNine.getDistanceTo(19), 0);
		assertEquals(3, (double) oneToNine.getDistanceTo(18), 0);
		assertEquals(3, (double) oneToNine.getDistanceTo(17), 0);
		assertEquals(3, (double) oneToNine.getDistanceTo(16), 0);
		
		assertEquals(4, (double) oneToNine.getDistanceTo(5), 0);
		assertEquals(4, (double) oneToNine.getDistanceTo(10), 0);
		assertEquals(4, (double) oneToNine.getDistanceTo(15), 0);
		assertEquals(4, (double) oneToNine.getDistanceTo(20), 0);
		assertEquals(4, (double) oneToNine.getDistanceTo(25), 0);
		assertEquals(4, (double) oneToNine.getDistanceTo(24), 0);
		assertEquals(4, (double) oneToNine.getDistanceTo(23), 0);
		assertEquals(4, (double) oneToNine.getDistanceTo(22), 0);
		assertEquals(4, (double) oneToNine.getDistanceTo(21), 0);
	}

	@Test
	public void testIsSurrounded() throws Exception {
		assertTrue(new HidatoBoard(new String[] { "XX XX XX", "XX 01 XX", "XX XX XX" }).isSurrounded(new Point(1,1)));
		assertTrue(new HidatoBoard(new String[] { "XX XX XX", "XX 01 02", "XX XX XX" }).isSurrounded(new Point(1,1)));
		
		assertFalse(new HidatoBoard(new String[] { "XX XX XX", "-- 01 XX", "XX XX XX" }).isSurrounded(new Point(1,1)));
		assertFalse(new HidatoBoard(new String[] { "XX XX --", "XX 01 XX", "XX XX XX" }).isSurrounded(new Point(1,1)));
		assertFalse(new HidatoBoard(new String[] { "XX -- XX", "XX 01 XX", "XX XX XX" }).isSurrounded(new Point(1,1)));
		assertFalse(new HidatoBoard(new String[] { "-- XX XX", "XX 01 XX", "XX XX XX" }).isSurrounded(new Point(1,1)));
		assertFalse(new HidatoBoard(new String[] { "XX XX XX", "XX 01 --", "XX XX XX" }).isSurrounded(new Point(1,1)));
		assertFalse(new HidatoBoard(new String[] { "XX XX XX", "XX 01 XX", "-- XX XX" }).isSurrounded(new Point(1,1)));
		assertFalse(new HidatoBoard(new String[] { "XX XX XX", "XX 01 XX", "XX -- XX" }).isSurrounded(new Point(1,1)));
		assertFalse(new HidatoBoard(new String[] { "XX XX XX", "XX 01 XX", "XX XX --" }).isSurrounded(new Point(1,1)));
	}
	
	@Test
	public void testIsIsolated() throws Exception {
		// walls aren't isolated
		assertFalse(new HidatoBoard(new String[] { "XX XX XX" }).isIsolated(new Point(0,0)));
		
		// not isolated when not surrounded
		assertFalse(new HidatoBoard(new String[] { "01 -- XX" }).isIsolated(new Point(0,0)));
		
		// connected values aren't isolated
		assertFalse(new HidatoBoard(new String[] { "01 02 XX" }).isIsolated(new Point(0,0)));
		
		// isolated
		assertTrue(new HidatoBoard(new String[] { "XX 01 XX" }).isIsolated(new Point(1,0)));
		
		
	}	

	@Test
		public void testHasDuplicateNumbers() throws Exception {
			assertFalse(new HidatoBoard(new String[] {"1 -- -- XX" }).hasDuplicateNumbers());
			assertFalse(new HidatoBoard(new String[] {"1 -- XX XX" }).hasDuplicateNumbers());
			
			assertTrue(new HidatoBoard(new String[] {"1 1 -- XX" }).hasDuplicateNumbers());
			assertTrue(new HidatoBoard(new String[] {"1 2 2 --" }).hasDuplicateNumbers());
		}

	@Test
	public void testHasIsolatedNumbers() throws Exception {
		String[] isolatedBoard = new String[] {
			"XX XX XX XX XX",
			"XX 01 XX -- XX",
			"XX XX XX XX XX"
		};
		assertTrue(new HidatoBoard(isolatedBoard).hasIsolatedNumbers());
		
		String[] wasNotMeantToBeBoard = new String[] {
				"XX XX XX XX XX",
				"XX 01 XX 02 XX",
				"XX XX XX XX XX"
		};
		assertTrue(new HidatoBoard(wasNotMeantToBeBoard).hasIsolatedNumbers());		
		

		// TODO: Write test case where one side is not reachable anymore.
		// Idea for solution...
		// 1. From the largest connected number, collect/count reachable neighbors 
		// 2. Assert that all empty tiles are in this list
//		String[] splitUpBoard = new String[] {
//				"XX XX XX XX XX",
//				"XX 04 03 -- XX",
//				"XX -- 02 -- XX",
//				"XX -- 01 -- XX",
//				"XX XX XX XX XX"
//		};
//		assertTrue(new HidatoBoard(splitUpBoard).hasIsolatedNumbers());		
	}

	@Test
	public void testCellStream() throws Exception {
		assertEquals(6, new HidatoBoard(new String[] { "1 3 5", "2 4 6" }).cellStream().count());
		assertEquals(3, new HidatoBoard(new String[] { "1 2 3" }).cellStream().count());
		assertEquals(3, new HidatoBoard(new String[] { "1", "2", "3" }).cellStream().count());
		
		// HidatoBoard is 2D array of row x col where col.length = row[0].length. See setup().
		int threeNotFour = 3;
		assertEquals(threeNotFour, new HidatoBoard(new String[] { "1", "2 3", "4" }).cellStream().count());
	}

	@Test
	public void testPointStreamIntArrayArray() throws Exception {
		assertEquals(6, HidatoBoard.pointStream(new int[][] { {1, 3, 5}, {2, 4, 6} }).count());
		assertEquals(3, HidatoBoard.pointStream(new int[][] { {1, 2, 3} }).count());
		assertEquals(3, HidatoBoard.pointStream(new int[][] { {1}, {2}, {3} }).count());
		
		// Unlike the internal hidato board, columns can be different lengths. See testCellStream() 
		int unevenBoardIsOkay = 4;
		assertEquals(unevenBoardIsOkay, HidatoBoard.pointStream(new int[][] {{1}, {2, 3}, {4}}).count());
	}

	@Test
	public void testPointStreamPoint() throws Exception {
		assertEquals(8, HidatoBoard.pointStream(new Point(0,0)).count());
		
		List<Point> generated = HidatoBoard.pointStream(new Point(0,0)).collect(Collectors.toList());
		assertTrue(generated.containsAll(Arrays.asList(new Point[] {
			new Point(-1,-1), new Point(0,-1), new Point(1,-1),	
			new Point(-1, 0), /* the centre */ new Point(1, 0),	
			new Point(-1, 1), new Point(0, 1), new Point(1, 1)
		})));
	}

	@Test
	public void testSetup() throws Exception {
		HidatoBoard hidatoBoard = new HidatoBoard(new String[] { "XX XX XX", "01 -- --", "XX XX XX" });
		assertEquals(9, hidatoBoard.cellStream().count());
		assertEquals(HidatoBoard.WALL, hidatoBoard.getCellValue(new Point(0,0)));
		assertEquals(1, hidatoBoard.getCellValue(new Point(0,1)));
		assertEquals(HidatoBoard.NUMBER_PLACEHOLDER, hidatoBoard.getCellValue(new Point(1,1)));
	}
}
