/*
 * MailTask.java
 *
 * Created on November 19, 2002, 5:51 PM
 */

package org.wyona.cms.mail;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.LinkageError;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.log4j.Category;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.wyona.cms.task.AbstractTask;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A task that sends an e-mail. Each parameter can either be
 * provided as a task parameter or extracted from an XML document.
 * If the parameter "uri" starts with a <code>http://</code> or <code>ftp://</code>
 * prefix, the absolute
 * URI is used. If not, the URI is interpreted as relative to the
 * local publication.
 * <br/><br/>
 * The task parameters are:<br/>
 * <code><strong>uri</strong></code>: the URI to get the XML file from<br/>
 * <code><strong>server</strong></code>: the SMTP server<br/>
 * <code><strong>from</strong></code>:<br/>
 * <code><strong>to</strong></code>:<br/>
 * <code><strong>cc</strong></code>:<br/>
 * <code><strong>subject</strong></code>:<br/>
 * <code><strong>body</strong></code>:<br/>
 * <br/>
 * All parameters are optional. If the uri parameter is provided, the
 * document is fetched from the URI and the parameters are extracted.
 * Task parameters have a higher priority than elements of the document.
 * <br/><br/>
 * The document has the following form:<br/><br/>
 * <code>
 * &lt;mail:mail xmlns:mail="http://www.wyona.org/2002/mail"&gt;<br/>
 * &#160;&#160;&lt;mail:server&gt;mail.yourhost.com&lt;/mail:server&gt;<br/>
 * &#160;&#160;...<br/>
 * &lt;/mail:mail&gt;<br/>
 * </code>
 * @author  <a href="mailto:ah@wyona.org">Andreas Hartmann</a>
 */
public class MailTask
    extends AbstractTask {

    static Category log = Category.getInstance(MailTask.class);
    
    public static final String ELEMENT_TO = "to";
    public static final String ELEMENT_CC = "cc";
    public static final String ELEMENT_SUBJECT = "subject";
    public static final String ELEMENT_BODY = "body";
    public static final String ELEMENT_FROM = "from";
    public static final String ELEMENT_SERVER = "server";
    public static final String PARAMETER_URI = "uri";
    public static final String NAMESPACE_URI = "http://www.wyona.org/2002/mail";

    public void execute(String contextPath) {
        log.debug(
            "\n---------------------------" +
            "\n- Sending mail" +
            "\n---------------------------");
        try {
            Parameters taskParameters = new Parameters();
            
            String uri = getParameters().getParameter(PARAMETER_URI, "");
            log.debug("\nURI: " + uri);
            
            if (!uri.equals("")) {

                // generate absolute URI from relative URI
                
                if (!uri.startsWith("http://") && !uri.startsWith("ftp://")) {
//                    String absoluteUri =
//                        getParameters().getParameter(PARAMETER_SERVER_URI);
                    String absoluteUri = "http://127.0.0.1";
                    String serverPort = getParameters().getParameter(PARAMETER_SERVER_PORT, "");
                    if (!serverPort.equals(""))
                        absoluteUri += ":" + Integer.parseInt(serverPort);
                    absoluteUri +=
                        getParameters().getParameter(PARAMETER_CONTEXT_PREFIX) +
                        getParameters().getParameter(PARAMETER_PUBLICATION_ID) +
                        uri;
                    uri = absoluteUri;
                }

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setValidating(false);
                factory.setNamespaceAware(true);
                factory.setExpandEntityReferences(true);
                DocumentBuilder builder = factory.newDocumentBuilder();
                
                
                Document document = builder.parse(uri);
                Element root = (Element) document.getChildNodes().item(0);
/*
                log.debug(
                    "\n---------------------------" +
                    "\n- Entity resolver: " + builder
                    "\n---------------------------");
*/                
                
                String keys[] = {
                    ELEMENT_SERVER,
                    ELEMENT_FROM,
                    ELEMENT_TO,
                    ELEMENT_CC,
                    ELEMENT_SUBJECT,
                    ELEMENT_BODY
                };

                NodeList children = root.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    Node child = children.item(i);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) child;
                        if (element.getChildNodes().getLength() > 0) {
                            Node firstChild = element.getChildNodes().item(0);
                            if (firstChild instanceof Text) {
                                Text text = (Text) firstChild;
                                String key = element.getLocalName();
                                if (Arrays.asList(keys).contains(key)) {
                                    taskParameters.setParameter(key, getValue(text));
                                }
                            }
                        }
                    }
                }
                
            }
            // task parameters have a higher priority than XML elements
            taskParameters = taskParameters.merge(getParameters());
            
            sendMail(
                taskParameters.getParameter(ELEMENT_SERVER),
                taskParameters.getParameter(ELEMENT_FROM),
                taskParameters.getParameter(ELEMENT_TO),
                taskParameters.getParameter(ELEMENT_CC, ""),
                taskParameters.getParameter(ELEMENT_SUBJECT, ""),
                taskParameters.getParameter(ELEMENT_BODY, ""));
        }
        catch(Exception e) {
            log.error("Sending mail failed: ", e);
        }
    }        
    
    public void sendMail(String host, String from, String to, String cc, String subject, String body) {
        log.debug(
            "\nTrying to send mail:" +
            "\n-------------------------------------------------------------" +
            "\nhost:    " + to +
            "\n-------------------------------------------------------------" +
            "\nfrom:    " + to +
            "\n-------------------------------------------------------------" +
            "\nto:      " + to +
            "\n-------------------------------------------------------------" +
            "\ncc:      " + cc +
            "\n-------------------------------------------------------------" +
            "\nsubject: " + subject +
            "\n-------------------------------------------------------------" +
            "\nbody:\n" + body +
            "\n-------------------------------------------------------------\n\n");
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            Session mailSession = Session.getInstance(props, null);

            MimeMessage pm = new MimeMessage(mailSession);

            // set from
            pm.setFrom(new InternetAddress(from));
            // set to
            pm.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            // set cc
            if (!cc.equals(""))
                pm.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
             // set subject
            if (!subject.equals(""))
              pm.setSubject(subject);
            // set date
            pm.setSentDate(new Date());
            // set content
            if (!body.equals(""))
              pm.setText(body);
            // send mail
            Transport trans = mailSession.getTransport("smtp");
            Transport.send(pm);
            // success
        } catch (Exception e) {
            log.error("Sending mail failed: ", e);
        }
    }
        
    protected String getValue(Text text) {
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();

            DOMSource source = new DOMSource(text);
            
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(stream);
            transformer.transform(source, result);
            return stream.toString();
        }
        catch(Exception e) {
            log.error("Failed: ", e);
            return null;
        }
    }
    
    
}
