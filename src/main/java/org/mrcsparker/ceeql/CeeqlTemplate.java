package org.mrcsparker.ceeql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import org.mrcsparker.ceeql.handlbars.EachHelper;
import org.mrcsparker.ceeql.handlbars.ParamHelper;
import org.mrcsparker.ceeql.handlbars.SafeHelper;
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
    		
    		ObjectReader mapper = new ObjectMapper().readerFor(Object.class);

            HashMap<String, Object> parsedArgs = new HashMap<>();
            for (Map.Entry<String, String> a : args.entrySet()) {
                try {
                    parsedArgs.put(a.getKey(), mapper.readValue(a.getValue()));
                } catch (Exception e) {
                    parsedArgs.put(a.getKey(), a.getValue());
                }
            }

            Handlebars handlebars = new Handlebars().with((final CharSequence value) -> value.toString());
            NameList names = new NameList();
            parameters = new HashMap<String, String>();
            handlebars.registerHelper("safe", new SafeHelper());
            handlebars.registerHelper("s", new ParamHelper(parameters, names));
            eh = new EachHelper(parameters, names, isBatch, dbiHandle);
            handlebars.registerHelper("each", eh);
            template = handlebars.compileInline(s);
            sql = template.apply(parsedArgs);
            
            return this;     		
    	}
    }
}
