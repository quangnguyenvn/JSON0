package pineteam.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JSON0RegressionTest {

	@Test
	public void parse_handlesColonInKey_andBracketInQuotedString() {
		JSON0 json = new JSON0();
		boolean ok = json.parse("{\"a:b\":\"value\",\"nested\":{\"k\":\"}\"}}");

		assertTrue(ok);
		assertEquals("value", json.getString("a:b"));

		JSON0 nested = (JSON0) json.get("nested");
		assertEquals("}", nested.getString("k"));
	}

	@Test
	public void toString_skipsNullWithoutLeadingComma() {
		JSON0 json = new JSON0();
		json.put("skip", null);
		json.put("ok", "v");

		assertEquals("{\"ok\":\"v\"}", json.toString());
	}

	@Test
	public void parse_failure_rollsBackAllObservableState() {
		JSON0 json = new JSON0();
		json.put("k", "old");
		json.initMap();

		boolean ok = json.parse("{\"k\":\"new\",\"x\":[1,2}");

		assertFalse(ok);
		assertEquals(1, json.getLength());
		assertEquals("old", json.getString("k"));
		assertEquals("{\"k\":\"old\"}", json.toString());
	}
}
