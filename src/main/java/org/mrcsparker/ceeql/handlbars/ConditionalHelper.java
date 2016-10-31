package org.mrcsparker.ceeql.handlbars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.TagType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public enum ConditionalHelper implements Helper<Object> {
	
	t("t", "if") {		
	    @Override
	    public CharSequence apply(final Object context, Options options) throws IOException {
	        
	    	CharSequence cs = null;
	    	boolean t = !options.isFalsy(context);
	    	if (options.tagType == TagType.SECTION) {
	    		cs = t? options.fn() :  options.inverse();
	    	} else if (t) {
	    		Object o = options.param(0, context.toString());
	        	cs = (o==null)? null : o.toString();
	    	} else {
	    		Object o = options.param(1, null); 
	        	cs = (o==null)? null : o.toString();
	    	}
	    		    	
	    	return cs;
	    }
	},

	f("f", "not", "unless") {		
	    @Override
	    public CharSequence apply(final Object context, Options options) throws IOException {
	        
	    	CharSequence cs = null;
	    	boolean f = options.isFalsy(context);
	    	if (options.tagType == TagType.SECTION) {
	    		cs = f? options.fn() :  options.inverse();
	    	} else if (f) {
	    		Object o = options.param(0, null);
	        	cs = (o==null)? null : o.toString();
	    	} else {
	    		Object o = options.param(1, context.toString()); 
	        	cs = (o==null)? null : o.toString();
	    	}
	    		    	
	    	return cs;
	    }
	},

	eq {
	    @Override
	    public CharSequence apply(final Object context, Options options) throws IOException {
	
	    	boolean t = false;

	    	if (context != null)
	        	for(int i=0; options.param(i, null) != null && !t; i++) {
	        		if (context.equals(options.param(i))) {
	        			t = true;
	        		}
	        	}

        	CharSequence cs = null;
	    	if (options.tagType == TagType.SECTION) {
	    		cs = t? options.fn() :  options.inverse();	    	
	    	} else if (t) {
	    		cs = context.toString();
	    	}

	    	return cs;
	    }
	};

	ConditionalHelper(){}
	ConditionalHelper(String... names){this.names = names;}
	
	private String[] names;
	
	private String[] aliases() {
		return (names==null)? new String[]{name()} : names;
	}

    public ConditionalHelper registerHelper(final Handlebars handlebars) {
    	for (String name: aliases())
    		handlebars.registerHelper(name, this);
    	return this;
    }

    public static void register(final Handlebars handlebars) {
    	ConditionalHelper[] helpers = values();
        for (ConditionalHelper helper : helpers) {
          helper.registerHelper(handlebars);
        }
	}

}
