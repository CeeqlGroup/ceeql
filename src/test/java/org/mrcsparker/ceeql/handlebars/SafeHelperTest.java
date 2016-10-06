package org.mrcsparker.ceeql.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.junit.Test;
import org.mrcsparker.ceeql.Ceeql;
import org.mrcsparker.ceeql.DbCreator;
import org.mrcsparker.ceeql.handlbars.SafeHelper;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SafeHelperTest {
    @Test
    public void safeRegistersTest() throws IOException {
        Handlebars handlebars = new Handlebars().with((final CharSequence value) -> value.toString());
        handlebars.registerHelper("safe", new SafeHelper());
        Template template = handlebars.compileInline("{{safe this}}");


        assertEquals("", template.apply(null));
    }

    @Test
    public void canInsertIntoTest() throws IOException {
        Handlebars handlebars = new Handlebars().with((final CharSequence value) -> value.toString());
        handlebars.registerHelper("safe", new SafeHelper());
        Template template = handlebars.compileInline("{{safe .}}");

        String name = "name";

        assertEquals("name", template.apply(name));
    }

    @Test
    public void canHandleNumbers() throws IOException {
        Handlebars handlebars = new Handlebars().with((final CharSequence value) -> value.toString());
        handlebars.registerHelper("safe", new SafeHelper());
        Template template = handlebars.compileInline("{{safe .}}");

        String name = "1";

        assertEquals("1", template.apply(name));
    }

    @Test
    public void handlesQuotesTest() throws IOException {
        Handlebars handlebars = new Handlebars().with((final CharSequence value) -> value.toString());
        handlebars.registerHelper("safe", new SafeHelper());
        Template template = handlebars.compileInline("select * from products where name = '{{safe .}}'");

        String name = "'foo'";

        assertEquals("select * from products where name = '''foo'''", template.apply(name));
    }
}
