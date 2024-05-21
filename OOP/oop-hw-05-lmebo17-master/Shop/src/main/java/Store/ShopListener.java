package Store;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class ShopListener implements ServletContextListener{


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Shop shop;
        try {
            shop = new Shop();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        servletContextEvent.getServletContext().setAttribute(Shop.NAME, shop);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
