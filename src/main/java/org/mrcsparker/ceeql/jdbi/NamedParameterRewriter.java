package org.mrcsparker.ceeql.jdbi;

import org.skife.jdbi.org.antlr.runtime.ANTLRStringStream;
import org.skife.jdbi.org.antlr.runtime.Token;
import org.skife.jdbi.rewriter.colon.ColonStatementLexer;
import org.skife.jdbi.v2.Binding;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.exceptions.UnableToCreateStatementException;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.RewrittenStatement;
import org.skife.jdbi.v2.tweak.StatementRewriter;

import com.github.jknack.handlebars.Context;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import static org.skife.jdbi.rewriter.colon.ColonStatementLexer.DOUBLE_QUOTED_TEXT;
import static org.skife.jdbi.rewriter.colon.ColonStatementLexer.ESCAPED_TEXT;
import static org.skife.jdbi.rewriter.colon.ColonStatementLexer.LITERAL;
import static org.skife.jdbi.rewriter.colon.ColonStatementLexer.NAMED_PARAM;
import static org.skife.jdbi.rewriter.colon.ColonStatementLexer.POSITIONAL_PARAM;
import static org.skife.jdbi.rewriter.colon.ColonStatementLexer.QUOTED_TEXT;

/**
 * Based on jdbi StatementRewriter to replace named parameters
 */
public class NamedParameterRewriter
{
	public static class NameList {
		private List<String> in =  new ArrayList<String>();
	    private List<String> out = new ArrayList<String>();

	    public NameList() {}
	    
	    public NameList(String[] names) {
	    	in.addAll(Arrays.asList(names));
	    }
	    
	    public String getName() {
	        String name = null;
	    	if (in != null && !in.isEmpty()) {
	    		name = in.remove(0);
	        } else {
	        	name = next();
	        	if (out != null) out.add(name);
	        }
	    	return name;
	    }

	    protected String next() {
	    	return "s"+UUID.randomUUID().toString().replace("-", "");
	    }
	    
	    public void rewind() {
	    	in.addAll(out);
	    }
	    
	    public void reset() {
	    	out.clear();
	    }
	    
	}
	
	public static ParsedStatement parseString(final String sql, Map<String, String> parameters, NameList names, Context itCtx) throws IllegalArgumentException
    {
        ParsedStatement stmt = new ParsedStatement();
        StringBuilder b = new StringBuilder(sql.length());
        ColonStatementLexer lexer = new ColonStatementLexer(new ANTLRStringStream(sql));
        Token t = lexer.nextToken();
        while (t.getType() != ColonStatementLexer.EOF) {
            switch (t.getType()) {
            case LITERAL:
                b.append(t.getText());
                break;
            case NAMED_PARAM:
            	String name = t.getText().substring(1, t.getText().length());
            	if (!parameters.containsKey(name)) {
    	        	Object value = itCtx.get(name); 
    	        	name = names.getName();
    	        	parameters.put(name, value.toString());    	        	
            	} 
            	b.append(":"+name);
                stmt.addNamedParamAt(name);
                //b.append("?");
                break;
            case QUOTED_TEXT:
                b.append(t.getText());
                break;
            case DOUBLE_QUOTED_TEXT:
                b.append(t.getText());
                break;
            case POSITIONAL_PARAM:
                b.append("?");
                stmt.addPositionalParamAt();
                break;
            case ESCAPED_TEXT:
                //b.append(t.getText().substring(1)); //TODO: test needed
                b.append(t.getText());
                break;
            default:
                break;
            }
            t = lexer.nextToken();
        }
        stmt.sql = b.toString();
        return stmt;
    }

    static public class ParsedStatement
    {
        private String sql;
        private boolean positionalOnly = true;
        public List<String> params = new ArrayList<String>();

        public void addNamedParamAt(String name)
        {
            positionalOnly = false;
            params.add(name);
        }

        public void addPositionalParamAt()
        {
            params.add("*");
        }

        public String getParsedSql()
        {
            return sql;
        }
    }
}