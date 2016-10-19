package org.mrcsparker.ceeql.handlbars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.TagType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mrcsparker.ceeql.jdbi.NamedParameterRewriter.NameList;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ParamHelper implements Helper<Object> {

    private final static Logger log = LogManager.getLogger(ParamHelper.class);
    private Map<String, String> parameters;
    private NameList names;
    
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
    	}
    	else 
    		s = (context==null)? null : context.toString();
    	
        String name = names.getName();       
        parameters.put(name, s);        
        return ":"+name;

    }
        
}
