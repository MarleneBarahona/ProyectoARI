package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jdk.internal.org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main extends Application {

    Button buttonTXTtoXML, buttonTXTtoJSON;
    Button b1, b2, b3;
    private static StreamResult out;
    private static AttributesImpl atts;
    private static TransformerHandler th;
    private File archivo;
    private JFileChooser selectorArchivos;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setX(110);
        primaryStage.setY(50);
        primaryStage.setScene(new Scene(root, 400, 375));
        //primaryStage.show();
        Stage contenido = new Stage();
        TextArea jta1 = new TextArea();
        jta1.setPrefSize(440,300);
        //jta1.getId(idk);
        b1 = new Button("Seleccionar archivo");
        b2 = new Button("Convertir este archivo");
        b3 = new Button("Cancelar");
        b1.setPrefSize(150,10);
        b2.setPrefSize(150,10);
        b3.setPrefSize(150,10);
        b2.setDisable(true);
        Text piyu1 = new Text("Archivo seleccionado:");
        Text piyu2 = new Text("");
        jta1.setText("Aqui se mostrara el contenido del archivo seleccionado");
        jta1.setEditable(false);
        Image nombrexd = new Image(getClass().getResourceAsStream("/sample/Imagen1.PNG"));
        ImageView imageView = new ImageView(nombrexd);
        imageView.setFitWidth(300);
        imageView.setFitHeight(80);
        VBox layout = new VBox(8);
        layout.getChildren().addAll(imageView, piyu1, piyu2, jta1,b1,b2, b3);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(1,5,1,5));
        Image imageMenu = new Image(getClass().getResourceAsStream("/sample/Imagen3.png"));
        // new BackgroundSize(width, height, widthAsPercentage, heightAsPercentage, contain, cover)
        BackgroundSize backgroundSize = new BackgroundSize(400, 400, true, true, true, false);
        // new BackgroundImage(image, repeatX, repeatY, position, size)
        BackgroundImage backgroundImage = new BackgroundImage(imageMenu, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
        // new Background(images...)
        layout.setBackground(new Background(backgroundImage));
        contenido.setScene(new Scene(layout,550,550));
        contenido.setX(400);
        contenido.setY(50);
        //contenido.initStyle(StageStyle.TRANSPARENT);
        contenido.show();
        b1.setOnAction(event -> {
            jta1.clear();
            selectorArchivos = new JFileChooser();
            selectorArchivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de texto", "txt");
            selectorArchivos.setFileFilter(filtro);
            selectorArchivos.showOpenDialog(selectorArchivos);
            archivo = selectorArchivos.getSelectedFile();
            //System.out.println(archivo.getName());
            // File archivo = new File("C:/Users/Marlene/Desktop/idk.xml");
            piyu2.setText(archivo.getName());
            mostrarContenidoTextArea(archivo,jta1);
            b2.setDisable(false);
        }
        );
        b3.setOnAction(event -> {jta1.clear(); b2.setDisable(true);});
        b2.setOnAction(event -> {
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


    public static void main(String[] args) {
        launch(args);
    }
}
