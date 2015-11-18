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
    public static ArrayList<String> listError = new ArrayList<>();
    public static ArrayList<String> listFichError = new ArrayList<>();


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

        //getCantantes();
        //getAñoAlbumes();
        //getAlbumCantante("Joaquin Sabina");
        getCancionesCantante("Joaquin Sabina", "19 dias y 500 noches");
    }

    public static void processIML(String XML) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        DocumentBuilder db;
        Document doc;
        XML_DTD_ErrorHandler errorHandler = new XML_DTD_ErrorHandler();

        try {
            db = dbf.newDocumentBuilder();

            db.setErrorHandler(errorHandler);

            doc = db.parse(XML);


        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return;

        }

        if (errorHandler.hasError() || errorHandler.hasWarning() || errorHandler.hasFatalError()) {
            listFichError.add(XML);
            listError.add(errorHandler.getMessage());
            errorHandler.clear();
            return;
        }

        listDoc.add(doc);

        NodeList iml = doc.getElementsByTagName("IML");
        for (int i = 0; i < iml.getLength(); i++) {

            if ((!listaXML.contains(iml.item(i).getTextContent()) && !iml.item(i).getTextContent().equals("")) && !listaXMLleidos.contains(iml.item(i).getTextContent())) {
                listaXML.add(iml.item(i).getTextContent());
            }
        }
        /*
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList nodelist = null;

        try {
            nodelist = (NodeList) xpath.evaluate("/Interprete", doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

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
        }*/

    }

    /***
     * CONSULTA 1
     *********/
    public static ArrayList getCantantes() {

        ArrayList<String> lista = new ArrayList<>();
        for (Document doc : listDoc) {
            Element element = doc.getDocumentElement(); //Element Interprete
            Node firstChild = element.getFirstChild(); //Primer hijo (#text) del elemento Interprete
            Node nextSibling = firstChild.getNextSibling(); //Hermano -> element Nombre
            Node firstChild1 = nextSibling.getFirstChild();
            Node nombre = firstChild1.getNextSibling(); //Elemento NombreC o NombreG
            lista.add(nombre.getTextContent());
        }
        return lista;
    }

    public static ArrayList getAlbumCantante(String cantante) {
        ArrayList<String> lista = new ArrayList<>();
        for (Document doc : listDoc) {
            Element element = doc.getDocumentElement();
            String nombre = element.getFirstChild().getNextSibling().getFirstChild().getNextSibling().getTextContent();
            if (nombre.equals(cantante)) {
                NodeList nombreA = doc.getElementsByTagName("NombreA");
                for (int i = 0; i < nombreA.getLength(); i++) {
                    String albumes = nombreA.item(i).getTextContent();
                    lista.add(albumes);
                }
            }
        }
        return lista;
    }

    public static ArrayList getCancionesCantante(String cantante, String album) {
        ArrayList<String> lista = new ArrayList<>();

        if(!cantante.equalsIgnoreCase("todos")) {
            for (Document doc : listDoc) {
                Element element = doc.getDocumentElement();
                String nombre = element.getFirstChild().getNextSibling().getFirstChild().getNextSibling().getTextContent();
                if (nombre.equals(cantante)) {
                    NodeList canciones = doc.getElementsByTagName("Cancion");
                    for (int j = 0; j < canciones.getLength(); j++) {
                        String nombreA = canciones.item(j).getParentNode().getFirstChild().getNextSibling().getTextContent();
                        if (nombreA.equals(album)) {
                            NodeList childNodes = canciones.item(j).getChildNodes();

                            ArrayList<String> descrp = new ArrayList<>();
                            String nombreC = null;
                            String duracion = null;
                            String descrip = "";

                            for (int i = 0; i < childNodes.getLength(); i++) {
                                if (childNodes.item(i).getNodeName().equals("#text")) {
                                    String aux = childNodes.item(i).getTextContent();
                                    aux = aux.replaceAll("           ", "").replaceAll("       ", "").replaceAll("\n", "");
                                    descrp.add(aux);
                                } else {
                                    if (childNodes.item(i).getNodeName().equals("NombreT")) {
                                        nombreC = childNodes.item(i).getTextContent();
                                    } else {
                                        if (childNodes.item(i).getNodeName().equals("Duracion")) {
                                            duracion = childNodes.item(i).getTextContent();
                                        }
                                    }
                                }
                            }
                            for (String cad : descrp) {
                                descrip = descrip + cad;
                            }
                            String cancion = nombreC + " (" + descrip + "; " + duracion + ")";
                            System.out.println(cancion);
                        }
                    }
                }
            }
        }else{ //opcion cantante todos

        }
        return lista;
    }

    /*******
     * CONSULTA 2
     *******/
    public static ArrayList getAñoAlbumes() {

        ArrayList<String> lista = new ArrayList<>();
        for (Document doc : listDoc) {
            NodeList listanho = doc.getElementsByTagName("Año"); //Element Año
            for (int i = 0; i < listanho.getLength(); i++) {
                String anho = listanho.item(i).getTextContent();
                lista.add(anho);
            }
        }
        return lista;
    }
}

class XML_DTD_ErrorHandler extends DefaultHandler {

    public boolean error;
    public boolean warning;
    public boolean fatalerror;
    public String message;

    public XML_DTD_ErrorHandler() {
        error = false;
        warning = false;
        fatalerror = false;
        message = null;
    }

    public void warning(SAXParseException spe) {
        warning = true;
        message = "Warning: " + spe.toString();
        System.out.println(message);
    }

    public void error(SAXParseException spe) {
        error = true;
        message = "Error: " + spe.toString();

        System.out.println(message);
    }

    public void fatalerror(SAXParseException spe) {
        fatalerror = true;
        message = "Fatal Error: " + spe.toString();

        System.out.println(message);
    }

    public boolean hasWarning() {
        return warning;
    }

    public boolean hasError() {
        return error;
    }

    public boolean hasFatalError() {
        return fatalerror;
    }

    public String getMessage() {
        return message;
    }

    public void clear() {
        warning = false;
        error = false;
        fatalerror = false;
        message = null;
    }


}
