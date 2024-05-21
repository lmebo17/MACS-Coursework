import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
public class MetropolisTable extends AbstractTableModel {
    ArrayList<Metropolis> metros;
    MetropolisDatabase database;

    public  MetropolisTable(String name) throws SQLException, ClassNotFoundException {
        metros = new ArrayList<>();
        database = new MetropolisDatabase(name);
    }

    public void newMetropolisTable() throws SQLException {
        metros.clear();
        database.newDatabase();
    }

    @Override
    public int getRowCount() {
        return metros.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Metropolis metropolis = metros.get(rowIndex);
        System.out.println(metropolis.getCity());
        switch (columnIndex){
            case 0: return metropolis.getCity();
            case 1: return metropolis.getContinent();
            default: return metropolis.getPopulation();
        }
    }

    @Override
    public String getColumnName(int column){
        switch (column){
            case 0: return "METROPOLIS";
            case 1: return "CONTINENT";
            default: return "POPULATION";
        }
    }

    public void addMetropolis(String city, String continent, String population) throws SQLException {
        if(city.equals("") || continent.equals("") || population.equals("")) return;
        Metropolis newMetropolis = new Metropolis(city, continent, population);
        database.add(newMetropolis);
        metros.clear();
        metros.add(newMetropolis);
        fireTableDataChanged();
    }

    public void search(String city, String continent, String population, int populationArgs, int matchOptionArgs) throws SQLException {
        metros = database.search(city, continent, String.valueOf(population), populationArgs, matchOptionArgs);
        fireTableDataChanged();

    }
}
