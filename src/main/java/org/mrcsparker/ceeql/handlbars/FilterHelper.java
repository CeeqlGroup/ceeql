package org.mrcsparker.ceeql.handlbars;

import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.TagType;

public enum FilterHelper implements Helper<Object> {
	
	safe {		
	    private static final String SQL_REGEX = "('.+--)|(--)|(\\|)|(%7C)";

	    @Override
	    public CharSequence apply(final Object context, Options options) throws IOException {

	        if (context == null) {
	            return "";
	        }

	        if (context instanceof Number) {
	            return context.toString();
	        } else {
	            return context.toString().replace(SQL_REGEX, "");
	        }
	    }
	},

	identifier {		
	    private static final String SQL_IDENTIFIER = 
	    		"(?:([`\"]?)[a-zA-Z_]\\w*\\1(?:\\.([`\"]?)[a-zA-Z_]\\w*\\2){0,2})(?:\\s*,\\s*([`\"]?)[a-zA-Z_]\\w*\\3(?:\\.([`\"]?)[a-zA-Z_]\\w*\\4){0,2})*";
	    private final Pattern identifierPattern = Pattern.compile(SQL_IDENTIFIER);

	    @Override
	    public CharSequence apply(final Object context, Options options) throws IOException {
	        
	    	String s = null;	    	
	    	if (options.tagType == TagType.SECTION) {
	    		CharSequence cs = options.fn();
	    		s = (cs==null)? null : cs.toString();
	    	} else {
	    		s = join(context);
	    	}
    		if (s != null && !identifierPattern.matcher(s).matches()) {
    			throw new IllegalArgumentException("Input not a valid identifier.");
    		}
    		
    		return s;
	    }
	},

	number {
	    private static final String SQL_LITERAL = "\\d+(\\s*,\\s*\\d+)*";
	    private final Pattern literalPattern = Pattern.compile(SQL_LITERAL);
	   
	    @Override
	    public CharSequence apply(final Object context, Options options) throws IOException {
	        
	    	String s = null;	    	
	    	if (options.tagType == TagType.SECTION) {
	    		CharSequence cs = options.fn();
	    		s = (cs==null)? null : cs.toString();
	    	} else {
	    		s = join(context);
	    	}
    		if (s != null && !literalPattern.matcher(s).matches()) {
    			throw new IllegalArgumentException("Input not a valid number.");
    		}
    		
    		return s;
	    }
	};

	FilterHelper(){}
	FilterHelper(String... names){this.names = names;}
	
	private String[] names;
	
	private String[] aliases() {
		return (names==null)? new String[]{name()} : names;
	}

    public FilterHelper registerHelper(final Handlebars handlebars) {
    	for (String name: aliases())
    		handlebars.registerHelper(name, this);
    	return this;
    }

    public static void register(final Handlebars handlebars) {
    	FilterHelper[] helpers = values();
        for (FilterHelper helper : helpers) {
          helper.registerHelper(handlebars);
        }
	}

    //TODO: dry
	private static String join(Object context) {
		String s = null;
		if (context==null) {
   		 //  s = null	
   		} else if (context instanceof Iterable) {
   			s = StringUtils.join((Iterable) context, ",");
		} else if (context instanceof Iterator) {
			s = StringUtils.join((Iterator) context, ",");
		} else if (context.getClass().isArray()) {
			s = StringUtils.join((Object[]) context, ",");
		} else {   	
			s = context.toString();
		}
		return s;
	}

}
