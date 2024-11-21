import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {

            // Create server Socket that listens/bonds to port/endpoint address 6666 (any port id of your choice, should be >=1024, as other port addresses are reserved for system use)
            ServerSocket mySocket = new ServerSocket(6666);
            System.out.println("Startup the server side over port 6666 ....");

            // use the created ServerSocket and accept() to start listening for incoming client requests targeting this server and this port
            Socket connectedClient = mySocket.accept();

            // reaching this point means that a client established a connection with your server and this particular port.
            System.out.println("Connection established");


            // BufferReader
            BufferedReader br = new BufferedReader(new InputStreamReader(connectedClient.getInputStream()));

            // PrintStream
            PrintStream ps = new PrintStream(connectedClient.getOutputStream());


            // Let's keep reading data from the client, as long as the client doesn't send "exit".
            String incomingCommand = "";
            while (incomingCommand != "exit") {
                incomingCommand = br.readLine();
                System.out.println("message received: " + incomingCommand);   //print the incoming data from the client


            }

            System.out.println("Closing the connection and the sockets");
            // close
            ps.close();
            br.close();
            mySocket.close();
            connectedClient.close();

        } catch (Exception exc) {
            System.out.println("Error :" + exc.toString());
        }
    }
}