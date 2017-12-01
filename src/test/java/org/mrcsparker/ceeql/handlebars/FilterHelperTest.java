package org.mrcsparker.ceeql.handlebars;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class FilterHelperTest extends AbstractHelperTest {

	@Test
	public void safeRegistersTest() throws IOException {

		assertEquals("",
				apply("{{safe this}}",
					null
					)
			);

	}

	@Test
	public void canInsertIntoTest() throws IOException {
		assertEquals("name",
				apply("{{safe .}}",
					"name"
					)
			);
	}

	@Test
	public void canHandleNumbers() throws IOException {
		assertEquals("1",
				apply("{{safe .}}",
					"1"
					)
			);
	}

	@Test
	public void handlesQuotesTest() throws IOException {
		assertEquals("'name'",
				apply("{{safe .}}",
					"'name'"
					)
			);
	}

	@Test
	public void handlesSQLInjectionTest() throws IOException {
		assertEquals("dataset1; drop table dataset2",
				apply("{{safe .}}",
					"dataset1; drop table dataset2"
					)
			);
	}

	@Test
	public void identifierTest() throws IOException {

		// Sql injection
		try {
			apply("{{identifier .}}",
				"dataset1; drop table dataset2"
			);
			fail();
		} catch(Exception e) {
			assertEquals("Input not a valid identifier.", e.getCause().getMessage());
		}

		assertEquals("one,two,three",
				apply("{{identifier .}}",
					new String[] {"one", "two", "three"}
					)
			);

		assertEquals("one,two,three",
				apply("{{identifier .}}",
					"one,two,three"
					)
			);

		assertEquals("a.b.c, a.b.d, a.b.e",
				apply("{{identifier .}}",
					"a.b.c, a.b.d, a.b.e"
					)
			);

		assertEquals("\"select\"",
				apply("{{identifier .}}",
					"\"select\""
					)
			);

		assertEquals("one",
				apply("{{#identifier}}{{this}}{{/identifier}}",
					"one"
					)
			);

		assertEquals("one",
				apply("{{concat (identifier .)}}",
					"one"
					)
			);

	}

	@Test
	public void numberTest() throws IOException {

		// Sql injection
		try {
			apply("{{number .}}",
				"dataset1; drop table dataset2"
			);
			fail();
		} catch(Exception e) {
			assertEquals("Input not a valid number.", e.getCause().getMessage());
		}

		// Signed number
		try {
			apply("{{number .}}",
				"-1"
			);
			fail();
		} catch(Exception e) {
			assertEquals("Input not a valid number.", e.getCause().getMessage());
		}

		// Decimal number
		try {
			apply("{{number .}}",
				"1.0"
			);
			fail();
		} catch(Exception e) {
			assertEquals("Input not a valid number.", e.getCause().getMessage());
		}

		assertEquals("1,2,3",
				apply("{{number .}}",
					new Integer[] {1, 2, 3}
					)
			);

		assertEquals("1,2,3",
				apply("{{number .}}",
					"1,2,3"
					)
			);

		assertEquals("1,2,3",
				apply("{{#number}}{{this}}{{/number}}",
					"1,2,3"
					)
			);

		assertEquals("1",
				apply("{{concat (number .)}}",
					"1"
					)
			);

		assertEquals("773",
				apply("{{number .}}",
					"773"
					)
			);
	}

	@Test
	public void decimalTest() throws IOException {

		// Sql injection
		try {
			apply("{{decimal .}}",
				"dataset1; drop table dataset2"
			);
			fail();
		} catch(Exception e) {
			assertEquals("Input not a valid number.", e.getCause().getMessage());
		}

		assertEquals("1",
				apply("{{concat (decimal .)}}",
					"1"
					)
			);

		assertEquals("-10",
				apply("{{decimal .}}",
					"-10"
					)
			);

		assertEquals("773",
				apply("{{decimal .}}",
					"773"
					)
			);

		assertEquals(".76",
				apply("{{decimal .}}",
					".76"
					)
			);

		assertEquals("0.76",
				apply("{{decimal .}}",
					"0.76"
					)
			);

		assertEquals("-760.0",
				apply("{{decimal .}}",
					"-760.0"
					)
			);

		assertEquals("773.44",
				apply("{{decimal .}}",
					"773.44"
					)
			);
	}

	@Test
	public void sqlTest() throws IOException {

				System.out.println(apply("{{sql .}}",
					"10; select pg_sleep(10); --"
					));

		assertEquals("10 select pg_sleep(10)",
				apply("{{sql .}}",
					"10; select pg_sleep(10); --"
					)
			);
	}

}
