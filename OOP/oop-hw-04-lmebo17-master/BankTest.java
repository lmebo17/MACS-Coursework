import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BankTest {

    private String file = "5k.txt";

    private boolean cmpFn(Account a, Account b) {
        if(a.id != b.id) return false;
        if(a.GetTransactions() != b.GetTransactions()) return false;
        if(a.GetBalance() != b.GetBalance()) return false;
        return true;
    }
    @Test
    public void testcase() throws InterruptedException {
        Bank bank1 = new Bank(1);
        bank1.processFile(file);
        Bank bank2 = new Bank(2);
        bank2.processFile(file);
        Bank bank3 = new Bank(4);
        bank3.processFile(file);
        Bank bank4 = new Bank(8);
        bank4.processFile(file);

        for(int i = 0; i < bank1.accounts.size(); i++){
            assertTrue(cmpFn(bank1.accounts.get(i), bank2.accounts.get(i)));
            assertTrue(cmpFn(bank2.accounts.get(i), bank3.accounts.get(i)));
            assertTrue(cmpFn(bank3.accounts.get(i), bank4.accounts.get(i)));
            assertTrue(cmpFn(bank1.accounts.get(i), bank4.accounts.get(i)));
        }
    }

    @Test
    public void testMain() throws InterruptedException {
        String[] args = {"small.txt", "4"};
        Bank.main(args);

        args = new String[0];
        Bank.main(args);
    }
}
