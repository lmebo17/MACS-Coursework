import Login.AccountManager;
import com.beust.ah.A;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountManagerTest{


    @Test
    public void hasAccount(){
        AccountManager manager = new AccountManager();
        assertTrue(manager.existsAccount("Molly"));
        assertTrue(manager.existsAccount("Patrick"));
        assertTrue(!manager.existsAccount("Luka"));
        assertTrue(!manager.existsAccount("Giorgi"));

        manager.createAccount("Luka", "1234");
        assertTrue(manager.existsAccount("Luka"));
    }

    @Test
    public void ValidAccount(){
        AccountManager manager = new AccountManager();
        assertTrue(manager.isValidLogin("Patrick", "1234"));
        assertTrue(!manager.isValidLogin("Luka", "4321"));
        manager.createAccount("Luka", "4321");
        assertTrue(manager.isValidLogin("Luka", "4321"));

    }


}
