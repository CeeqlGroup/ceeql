package org.mrcsparker.ceeql.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.junit.Test;
import org.mrcsparker.ceeql.Ceeql;
import org.mrcsparker.ceeql.handlbars.ParameterHelper;
import org.mrcsparker.ceeql.jdbi.NamedParameterRewriter.NameList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ParameterHelperTest extends AbstractHelperTest {
    @Test
    public void nullTest() throws IOException {
    	assertEquals(":p0", 
        		apply("{{s this}}",
        			null
        			)
        	);
    	assertTrue(parameters.containsKey("p0"));
    	assertNull(parameters.get("p0"));
    }

    @Test
    public void blockTest() throws IOException {
    	assertEquals(":p0", 
        		apply("{{#s}}{{this}}{{/s}}",
        			"one"
        			)
        	);
    	assertEquals("one", parameters.get("p0"));
    }

    @Test
    public void subexpressionTest() throws IOException {
    	assertEquals(":p0", 
        		apply("{{concat (s this)}}",
        			"one"
        			)
        	);
    	assertEquals("one", parameters.get("p0"));
    }

    @Test
    public void handlesSQLInjectionTest() throws IOException {
    	assertEquals(":p0", 
        		apply("{{s this}}",
        			"dataset1; drop table dataset2 --"
        			)
        	);
    	assertEquals("dataset1; drop table dataset2 --", parameters.get("p0"));
    }

    @Test
    public void arrayTest() throws IOException {
    	assertEquals(":p0,:p1,:p2", 
        		apply("{{s this}}",
        			new String[] {"one", "two", "three"} 
        			)
        	);
    	assertEquals("one", parameters.get("p0"));
    	assertEquals("two", parameters.get("p1"));
    	assertEquals("three", parameters.get("p2"));
    }

}
