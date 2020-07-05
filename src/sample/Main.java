package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Main extends Application {

    Button buttonTXTtoXML, buttonTXTtoJSON;
    Button b1;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setX(110);
        primaryStage.setY(50);
        primaryStage.setScene(new Scene(root, 400, 375));
        primaryStage.show();
        Stage contenido = new Stage();
        TextArea jta1 = new TextArea();
        jta1.setPrefSize(440,300);
        //jta1.getId(idk);
        b1 = new Button("Mostrar contenido");

        VBox layout = new VBox(5);
        layout.getChildren().addAll(jta1,b1);
        contenido.setScene(new Scene(layout,450,350));
        contenido.setX(600);
        contenido.setY(50);
        contenido.show();
        b1.setOnAction(event -> {
                File archivo = new File("C:/Users/Marlene/Desktop/idk.xml");
        try {
            BufferedReader leer = new BufferedReader(new FileReader(archivo));
            String linea = leer.readLine();
            while (linea != null){
                jta1.appendText(linea+"\n");
                linea = leer.readLine();
            }
        }catch (Exception e){
            //Logger.getLogger()
        }}
        );
    }


    public static void main(String[] args) {
        launch(args);
    }
}
