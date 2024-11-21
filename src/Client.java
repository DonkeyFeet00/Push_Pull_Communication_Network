import javax.swing.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            JFrame frame = new JFrame("My First GUI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300,300);


            JButton button1 = new JButton("Button 1");
            JButton button2 = new JButton("Button 2");
            JTextField text1 = new JTextField(10);


            frame.getContentPane().add(button1);
            frame.getContentPane().add(button2);
            frame.getContentPane().add(text1);
            frame.setVisible(true);

            // Create client socket to connect to certain server (Server IP, Port address)
            // we use either "localhost" or "127.0.0.1" if the server runs on the same device as the client
            Socket mySocket = new Socket("127.0.0.1", 6666);


            // to interact (send data / read incoming data) with the server, we need to create the following:

            //DataOutputStream object to send data through the socket
            DataOutputStream outStream = new DataOutputStream(mySocket.getOutputStream());

            // BufferReader object to read data coming from the server through the socket
            BufferedReader inStream = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));


            String statement = "";
            Scanner in = new Scanner(System.in);

            while(!statement.equals("exit")) {

                statement = in.nextLine();  			// read user input from the terminal data to the server

                outStream.writeBytes(statement+"\n");		// send such input data to the server


                String str = inStream.readLine();     	// receive response from server

                System.out.println(str);                // print this response

            }

            System.out.println("Closing the connection and the sockets");

            // close connection.
            outStream.close();
            inStream.close();
            mySocket.close();

        } catch (Exception exc) {
            System.out.println("Error is : " + exc.toString());

        }
    }

}
