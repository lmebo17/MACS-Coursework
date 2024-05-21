package Login;
import java.util.HashMap;

public class AccountManager {
    private HashMap<String,String> dataBase;
    public static String NAME = "MANAGER";
    public AccountManager(){
        dataBase = new HashMap<>();
        dataBase.put("Patrick", "1234");
        dataBase.put("Molly", "FloPup");
    }

    public Boolean existsAccount(String account){
        return dataBase.containsKey(account);
    }

    public Boolean isValidLogin(String account, String password){
        return existsAccount(account) && dataBase.get(account).equals(password);
    }

    public void createAccount(String account, String password){
        dataBase.put(account, password);
    }

}
