package org.mrcsparker.ceeql.handlebars;

import com.github.jknack.handlebars.EscapingStrategy;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.helper.StringHelpers;

import org.junit.Test;
import org.mrcsparker.ceeql.Ceeql;
import org.mrcsparker.ceeql.TestUtils;
import org.mrcsparker.ceeql.handlbars.ConditionalHelper;
import org.mrcsparker.ceeql.handlbars.FilterHelper;
import org.mrcsparker.ceeql.handlbars.ParameterHelper;
import org.mrcsparker.ceeql.handlbars.StringHelper;
import org.mrcsparker.ceeql.jdbi.NamedParameterRewriter.NameList;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class AbstractHelperTest {

	protected TestUtils.MapBuilder<String, Object> context() {
		return new TestUtils.MapBuilder<String, Object>();
	}

	protected Map<String, String> parameters;
	protected NameList names;
	protected String apply(String s, Object context) throws IOException {
        Handlebars handlebars = new Handlebars().with(EscapingStrategy.NOOP);
        StringHelper.register(handlebars);
        StringHelpers.register(handlebars);
        FilterHelper.register(handlebars);
        ConditionalHelper.register(handlebars);
        parameters = new LinkedHashMap<String, String>();
        names = new NameList() {
        	int i = 0;
    	    protected String next() {
    	    	return "p"+(i++);
    	    }       	
        };
        new ParameterHelper(parameters, names).registerHelper(handlebars);
    	return handlebars.compileInline(s).apply(
    		(context!=null && context instanceof TestUtils.MapBuilder)?
				((TestUtils.MapBuilder)context).build() : context);
	}

}
