import Store.Product;
import Store.Shop;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ShopTest {

    @Test
    public void testCase() throws SQLException {
        Shop shop = new Shop();

        ArrayList<Product> list = shop.listAllProducts();
        assertEquals(14, list.size());
        assertEquals("Classic Hoodie", shop.getProductWithID("HC").getProductName());
        assertNull(shop.getProductWithID("FC"));

        Product product = list.get(2);
        assertEquals("TC", product.getProductID());
        assertEquals("Classic Tee", product.getProductName());
        assertEquals("TShirt.jpg", product.getImage());
        assertTrue(13.95 == product.getPrice());


        Product product1 = new Product("TC", "FF", "SS", 1);
        assertTrue(product.equals(product1));
        assertTrue(product.hashCode() == product1.hashCode());
    }
}
