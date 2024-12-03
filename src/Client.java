//Patrick Olszewski and Jenny McDonnell

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.io.*;
import java.net.Socket;
public class Client extends Application {

//____________Server address and port__________________
    final String SERVER_ADDRESS = "127.0.0.1";
    final int SERVER_PORT = 6666;


    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        try {
            Font font = new Font(18);

            //box for server responses and error output
            TextArea textFromServer = new TextArea();
            textFromServer.setFont(font);
            textFromServer.setEditable(false);
            textFromServer.appendText(
                    "Accepted Commands:" +
                    "\nNewClient (username)" +
                    "\nPush (username), {receivers}, msg" +
                    "\nPull (username)" +
                    "\nDeleteMessages (username)" +
                    "\nKnowOthers" +
                    "\nExit\n");

            // Create a TextField for user to type commands in
            TextField textField = new TextField();
            textField.setFont(font);
            textField.setPromptText("Enter commands here");

            // Create a Button (submit button)
            Button submitButton = new Button("Submit");
            submitButton.setDefaultButton(true);


            // Set an action for the button
            submitButton.setOnAction(event -> {
                try {
                    //open a socket with the server
                    Socket mySocket = new Socket(SERVER_ADDRESS, SERVER_PORT);

                    //create buffered reader and output stream
                    DataOutputStream outStream = new DataOutputStream(mySocket.getOutputStream());
                    BufferedReader inStream = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));

                    //this block sends the text to the server and the server decides whether the command is valid
                    String enteredText = textField.getText();
                    if (!enteredText.equals("Exit")) {
                        outStream.writeBytes(enteredText + "\n");
                        textField.setText("");
                        textFromServer.appendText(inStream.readLine() + "\n");
                    }

                    // close connection.
                    System.out.println("Closing the connection and the sockets");
                    outStream.close();
                    inStream.close();
                    mySocket.close();

                    //also close the window if the user typed exit
                    if (enteredText.equals("Exit"))
                        primaryStage.close();

                } catch(Exception e) {
                    textFromServer.appendText("Error: " + e + "\n");
                }
            });

            // Layout
            VBox layout = new VBox(10, textFromServer, textField, submitButton);
            Scene scene = new Scene(layout, 800, 500);

            // Set up the stage
            primaryStage.setTitle("Client");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception exc) {
            System.out.println("Error is : " + exc.toString());
        }
    }
}