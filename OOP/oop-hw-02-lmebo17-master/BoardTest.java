import junit.framework.TestCase;

import java.awt.*;


public class BoardTest extends TestCase {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;
	Piece[] pieces;

	// This shows how to build things in setUp() to re-use
	// across tests.

	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.

	protected void setUp() throws Exception {
		b = new Board(3, 6);

		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();

		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
	}

	// Check the basic width/height/max after the one placement
	public void testSample1() {
		b.place(pyr1, 0, 0);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}

	// Place sRotated into the board, then check some measures
	public void testSample2() {
		b.place(pyr1, 0, 0);
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}

	public void testCustom1(){
		b.place(new Piece(Piece.SQUARE_STR), 0, 0);
		assertEquals(2, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		assertEquals(2, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
	}

	public void testCustom2(){
		b.place(new Piece(Piece.SQUARE_STR), 0, 0);
		b.commit();
		b.place(new Piece(Piece.STICK_STR),2,0);
		assertEquals(3, b.getRowWidth(0));
		assertEquals(3, b.getRowWidth(1));
		assertEquals(1, b.getRowWidth(2));
		assertEquals(2, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(4, b.getColumnHeight(2));
	}

	public void testCustomClearRows(){
		for(int i = 0; i < 2; i++){
			b.place(new Piece(Piece.STICK_STR), i, 0);
			b.commit();
		}
		int adding = b.place(new Piece(Piece.STICK_STR), 2, 0);
		assertEquals(adding, Board.PLACE_ROW_FILLED);
		int clearedRows = b.clearRows();
		assertEquals(4, clearedRows);
		assertEquals(0, b.maxHeight);
		assertEquals(0, b.getRowWidth(0));
		assertEquals(0, b.getColumnHeight(0));
	}

	public void testCustomClearRows1(){
		int res = b.place(new Piece(Piece.SQUARE_STR), 0, 0);
		b.commit();
		assertEquals(res , Board.PLACE_OK);
		res = b.place(new Piece(Piece.STICK_STR), 2, 0);
		b.commit();
		assertEquals(res , Board.PLACE_ROW_FILLED);
		int cleared = b.clearRows();
		assertEquals(2, cleared);
		assertEquals(2, b.getMaxHeight());
	}

	public void testAll() {
		pieces = Piece.getPieces();
		b.place(pieces[Piece.L1], 0, 0);
		b.commit();
		assertEquals(2, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(1, b.getRowWidth(2));
		assertEquals(3, b.getColumnHeight(0));

		assertEquals(Board.PLACE_ROW_FILLED, b.place(pieces[Piece.PYRAMID].fastRotation(), 1, b.dropHeight(pieces[Piece.PYRAMID].fastRotation(), 1)));
		assertEquals(3, b.getRowWidth(0));
		assertEquals(3, b.getRowWidth(1));
		assertEquals(2, b.getRowWidth(2));
		assertEquals(3, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(3, b.getMaxHeight());

		assertEquals(2, b.clearRows());
		b.undo();
		b.commit();
		assertEquals(2, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(1, b.getRowWidth(2));
		assertEquals(3, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		String test = "|   |\n" +
				"|   |\n" +
				"|   |\n" +
				"|+  |\n" +
				"|+  |\n" +
				"|++ |\n" +
				"-----";
		assertEquals(test, b.toString());
	}

	public void testBadPlace(){
		pieces = Piece.getPieces();
		int res = b.place(pieces[Piece.SQUARE], 0, 0);
		assertEquals(res, Board.PLACE_OK);
		b.commit();
		res = b.place(pieces[Piece.SQUARE], 0, 0);
		b.commit();
		assertEquals(res, Board.PLACE_BAD);
		b.undo();
		assertEquals(2, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
		assertEquals(2, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		res = b.place(pieces[Piece.PYRAMID], 7, 0);
		assertEquals(Board.PLACE_OUT_BOUNDS, res);
		b.undo();
		assertEquals(2, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
		assertEquals(2, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
	}



}
