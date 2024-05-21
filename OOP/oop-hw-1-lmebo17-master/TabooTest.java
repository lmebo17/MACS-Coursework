// TabooTest.java
// Taboo class tests -- nothing provided.

import java.util.*;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class TabooTest {

    @Test
    public void nofollowTest1(){
        List<Character> rules = new ArrayList<>();
        rules.add('a');
        rules.add('c');
        rules.add('a');
        rules.add('b');
        Taboo<Character> taboo = new Taboo<>(rules);
        HashSet<Character> st = new HashSet<>();
        st.add('b');
        st.add('c');
        Assert.assertEquals(st, taboo.noFollow('a'));
        st.clear();
        Assert.assertEquals(st, taboo.noFollow('x'));
    }

    @Test
    public void nofollowTest2(){
        List<Character> rules = new ArrayList<>();
        rules.add('a');
        rules.add('a');
        rules.add('a');
        rules.add('b');
        Taboo<Character> taboo = new Taboo<>(rules);
        HashSet<Character> st = new HashSet<>();
        st.add('b');
        st.add('a');
        Assert.assertEquals(st, taboo.noFollow('a'));
        st.clear();
        Assert.assertEquals(st, taboo.noFollow('x'));
        Assert.assertEquals(st, taboo.noFollow('b'));
    }

    @Test
    public void nofollowTest3(){
        List<Character> rules = new ArrayList<>();
        rules.add('a');
        rules.add('b');
        rules.add('c');
        rules.add('d');
        rules.add('a');
        rules.add('a');
        rules.add('c');
        rules.add('b');
        rules.add('f');
        rules.add('d');
        rules.add('c');
        rules.add('a');
        Taboo<Character> taboo = new Taboo<>(rules);
        HashSet<Character> st = new HashSet<>();
        // noFollow('a')
        st.add('b');
        st.add('a');
        st.add('c');
        Assert.assertEquals(st, taboo.noFollow('a'));
        st.clear();
        // noFollow('b')
        st.add('c');
        st.add('f');
        Assert.assertEquals(st, taboo.noFollow('b'));
        st.clear();
        // noFollow('c')
        st.add('b');
        st.add('a');
        st.add('d');
        Assert.assertEquals(st, taboo.noFollow('c'));
        st.clear();
        // noFollow('d')
        st.add('a');
        st.add('c');
        Assert.assertEquals(st, taboo.noFollow('d'));
        st.clear();
        // noFollow('f')
        st.add('d');
        Assert.assertEquals(st, taboo.noFollow('f'));
        st.clear();
        // noFollow('e')
        Assert.assertEquals(st, taboo.noFollow('e'));

    }

    @Test
    public void reduceTest1(){
        List<Character> rules = new ArrayList<>();
        rules.add('a');
        rules.add('c');
        rules.add('a');
        rules.add('b');
        Taboo<Character> taboo = new Taboo<>(rules);
        List<Character> toReduce = new ArrayList<>();
        toReduce.add('a');
        toReduce.add('c');
        toReduce.add('b');
        toReduce.add('x');
        toReduce.add('c');
        toReduce.add('a');
        List<Character> reduced = new ArrayList<>();
        reduced.add('a');
        reduced.add('x');
        reduced.add('c');
        taboo.reduce(toReduce);
        Assert.assertEquals(reduced, toReduce);
    }

    @Test
    public void reduceTest2(){
        List<Character> rules = new ArrayList<>();
        rules.add('a');
        rules.add('b');
        rules.add('a');
        Taboo<Character> taboo = new Taboo<>(rules);
        List<Character> toReduce = new ArrayList<>();
        toReduce.add('a');
        toReduce.add('b');
        toReduce.add('c');
        toReduce.add('b');
        toReduce.add('a');
        List<Character> reduced = new ArrayList<>();
        reduced.add('a');
        reduced.add('c');
        reduced.add('b');
        taboo.reduce(toReduce);
        Assert.assertEquals(reduced, toReduce);
    }

    @Test
    public void reduceTest3(){
        List<Character> rules = new ArrayList<>();
        rules.add('a');
        rules.add('b');
        rules.add('a');
        Taboo<Character> taboo = new Taboo<>(rules);
        List<Character> toReduce = new ArrayList<>();
        toReduce.add('a');
        toReduce.add('b');
        toReduce.add('b');
        toReduce.add('a');
        List<Character> reduced = new ArrayList<>();
        reduced.add('a');
        reduced.add('a');
        taboo.reduce(toReduce);
        Assert.assertEquals(reduced, toReduce);
    }

    @Test
    public void reduceTest4(){
        List<Character> rules = new ArrayList<>();
        rules.add('a');
        rules.add(null);
        rules.add('b');
        rules.add('a');
        rules.add('a');
        Taboo<Character> taboo = new Taboo<>(rules);
        List<Character> toReduce = new ArrayList<>();
        toReduce.add('a');
        toReduce.add('b');
        toReduce.add('b');
        toReduce.add('a');
        List<Character> reduced = new ArrayList<>();
        taboo.reduce(toReduce);
        reduced.add('a');
        reduced.add('b');
        reduced.add('b');
        Assert.assertEquals(reduced, toReduce);
    }

    @Test
    public void reduceTest5(){
        List<Character> rules = new ArrayList<>();
        rules.add('a');
        rules.add('b');
        rules.add(null);
        rules.add('c');
        rules.add('d');
        Taboo<Character> taboo = new Taboo<>(rules);
        List<Character> toReduce = new ArrayList<>();
        toReduce.add('a');
        toReduce.add('a');
        toReduce.add('b');
        toReduce.add('d');
        toReduce.add('c');
        toReduce.add('d');
        toReduce.add('a');
        toReduce.add('b');
        toReduce.add('d');
        toReduce.add('b');
        toReduce.add('c');
        List<Character> reduced = new ArrayList<>();
        taboo.reduce(toReduce);
        reduced.add('a');
        reduced.add('a');
        reduced.add('d');
        reduced.add('c');
        reduced.add('a');
        reduced.add('d');
        reduced.add('b');
        reduced.add('c');
        Assert.assertEquals(reduced, toReduce);
    }

}
