package com.company;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;

public class XML_DTD {
    public static void main(String[] args) {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            db.setErrorHandler(new XML_DTD_ErrorHandler());
            Document doc;

            doc = db.parse("IML1.xml");

            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nodelist = (NodeList) xpath.evaluate("/Interprete", doc, XPathConstants.NODESET);


            Node node = nodelist.item(0); //Nodo del elemento raiz (Interprete)
            System.out.println("Elemento raiz-> " + node.getNodeName());

            NodeList childs = node.getChildNodes(); //Elemetos hijos del elemento raiz

            for (int i = 1; i < childs.getLength(); i++) {
                //  Node child = childs.item(i);
                System.out.println("Element1-> " + childs.item(i).getNodeName());
                NodeList childs2 = childs.item(i).getChildNodes();

                if (childs.item(i).hasAttributes()) {
                    NamedNodeMap attributes1 = childs.item(i).getAttributes();
                    for (int k = 0; k < attributes1.getLength(); k++) {
                        System.out.println("Atr-> " + attributes1.item(k).getNodeName() + " : " + attributes1.item(k).getTextContent());
                    }
                }
                i++;
                for (int j = 1; j < childs2.getLength(); j++) {
                    if (!childs2.item(j).getNodeName().equals("Cancion")) {
                        System.out.println("Element2-> " + childs2.item(j).getNodeName());
                        System.out.println(" -->" + childs2.item(j).getTextContent());
                    }else{
                        System.out.println("Element2-> " + childs2.item(j).getNodeName());

                        NamedNodeMap attributes2 = childs2.item(j).getAttributes();
                        for (int k = 0; k < attributes2.getLength(); k++) {
                            System.out.println("Atr-> " + attributes2.item(k).getNodeName() + " : " + attributes2.item(k).getTextContent());
                        }

                        NodeList childsCancion = childs2.item(j).getChildNodes();
                        for (int n = 1; n < childsCancion.getLength(); n++) {
                            if(!childsCancion.item(n).getNodeName().equals("Version")) {
                                System.out.println("ElementC-> " + childsCancion.item(n).getNodeName());
                                System.out.println("-->" + childsCancion.item(n).getTextContent());
                                n++;
                            }else{
                                System.out.println("ElementC-> " + childsCancion.item(n).getNodeName());
                                NodeList childsVersion = childsCancion.item(n).getChildNodes();
                                for (int a = 1; a < childsVersion.getLength(); a++) {
                                    if(!childsVersion.item(a).getNodeName().equals("Nombre")) {
                                        System.out.println("ElementV-> " + childsVersion.item(a).getNodeName());
                                        System.out.println("-->" + childsVersion.item(a).getTextContent());
                                        a++;
                                    }else{
                                        System.out.println("ElementV-> " + childsVersion.item(a).getNodeName());

                                        NodeList childsNF = childsVersion.item(a).getChildNodes();
                                        System.out.println("ElementN-> "+childsNF.item(1).getNodeName()+": "+childsNF.item(1).getTextContent());
                                        System.out.println("ElementN-> "+childsNF.item(3).getNodeName()+": "+childsNF.item(3).getTextContent());
                                        a++;
                                    }
                                }
                                n++;
                            }

                        }
                    }
                    j++;
                }
            }


        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            e.printStackTrace();
        }

    }
}

class XML_DTD_ErrorHandler extends DefaultHandler {
    public XML_DTD_ErrorHandler() {
    }

    public void warning(SAXParseException spe) {
        System.out.println("Warning: " + spe.toString());
    }

    public void error(SAXParseException spe) {
        System.out.println("Error: " + spe.toString());
    }

    public void fatalerror(SAXParseException spe) {
        System.out.println("Fatal Error: " + spe.toString());
    }
}
