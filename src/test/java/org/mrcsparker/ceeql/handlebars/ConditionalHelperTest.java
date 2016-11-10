package org.mrcsparker.ceeql.handlebars;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.EscapingStrategy;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.helper.StringHelpers;

import org.junit.Before;
import org.junit.Test;
import org.mrcsparker.ceeql.Ceeql;
import org.mrcsparker.ceeql.TestUtils;
import org.mrcsparker.ceeql.handlbars.ConditionalHelper;
import org.mrcsparker.ceeql.handlbars.ParameterHelper;
import org.mrcsparker.ceeql.handlbars.StringHelper;
import org.mrcsparker.ceeql.jdbi.NamedParameterRewriter.NameList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ConditionalHelperTest extends AbstractHelperTest {

    @Test
    public void ifTest() throws IOException {
    	
    	//
    	// Note: semantics - null vs 0
    	// If-helper uses a falsy definition which includes Number equal zero.
    	// Could cause confusion in some context where null check is intended.
    	// f.e. {{#if n}} n = {{n}} {{/if}} might require n = 0  
    	//

    	// alias
    	// {{t n}} 
    	assertEquals("1", 
        		apply("{{t n}}",
        			context()
        			.put("n", 1)
        			)
        	);

    	//
    	// Section
    	//
    	// {{#if n}}1{{/if}} 
    	assertEquals("1", 
    		apply("{{#if n}}1{{/if}}",
    			context()
    			.put("n", 1)
    			)
    	);
    		
    	// {{#if n}}1{{/if}} 
    	assertEquals("", 
    		apply("{{#if n}}1{{/if}}",
    			context()
    			)
    	);
    		
    	// {{#if n}}1{{else}}2{{/if}} 
    	assertEquals("2", 
    		apply("{{#if n}}1{{else}}2{{/if}}",
    			context()
    			)
    	);
    		
    	//
    	// Variable
    	//
    	// {{if n}} 
    	assertEquals("1", 
        		apply("{{if n}}",
        			context()
        			.put("n", 1)
        			)
        	);
    	// {{if n 1}} 
    	assertEquals("1", 
        		apply("{{if n 1}}",
        			context()
        			.put("n", 1)
        			)
        	);
    	// {{if n 1}} 
    	assertEquals("", 
        		apply("{{if n 1}}",
        			context()
        			)
        	);
    	// {{if n 1 2}} 
    	assertEquals("2", 
        		apply("{{if n 1 2}}",
        			context()
        			)
        	);

    	//
    	// Subexpression
    	//
    	// {{concat (if n 1)}} 
    	assertEquals("1", 
        		apply("{{concat (if n 1)}}",
        			context()
        			.put("n", 1)
        			)
        	);
    	    	    	
    	// {{concat (if n 1)}} 
    	assertEquals("", 
        		apply("{{concat (if n 1)}}",
        			context()
        			)
        	);
    	    	    	
    	// {{concat (if n 1 2)}} 
    	assertEquals("2", 
        		apply("{{concat (if n 1 2)}}",
        			context()
        			)
        	);
    	    	    	
    }

    @Test
    public void notTest() throws IOException {

    	// alias
    	// {{unless n}} 
    	assertEquals("1", 
        		apply("{{unless n}}",
        			context()
        			.put("n", 1)
        			)
        	);

    	// alias
    	// {{f n}} 
    	assertEquals("1", 
        		apply("{{f n}}",
        			context()
        			.put("n", 1)
        			)
        	);

    	//
    	// Section
    	//
    	// {{#not n}}1{{/not}} 
    	assertEquals("1", 
    		apply("{{#not n}}1{{/not}}",
    			context()
    			)
    	);
    		
    	// {{#not n}}1{{/not}} 
    	assertEquals("", 
    		apply("{{#not n}}1{{/not}}",
    			context()
    			.put("n", 1)
    			)
    	);
    		
    	// {{#not n}}1{{else}}2{{/if}} 
    	assertEquals("2", 
    		apply("{{#not n}}1{{else}}2{{/not}}",
    			context()
    			.put("n", 1)
    			)
    	);
    		
    	//
    	// Variable
    	//
    	// {{not n}} 
    	assertEquals("1", 
        		apply("{{not n}}",
        			context()
        			.put("n", 1)
        			)
        	);
    	// {{not n 1}} 
    	assertEquals("1", 
        		apply("{{not n 1}}",
        			context()
        			)
        	);
    	// {{not n 1}} 
    	assertEquals("2", 
        		apply("{{not n 1}}",
        			context()
        			.put("n", 2)
        			)
        	);
    	// {{not n 1 2}} 
    	assertEquals("2", 
        		apply("{{not n 1 2}}",
        			context()
        			.put("n", 1)
        			)
        	);

    	//
    	// Subexpression
    	//
    	// {{concat (not n 1)}} 
    	assertEquals("1", 
        		apply("{{concat (not n 1)}}",
        			context()
        			)
        	);
    	    	    	
    	// {{concat (not n 1)}} 
    	assertEquals("2", 
        		apply("{{concat (not n 1)}}",
        			context()
        			.put("n", 2)
        			)
        	);
    	    	    	
    	// {{concat (not n 1 2)}} 
    	assertEquals("2", 
        		apply("{{concat (not n 1 2)}}",
        			context()
        			.put("n", 3)
        			)
        	);
    	    	    	
    }

    @Test
    public void trivialTest() throws IOException {

    	//
    	// Note: trivial handling of {{f n}} and {{t n}} as singularity.
    	//
    	
    	// {{f n}} = {{t n}} = n
    	assertEquals(
    		apply("{{f n}}",
    			context()
    			.put("n", 1)
    			),
    		apply("{{t n}}",
    			context()
    			.put("n", 1)
    			)
        );
    	assertEquals(
    		apply("{{f n}}",
    			context()
    			),
    		apply("{{t n}}",
    			context()
    			)
        );

    }
    
    @Test
    public void symmetryTest() throws IOException {

    	// {{t n 1 2}} = {{f n 2 1}}
    	assertEquals(
    		apply("{{t n 1 2}}",
    			context()
    			.put("n", 1)
    			),
    		apply("{{f n 2 1}}",
    			context()
    			.put("n", 1)
    			)
        );
    	assertEquals(
    		apply("{{t n 1 2}}",
    			context()
    			),
    		apply("{{f n 2 1}}",
    			context()
    			)
            );

    }
    
    @Test
    public void eqTest() throws IOException {
    	//
    	// Section
    	//
    	// {{#eq n 1}}1{{/eq}} 
    	assertEquals("1", 
    		apply("{{#eq n 1}}1{{/eq}}",
    			context()
    			.put("n", 1)
    			)
    	);
    		
    	// {{#eq n 1}}1{{/eq}} 
    	assertEquals("", 
    		apply("{{#eq n 1}}1{{/eq}}",
    			context()
    			)
    	);
    		
    	// {{#eq n 1}}1{{else}}2{{/eq}} 
    	assertEquals("2", 
    		apply("{{#eq n 1}}1{{else}}2{{/eq}}",
    			context()
    			.put("n", 2)
    			)
    	);
    		
    	// {{#eq n 1}}1{{else}}2{{/eq}} 
    	assertEquals("2", 
    		apply("{{#eq n 1}}1{{else}}2{{/eq}}",
    			context()
    			)
    	);
    		
    	//
    	// Variable
    	//
    	// {{eq n 1}} 
    	assertEquals("1", 
        		apply("{{eq n 1}}",
        			context()
        			.put("n", 1)
        			)
        	);
    	// {{eq n 1}} 
    	assertEquals("", 
        		apply("{{eq n 1}}",
        			context()
        			)
        	);
    	// {{eq n 1 2}} 
    	assertEquals("2", 
        		apply("{{eq n 1 2}}",
        			context()
        			.put("n", 2)
        			)
        	);

    	//
    	// Subexpression
    	//
    	// {{concat (eq n 1)}} 
    	assertEquals("1", 
        		apply("{{concat (eq n 1)}}",
        			context()
        			.put("n", 1)
        			)
        	);
    	    	    	
    	// {{concat (eq n 1)}} 
    	assertEquals("", 
        		apply("{{concat (eq n 1)}}",
        			context()
        			)
        	);
    	    	    	
    	// {{concat (eq n 1 2)}} 
    	assertEquals("2", 
        		apply("{{concat (eq n 1 2)}}",
        			context()
        			.put("n", 2)
        			)
        	);
    	// {{concat (eq n 1 2)}} 
    	assertEquals("", 
        		apply("{{concat (eq n 1 2)}}",
        			context()
        			)
        	);
    	    	    	
    }

    @Test
    public void guardTest() throws IOException {

    	String s = 
        		"{{if (eq type \"varchar\" \"int\" \"decimal\") "+ 
    				"(concat type (if length (stringFormat \"(%d)\" length))) }}";
        	        	
    	assertEquals("int(11)", 
        		apply(s,
        			context()
        			.put("type", "int")
        			.put("length", 11)
        			)
        	);

    	assertEquals("int", 
        		apply(s,
        			context()
        			.put("type", "int")
        			)
        	);

    	assertEquals("int", 
        		apply(s,
        			context()
        			.put("type", "int")
        			.put("length", 0)
        			)
        	);

    	assertEquals("", 
        		apply(s,
        			context()
        			.put("type", "float")
        			)
        	);

    }
    
    @Test
    public void appliedTest() throws IOException {
    	// Section
    	// select * from products {{#if id}} where id = :id {{/if}} 
    	assertEquals("select * from products where id = :id", 
    		apply("select * from products{{#if id}} where id = :id{{/if}}",
    			context()
    			.put("id", 1)
    			)
    	);
    		
    	// Variable
    	// select * from products {{if id "where id = :id"}} 
    	assertEquals("select * from products where id = :id", 
        		apply("select * from products {{if id \"where id = :id\"}}",
        			context()
        			.put("id", 1)
        			)
        	);

    	// Subexpression
    	// {{concat "select * from products" (if id "where id = :id")}} 
    	assertEquals("select * from products where id = :id", 
        		apply("{{concat \"select * from products\" (if id \" where id = :id\")}}",
        			context()
        			.put("id", 1)
        			)
        	);
    	
    }
    
}
