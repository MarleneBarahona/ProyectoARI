package sample;

import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import jdk.internal.org.xml.sax.SAXException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.helpers.AttributesImpl;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Controller {

    private static StreamResult out;
    private static AttributesImpl atts;
    private static TransformerHandler th;

    public void convertTXTtoXML(ActionEvent event){
        //SELECIONA ARCHIVO A LEER
        JFileChooser selectorArchivos = new JFileChooser();
        selectorArchivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de texto", "txt");
        selectorArchivos.setFileFilter(filtro);
        selectorArchivos.showOpenDialog(selectorArchivos);
        File archivo = selectorArchivos.getSelectedFile(); // obtiene el archivo seleccionado
        //mostrarContenido(archivo);

// -------------------------------------------------------------------------------------------
        //ELIGE DONDE GUARDAR
        JFileChooser archivoG = new JFileChooser();
        archivoG.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        //FileNameExtensionFilter filtroG = new FileNameExtensionFilter("Archivos XML", "xml");
        //selectorArchivos.setFileFilter(filtroG);
        archivoG.showSaveDialog(archivoG);
        File guarda = archivoG.getSelectedFile();
//--------------------------------------------------------------------------------------------

        if ((archivo == null) || (archivo.getName().equals(""))) {
            JOptionPane.showMessageDialog(selectorArchivos, "Nombre de archivo inválido",
                    "Nombre de archivo inválido", JOptionPane.ERROR_MESSAGE);
        } else {

            Scanner entrada = null;

            try {
                String ruta = selectorArchivos.getSelectedFile().getAbsolutePath();
                File f = new File(ruta);
                entrada = new Scanner(f);

                /*
                JTextArea idk = new JTextArea();
                String codigo = new String();
                codigo = entrada.nextLine();
                idk.setText(codigo);*/

                out = new StreamResult(guarda + ".xml");
                openXml();//---> Funcion que abre xml (Estructura)

                while (entrada.hasNext()) {
                    proceso(entrada.nextLine());
                }

                closeXml();//---> Funcion que cierra xml (Estructura)
                entrada.close();

            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (NullPointerException e) {
                System.out.println("No se ha seleccionado ningún fichero");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

    }
    @FXML
    private void mostrarContenido(File archivo) {
        TextArea idk = new TextArea();
        idk.setText("hOLA");
    }

    //Abre el archivo XML (Estructura) para empezar a crearlo
    public static void openXml () throws ParserConfigurationException, TransformerConfigurationException, SAXException, org.xml.sax.SAXException {

        SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        th = tf.newTransformerHandler();

        // SALIDA XML
        Transformer serializer = th.getTransformer();
        serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");

        //Se empiezan a poner las etiquetas
        th.setResult(out);
        th.startDocument();
        atts = new AttributesImpl();
        th.startElement("", "", "Clientes", atts);
    }

    //FUNCION de Proceso para crear el archivo.xml a traves de la informacion del archivo.txt
    public static void proceso (String s) throws SAXException, org.xml.sax.SAXException {


        String[] elements = s.split("\\;|\\,|\\^|\\$|\\?|\\+|\\(|\\)|\\:|\\[|\\{"); //--->Delimitador
        atts.clear();
        th.startElement("", "", "cliente", atts);

        th.startElement("", "", "documento", atts);
        th.characters(elements[1].toCharArray(), 0, elements[1].length());
        th.endElement("", "", "documento");

        th.startElement("", "", "primer-nombre", atts);
        th.characters(elements[2].toCharArray(), 0, elements[2].length());
        th.endElement("", "", "primer-nombre");

        th.startElement("", "", "apellido", atts);
        th.characters(elements[3].toCharArray(), 0, elements[3].length());
        th.endElement("", "", "apellido");

        boolean ban = true;
        th.startElement("", "", "credit-card", atts);
        /*while (ban){
            Vigenere vigenere = new Vigenere(elements[4],"CiAri");
            th.characters(vigenere.cifrado, 0, elements[4].length());
            System.out.println(vigenere.cifrado);
            ban= false;
        }*/
        th.endElement("", "", "credit-card");

        th.startElement("", "", "tipo", atts);
        th.characters(elements[5].toCharArray(), 0, elements[5].length());
        th.endElement("", "", "tipo");

        th.startElement("", "", "telefono", atts);
        th.characters(elements[6].toCharArray(), 0, elements[6].length());
        th.endElement("", "", "telefono");

        th.endElement("", "", "cliente");

    }

    //Cierra el archivo.xml (Estructura)
    public static void closeXml () throws SAXException, org.xml.sax.SAXException {
        th.endElement("", "", "Clientes");
        th.endDocument();
    }

    public void convertXMLtoTXT(ActionEvent event) throws IOException {
            //SELECCIONA ARCHIVO A LEER
            JFileChooser selectorArchivos = new JFileChooser();
            selectorArchivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos XML", "xml");
            selectorArchivos.setFileFilter(filtro);
            selectorArchivos.showOpenDialog(selectorArchivos);
            File archivo = selectorArchivos.getSelectedFile(); // obtiene el archivo seleccionado
//---------------------------------------------------------------------------------------------
            //ELIGE DONDE GUARDAR
            JFileChooser archivoG = new JFileChooser();
            archivoG.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            FileNameExtensionFilter filtroG = new FileNameExtensionFilter("Archivos de Texto", "txt");
            selectorArchivos.setFileFilter(filtroG);
            archivoG.showSaveDialog(archivoG);
            File guarda = archivoG.getSelectedFile();
//---------------------------------------------------------------------------------------------

            if ((archivo == null) || (archivo.getName().equals(""))) {
                JOptionPane.showMessageDialog(selectorArchivos, "Nombre de archivo inválido",
                        "Nombre de archivo inválido", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    //Archivo.txt que va a crear
                    BufferedWriter writer = new BufferedWriter(new FileWriter(guarda + ".txt"));

                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(archivo); //Archivo elegido a leer

                    //Etiqueta va a leer para pasarla a texto
                    NodeList nList = doc.getElementsByTagName("cliente");
                    int cont = 1;
                    //Nodo Padre
                    for (int i = 0; i < nList.getLength(); i++) {
                        Node node = nList.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            //Creo un elemento que obtendra los hijos
                            Element eElement = (Element) node;
                            if (eElement.hasChildNodes()) {
                                NodeList nl = node.getChildNodes();
                                for (int j = 0; j < nl.getLength(); j++) {
                                    Node nd = nl.item(j);
                                    String name = nd.getTextContent();
                                    if(j==0){
                                        writer.write(cont+";");
                                        //System.out.println(cont+";");
                                        cont++;
                                    }
                                    //Compruebo que no este vacio y escribo en el archivo.txt
                                    if (name != null && !name.trim().equals("")) {
                                        writer.write(nd.getTextContent().trim());
                                        if(j < nl.getLength()-4){
                                            //Vigenere vigenere = new Vigenere(nd.getTextContent().trim(),"CiAri");
                                            //writer.write(vigenere.descifrado);
                                            System.out.println(nd.getTextContent().trim());
                                        }else{
                                        }
                                        //Para que el último dato no tenga el delimitador
                                        if (j < nl.getLength() - 2) {
                                            writer.write(";");
                                        }
                                    }
                                }
                            }
                        }
                        writer.write("\n");
                    }
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    public void convertTXTtoJSON(ActionEvent event) throws IOException {

            //Crea una matriz donde se almacenaran los datos
            JsonArray datasets = new JsonArray();

            //SELECCIONA ARCHIVO A LEER
            JFileChooser selectorArchivos = new JFileChooser();
            selectorArchivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de texto", "txt");
            selectorArchivos.setFileFilter(filtro);
            selectorArchivos.showOpenDialog(selectorArchivos);
            File archivo = selectorArchivos.getSelectedFile(); // obtiene el archivo seleccionado
            /*AbstractButton jlArchivo = null;
        jlArchivo.setText(archivo.getAbsolutePath());
        Scanner scn = new Scanner(archivo);
        while (scn.hasNext()) {
            TextArea jtaContenido = null;
            jtaContenido.insert(scn.nextLine() + "\n", jtaContenido.getText().length());
        }*/
            //archivo.so
            /*JFileChooser jlArchivo = new JFileChooser();
            jlArchivo.setText(archivo.getAbsolutePath());
            Scanner scn = new Scanner(archivo);
            while (scn.hasNext()) {
                jtaContenido.insert(scn.nextLine() + "\n", jtaContenido.getText().length());
            }*/
//---------------------------------------------------------------------------------------------
            //ELIGE DONDE GUARDAR
            //JFileChooser archivoG = new JFileChooser();
            //archivoG.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            //FileNameExtensionFilter filtroG = new FileNameExtensionFilter("Archivos JSON", "json");
            //selectorArchivos.setFileFilter(filtroG);
            //archivoG.showSaveDialog(archivoG);
            //File guarda = archivoG.getSelectedFile();

            JFileChooser archivoG = new JFileChooser();
            archivoG.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            archivoG.showSaveDialog(archivoG);
            File guarda = archivoG.getSelectedFile();
//---------------------------------------------------------------------------------------------

            if ((archivo == null) || (archivo.getName().equals(""))) {
                JOptionPane.showMessageDialog(selectorArchivos, "Nombre de archivo inválido",
                        "Nombre de archivo inválido", JOptionPane.ERROR_MESSAGE);
            } else {

                Scanner entrada = null;
                String ruta = selectorArchivos.getSelectedFile().getAbsolutePath();
                File f = new File(ruta);
                entrada = new Scanner(f);
                //Titlos para el JSON
                String titulo = "id;documento;primer-nombre;apellido;credit-card;tipo;telefono";
                String line;//--> Para las linea que leera del txt
                boolean flag = true;
                List<String> columns = null;
                while (entrada.hasNext()) {
                    if (flag) {
                        //process Titulos;
                        columns = Arrays.asList(titulo.split("\\;|\\,|\\^|\\$|\\?|\\+|\\(|\\)|\\:|\\[|\\{")); //---> delimitador
                        //Se crea el objeto JSON y lo almacena temporalmente
                        JsonObject obj = new JsonObject();
                        //Información del cliente (Linea por linea del archivo)
                        List<String> chunks = Arrays.asList(entrada.nextLine().split("\\;|\\,|\\^|\\$|\\?|\\+|\\(|\\)|\\:|\\[|\\{")); //---> delimitador
                        for (int i = 0; i < columns.size(); i++) {
                            obj.addProperty(columns.get(i), chunks.get(i));
                        }
                        //Agrega los datos a la matriz
                        datasets.add(obj);
                    } else {
                        flag = false;
                    }
                }

                //generateJWT(datasets);

                //Aqui se le da el formato de JSON y se empieza a crear
                Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
                Writer writer = null;
                try {
                    //Se crea el fichero JSON en la ruta establecida
                    writer = new FileWriter(guarda + ".json");
                    //Se crea el JSON y lo escribimos en el archivo.
                    gson.toJson(datasets, writer);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // Cerramos el archivo
                    try {
                        //Verificamos que no este nulo
                        if (null != writer) {
                            writer.flush();
                            writer.close();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }

        }

    public void convertJSONtoTXT(ActionEvent event) throws IOException, ParseException {

        //SELECCIONA ARCHIVO A LEER
        JFileChooser selectorArchivos = new JFileChooser();
        selectorArchivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos JSON", "json");
        selectorArchivos.setFileFilter(filtro);
        selectorArchivos.showOpenDialog(selectorArchivos);
        File archivo = selectorArchivos.getSelectedFile(); // obtiene el archivo seleccionado
//---------------------------------------------------------------------------------------------
        //ELIGE DONDE GUARDAR
        JFileChooser archivoG = new JFileChooser();
        archivoG.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        FileNameExtensionFilter filtroG = new FileNameExtensionFilter("Archivos de Texto", "txt");
        selectorArchivos.setFileFilter(filtroG);
        archivoG.showSaveDialog(archivoG);
        File guarda = archivoG.getSelectedFile();
//---------------------------------------------------------------------------------------------

        if ((archivo == null) || (archivo.getName().equals(""))) {
            JOptionPane.showMessageDialog(selectorArchivos, "Nombre de archivo inválido",
                    "Nombre de archivo inválido", JOptionPane.ERROR_MESSAGE);
        } else {
            //Archivo al que vamos a reescribir el JSON
            Writer writer;
            writer = new FileWriter(guarda + ".txt");

            JSONParser parser = new JSONParser();
            JSONArray a = (JSONArray) parser.parse(new FileReader(archivo));
            try {
                for (Object o : a) {

                    JSONObject jsonObject = (JSONObject) o;

                    String id = (String) jsonObject.get("id");
                    String doc = (String) jsonObject.get("documento");
                    String nombre = (String) jsonObject.get("primer-nombre");
                    String apellido = (String) jsonObject.get("apellido");
                    String nTarjeta = (String) jsonObject.get("credit-card");
                    String tipo = (String) jsonObject.get("tipo");
                    String telefono = (String) jsonObject.get("telefono");
                    if (jsonObject.get("id") == id) {
                        writer.write(id);
                        writer.write(";");
                    }
                    if (jsonObject.get("documento") == doc) {
                        writer.write(doc);
                        writer.write(";");
                    }
                    if (jsonObject.get("primer-nombre") == nombre) {
                        writer.write(nombre);
                        writer.write(";");
                    }
                    if (jsonObject.get("apellido") == apellido) {
                        writer.write(apellido);
                        writer.write(";");
                    }
                    if (jsonObject.get("credit-card") == nTarjeta) {
                        writer.write(nTarjeta);
                        writer.write(";");
                    }
                    if (jsonObject.get("tipo") == tipo) {
                        writer.write(tipo);
                        writer.write(";");
                    }
                    if (jsonObject.get("telefono") == telefono) {
                        writer.write(telefono);
                        writer.write("\n");
                    }
                }
            } finally {
                // Cerramos el archivo
                try {
                    //Verificamos que no este nulo
                    if (null != writer) {
                        writer.flush();
                        writer.close();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }


    }

    public void mostrarContenido2(ActionEvent event) {
        File archivo = new File("C:/Users/Marlene/Desktop/Cliente.txt");
        try {
            BufferedReader leer = new BufferedReader(new FileReader(archivo));
            String linea = leer.readLine();
            while (linea != null){
                //JTextArea
                linea = leer.readLine();
            }
        }catch (Exception e){
            //Logger.getLogger()
        }
        System.out.println("lo tocaste :O");
    }

    /*public static void generateJWT(JsonArray dataset){
        String clave = "EstaEsUnaClaveSuperSecretaYLargaParaQueEstoFuncione";
        byte[] decodedKey = Base64.getDecoder().decode(clave);
        String jwt = Jwts.builder()
                .setPayload(dataset.toString())
                .signWith(Keys.hmacShaKeyFor(decodedKey))
                .compact();

        System.out.println(jwt);
        System.out.println();

        String result = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(decodedKey))
                .parsePlaintextJws(jwt).getBody();

        System.out.println(result);
    }*/
}

