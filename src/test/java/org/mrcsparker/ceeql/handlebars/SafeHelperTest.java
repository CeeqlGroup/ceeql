package org.mrcsparker.ceeql.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.junit.Test;
import org.mrcsparker.ceeql.handlbars.SafeHelper;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SafeHelperTest {
    @Test
    public void safeRegistersTest() throws IOException {
        Handlebars handlebars = new Handlebars();
        handlebars.registerHelper("safe", new SafeHelper());
        Template template = handlebars.compileInline("{{safe this}}");


        assertEquals("", template.apply(null));
    }

    @Test
    public void canInsertIntoTest() throws IOException {
        Handlebars handlebars = new Handlebars();
        handlebars.registerHelper("safe", new SafeHelper());
        Template template = handlebars.compileInline("{{safe .}}");

        String name = "name";

        assertEquals("name", template.apply(name));
    }

    @Test
    public void canHandleNumbers() throws IOException {
        Handlebars handlebars = new Handlebars();
        handlebars.registerHelper("safe", new SafeHelper());
        Template template = handlebars.compileInline("{{safe .}}");

        String name = "1";

        assertEquals("1", template.apply(name));
    }

    @Test
    public void handlesQuotesTest() throws IOException {
        Handlebars handlebars = new Handlebars();
        handlebars.registerHelper("safe", new SafeHelper());
        Template template = handlebars.compileInline("{{safe .}}");

        String name = "'name'";

        assertEquals("&#x27;name&#x27;", template.apply(name));
    }

    @Test
    public void handlesSQLInjectionTest() throws IOException {
        Handlebars handlebars = new Handlebars();
        handlebars.registerHelper("safe", new SafeHelper());
        Template template = handlebars.compileInline("{{safe .}}");

        String name = "dataset1; drop table dataset2";

        assertEquals("name", template.apply(name));
    }
}
