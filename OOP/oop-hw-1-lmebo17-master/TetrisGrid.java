//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.

public class TetrisGrid {

	boolean[][] grid;

	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = grid;
	}
	

	boolean fullRow(int j){
		for(int i = 0; i < grid.length; i++){
			if(!grid[i][j]) return false;
		}
		return true;
	}

	/**
	 * Does row-clearing on the grid (see handout).
	 */


	public void clearRows() {
		for(int j = grid[0].length - 1; j >= 0; j--){
			if(fullRow(j)){
				int col = j;
				while(col < grid[0].length - 1){
					for(int i = 0; i < grid.length; i++){
						grid[i][col] = grid[i][col + 1];
					}
					col++;
				}
				for(int i = 0; i < grid.length; i++){
					grid[i][grid[0].length - 1] = false;
				}
			}
		}
	}
	
	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return grid;
	}
}
