package utilities.miscellanious;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Map;

public class AllureEnvironmentXML {
    private DocumentBuilder documentBuilder;
    private static AllureEnvironmentXML instance = null;

    private AllureEnvironmentXML() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException preconception) {
            preconception.printStackTrace();
        }
    }

    public static AllureEnvironmentXML getInstance(){
        if (instance == null){
            synchronized (AllureEnvironmentXML.class){
                if (instance == null){
                    instance = new AllureEnvironmentXML();
                }
            }
        }
        return instance;
    }

    public void createAllureEnvironmentXml(Map<String, String> element) {
        Document document = documentBuilder.newDocument();

        // root element
        Element environment = document.createElementNS("urn:model.commons.qatools.yandex.ru", "qa:environment");
        document.appendChild(environment);
        for (Map.Entry<String, String> entry : element.entrySet()) {

            Element parameter = document.createElement("parameter");

            Element key = document.createElement("key");
            key.setTextContent(entry.getKey());
            parameter.appendChild(key);

            Element value = document.createElement("value");
            value.setTextContent(entry.getValue());
            parameter.appendChild(value);

            environment.appendChild(parameter);
        }
        prettyPrint(document);
    }

    private void prettyPrint(Document document) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            DOMSource source = new DOMSource(document);

            File environmentXmlFile = new File("src\\test\\resources\\environment.xml");
            StreamResult result = new StreamResult(environmentXmlFile);
            transformer.transform(source, result);

        } catch (TransformerException transformer) {
            transformer.printStackTrace();
        }
    }
}
