package org.mrcsparker.ceeql.handlebars;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class FilterHelperTest extends AbstractHelperTest {

	@Test
    public void safeRegistersTest() throws IOException {

    	assertEquals("", 
        		apply("{{safe this}}",
        			null
        			)
        	);

	}

    @Test
    public void canInsertIntoTest() throws IOException {
        assertEquals("name", 
        		apply("{{safe .}}",
        			"name"
        			)
        	);
    }

    @Test
    public void canHandleNumbers() throws IOException {
        assertEquals("1", 
        		apply("{{safe .}}",
        			"1"
        			)
        	);
    }

    @Test
    public void handlesQuotesTest() throws IOException {
        assertEquals("'name'", 
        		apply("{{safe .}}",
        			"'name'"
        			)
        	);
    }

    @Test
    public void handlesSQLInjectionTest() throws IOException {
        assertEquals("dataset1; drop table dataset2", 
        		apply("{{safe .}}",
        			"dataset1; drop table dataset2"
        			)
        	);
    }

    @Test
    public void identifierTest() throws IOException {
    	
    	// Sql injection
    	try {
    		apply("{{identifier .}}",
    			"dataset1; drop table dataset2"
        	);
        	fail();
    	} catch(Exception e) {
    		assertEquals("Input not a valid identifier.", e.getCause().getMessage());
    	}

        assertEquals("one,two,three", 
        		apply("{{identifier .}}",
        			new String[] {"one", "two", "three"}
        			)
        	);

        assertEquals("one,two,three", 
        		apply("{{identifier .}}",
        			"one,two,three"
        			)
        	);

        assertEquals("a.b.c, a.b.d, a.b.e", 
        		apply("{{identifier .}}",
        			"a.b.c, a.b.d, a.b.e"
        			)
        	);

        assertEquals("\"select\"", 
        		apply("{{identifier .}}",
        			"\"select\""
        			)
        	);

        assertEquals("one", 
        		apply("{{#identifier}}{{this}}{{/identifier}}",
        			"one"
        			)
        	);

        assertEquals("one", 
        		apply("{{concat (identifier .)}}",
        			"one"
        			)
        	);

    }

    @Test
    public void numberTest() throws IOException {
    	
    	// Sql injection
    	try {
    		apply("{{number .}}",
    			"dataset1; drop table dataset2"
        	);
        	fail();
    	} catch(Exception e) {
    		assertEquals("Input not a valid number.", e.getCause().getMessage());
    	}

        assertEquals("1,2,3", 
        		apply("{{number .}}",
        			new Integer[] {1, 2, 3}
        			)
        	);

        assertEquals("1,2,3", 
        		apply("{{number .}}",
        			"1,2,3"
        			)
        	);

        assertEquals("1,2,3", 
        		apply("{{#number}}{{this}}{{/number}}",
        			"1,2,3"
        			)
        	);

        assertEquals("1", 
        		apply("{{concat (number .)}}",
        			"1"
        			)
        	);
        
    }

}
