import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;

public class MetropolisDatabase {

    public Connection connection;


    private String tablename = "metropolises";
    public MetropolisDatabase(String name) throws ClassNotFoundException, SQLException {
        tablename = name;
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306",
                "root", "Lmebo2021.");
    }

    public void newDatabase() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("USE mydatabase");
        statement.execute("DROP TABLE IF EXISTS " + tablename + ";");
        statement.execute("CREATE TABLE " + tablename + " (" +
                "metropolis CHAR(64)," +
                "continent CHAR(64)," +
                "population BIGINT" +
                ");");
        statement.close();
    }

    public void add(Metropolis newMetropolis) throws SQLException {
        StringBuilder state = new StringBuilder();
        state.append("INSERT INTO " + tablename + " VALUES "
                + "('" + newMetropolis.getCity() + "','"  + newMetropolis.getContinent() + "'," + newMetropolis.getPopulation() + ")");
        state.append(";");
        System.out.println(state);
        Statement statement = connection.createStatement();
        statement.execute("USE mydatabase");
        statement.executeUpdate(state.toString());
    }

    public ArrayList<Metropolis> search(String city, String continent, String population, int populationArgs, int matchOptionArgs) throws SQLException {
        ArrayList<Metropolis> metropolises = new ArrayList<>();
        String command = getCommand(city, continent, population, populationArgs, matchOptionArgs);
        Statement statement = connection.createStatement();
        System.out.println(command);
        statement.execute("USE mydatabase");
        ResultSet resultSet = statement.executeQuery(command);
        while (resultSet.next()){
            metropolises.add(new Metropolis(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)));
        }
        return metropolises;
    }

    private String getCommand(String city, String continent, String population, int populationArgs, int matchArgs) {
        StringBuilder result = new StringBuilder();
        result.append("SELECT* FROM " +  tablename + " e ");
        boolean nowAnd = false;
        if(city.equals("") && continent.equals("") && population.equals("")){
            result.append(";");
            return result.toString();
        };
        result.append("WHERE ");
        if(matchArgs == 1){
            if(!continent.isEmpty()) continent = "%" + continent + "%";
            if(!city.isEmpty()) city = "%" + city + "%";
        }

        if(!city.isEmpty()){
            if(!continent.isEmpty()) nowAnd = true;
            if(matchArgs == 0){
                result.append("Metropolis = \"" + city + "\"");
            } else result.append("Metropolis LIKE \"" + city + "\"");

        }
        if(!continent.isEmpty() && !city.isEmpty()) nowAnd = true;
        if(nowAnd) {
            result.append(" AND ");
            nowAnd = false;
        }
        if(!continent.isEmpty()) {
            if(matchArgs == 0){
                result.append("CONTINENT = '" + continent + "'");
            } else result.append("CONTINENT LIKE '" + continent + "'");

        }
        if(!population.isEmpty() && !continent.isEmpty()) nowAnd = true;
        if(!population.isEmpty() && continent.isEmpty() && !city.isEmpty()) nowAnd = true;
        if(nowAnd){
            result.append(" AND ");
        }
        if(!population.isEmpty()){
            result.append("Population ");
            Character comparison;
            if(populationArgs == 0){
                comparison = '>';
            } else comparison = '<';
            result.append(comparison);
            result.append(" " + population);
        }
        result.append(";");
        return result.toString();
    }

}
