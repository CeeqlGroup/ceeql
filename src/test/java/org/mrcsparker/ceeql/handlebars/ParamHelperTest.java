package org.mrcsparker.ceeql.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.junit.Test;
import org.mrcsparker.ceeql.Ceeql;
import org.mrcsparker.ceeql.handlbars.ParamHelper;
import org.mrcsparker.ceeql.jdbi.NamedParameterRewriter.NameList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ParamHelperTest {
    @Test
    public void safeRegistersTest() throws IOException {
        Handlebars handlebars = new Handlebars();
        Map<String, String> parameters = new HashMap<String, String>();
        handlebars.registerHelper("s", new ParamHelper(parameters, new NameList()));
        Template template = handlebars.compileInline("{{s this}}");


        assertEquals(null, parameters.get(template.apply(null)));
    }

    @Test
    public void canInsertIntoTest() throws IOException {
        Handlebars handlebars = new Handlebars();
        Map<String, String> parameters = new HashMap<String, String>();
        handlebars.registerHelper("s", new ParamHelper(parameters, new NameList()));
        Template template = handlebars.compileInline("{{s .}}");

        String name = "name";

        assertEquals("name", parameters.get(template.apply(name).substring(1)));
    }

    @Test
    public void canHandleNumbers() throws IOException {
        Handlebars handlebars = new Handlebars();
        Map<String, String> parameters = new HashMap<String, String>();
        handlebars.registerHelper("s", new ParamHelper(parameters, new NameList()));
        Template template = handlebars.compileInline("{{s .}}");

        String name = "1";

        assertEquals("1", parameters.get(template.apply(name).substring(1)));
    }

    @Test
    public void handlesQuotesTest() throws IOException {
        Handlebars handlebars = new Handlebars();
        Map<String, String> parameters = new HashMap<String, String>();
        handlebars.registerHelper("s", new ParamHelper(parameters, new NameList()));
        Template template = handlebars.compileInline("{{s .}}");

        String name = "'name'";

        assertEquals("'name'", parameters.get(template.apply(name).substring(1)));
    }

    @Test
    public void handlesSQLInjectionTest() throws IOException {
        Handlebars handlebars = new Handlebars();
        Map<String, String> parameters = new HashMap<String, String>();
        handlebars.registerHelper("s", new ParamHelper(parameters, new NameList()));
        Template template = handlebars.compileInline("{{s .}}");

        String name = "dataset1; drop table dataset2";

        assertEquals("dataset1; drop table dataset2", parameters.get(template.apply(name).substring(1)));
    }

}
