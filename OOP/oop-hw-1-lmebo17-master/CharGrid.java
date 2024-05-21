// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		int l_x = Integer.MAX_VALUE;
		int l_y = Integer.MAX_VALUE;
		int r_x = Integer.MIN_VALUE;
		int r_y = Integer.MIN_VALUE;
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[i].length; j++){
				if(grid[i][j] == ch){
					l_x = Math.min(l_x , i);
					l_y = Math.min(l_y , j);
					r_x = Math.max(r_x , i);
					r_y = Math.max(r_y , j);
				}
			}
		}
		return l_x != Integer.MAX_VALUE ? (r_x - l_x + 1)*(r_y - l_y + 1) : 0;
	}
	
	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */

	boolean inBounds(int i, int j){
		return (Math.min(i , j) >= 0 && i < grid.length && j < grid[i].length);
	}

	boolean isCross(int i, int j){
		char ch = grid[i][j];
		int up, down, left, right;
		down = left = right = up = 1;
		while(inBounds(i + right , j) && ch == grid[i+right][j]){
			right++;
		}
		while(inBounds(i - left , j) && ch == grid[i-left][j]){
			left++;
		}
		while(inBounds(i , j + up) && ch == grid[i][j + up]){
			up++;
		}
		while(inBounds(i , j - down) && ch == grid[i][j - down]){
			down++;
		}
		return right == left && right == up && right == down && right != 1;
	}

	public int countPlus() {
		int count = 0;
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[i].length; j++){
				if(isCross(i , j)){
					count++;
				}
			}
		}
		return count;
	}
	
}
