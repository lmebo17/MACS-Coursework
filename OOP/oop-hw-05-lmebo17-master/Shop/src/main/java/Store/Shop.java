package Store;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;

public class Shop {

    public static final String NAME = "SHOP";
    private Connection connection;

    private ArrayList<Product> allProducts;

    public Shop() throws SQLException {
        String tablename = "MYDATABASE";
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306",
                "root", "Lmebo2021.");

        allProducts = gatherAllProducts();
    }

    private ArrayList<Product> gatherAllProducts() throws SQLException {
        ArrayList<Product> list = new ArrayList<>();
        Statement statement = connection.createStatement();
        statement.execute("USE MYDATABASE;");
        ResultSet set = statement.executeQuery("SELECT * FROM PRODUCTS;");
        while (set.next()){
            Product product = new Product(set.getString(1), set.getString(2), set.getString(3), set.getDouble(4));
            list.add(product);
        }
        return list;
    }

    public ArrayList<Product> listAllProducts(){
        return allProducts;
    }

    public Product getProductWithID(String id){
        for(Product product : allProducts){
            if(product.getProductID().equals(id)){
                return product;
            }
        }
        return null;
    }


}
