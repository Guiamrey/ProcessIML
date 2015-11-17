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
import java.util.ArrayList;

public class XML_DTD {

    public static ArrayList<String> listaXML = new ArrayList<>();
    public static ArrayList<String> listaXMLleidos = new ArrayList<>();
    public static ArrayList<Document> listDoc = new ArrayList<>();


    public static void main(String[] args) {

        String URL = "sabina.xml";
        processIML(URL);
        listaXMLleidos.add(URL);


        while (listaXML.size() > 0) {
            String url = listaXML.get(0);
            processIML(url);
            listaXML.remove(0);
            listaXMLleidos.add(url);
        }

    }

    public static void processIML(String XML) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            db.setErrorHandler(new XML_DTD_ErrorHandler());
            Document doc;

            doc = db.parse(XML);

            listDoc.add(doc);

            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nodelist = (NodeList) xpath.evaluate("/Interprete", doc, XPathConstants.NODESET);


            Node node = nodelist.item(0); //Nodo del elemento raiz (Interprete)
            System.out.println(" " + node.getNodeName());

            NodeList childs = node.getChildNodes(); //Elemetos hijos del elemento raiz

            for (int i = 1; i < childs.getLength(); i++) {
                //  Node child = childs.item(i);
                System.out.println("\t " + childs.item(i).getNodeName());
                NodeList childs2 = childs.item(i).getChildNodes();

                if (childs.item(i).hasAttributes()) {
                    NamedNodeMap attributes1 = childs.item(i).getAttributes();
                    for (int k = 0; k < attributes1.getLength(); k++) {
                        System.out.println("\t\t " + attributes1.item(k).getNodeName() + " : " + attributes1.item(k).getTextContent());
                    }
                }
                i++;
                for (int j = 1; j < childs2.getLength(); j++) {
                    if (!childs2.item(j).getNodeName().equals("Cancion")) {
                        System.out.print("\t\t" + childs2.item(j).getNodeName() + ":");
                        System.out.println(" " + childs2.item(j).getTextContent());
                    } else {
                        System.out.print("\t\t" + childs2.item(j).getNodeName() + ":");

                        NamedNodeMap attributes2 = childs2.item(j).getAttributes();
                        for (int k = 0; k < attributes2.getLength(); k++) {
                            System.out.println("\t" + attributes2.item(k).getNodeName() + ": " + attributes2.item(k).getTextContent());
                        }

                        NodeList childsCancion = childs2.item(j).getChildNodes();
                        for (int n = 1; n < childsCancion.getLength(); n++) {
                            if (!childsCancion.item(n).getNodeName().equals("Version")) {
                                System.out.print("\t\t\t\t" + childsCancion.item(n).getNodeName() + ":");
                                System.out.println(" " + childsCancion.item(n).getTextContent());
                                n++;
                            } else {
                                System.out.println("\t\t\t\t" + childsCancion.item(n).getNodeName());
                                NodeList childsVersion = childsCancion.item(n).getChildNodes();
                                for (int a = 1; a < childsVersion.getLength(); a++) {
                                    if (!childsVersion.item(a).getNodeName().equals("Nombre")) {
                                        System.out.print("\t\t\t\t\t" + childsVersion.item(a).getNodeName());
                                        System.out.println(": " + childsVersion.item(a).getTextContent());

                                        if ((!listaXML.contains(childsVersion.item(a).getTextContent()) && !childsVersion.item(a).getTextContent().equals("")) && !listaXMLleidos.contains(childsVersion.item(a).getTextContent())) {
                                            listaXML.add(childsVersion.item(a).getTextContent());
                                        }
                                        //añadir nuevas rutas hacia IMLs
                                        a++;
                                    } else {
                                        System.out.println("\t\t\t\t\t" + childsVersion.item(a).getNodeName());

                                        NodeList childsNF = childsVersion.item(a).getChildNodes();
                                        System.out.println("\t\t\t\t\t\t" + childsNF.item(1).getNodeName() + ": " + childsNF.item(1).getTextContent());
                                        System.out.println("\t\t\t\t\t\t" + childsNF.item(3).getNodeName() + ": " + childsNF.item(3).getTextContent());
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
