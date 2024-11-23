import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    static ArrayList<User> userArrayList = new ArrayList<User>();


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

                //figure out what command the message uses and call that method
                if (incomingCommand.matches("NewUser")) {
                    ps.println(newClient(incomingCommand.substring(8)));
                }
                else if (incomingCommand.matches("Push")) {
                    ps.println(push(incomingCommand.substring(5)));
                }
                else if (incomingCommand.matches("Pull")) {
                    ps.println(pull(incomingCommand.substring(5)));
                }
                else if (incomingCommand.matches("DeleteMessages")) {
                    ps.println(deleteMessages(incomingCommand.substring(15)));
                }
                else if (incomingCommand.matches("KnowOthers")) {
                    ps.println(knowOthers());
                }
                else {
                    ps.println("Command not recognized");
                }
            }

            //if the code gets to this point it means the user typed exit
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


    public static String newClient(String userName) {
        //check if a user already exists with this name
        for (int i = 0; i < userArrayList.size(); i++) {
            if (userArrayList.get(i).getUserName() == userName) {
                return "“This username already existed";
            }
        }
        //if no user exists, create one
        User newUser = new User(userName);
        userArrayList.add(newUser);
        return "welcome to our communication server, you are added as a new client";
    }

    public static String pull(String userName) {
        //search for the user in userArrayList
        for (int i = 0; i < userArrayList.size(); i++) {
            if (userArrayList.get(i).getUserName() == userName) {
                //put all their messages into a String
                ArrayList messages = userArrayList.get(i).getMessages();
                String returnVal = "";
                for (int j = 0; i < messages.size(); i++) {
                     returnVal += messages.get(j).toString();
                }
                return returnVal;
            }
        }

        //if the code makes it to here, then no username existed that matches
        return "This username does not exist";
    }

    /*TODO*/
    public static String push(String command) {
        return "";
    }


    public static String deleteMessages(String userName) {
        //search for the user in userArrayList
        for (int i = 0; i < userArrayList.size(); i++) {
            if (userArrayList.get(i).getUserName() == userName) {
                userArrayList.get(i).clearMessages();
                return "Messages Deleted";
            }
        }
        return "This username does not exist";
    }


    public static String knowOthers() {
        String returnVal = "";
        for (int i = 0; i < userArrayList.size(); i++) {
            returnVal += userArrayList.get(i).getUserName() + "\n";
        }


        if (returnVal == "") {
            return "No current users exist";
        }
        else {
            return returnVal;
        }
    }

}