Often I just want to get or set a few values from/on a given XML document. XPath is the standard for specifying locations in an XML document, with a Java XPath API since Java 5.

But the API is a little clunky, resulting in code like this:

    DocumentBuilder builder =
        DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = builder.parse(new File("/widgets.xml"));
 
    XPath xpath = XPathFactory.newInstance().newXPath();
    String expression = "/widgets/widget";
    Node widgetNode = (Node) xpath.evaluate(expression,
        document, XPathConstants.NODE);

Wouldn’t it be nice if it was as simple as this (all the code below is now also on my x-xml github repo):

    Xml xml = new Xml(Paths.get("/widgets.xml"));
    Xml node = xml.node("/widgets/widget");
