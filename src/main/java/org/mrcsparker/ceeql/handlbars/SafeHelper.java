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

public class SafeHelper implements Helper<Object> {

    private final static Logger log = LogManager.getLogger(SafeHelper.class);
    private Map<String, String> parameters;
    private NameList names;
    
    public SafeHelper(Map<String, String> parameters, NameList names) {
    	this.parameters = parameters;
    	this.names = names;
    }
    
    private SafeHelper() {}
    
    @Override
    public CharSequence apply(final Object context, Options options) throws IOException {

//    	//move down??
//        if (context == null) {
//            return "";
//        }
        
        String name = names.getName();
        
        parameters.put(name, (context==null)? null : context.toString());
        
        return ":"+name;

    }
    
//    private String escapeMySQL( String s ) {
//    	StringBuilder sb = new StringBuilder();
//    	for (int i=0; i < s.length(); i++) {
//    		char c = s.charAt(i);
//	    	switch(c) {
//    		case(0x00 ): sb.append("\\0"); break;
//    		case(0x08 ): sb.append("\\b"); break;
//    		case(0x09 ): sb.append("\\t"); break;
//    		case(0x0a ): sb.append("\\n"); break;
//    		case(0x0d ): sb.append("\\r"); break;
//    		case(0x1a ): sb.append("\\Z"); break;
//    		case(0x22 ): sb.append("\\\""); break;
//    		case('%' ): sb.append("\\%"); break;
//    		case('\'' ): sb.append("\\'"); break;
//    		case('\\' ): sb.append("\\\\"); break;
//    		case('_' ): sb.append("\\_"); break;
//    		case('-' ): sb.append("\\-"); break;
//    		case('#' ): sb.append("\\#"); break;
//    		case('*' ): sb.append("\\*"); break;
//    		case(';' ): sb.append("\\;"); break;
//    		case('|' ): sb.append("\\;"); break;
//    		default: sb.append(c);
//	    	}	
//    	}
//    	return sb.toString();
//    }
    
}
