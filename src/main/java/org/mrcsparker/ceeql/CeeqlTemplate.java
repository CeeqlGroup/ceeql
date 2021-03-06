package org.mrcsparker.ceeql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.jknack.handlebars.EscapingStrategy;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.helper.StringHelpers;

import org.mrcsparker.ceeql.handlbars.ConditionalHelper;
import org.mrcsparker.ceeql.handlbars.EachHelper;
import org.mrcsparker.ceeql.handlbars.FilterHelper;
import org.mrcsparker.ceeql.handlbars.ParameterHelper;
import org.mrcsparker.ceeql.handlbars.StringHelper;
import org.mrcsparker.ceeql.jdbi.NamedParameterRewriter.NameList;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.PreparedBatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CeeqlTemplate {

    public static String apply(String s, Map<String, String> args) throws IOException {

    	Context ctx = new Context().apply(s, args, false, null);
        args.putAll(ctx.parameters);
        return ctx.sql;
    }
    
    //Testing support
    static String apply(String s, Map<String, String> args, Map<String, String> parameters, NameList names) throws IOException {

    	Context ctx = new Context().apply(s, args, false, null, parameters, names);
        args.putAll(ctx.parameters);
        return ctx.sql;
    }
    
    
    public static Batch apply(String s, Map<String, String> args, Handle dbiHandle) throws IOException {

    	Context ctx = new Context().apply(s, args, true, dbiHandle);
        args.putAll(ctx.parameters);
        return new Batch(ctx.eh);
    }

    static class Batch {
    	EachHelper eh; 
    	Batch(EachHelper eh) {this.eh = eh;}
    	void define (String key, Object value) {
    		if (eh.batch != null) eh.batch.define(key, value);
    		else if (eh.npbatch != null) eh.npbatch.define(key, value);
    	}
    	int[] execute () {
    		if (eh.batch != null) return eh.batch.execute();
    		else if (eh.npbatch != null) return eh.npbatch.execute();
    		else return null;
    	}
    }
    
    static class Context {
    	Map<String, String> parameters;
    	Template template;
    	EachHelper eh;
    	String sql;

    	Context apply(String s, Map<String, String> args, boolean isBatch, Handle dbiHandle) throws IOException {
    		return apply(s, args, isBatch, dbiHandle, new HashMap<String, String>(), new NameList());
    	}
    	
    	Context apply(String s, Map<String, String> args, boolean isBatch, Handle dbiHandle, Map<String, String> parameters, NameList names) throws IOException {
    		
    		ObjectReader mapper = new ObjectMapper().readerFor(Object.class);

            HashMap<String, Object> parsedArgs = new HashMap<>();
            for (Map.Entry<String, String> a : args.entrySet()) {
                try {
                    parsedArgs.put(a.getKey(), mapper.readValue(a.getValue()));
                } catch (Exception e) {
                    parsedArgs.put(a.getKey(), a.getValue());
                }
            }

            Handlebars handlebars = new Handlebars().with(EscapingStrategy.NOOP);
            this.parameters = parameters;
            StringHelper.register(handlebars);
            StringHelpers.register(handlebars);
            FilterHelper.register(handlebars);
            ConditionalHelper.register(handlebars);
            new ParameterHelper(parameters, names).registerHelper(handlebars);
            eh = new EachHelper(parameters, names, isBatch, dbiHandle).registerHelper(handlebars);
            template = handlebars.compileInline(s);
            sql = template.apply(parsedArgs);
            
            return this;     		
    	}
    }
}
