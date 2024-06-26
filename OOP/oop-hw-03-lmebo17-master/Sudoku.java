import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */

public class Sudoku {

	// Provided grid data for main/testing
	// The instance variable strategy is up to you.

	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
			"1 6 4 0 0 0 0 0 2",
			"2 0 0 4 0 3 9 1 0",
			"0 0 5 0 8 0 4 0 7",
			"0 9 0 0 0 6 5 0 0",
			"5 0 0 1 0 2 0 0 8",
			"0 0 8 9 0 0 0 3 0",
			"8 0 9 0 4 0 2 0 0",
			"0 7 3 5 0 9 0 0 1",
			"4 0 0 0 0 0 6 7 9");


	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
			"530070000",
			"600195000",
			"098000060",
			"800060003",
			"400803001",
			"700020006",
			"060000280",
			"000419005",
			"000080079");

	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
			"3 7 0 0 0 0 0 8 0",
			"0 0 1 0 9 3 0 0 0",
			"0 4 0 7 8 0 0 0 3",
			"0 9 3 8 0 0 0 1 2",
			"0 0 0 0 4 0 0 0 0",
			"5 2 0 0 0 6 7 9 0",
			"6 0 0 0 2 1 0 4 0",
			"0 0 0 5 3 0 9 0 0",
			"0 3 0 0 0 0 0 5 1");


	String TEXT;
	private int[][] BOARD;
	public int[][] SOLUTION_GRID;

	private boolean solved;
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	private int NUMBER_OF_SOLUTIONS;


	public class Spot implements Comparable<Spot>{
		int x;
		int y;
		int val;
		private ArrayList<Integer> possibilities;

		private Spot(int i, int j){
			this.x = i;
			this.y = j;
			this.val = BOARD[i][j];
			this.possibilities = new ArrayList<>();
			findPossibilities();
		}

		private void set(int val){
			BOARD[this.x][this.y] = val;
		}

		public Boolean isValid(int k){
			for(int i = 0; i < BOARD.length; i++){
				if(BOARD[this.x][i] == k || BOARD[i][this.y] == k){
					return false;
				}
			}
			int row = this.x/3;
			int col = this.y/3;
			for(int i = 3*row; i < 3*row + 3; i++){
				for(int j = 3*col; j < 3*col + 3; j++){
					if(BOARD[i][j] == k) return false;
				}
			}

			return true;
		}

		void findPossibilities(){
			for(int i = 1; i <= 9; i++){
				if(isValid(i)){
					possibilities.add(i);
				}
			}
		}

		@Override
		public int compareTo(Spot o) {
			return Integer.compare(this.possibilities.size(), o.possibilities.size());
		}
	}





	// Provided various static utility methods to
	// convert data formats to int[][] grid.

	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}


	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}

		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}


	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}




	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		this.BOARD = new int[ints.length][ints.length];
		this.SOLUTION_GRID = new int[ints.length][ints.length];
		for(int i = 0; i < ints.length; i++){
			System.arraycopy(ints[i], 0, BOARD[i], 0 , ints.length);
			System.arraycopy(ints[i], 0, SOLUTION_GRID[i], 0, ints.length);
		}
		this.NUMBER_OF_SOLUTIONS = 0;
	}

	public Sudoku(String text){
		this.BOARD = textToGrid(text);
		this.SOLUTION_GRID = textToGrid(text);
		this.NUMBER_OF_SOLUTIONS = 0;
	}

	@Override
	public String toString(){
		return toStringHelper(BOARD);
	}

	private String toStringHelper(int[][] board){
		StringBuilder result = new StringBuilder();
		String ENTER = "\n";
		for (int[] ints : board) {
			for (int j = 0; j < board.length; j++) {
				result.append(ints[j]).append(" ");
			}
			result.append(ENTER);
		}
		return result.toString();
	}
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */

	private long ms;
	public int solve() {
		ms = System.currentTimeMillis();
		ArrayList<Spot> spots = new ArrayList<>();
		calculateInitialPossibilities(spots);
		Collections.sort(spots);
//		for (Spot spot : spots) {
//			System.out.println("x: " + spot.x + " y: " + spot.y + " size: " + spot.possibilities.size());
//		}
		helper(spots, 0);
		return NUMBER_OF_SOLUTIONS;

	}

	public void calculateNotPossibilities(ArrayList<Spot> spots){
		for(int i = 0; i < BOARD.length; i++){
			for(int j = 0; j < BOARD.length; j++){
				if(BOARD[i][j] != 0){
					Spot spot = new Spot(i, j);
					spots.add(spot);
				}
			}
		}
	}

	public void calculateInitialPossibilities(ArrayList<Spot> spots){
		for(int i = 0; i < BOARD.length; i++){
			for(int j = 0; j < BOARD.length; j++){
				if(BOARD[i][j] == 0){
					Spot spot = new Spot(i, j);
					spots.add(spot);
				}
			}
		}
	}

	private void helper(ArrayList<Spot> spots, int index){
		//	System.out.println(toStringHelper(BOARD));
		if(NUMBER_OF_SOLUTIONS >= MAX_SOLUTIONS) return;
		if(index == spots.size()){
			NUMBER_OF_SOLUTIONS++;
			if(NUMBER_OF_SOLUTIONS == 1){
				for(int i = 0; i < BOARD.length; i++) {
					System.arraycopy(BOARD[i], 0, SOLUTION_GRID[i], 0, BOARD.length);
				}
			}
			return;
		}
		Spot curr = spots.get(index);
		curr.possibilities.clear();
		curr.findPossibilities();
		for(int i = 0; i < curr.possibilities.size(); i++){
			curr.set(curr.possibilities.get(i));
			helper(spots, index + 1);
			curr.set(0);
		}
	}

	public String getSolutionText() {
		return toStringHelper(SOLUTION_GRID);
	}

	public long getElapsed() {
		return System.currentTimeMillis() - ms;
	}

}
