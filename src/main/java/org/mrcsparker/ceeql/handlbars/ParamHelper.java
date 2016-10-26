package org.mrcsparker.ceeql.handlbars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.TagType;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mrcsparker.ceeql.jdbi.NamedParameterRewriter.NameList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParamHelper implements Helper<Object> {

    private static final String SQL_IDENTIFIER = 
    		"(?:([`\"]?)[a-zA-Z_]\\w*\\1(?:\\.([`\"]?)[a-zA-Z_]\\w*\\2){0,2})(?:\\s*,\\s*([`\"]?)[a-zA-Z_]\\w*\\3(?:\\.([`\"]?)[a-zA-Z_]\\w*\\4){0,2})*";

    private final static Logger log = LogManager.getLogger(ParamHelper.class);
    private Map<String, String> parameters;
    private NameList names;
    private Pattern identifier = Pattern.compile(SQL_IDENTIFIER);
    
    public ParamHelper(Map<String, String> parameters, NameList names) {
    	this.parameters = parameters;
    	this.names = names;  	
    }
    
    private ParamHelper() {}
    
    @Override
    public CharSequence apply(final Object context, Options options) throws IOException {

    	String s = null;
    	
    	if (options.tagType == TagType.SECTION) {
    		CharSequence cs = options.apply(options.fn, context);
    		s = (cs==null)? null : cs.toString();
    	} else if (options.param(0, null) != null && options.param(0).toString().equals("in")) {
    		StringBuilder sb = new StringBuilder();;
	    	if (context instanceof Iterable) {
				for (Object o: (Iterable) context) {
					list(sb, o);
				}
			} else 
			if (context instanceof Iterator) {
				Iterator i = (Iterator)context;
				while (i.hasNext()) {
					list(sb, i.next());
				}
			} else
			if (context.getClass().isArray()) {
				for (Object o: (Object[]) context) {
					list(sb, o);
				}
			} else {   	
				list(sb, context.toString());
			}
    		return sb.toString();
    	} else {
    		if (context==null) {
    		 //  s = null	
    		} else 
	    	if (context instanceof Iterable) {
				s = StringUtils.join((Iterable) context, ",");
			} else 
			if (context instanceof Iterator) {
				s = StringUtils.join((Iterator) context, ",");
			} else
			if (context.getClass().isArray()) {
				s = StringUtils.join((Object[]) context, ",");
			} else {   	
				s = context.toString();
			}
    	}
    	if (options.param(0, null) != null) {
    		switch(options.param(0).toString()) {
    		case("identifier"): 
    			if (identifier.matcher(s).matches()) return s;
    			throw new IllegalArgumentException("Input not a valid identifier.");
    		}
    		return null;
    	} else {
	        String name = names.getName();       
	        parameters.put(name, s);        
	        return ":"+name;
    	}

    }

    private StringBuilder list(StringBuilder sb, Object o) {  	
    	if (sb.length() > 0) sb.append(",");
        String name = names.getName();       
        parameters.put(name, ((o!=null)?o.toString():null));        
        sb.append(":"+name);
    	return sb;
    }
}
