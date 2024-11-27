import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;


public class Client extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        try {
            //open a socket with the server
            Socket mySocket = new Socket("127.0.0.1", 6666);

            //create buffered reader and output stream
            DataOutputStream outStream = new DataOutputStream(mySocket.getOutputStream());
            BufferedReader inStream = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));


            // Create a ComboBox (dropdown menu)
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.getItems().addAll("Add", "Remove", "Clear", "Get_Summation", "Get_Minimum", "Get_Maximum", "Display_Content", "Exit");
            comboBox.getSelectionModel().selectFirst();

            // Create a TextField
            TextField textField = new TextField();
            textField.setPromptText("Enter number here");

            // Create a Button (submit button)
            Button submitButton = new Button("Submit");


            // Set an action for the button
            submitButton.setOnAction(event -> {
                String selectedCommand = comboBox.getValue();
                String enteredText = textField.getText();
                try {
                    if (selectedCommand.equals("Add") || selectedCommand.equals("Remove")) {
                        outStream.writeBytes("Sender: User_A; Receiver; Server_A; Payload: " + selectedCommand + " " + enteredText + "\n");
                        System.out.println(inStream.readLine());
                    } else if (selectedCommand.equals("Exit")) {
                        // close connection.
                        System.out.println("Closing the connection and the sockets");
                        outStream.close();
                        inStream.close();
                        mySocket.close();
                        primaryStage.close();
                    } else {
                        outStream.writeBytes("Sender: User_A; Receiver; Server_A; Payload: " + selectedCommand + "\n");
                        System.out.println(inStream.readLine());
                    }
                } catch (Exception exc){
                    System.out.println("Error is : " + exc.toString());
                }
            });

            // Layout
            VBox layout = new VBox(10, comboBox, textField, submitButton);
            Scene scene = new Scene(layout, 500, 200);

            // Set up the stage
            primaryStage.setTitle("Client");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception exc) {
            System.out.println("Error is : " + exc.toString());
        }
    }

}
