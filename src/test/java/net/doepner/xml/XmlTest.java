package net.doepner.xml;

import org.junit.Test;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.junit.Assert.assertEquals;

/**
 * Tests Xml class
 */
public class XmlTest {

    @Test
    public void testGetByXpath() throws Exception {
        check("text", "<ns:a><b>text</b></ns:a>", "/a/b");
        check("text", "<a><b>text</b></a>", "/a/b");
    }

    @Test
    public void testSetValueWithSpecialChars() throws Exception {
        final Xml xml = new Xml("<a></a>");
        final String valueWithSpecialChars = "<&>\"bread\" & \'butter\'";
        xml.set("/a", valueWithSpecialChars);
        assertEquals(xml.get("/a"), valueWithSpecialChars);

        final String s = xml.toString();

        final Xml xml2 = new Xml(s);

        assertXMLEqual(s, xml2.toString());
    }

    private void check(String text, String content, String xpathExpression) throws Exception {
        final String value = new Xml(content).get(xpathExpression);
        assertEquals(text, value);
    }
}
