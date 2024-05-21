import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;


public class CrackerTest {
    @Test
    public void generateHash() throws NoSuchAlgorithmException {
        Cracker cracker = new Cracker(0, "", 1);
        assertEquals("34800e15707fae815d7c90d49de44aca97e2d759", cracker.hashFunction("a!"));
        assertEquals("adeb6f2a18fe33af368d91b09587b68e3abcb9a7", cracker.hashFunction("fm"));
        assertEquals("86f7e437faa5a7fce15d1ddcb9eaeaea377667b8", cracker.hashFunction("a"));
        assertEquals("eacd2617f105704f51c912099316c7aece2df8ef", cracker.hashFunction("giorgi"));
        assertEquals("d9ffb8d4295eab2f2c6090c9f3b8a34b438f32f1", cracker.hashFunction("freeuni"));
        assertEquals("914d61e816b0bcae6a411366eee1c7d0b91078f7", cracker.hashFunction("oop"));
    }

    @Test
    public void crackPassword1() throws Exception {
        String password = "oop";
        String hash = "914d61e816b0bcae6a411366eee1c7d0b91078f7";
        Cracker cracker = new Cracker(3, hash, 1);
        assertEquals(password, cracker.findPassword());
        cracker = new Cracker(3, hash, 2);
        assertEquals(password, cracker.findPassword());
        cracker = new Cracker(3, hash, 4);
        assertEquals(password, cracker.findPassword());
        cracker = new Cracker(3, hash, 8);
        assertEquals(password, cracker.findPassword());
        cracker = new Cracker(3, hash, 20);
        assertEquals(password, cracker.findPassword());
        cracker = new Cracker(3, hash, 40);
        assertEquals(password, cracker.findPassword());
    }

    @Test
    public void testMain() throws Exception {
        String[] args = {"oop", "3", "8"};
        Cracker.main(args);
        args = new String[]{"oop"};
        Cracker.main(args);
        args = new String[0];
        Cracker.main(args);
    }

    @Test
    public void testHex(){
        String str = "4f4f50";
        Cracker.hexToArray(str);

    }



}
