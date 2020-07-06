package sample;

import com.google.gson.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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

public class Main extends Application {

    Button b1, b2, b3, b4;
    private static StreamResult out;
    private static AttributesImpl atts;
    private static TransformerHandler th;
    private File archivo;
    private JFileChooser selectorArchivos;
    private static String clave;
    private static String delimitador;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setX(110);
        primaryStage.setY(50);
        primaryStage.setScene(new Scene(root, 400, 375));
        //primaryStage.show();
        Stage contenido = new Stage();
        //Text para nombre del archivo mostrado
        Text text1 = new Text("Archivo seleccionado:");
        Text textNombreArchivo = new Text("");
        //Area para mostrar
        TextArea jta1 = new TextArea();
        jta1.setPrefSize(440,300);
        jta1.setText("Aqui se mostrara el contenido del archivo seleccionado");
        jta1.setEditable(false);
        //Opciones generales
        b1 = new Button("Seleccionar archivo");
        b2 = new Button("Convertir este archivo");
        b3 = new Button("Mostrar archivo");
        b4 = new Button("Cancelar");
        b1.setPrefSize(150,10);
        b2.setPrefSize(150,10);
        b4.setPrefSize(150,10);
        b2.setDisable(true);
        //Opciones de conversion
        Button convert1 = new Button("Convertir a XML");
        Button convert2 = new Button("Convertir a JSON");
        Button convert3 = new Button("Convertir de XML a TXT");
        Button convert4 = new Button("Convertir de JSON a TXT ");
        HBox opcionesConvert = new HBox(3);
        opcionesConvert.setAlignment(Pos.CENTER);
        opcionesConvert.getChildren().addAll(convert1,convert2,convert3,convert4);
        convert1.setDisable(true);
        convert2.setDisable(true);
        convert3.setDisable(true);
        convert4.setDisable(true);
        //Para meter la llave
        Text textLlave = new Text("Llave de cifrado: ");
        TextField textFieldllaveCi = new TextField("Introduzca la llave");
        HBox llaveCi = new HBox(textLlave,textFieldllaveCi);
        llaveCi.setAlignment(Pos.CENTER);
        llaveCi.setDisable(true);
        //Para meter el delimitador
        Text texDelimitador = new Text("Delimitador del archivo: ");
        TextField textFieldDelimitador = new TextField("Introduzca el delimitador: ");
        textFieldDelimitador.setPrefSize(110,10);
        HBox HBoxDelimitador = new HBox(texDelimitador,textFieldDelimitador);
        HBoxDelimitador.setAlignment(Pos.CENTER);
        //HBoxDelimitador.setDisable(true);
        //Imagen de nombre del SW
        Image nombrexd = new Image(getClass().getResourceAsStream("/sample/Imagen1.PNG"));
        ImageView imageView = new ImageView(nombrexd);
        imageView.setFitWidth(300);
        imageView.setFitHeight(80);

        VBox layout = new VBox(7);
        layout.getChildren().addAll(imageView, text1, textNombreArchivo, jta1,b1,b2,opcionesConvert,llaveCi,HBoxDelimitador ,b3, b4);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(1,5,1,5));
        //Background
        Image imageMenu = new Image(getClass().getResourceAsStream("/sample/Imagen3.png"));
        // new BackgroundSize(width, height, widthAsPercentage, heightAsPercentage, contain, cover)
        BackgroundSize backgroundSize = new BackgroundSize(400, 400, true, true, true, false);
        // new BackgroundImage(image, repeatX, repeatY, position, size)
        BackgroundImage backgroundImage = new BackgroundImage(imageMenu, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
        // new Background(images...)
        layout.setBackground(new Background(backgroundImage));

        contenido.setScene(new Scene(layout,580,680));
        contenido.setX(400);
        contenido.setY(15);
        //contenido.initStyle(StageStyle.TRANSPARENT);
        contenido.show();
        //Filtros para los FileChooser
        FileNameExtensionFilter filtroTXT = new FileNameExtensionFilter("Archivos de texto", "txt");
        FileNameExtensionFilter filtroXLM = new FileNameExtensionFilter("Archivos XML", "xml");
        FileNameExtensionFilter filtroJSON = new FileNameExtensionFilter("Archivos JSON", "json");
        //Al presionar b1 (Seleccionar archivo)
        b1.setOnAction(event -> {
            jta1.clear();
            selectorArchivos = new JFileChooser();
            selectorArchivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            //FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de texto", "txt");
            selectorArchivos.setFileFilter(filtroJSON);
            selectorArchivos.setFileFilter(filtroXLM);
            selectorArchivos.setFileFilter(filtroTXT);
            selectorArchivos.showOpenDialog(selectorArchivos);
            archivo = selectorArchivos.getSelectedFile();
            textNombreArchivo.setText(archivo.getName());
            mostrarContenidoTextArea(archivo,jta1);
            b2.setDisable(false);
        }
        );
        //Al presionar b4 (Cancelar)
        b4.setOnAction(event -> {
            jta1.clear();
            b2.setDisable(true);
            textNombreArchivo.setText("");
            convert1.setDisable(true);
            convert2.setDisable(true);
            convert3.setDisable(true);
            convert4.setDisable(true);});
        //Al presionar b3 (Mostrar archivo)
        b3.setOnAction(event -> {
            jta1.clear();
            b2.setDisable(true);
            convert1.setDisable(true);
            convert2.setDisable(true);
            convert3.setDisable(true);
            convert4.setDisable(true);
            selectorArchivos = new JFileChooser();
            selectorArchivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            selectorArchivos.setFileFilter(filtroJSON);
            selectorArchivos.setFileFilter(filtroXLM);
            selectorArchivos.setFileFilter(filtroTXT);
            selectorArchivos.showOpenDialog(selectorArchivos);
            archivo = selectorArchivos.getSelectedFile();
            textNombreArchivo.setText(archivo.getName());
            mostrarContenidoTextArea(archivo,jta1);
        });
        //Al presionar b2 (Convertir archivo) Habilita qué opciones de conversion se tienen
        b2.setOnAction(event -> {
            llaveCi.setDisable(false);
            System.out.println(textFieldllaveCi.getText());
            String ext = archivo.getName().substring(archivo.getName().lastIndexOf("."));
            System.out.println(ext);
            if(ext.equals(".txt")){
                convert1.setDisable(false); convert2.setDisable(false); convert3.setDisable(true); convert4.setDisable(true);
            }else if(ext.equals(".xml")){
                convert3.setDisable(false); convert1.setDisable(true); convert2.setDisable(true); convert4.setDisable(true);
            }else if (ext.equals(".json")){
                convert4.setDisable(false); convert1.setDisable(true); convert2.setDisable(true); convert3.setDisable(true);
            }
            });
        //Al presionar convert1 (Convertir a XML)
        convert1.setOnAction(event -> {
            //llaveCi.setDisable(false);
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

                    out = new StreamResult(guarda + ".xml");
                    openXml();//---> Funcion que abre xml (Estructura)

                    clave =  textFieldllaveCi.getText();
                    delimitador = textFieldDelimitador.getText();

                    while (entrada.hasNext()) {
                        proceso(entrada.nextLine());
                    }

                    closeXml();//---> Funcion que cierra xml (Estructura)
                    entrada.close();
                    mostrarContenidoTextArea(guarda,jta1);
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (NullPointerException e) {
                    System.out.println("No se ha seleccionado ningún fichero");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }});

        //Al presionar convert2 (Convertir a JSON)
            convert2.setOnAction(event -> {
               // llaveCi.setDisable(false);
                //Crea una matriz donde se almacenaran los datos
                JsonArray datasets = new JsonArray();
//---------------------------------------------------------------------------------------------
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
                    try {
                        entrada = new Scanner(f);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    //Titulos para el JSON
                    String titulo = "id;documento;primer-nombre;apellido;credit-card;tipo;telefono";
                    String line;//--> Para las linea que leera del txt
                    boolean flag = true;
                    List<String> columns = null;

                    delimitador = textFieldDelimitador.getText();
                    
                    while (entrada.hasNext()) {
                        if (flag) {
                            //process Titulos;
                            columns = Arrays.asList(titulo.split(";")); //---> delimitador
                            //Se crea el objeto JSON y lo almacena temporalmente
                            JsonObject obj = new JsonObject();
                            //Información del cliente (Linea por linea del archivo)
                            List<String> chunks = Arrays.asList(entrada.nextLine().split(delimitador)); //---> delimitador
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
            });
        //Al presionar convert3 (Convertir de XML a TXT)
            convert3.setOnAction(event -> {
               // llaveCi.setDisable(false);
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

                        clave =  textFieldllaveCi.getText();
                        delimitador = textFieldDelimitador.getText();

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
                                            writer.write(cont+delimitador);
                                            cont++;
                                        }
                                        //Compruebo que no este vacio y escribo en el archivo.txt
                                        if (name != null && !name.trim().equals("")) {

                                            if(j == 7){
                                                VigenereDescifrado vigenereDescifrado = new VigenereDescifrado(name,clave);
                                                writer.write(vigenereDescifrado.descifrado);
                                            }else{
                                                writer.write(nd.getTextContent().trim());
                                            }
                                            //Para que el último dato no tenga el delimitador
                                            if (j < nl.getLength() - 2) {
                                                writer.write(delimitador);
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
            });

        //Al presionar convert4 (Convertir de JSON a TXT)
           convert4.setOnAction(event -> {
               //llaveCi.setDisable(false);
                //ELIGE DONDE GUARDAR
                JFileChooser archivoG = new JFileChooser();
                archivoG.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                FileNameExtensionFilter filtroG = new FileNameExtensionFilter("Archivos de Texto", "txt");
                selectorArchivos.setFileFilter(filtroG);
                archivoG.showSaveDialog(archivoG);
                File guarda = archivoG.getSelectedFile();
               //System.out.println(textFieldllaveCi.getText());
//---------------------------------------------------------------------------------------------
                if ((archivo == null) || (archivo.getName().equals(""))) {
                    JOptionPane.showMessageDialog(selectorArchivos, "Nombre de archivo inválido",
                            "Nombre de archivo inválido", JOptionPane.ERROR_MESSAGE);
                } else {
                    //Archivo al que vamos a reescribir el JSON
                    Writer writer = null;
                    try {
                        writer = new FileWriter(guarda + ".txt");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    JSONParser parser = new JSONParser();
                    JSONArray a = null;

                    delimitador = textFieldDelimitador.getText();
                    
                    try {
                        a = (JSONArray) parser.parse(new FileReader(archivo));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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
                                writer.write(delimitador);
                            }
                            if (jsonObject.get("documento") == doc) {
                                writer.write(doc);
                                writer.write(delimitador);
                            }
                            if (jsonObject.get("primer-nombre") == nombre) {
                                writer.write(nombre);
                                writer.write(delimitador);
                            }
                            if (jsonObject.get("apellido") == apellido) {
                                writer.write(apellido);
                                writer.write(delimitador);
                            }
                            if (jsonObject.get("credit-card") == nTarjeta) {
                                writer.write(nTarjeta);
                                writer.write(delimitador);
                            }
                            if (jsonObject.get("tipo") == tipo) {
                                writer.write(tipo);
                                writer.write(delimitador);
                            }
                            if (jsonObject.get("telefono") == telefono) {
                                writer.write(telefono);
                                writer.write("\n");
                            }
                        }
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
            });
    }
    @FXML
    private void mostrarContenido(File archivo) {
        TextArea idk = new TextArea();
        idk.setText("hOLA");
    }
    public static void mostrarContenidoTextArea(File archivo2, TextArea jta){
        try {
            BufferedReader leer = new BufferedReader(new FileReader(archivo2));
            String linea = leer.readLine();
            while (linea != null){
                jta.appendText(linea+"\n");
                linea = leer.readLine();
            }
        }catch (Exception e){
            //Logger.getLogger()
        }
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

        String[] elements = s.split(delimitador); //--->Delimitador
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
        while (ban){
            VigenereCifrado vigenere = new VigenereCifrado(elements[4],clave);
            th.characters(vigenere.cifrado, 0, elements[4].length());
            ban= false;
        }
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


    public static void main(String[] args) {
        launch(args);
    }
}
