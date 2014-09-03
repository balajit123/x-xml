package net.doepner.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Allows node navigation and getting and setting of xpath values
 */
public class Xml implements Tree {

    private final TransformerFactory tf = TransformerFactory.newInstance();
    private final XPath xp = XPathFactory.newInstance().newXPath();
    private final QNames qNames = new QNames();

    private final Node root;

    public Xml(String payload) {
        this(new InputSource(new StringReader(payload)));
    }

    public Xml(Path path) {
        this(new InputSource(path.toFile().toURI().toASCIIString()));
    }

    public Xml(InputSource inputSource) {
        this(parse(inputSource));
    }

    private Xml(Node root) {
        this.root = root;
    }

    private static Document parse(InputSource inputSource) {
        try {
            return DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(inputSource);
        } catch (ParserConfigurationException
                | SAXException | IOException e) {
            throw new XmlException(e);
        }
    }

    @Override
    public Iterable<Xml> nodes(String xpath) {
        final Collection<Xml> result = new LinkedList<>();
        for (Node node : new IterableNodes(eval(xpath, NodeList.class))) {
            result.add(new Xml(node));
        }
        return result;
    }

    @Override
    public Xml node(String xpath) {
        return new Xml(eval(xpath, Node.class));
    }

    @Override
    public String get(String xpath) {
        return eval(xpath, String.class);
    }

    @Override
    public void set(String xpath, String value) {
        eval(xpath, Node.class).setTextContent(value);
    }

    private final Map<String, XPathExpression> cache = new HashMap<>();

    private <T> T eval(String xpath, Class<T> resultType) {
        return resultType.cast(evaluate(resultType, getCompiled(xpath)));
    }

    private XPathExpression getCompiled(String xpath) {
        final XPathExpression cachedExpression = cache.get(xpath);
        if (cachedExpression != null) {
            return cachedExpression;
        } else {
            final XPathExpression expr = compile(xpath);
            cache.put(xpath, expr);
            return expr;
        }
    }

    private Object evaluate(Class<?> resultType, XPathExpression expr) {
        try {
            return expr.evaluate(root, qNames.get(resultType));
        } catch (XPathExpressionException e) {
            throw new XmlException(e);
        }
    }

    private XPathExpression compile(String xpath) {
        try {
            return xp.compile(xpath);
        } catch (XPathExpressionException e) {
            throw new XmlException(e);
        }
    }

    @Override
    public String toString() {
        try (final StringWriter writer = new StringWriter()) {
            tf.newTransformer().transform(new DOMSource(root),
                    new StreamResult(writer));
            return writer.getBuffer().toString();
        } catch (TransformerException | IOException e) {
            throw new XmlException(e);
        }
    }
}
