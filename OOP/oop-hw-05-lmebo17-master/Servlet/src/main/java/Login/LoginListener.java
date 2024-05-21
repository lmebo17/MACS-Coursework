package Login;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class LoginListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        AccountManager manager = new AccountManager();
        servletContextEvent.getServletContext().setAttribute(AccountManager.NAME, manager);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
