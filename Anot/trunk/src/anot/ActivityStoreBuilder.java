/*
 */
package anot;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.validation.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.Source;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.w3c.dom.*;

import java.util.jar.*;
import java.util.zip.*;
import java.util.*;

/**
 * 
 * @author Adam Renberg <tgwizard@gmail.com>
 */
public class ActivityStoreBuilder {

    public static ActivityStore loadActivityStoreFromFile(String filename) {
        ActivityStore as = null;
        Document document = null;
        try {
            document = openXMLDocument(new FileInputStream(new File(filename)),
                    new StreamSource(ClassLoader.getSystemResourceAsStream("data/activitiesSchema.xsd")));
        } catch (Exception e) {
            System.err.println("Exception in FileInputStream stuff");
            System.err.println(e);
        }

        if (document != null) {
            as = getActivityStore(document);
        }
        return as;
    }

    public static ActivityStore loadActivityStoreFromJar(String filename) {
        ActivityStore as = null;
        Document document = openXMLDocument(ClassLoader.getSystemResourceAsStream(filename),
                new StreamSource(ClassLoader.getSystemResourceAsStream("data/activitiesSchema.xsd")));
        if (document != null) {
            as = getActivityStore(document);
        }
        return as;
    }

    protected static ActivityStore getActivityStore(Document document) {
        return null;
    }

    /**
     * Attempts to open the xml-file file and validate it using schema. If
     * either the opening and loading of the xml-data (of either "filename" or
     * the schema used to validate "filename" after loading) or the validation
     * fails, exceptions will be cast hither and dither, and you might wanna fix
     * 'em before you proceed.
     *
     * @param filename The InputStream of an xml-file.
     * @param schema The Source for a schema. Can be null, and thus ignored
     * @return The opened and validated xml-file in a DOM-structure.
     */
    protected static Document openXMLDocument(InputStream file, Source schema) {
        Document document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            if (schema != null) {
                SchemaFactory sFactory = SchemaFactory.newInstance(
                        XMLConstants.W3C_XML_SCHEMA_NS_URI);
                factory.setNamespaceAware(true);
                sFactory.setErrorHandler(
                        new org.xml.sax.ErrorHandler() {
                            // ignore fatal errors (an exception is guaranteed)
                            public void fatalError(SAXParseException exception)
                                    throws SAXException {
                            }

                            // treat validation errors as fatal
                            public void error(SAXParseException e)
                                    throws SAXParseException {
                                throw e;
                            }

                            // dump warnings too
                            public void warning(SAXParseException err)
                                    throws SAXParseException {
                                System.err.println("** Warning" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
                                System.err.println("   " + err.getMessage());
                            }
                        });
                Schema tschema = sFactory.newSchema(schema);
                factory.setSchema(tschema);
            }
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(
                    new org.xml.sax.ErrorHandler() {
                        // ignore fatal errors (an exception is guaranteed)
                        public void fatalError(SAXParseException exception)
                                throws SAXException {
                        }

                        // treat validation errors as fatal
                        public void error(SAXParseException e)
                                throws SAXParseException {
                            throw e;
                        }

                        // dump warnings too
                        public void warning(SAXParseException err)
                                throws SAXParseException {
                            System.err.println("** Warning" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
                            System.err.println("   " + err.getMessage());
                        }
                    });
            document = builder.parse(file);
        } catch (SAXException sxe) {
            // Error generated during parsing
            System.err.println(sxe);
            Exception x = sxe;
            if (sxe.getException() != null) {
                x = sxe.getException();
            }
            x.printStackTrace();

        } catch (ParserConfigurationException pce) {
            // Parser with specified options can't be built
            System.err.println(pce);
            pce.printStackTrace();

        } catch (IOException ioe) {
            // I/O error
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
        } // try-catch


        return document;
    }

    public static void saveActivityStoreToFile() {

    }

    public static void saveActivityStoreToJar() {

    }
}
