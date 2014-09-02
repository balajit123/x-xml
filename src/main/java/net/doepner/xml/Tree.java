package net.doepner.xml;

/**
 * Conveniently extract and change text values
 * and navigate nodes on an XML document
 */
public interface Tree {

    /**
     * @param xpath An XPath expression
     * @return The nodes matching the XPath location
     * @throws XmlException Wrapping any underlying XML API exception
     */
    Iterable<? extends Tree> nodes(String xpath);

    /**
     * @param xpath An XPath expression
     * @return The single node matching the XPath location
     * @throws XmlException Wrapping any underlying XML API exception
     */
    Tree node(String xpath);

    /**
     * @param xpath An XPath expression
     * @return The text value from the unique XPath location
     * @throws XmlException Wrapping any underlying XML API exception
     */
    String get(String xpath);

    /**
     * @param xpath An XPath expression
     * @param value The text value to be set on the unique XPath location
     * @throws XmlException Wrapping any underlying XML API exception
     */
    void set(String xpath, String value);
}
