// Board.java

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board {
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] backupGrid;
	private boolean[][] grid;
	int maxHeight;
	int backupHeight;
	private int[] backupRows;
	private int[] rows;
	private int[] backupColumns;
	private int[] columns;

	private boolean DEBUG = true;
	boolean committed;


	// Here a few trivial methods are provided:

	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	 */
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		this.maxHeight = 0;
		this.backupHeight = 0;
		grid = new boolean[width][height];
		backupGrid = new boolean[width][height];
		rows = new int[height];
		backupRows = new int[height];
		columns = new int[width];
		backupColumns = new int[width];
		committed = true;
	}


	/**
	 Returns the width of the board in blocks.
	 */
	public int getWidth() {
		return width;
	}


	/**
	 Returns the height of the board in blocks.
	 */
	public int getHeight() {
		return height;
	}


	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	 */
	public int getMaxHeight() {
		return maxHeight;
	}


	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	 */
	public void sanityCheck() {
		if (DEBUG) {
			int checkMaxHeight = 0;
			int[] checkRows = new int[height];
			int[] checkCols = new int[width];
			for(int i = 0; i < getWidth(); i++){
				for(int j = 0; j < getHeight(); j++){
					if(grid[i][j]){
						checkRows[j]++;
						checkCols[i] = Math.max(checkCols[i], j + 1);
						checkMaxHeight = Math.max(checkMaxHeight, checkCols[i]);
					}
				}

			}
			if(checkMaxHeight != maxHeight){
				throw new RuntimeException("MaxHeights does not match");
			}
			for(int i = 0; i < checkCols.length; i++){
				if(checkCols[i] != columns[i]){
					throw new RuntimeException("Heights does not match");
				}
			}
			for(int i = 0; i < checkRows.length; i++){
				if(checkRows[i] != rows[i]){
					throw new RuntimeException("Widths does not match");
				}
			}

		}

	}

	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.

	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	 */
	public int dropHeight(Piece piece, int x) {
		int res = 0;
		for(int i = 0; i < piece.getSkirt().length; i++){
			res = Math.max(res, getColumnHeight(x+i) - piece.getSkirt()[i]);
		}
		return res;
	}


	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	 */
	public int getColumnHeight(int x) {
		return columns[x];
	}

	public int getRowWidth(int y) {
		return rows[y];
	}

	public boolean getGrid(int x, int y) {
		if(OutOfBounds(x, y)) return true;
		return grid[x][y];
	}

	private boolean OutOfBounds(int x, int y){
		return Math.min(x, y) < 0 || x >= getWidth() || y >= getHeight();
	}


	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;

	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.

	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	 */
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
		commit();
		committed = false;

		int result = PLACE_OK;
		for(TPoint point : piece.getBody()){
			if(OutOfBounds(x + point.x, y + point.y)){
				result = PLACE_OUT_BOUNDS;
				break;
			}
			if(grid[x+point.x][y+point.y]){
				result = PLACE_BAD;
				break;
			}
			grid[x+point.x][y+point.y] = true;
			rows[y+point.y]++;
			columns[x+point.x] = Math.max(columns[x+point.x], y+point.y+1);
			if(rows[y+point.y] == getWidth()) result = PLACE_ROW_FILLED;
			maxHeight = Math.max(maxHeight, columns[x+point.x]);
		}
		sanityCheck();
		return result;
	}

	public int clearRows() {
		if (committed) commit();
		committed = false;
		int rowsCleared = 0;
		int from = 0;
		for(int to = 0; to < getHeight(); to++){
			while(from < getHeight() && getRowWidth(from) == getWidth()){
				from++;
				rowsCleared++;
			}
			if(from >= getHeight()){
				rows[to] = 0;
				for(int i = 0; i < grid.length; i++){
					grid[i][to] = false;
				}
			} else {
				rows[to] = rows[from];
				for(int i = 0; i < grid.length; i++){
					grid[i][to] = grid[i][from];
				}
			}
			from++;
		}

		maxHeight = 0;
		for(int i = 0; i < getWidth(); i++){
			columns[i] = 0;
			for(int j = 0; j < getHeight(); j++){
				if(grid[i][j]){
					columns[i] = Math.max(columns[i], j + 1);
					maxHeight = Math.max(maxHeight, j + 1);
				}
			}
		}
		sanityCheck();
		return rowsCleared;
	}

	public void undo() {
		if(committed) return;
		committed = true;
		int[] tempRows = rows;
		rows = backupRows;
		backupRows = tempRows;
		int[] tempCols = columns;
		columns = backupColumns;
		backupColumns = tempCols;
		int tempHeight = maxHeight;
		maxHeight = backupHeight;
		backupHeight = tempHeight;
		boolean[][] tempGrid = grid;
		grid = backupGrid;
		backupGrid = tempGrid;
		sanityCheck();
	}

	public void commit() {
		committed = true;
		System.arraycopy(columns, 0, backupColumns, 0, columns.length);
		System.arraycopy(rows, 0, backupRows, 0, rows.length);
		for(int i = 0; i < grid.length; i++){
			System.arraycopy(grid[i], 0, backupGrid[i], 0, grid[i].length);
		}
		backupHeight = maxHeight;
	}

	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


