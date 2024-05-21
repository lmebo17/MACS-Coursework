import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SudokuTest {

    @Test
    public void testCount(){
        Sudoku sudoku = new Sudoku(Sudoku.stringsToGrid("3 0 0 0 0 0 0 8 0",
                "0 0 1 0 9 3 0 0 0",
                "0 4 0 7 8 0 0 0 3",
                "0 9 3 8 0 0 0 1 2",
                "0 0 0 0 4 0 0 0 0",
                "5 2 0 0 0 6 7 9 0",
                "6 0 0 0 2 1 0 4 0",
                "0 0 0 5 3 0 9 0 0",
                "0 3 0 0 0 0 0 5 1"));
        assertEquals(6, sudoku.solve());
    }

    @Test
    public void noSolutionTest(){
        Sudoku sudoku = new Sudoku(Sudoku.stringsToGrid("3 0 4 0 7 0 6 0 0",
                "0 0 1 0 0 9 0 2 0",
                "0 9 0 0 5 8 0 0 7",
                "0 1 2 0 0 0 9 7 0",
                "7 0 0 0 0 0 0 0 8",
                "0 4 8 0 0 0 1 5 0",
                "9 0 0 2 4 0 0 8 0",
                "0 7 0 8 0 0 2 0 0",
                "0 0 5 0 1 0 4 0 9"));
        assertEquals(0, sudoku.solve());
    }

    @Test
    public void spotTest(){
        Sudoku sudoku = new Sudoku("0 6 1 0 0 4 0 3 0 4 0 3 0 6 0 0 0 2 0 0 2 0 1 0 0 6 4 1 4 0 0 0 0 0 0 6 0 0 0 2 0 6 0 0 0 2 0 0 0 0 0 0 7 5 0 2 0 0 9 0 8 0 0 7 0 0 0 5 0 6 2 0 0 5 0 1 0 0 9 4 0");
        ArrayList<Sudoku.Spot> spots = new ArrayList<>();
        ArrayList<Sudoku.Spot> notSpots = new ArrayList<>();
        sudoku.calculateInitialPossibilities(spots);
        sudoku.calculateNotPossibilities(notSpots);
        assertEquals(50, spots.size());
        assertEquals(81, spots.size() + notSpots.size());
        for(int i = 0; i < spots.size(); i++){
            Sudoku.Spot spot = spots.get(i);
            int row = spot.x;
            int col = spot.y;
            Assert.assertFalse(row == 0 && col == 1);
            Assert.assertFalse(row == 8 && col == 3);
            Assert.assertFalse(row == 1 && col == 0);
            if(row == 0 && col == 0){
                assertFalse(spot.isValid(6));
                assertFalse(spot.isValid(1));
                assertFalse(spot.isValid(4));
                assertFalse(spot.isValid(3));
                assertFalse(spot.isValid(2));
                assertFalse(spot.isValid(1));
                assertFalse(spot.isValid(7));
                assertTrue(spot.isValid(5));
                assertTrue(spot.isValid(8));
                assertTrue(spot.isValid(9));
            }
        }
    }

    @Test
    public void testStrings(){
        Sudoku sudoku = new Sudoku(Sudoku.stringsToGrid("7 0 6 0 0 0 0 5 3",
                "1 8 5 3 0 7 0 6 9",
                "2 9 0 0 0 5 0 4 1",
                "9 0 0 8 7 0 5 0 2",
                "8 0 0 5 0 4 0 3 7",
                "0 5 7 9 1 0 0 0 0",
                "0 0 0 1 0 6 0 9 0",
                "0 3 1 7 0 9 0 0 8",
                "5 0 9 0 3 0 0 0 0"));
        assertEquals(1, sudoku.solve());
        String solution = sudoku.getSolutionText();
        String actualSolution = "7 4 6 2 9 1 8 5 3 \n" +
                "1 8 5 3 4 7 2 6 9 \n" +
                "2 9 3 6 8 5 7 4 1 \n" +
                "9 6 4 8 7 3 5 1 2 \n" +
                "8 1 2 5 6 4 9 3 7 \n" +
                "3 5 7 9 1 2 6 8 4 \n" +
                "4 7 8 1 2 6 3 9 5 \n" +
                "6 3 1 7 5 9 4 2 8 \n" +
                "5 2 9 4 3 8 1 7 6 \n";
        assertTrue(actualSolution.equals(solution));
        assertTrue(sudoku.getElapsed() < 100);
    }


    @Test
    public void testMain(){
        Sudoku sudoku = new Sudoku(Sudoku.stringsToGrid("7 0 6 0 0 0 0 5 3",
                "1 8 5 3 0 7 0 6 9",
                "2 9 0 0 0 5 0 4 1",
                "9 0 0 8 7 0 5 0 2",
                "8 0 0 5 0 4 0 3 7",
                "0 5 7 9 1 0 0 0 0",
                "0 0 0 1 0 6 0 9 0",
                "0 3 1 7 0 9 0 0 8",
                "5 0 9 0 3 0 0 0 0"));
        String[] args = new String[1];
        sudoku.main(args);

    }

    @Test
    public void testValid(){
            try{
                Sudoku sudoku = new Sudoku("3 3 3");} catch(Exception e){};
    }

}
