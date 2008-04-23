/*
 */
package anot;

import java.awt.Color;
import java.io.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.stream.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.*;

import org.w3c.dom.*;
import org.w3c.dom.ls.*;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;

/**
 * @author William Lundin Forssén <shazmodan@gmail.com>
 * @author Adam Renberg <tgwizard@gmail.com>
 */
public class ActivityStoreBuilder {

    private static SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Loads an {@link ActivityStore} from a file.
     * @param filename Path to the file to be loaded.
     * @return An {@link ActivityStore} representing the file.
     */
    public static ActivityStore loadActivityStoreFromFile(String filename)
            throws FileNotFoundException, Exception {
        ActivityStore as = null;
        Document document = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
            document = openXMLDocument(new BufferedInputStream(fis),
                    new StreamSource(ClassLoader.getSystemResourceAsStream("activitiesSchema.xsd")));
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
            System.err.println(e);
            throw e;
        } catch (Exception e) {
            System.err.println("Exception blah");
            System.err.println(e);
            throw e;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                // nothing
                }
            }
        }

        if (document != null) {
            as = new ActivityStore(filename);
            getActivityStore(as, document);
        }
        return as;
    }

    /**
     * Loads an {@link ActivityStore} from a file inside the executing jar-file.
     * @param filename Path to the file to be loaded.
     * @return An {@link ActivityStore} representing the file.
     */
    public static ActivityStore loadActivityStoreFromJar(String filename) throws Exception {
        ActivityStore as = null;
        Document document = null;
        try {
            document = openXMLDocument(ClassLoader.getSystemResourceAsStream(filename),
                    new StreamSource(ClassLoader.getSystemResourceAsStream("activitiesSchema.xsd")));

        } catch (Exception e) {
            System.err.println("Exception blah");
            System.err.println(e);
            throw e;
        }
        if (document != null) {
            as = new ActivityStore("");
            getActivityStore(as, document);
        }
        return as;
    }

    protected static void getActivityStore(ActivityStore as, Document document) {
        NodeList nodes = document.getElementsByTagName("sort");
        if (nodes.getLength() != 0) {
            Node node = nodes.item(0);
            String type = node.getAttributes().getNamedItem("type").getTextContent();
            boolean reverse = Boolean.parseBoolean(node.getAttributes().getNamedItem("reverse").getTextContent());

            as.setReverseSort(reverse);

            // should be some type of factory with prototypes and such
            as.sortActivites(ActivityStore.getSortComparatorFactory().createSortComparator(type));
        }
        nodes = document.getElementsByTagName("activity");
        for (int i = 0; i < nodes.getLength(); i++) {
            Activity activity = getActivity(nodes.item(i));
            if (activity != null) {
                as.addActivity(activity);
            }
        }
    }

    protected static Activity getActivity(Node node) {
        Activity activity = null;
        if (node.hasChildNodes()) {
            String title = node.getAttributes().getNamedItem("title").
                    getTextContent();

            String subject = node.getAttributes().getNamedItem("subject").
                    getTextContent();

            activity = new Activity(title);
            activity.setSubject(subject);
            Node child = node.getFirstChild();
            do {
                if (child.getNodeName().equals("date")) {
                    String dateString = child.getTextContent();
                    try {
                        activity.setDate(simpleDateFormat.parse(dateString));
                    } catch (ParseException pe) {
                        System.err.println("Got a parse exception when parsing a date");
                        System.err.println(pe);
                    }
                } else if (child.getNodeName().equals("description")) {
                    activity.setDescription(parseRichText(child.getTextContent()));
                } else if (child.getNodeName().equals("color")) {
                    // beware of the negative sign!! 
                    activity.setColor(new Color(-Integer.parseInt(child.getTextContent(), 16)));
                }
            } while ((child = child.getNextSibling()) != null);
        }
        return activity;
    }

    /**
     * Removes all superfluous whitespace, but keeps newlines. So:
     * "text     t\n   text" will become "text t\ntext".
     * @param text The string to be parsed.
     * @return The string without superfluous whitespace.
     */
    protected static String parseRichText(String text) {
        Scanner tokenizer = new Scanner(text.trim().replace("\n", "<br/>"));
        StringBuilder builder = new StringBuilder();
        while (tokenizer.hasNext()) {
            builder.append(tokenizer.next());
            builder.append(" ");
        }
        String ret = builder.toString();
        ret = ret.replace("<br/>", "\n");
        ret = ret.trim();
        return ret;
    }

    /**
     * Attempts to open the xml-file file and validate it using schema. If
     * either the opening and loading of the xml-data (of either "filename" or
     * the schema used to validate "filename" after loading) or the validation
     * fails, exceptions will be cast hither and dither, and you might wanna fix
     * 'em before you proceed.
     *
     * @param filename The InputStream of an xml-file.
     * @param schema The Source for a schema. Can be null, and thus ignored.
     * @return The opened and validated xml-file in a DOM-structure.
     */
    protected static Document openXMLDocument(InputStream file, Source schema) throws Exception {
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
        } catch (SAXException e) {
            // Error generated during parsing
            System.err.println(e);
            Exception x = e;
            if (e.getException() != null) {
                x = e.getException();
            }
            x.printStackTrace();
            throw e;
        } catch (ParserConfigurationException e) {
            // Parser with specified options can't be built
            System.err.println(e);
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            // I/O error
            System.err.println(e.getMessage());
            e.printStackTrace();
            throw e;
        } // try-catch
        return document;
    }

    /**
     * Writes the <code>as</code> as an xmlfile to disk, specified by
     * <code>filename</code>.
     * @param as The ActivityStore to save.
     * @param filename The relative path to the file to save to.
     */
    public static void saveActivityStoreToFile(ActivityStore as, String filename) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filename);
            writeXMLDocument(as, new BufferedOutputStream(fos));
        } catch (FileNotFoundException ex) {
            System.err.println("Exception in FileOutputStream stuff");
            System.err.println(ex);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                // nothing
                }
            }
        }
    }

    /*public static void saveActivityStoreToJar(ActivityStore as, String filename) {
    Stream s = ClassLoader.getSystemResourceAsStream(filename).;
    Document document = openXMLDocument(ClassLoader.getSystemResourceAsStream(filename),
    new StreamSource(ClassLoader.getSystemResourceAsStream("activitiesSchema.xsd")));
    if (document != null) {
    as = getActivityStore(document);
    }
    }*/
    protected static void writeXMLDocument(ActivityStore as, OutputStream os) {
        try {
            // create a document and add a root element with appropriate 
            // attributes
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();
            Document document = impl.createDocument("http://anot.berlios.de/schema/activitiesSchema",
                    "activity-store", null);

            Element rootElement = document.getDocumentElement();
            rootElement.setAttribute("xmlns:xsi",
                    "http://www.w3.org/2001/XMLSchema-instance");
            rootElement.setAttribute("xsi:schemaLocation",
                    "http://anot.berlios.de/schema/activitiesSchema activitiesSchema.xsd");

            Element sortElement = document.createElement("sort");
            sortElement.setAttribute("reverse", Boolean.toString(as.isReverseSort()));
            sortElement.setAttribute("type", as.getSortComparator().getType());
            rootElement.appendChild(sortElement);

            Iterator<Activity> i = as.iterator();
            while (i.hasNext()) {
                addActivity(document, i.next());
            }

            // this is so stupid
            // the following lines are to write the xml to file with correct
            // indentation

            // Prepare the DOM document for writing
            Source source = new DOMSource(document);
            Result result = new StreamResult(new OutputStreamWriter(os));

            // Write the DOM document to the file
            TransformerFactory tffactory = TransformerFactory.newInstance();
            tffactory.setAttribute("indent-number", 4);
            Transformer xformer = tffactory.newTransformer();
            xformer.setOutputProperty(OutputKeys.METHOD, "xml");
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.transform(source, result);

        } catch (TransformerException ex) {
            // ??
            System.err.println(ex);
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            // ??
            System.err.println(ex);
            ex.printStackTrace();
        } // try-catch
    }

    /**
     * "Transforms" <code>activity</code> into an xml-tag and inserts it into
     * <code>document</code>. 
     * @param document
     * @param activity
     */
    protected static void addActivity(Document document, Activity activity) {
        Element activityElement = document.createElement("activity");
        activityElement.setAttribute("title", activity.getTitle());
        activityElement.setAttribute("subject", activity.getSubject());
        document.getDocumentElement().appendChild(activityElement);

        Element dateElement = document.createElement("date");
        dateElement.setTextContent(simpleDateFormat.format(activity.getDate()));
        activityElement.appendChild(dateElement);

        Element descriptionElement = document.createElement("description");
        descriptionElement.setTextContent(activity.getDescription());
        activityElement.appendChild(descriptionElement);

        Element colorElement = document.createElement("color");
        String s = Integer.toHexString(Math.abs((activity.getColor().getRGB())));
        for (int i = 6; i > s.length(); i--) {
            s = "0" + s;
        }
        colorElement.setTextContent(s);
        activityElement.appendChild(colorElement);
    }
}
