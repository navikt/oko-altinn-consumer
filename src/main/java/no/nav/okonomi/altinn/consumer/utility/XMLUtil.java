package no.nav.okonomi.altinn.consumer.utility;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XMLUtil {

    public static Document getDocument(byte[] input, boolean namespaceAware) throws ParserConfigurationException, SAXException, IOException {
        return getDocument(input, namespaceAware, true);
    }

    public static Document getDocument(byte[] input, boolean namespaceAware, boolean validating) throws ParserConfigurationException, SAXException, IOException {
        InputStream stream = null;
        try {
            stream = new ByteArrayInputStream(input);
            return getDocument(stream, namespaceAware, validating);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    public static Document getDocument(InputStream input, boolean namespaceAware, boolean validating) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        // XML Signature needs to be namespace aware
        dbf.setNamespaceAware(namespaceAware);

        if (!validating) {
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            dbf.setValidating(false);
        }

        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(input);
    }
}
