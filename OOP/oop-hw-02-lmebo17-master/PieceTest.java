import junit.framework.TestCase;

import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece[] pieces;
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;

	protected void setUp() throws Exception {
		super.setUp();
		pieces = Piece.getPieces();
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();

		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
	}


	// Here are some sample tests to get you started
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());

		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());

		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}


	// Test the skirt returned by a few pieces
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));

		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
	}

	public void testStickSizes(){
		Piece stick = pieces[0];
		assertEquals(stick.getWidth(), 1);
		assertEquals(stick.getHeight(), 4);
		Piece nextRotation = stick.fastRotation();
		assertEquals(nextRotation.getWidth(), 4);
		assertEquals(nextRotation.getHeight(), 1);
	}

	public void testStickRotation(){
		Piece stick = pieces[0];
		Piece nextRotation1 = stick.fastRotation();
		Piece nextRotation2 = nextRotation1.fastRotation();
		Piece nextRotation3 = nextRotation2.fastRotation();
		Piece nextRotation4 = nextRotation3.fastRotation();
		assertEquals(stick, nextRotation4);
		assertEquals(stick, nextRotation2);
		assertEquals(nextRotation1, nextRotation3);
		assertNotSame(stick, nextRotation1);
	}

	public void testStickSkirt(){
		Piece stick = pieces[0];
		Piece nextRotation = stick.fastRotation();
		assertTrue(Arrays.equals(stick.getSkirt(), new int[]{0}));
		assertTrue(Arrays.equals(nextRotation.getSkirt(), new int[]{0,0,0,0}));

	}

	public void testL1Sizes(){
		Piece figure = pieces[1];
		Piece figure1 = figure.fastRotation();
		Piece figure2 = figure1.fastRotation();
		Piece figure3 = figure2.fastRotation();
		assertEquals(figure.getWidth(), 2);
		assertEquals(figure.getHeight(), 3);
		assertEquals(figure1.getWidth(), 3);
		assertEquals(figure1.getHeight(), 2);
		assertEquals(figure2.getWidth(), 2);
		assertEquals(figure2.getHeight(), 3);
		assertEquals(figure3.getWidth(), 3);
		assertEquals(figure3.getHeight(), 2);
	}

	public void testL1Rotations(){
		Piece figure = pieces[1];
		Piece figure1 = figure.fastRotation();
		Piece figure2 = figure1.fastRotation();
		Piece figure3 = figure2.fastRotation();
		Piece figure4 = figure3.fastRotation();
		assertEquals(figure, figure4);
	}

	public void testL1Skirts(){
		Piece figure = pieces[1];
		Piece figure1 = figure.fastRotation();
		Piece figure2 = figure1.fastRotation();
		Piece figure3 = figure2.fastRotation();
		assertTrue(Arrays.equals(figure.getSkirt(), new int[]{0,0}));
		assertTrue(Arrays.equals(figure1.getSkirt(), new int[]{0,0,0}));
		assertTrue(Arrays.equals(figure2.getSkirt(), new int[]{2,0}));
		assertTrue(Arrays.equals(figure3.getSkirt(), new int[]{0,1,1}));
	}


	public void testL2Rotations(){
		Piece figure = pieces[2];
		Piece figure1 = figure.fastRotation();
		Piece figure2 = figure1.fastRotation();
		Piece figure3 = figure2.fastRotation();
		Piece figure4 = figure3.fastRotation();
		assertEquals(figure, figure4);
	}

	public void testL2Skirts(){
		Piece figure = pieces[2];
		Piece figure1 = figure.fastRotation();
		Piece figure2 = figure1.fastRotation();
		Piece figure3 = figure2.fastRotation();
		assertTrue(Arrays.equals(figure.getSkirt(), new int[]{0,0}));
		assertTrue(Arrays.equals(figure1.getSkirt(), new int[]{1,1,0}));
		assertTrue(Arrays.equals(figure2.getSkirt(), new int[]{0,2}));
		assertTrue(Arrays.equals(figure3.getSkirt(), new int[]{0,0,0}));
	}

	public void testL2Sizes(){
		Piece figure = pieces[2];
		Piece figure1 = figure.fastRotation();
		Piece figure2 = figure1.fastRotation();
		Piece figure3 = figure2.fastRotation();
		assertEquals(figure.getWidth(), 2);
		assertEquals(figure.getHeight(), 3);
		assertEquals(figure1.getWidth(), 3);
		assertEquals(figure1.getHeight(), 2);
		assertEquals(figure2.getWidth(), 2);
		assertEquals(figure2.getHeight(), 3);
		assertEquals(figure3.getWidth(), 3);
		assertEquals(figure3.getHeight(), 2);
	}

	public void testS1Sizes(){
		Piece figure = pieces[3];
		Piece figure1 = figure.fastRotation();
		Piece figure2 = figure1.fastRotation();
		Piece figure3 = figure2.fastRotation();
		assertEquals(figure.getWidth(), 3);
		assertEquals(figure.getHeight(), 2);
		assertEquals(figure1.getWidth(), 2);
		assertEquals(figure1.getHeight(), 3);
		assertEquals(figure2.getWidth(), 3);
		assertEquals(figure2.getHeight(), 2);
		assertEquals(figure3.getWidth(), 2);
		assertEquals(figure3.getHeight(), 3);
	}

	public void testS1Rotations(){
		Piece figure = pieces[3];
		Piece figure1 = figure.fastRotation();
		Piece figure2 = figure1.fastRotation();
		Piece figure3 = figure2.fastRotation();
		Piece figure4 = figure3.fastRotation();
		assertEquals(figure, figure4);
		assertEquals(figure, figure2);
		assertEquals(figure1, figure3);
	}

	public void testS1Skirts(){
		Piece figure = pieces[3];
		Piece figure1 = figure.fastRotation();
		Piece figure2 = figure1.fastRotation();
		Piece figure3 = figure2.fastRotation();
		assertTrue(Arrays.equals(figure.getSkirt(), new int[]{0,0,1}));
		assertTrue(Arrays.equals(figure1.getSkirt(), new int[]{1,0}));
		assertTrue(Arrays.equals(figure2.getSkirt(), new int[]{0,0,1}));
		assertTrue(Arrays.equals(figure3.getSkirt(), new int[]{1,0}));
	}

	public void testS2Sizes(){
		Piece figure = pieces[4];
		Piece figure1 = figure.fastRotation();
		Piece figure2 = figure1.fastRotation();
		Piece figure3 = figure2.fastRotation();
		assertEquals(figure.getWidth(), 3);
		assertEquals(figure.getHeight(), 2);
		assertEquals(figure1.getWidth(), 2);
		assertEquals(figure1.getHeight(), 3);
		assertEquals(figure2.getWidth(), 3);
		assertEquals(figure2.getHeight(), 2);
		assertEquals(figure3.getWidth(), 2);
		assertEquals(figure3.getHeight(), 3);
	}

	public void testS2Rotations(){
		Piece figure = pieces[4];
		Piece figure1 = figure.fastRotation();
		Piece figure2 = figure1.fastRotation();
		Piece figure3 = figure2.fastRotation();
		Piece figure4 = figure3.fastRotation();
		assertEquals(figure, figure4);
		assertEquals(figure, figure2);
		assertEquals(figure1, figure3);
	}

	public void testS2Skirts(){
		Piece figure = pieces[4];
		Piece figure1 = figure.fastRotation();
		Piece figure2 = figure1.fastRotation();
		Piece figure3 = figure2.fastRotation();
		assertTrue(Arrays.equals(figure.getSkirt(), new int[]{1,0,0}));
		assertTrue(Arrays.equals(figure1.getSkirt(), new int[]{0,1}));
		assertTrue(Arrays.equals(figure2.getSkirt(), new int[]{1,0,0}));
		assertTrue(Arrays.equals(figure3.getSkirt(), new int[]{0,1}));
	}

	public void testSquare(){
		Piece square = pieces[5];
		Piece rotated = square.computeNextRotation();
		assertEquals(square, rotated);
		assertEquals(square.getWidth(), 2);
		assertEquals(square.getHeight(), 2);
		assertTrue(Arrays.equals(square.getSkirt(), new int[]{0,0}));
	}

	public void testBadInput(){
		try{
			Piece piece = new Piece("1 2 06 a 3");
		} catch (RuntimeException e){
			assertEquals("Could not parse x,y string:1 2 06 a 3", e.getMessage());}
	}
}
