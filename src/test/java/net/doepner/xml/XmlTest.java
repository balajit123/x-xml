package net.doepner.xml;

import org.junit.Test;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.junit.Assert.assertEquals;

/**
 * Tests Xml class
 */
public class XmlTest {

    private static final String TEST_XML_FILE_1 = "/net/doepner/xml/test-1.xml";

    @Test
    public void testXmlFromPath() throws IOException {
        final Path tempFilePath = Files.createTempFile(getClass().getName() + "_", ".xml");
        Files.copy(read(TEST_XML_FILE_1), tempFilePath, REPLACE_EXISTING);

        final Xml xml = new Xml(tempFilePath);
        check("text", xml, "/a/b");
    }

    @Test
    public void testXmlFromClasspath() {
        final InputSource inputSource = new InputSource(read(TEST_XML_FILE_1));
        final Xml xml = new Xml(inputSource);
        check("text", xml, "/a/b");
    }

    private InputStream read(String location) {
        return getClass().getResourceAsStream(location);
    }

    @Test
    public void testGetByXpath() {
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

    private void check(String text, String content, String xpathExpression) {
        check(text, new Xml(content), xpathExpression);
    }

    private void check(String text, Xml xml, String xpathExpression) {
        assertEquals(text, xml.get(xpathExpression));
    }
}
