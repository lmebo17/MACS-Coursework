import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;


public class MetropolisTests extends TestCase {
    private MetropolisTable table;

    @BeforeEach
    public void setUp() throws Exception {
        table = new MetropolisTable("testtable");
        table.newMetropolisTable();

    }


    @Test
    public void testAll() throws SQLException {
        table.addMetropolis("Tbilisi", "Europe", "1500000");
        table.addMetropolis("Tokyo", "Asia", "10000000");
        table.addMetropolis("New York", "America", "9000000");
        table.addMetropolis("Batumi", "Europe", "200000");
        table.search("","","",0,0);
        Assert.assertEquals(4, table.getRowCount());
    }

    @Test
    public void testAdd() throws SQLException {
        table.addMetropolis("Tbilisi", "Europe", "1500000");
        table.search("","","",0,0);
        Assert.assertEquals(1, table.getRowCount());
        table.search("", "", "1499999", 0, 0);
        Assert.assertEquals(1, table.getRowCount());
        table.search("", "", "1499999", 1, 0);
        Assert.assertEquals(0, table.getRowCount());
        table.search("", "", "1500001", 0, 0);
        Assert.assertEquals(0, table.getRowCount());
        table.search("", "", "1500001", 1, 0);
        Assert.assertEquals(1, table.getRowCount());
    }

    @Test
    public void testComplicatedSearch() throws SQLException {
        table.addMetropolis("Tbilisi", "Europe", "1500000");
        table.addMetropolis("Tokyo", "Asia", "10000000");
        table.addMetropolis("New York", "America", "9000000");
        table.addMetropolis("Batumi", "Europe", "200000");

        table.search("","", "2000000", 0, 0);
        Assert.assertEquals(2, table.getRowCount());
        table.search("","", "2000000", 1, 0);
        Assert.assertEquals(2, table.getRowCount());
        table.search("","", "200001", 1, 0);
        Assert.assertEquals(1, table.getRowCount());

        table.search("", "op", "", 0, 1);
        Assert.assertEquals(2, table.getRowCount());

        table.search("", "op", "", 0, 1);
        Assert.assertEquals(2, table.getRowCount());

        table.search("", "op", "200000", 0, 1);
        Assert.assertEquals(1, table.getRowCount());

        table.search("", "op", "2000000", 1, 1);
        Assert.assertEquals(2, table.getRowCount());

        table.search("Y", "A", "", 0, 1);
        Assert.assertEquals(2, table.getRowCount());

        table.search("Tbilisi", "", "", 0 ,0);
        Assert.assertEquals(1, table.getRowCount());

        table.search("", "EUROPE", "", 0, 0);
        Assert.assertEquals(2, table.getRowCount());
    }


    @Test
    public void testColumns() throws SQLException {
        table.addMetropolis("Tbilisi", "Europe", "1500000");
        table.addMetropolis("Tokyo", "Asia", "10000000");
        table.addMetropolis("New York", "America", "9000000");
        table.addMetropolis("Batumi", "Europe", "200000");
        assertEquals(1, table.getRowCount());
        table.search("", "", "",0 ,0 );
        assertEquals(3, table.getColumnCount());
        assertEquals("1500000" , table.getValueAt(0, 2));
        assertEquals("10000000" , table.getValueAt(1, 2));
        assertEquals("9000000" , table.getValueAt(2, 2));
        assertEquals("200000" , table.getValueAt(3, 2));
        assertEquals("Tbilisi" , table.getValueAt(0, 0));
        assertEquals("Europe" , table.getValueAt(0, 1));
    }

    @Test
    public void testColumnNames(){
        assertEquals("METROPOLIS", table.getColumnName(0));
        assertEquals("CONTINENT", table.getColumnName(1));
        assertEquals("POPULATION", table.getColumnName(2));
    }




}
