package org.mrcsparker.ceeql.handlbars;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mrcsparker.ceeql.jdbi.NamedParameterRewriter.NameList;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.TagType;

public class ParameterHelper implements Helper<Object> {

    private final static Logger log = LogManager.getLogger(ParameterHelper.class);
    private Map<String, String> parameters;
    private NameList names;
    
    public ParameterHelper(Map<String, String> parameters, NameList names) {
    	this.parameters = parameters;
    	this.names = names;  	
    }
    
    @Override
    public CharSequence apply(final Object context, Options options) throws IOException {

    	String s = null;
    	
    	if (options.tagType == TagType.SECTION) {
    		CharSequence cs = options.fn();
    		s = param(names, (cs==null)? null : cs.toString());
    	} else {
    		StringBuilder sb = new StringBuilder();
	    	if (context instanceof Iterable) {
				for (Object o: (Iterable) context) {
					list(sb, names, o);
				}
			} else 
			if (context instanceof Iterator) {
				Iterator i = (Iterator)context;
				while (i.hasNext()) {
					list(sb, names, i.next());
				}
			} else
			if (context != null && context.getClass().isArray()) {
				for (Object o: (Object[]) context) {
					list(sb, names, o);
				}
			} else {   	
				list(sb, names, context);
			}
    		s = sb.toString();
    	}
	    return s;

    }

    private StringBuilder list(StringBuilder sb, NameList names, Object o) {  	
    	if (sb.length() > 0) sb.append(",");
    	sb.append(param(names, o));
    	return sb;
    }
    
    private String param(NameList names, Object o) {  	
        String name = names.getName();       
        parameters.put(name, ((o!=null)?o.toString():null));        
        return ":"+name;
    }

	private String[] aliases() {
		return new String[] {"parameter","s"};
	}

    public ParameterHelper registerHelper(final Handlebars handlebars) {
    	for (String name: aliases())
    		handlebars.registerHelper(name, this);
    	return this;
    }

}
