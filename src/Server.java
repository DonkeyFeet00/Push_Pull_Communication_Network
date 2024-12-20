import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class Server {
    //port to run the server on
    static final int SERVER_PORT = 6666;

    static ArrayList<User> userArrayList = new ArrayList<User>();

    public static void main(String[] args) {
        // Create server Socket that listens/bonds to port/endpoint address 6666 (any port id of your choice, should be >=1024, as other port addresses are reserved for system use)
        while (true){
            try {
                ServerSocket mySocket = new ServerSocket(SERVER_PORT);
                System.out.println("Startup the server side over port " + SERVER_PORT + " ....");

                // use the created ServerSocket and accept() to start listening for incoming client requests targeting this server and this port
                Socket connectedClient = mySocket.accept();

                // reaching this point means that a client established a connection with your server and this particular port.
                System.out.println("Connection established");

                // BufferReader
                BufferedReader br = new BufferedReader(new InputStreamReader(connectedClient.getInputStream()));

                // PrintStream
                PrintStream ps = new PrintStream(connectedClient.getOutputStream());


                // read data from the client
                String incomingCommand = "";

                incomingCommand = br.readLine();
                System.out.println("message received: " + incomingCommand);   //print the incoming data from the client

                //figure out what command the message uses and call that method
                //uses substring to not send the command word to the method
                //ex: "NewClient Jenny" only sends "Jenny" to the function
                if (incomingCommand.matches("NewClient.+")) {
                    ps.println(newClient(incomingCommand.substring(10)));
                } else if (incomingCommand.matches("Push.+")) {
                    ps.println(push(incomingCommand.substring(5)));
                } else if (incomingCommand.matches("Pull.+")) {
                    ps.println(pull(incomingCommand.substring(5)));
                } else if (incomingCommand.matches("DeleteMessages.+")) {
                    ps.println(deleteMessages(incomingCommand.substring(15)));
                } else if (incomingCommand.matches("KnowOthers")) {
                    ps.println(knowOthers());
                } else {
                    ps.println("Command not recognized");
                }

            } catch (Exception exc) {
                System.out.println("Error :" + exc.toString());
            }
        }
    }


    public static String newClient(String userName) {
        if (userName.equals("ALL"))
            return "This username is invalid";

        //check if a user already exists with this name
        for (int i = 0; i < userArrayList.size(); i++) {
            if (Objects.equals(userArrayList.get(i).getUserName(), userName)) {
                return "“This username already exists";
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
            if (Objects.equals(userArrayList.get(i).getUserName(), userName)) {
                //put all their messages into a String
                ArrayList messages = userArrayList.get(i).getMessages();

                //append all messages into a string to be returned
                String returnVal = "Messages for "+ userName + ": ";
                for (int j = 0; j < messages.size(); j++) {
                     returnVal += messages.get(j).toString()+", ";
                }

                if (returnVal.isEmpty())
                    return "No Messages for this username.";
                return returnVal;
            }
        }
        //if the code makes it to here, then no username existed that matches
        return "This username does not exist";
    }

    public static String push(String command) {
        String returnVal = "";
        //ensure command matches the format we need
        if (command.matches("[a-zA-z0-9]+, \\{[a-zA-z0-9, ]+}, .+")){

            //split the command into usable segments
            String sender = command.substring(0, command.indexOf(","));
            String message = command.substring(command.indexOf("}") + 3);

            //make substring of receivers and split it into a string array to make it easier to work with
            String tempReceivers = command.substring(command.indexOf("{") + 1, command.indexOf("}"));
            String[] receivers = tempReceivers.split(", ");

            // check if the message is intended for all users, if so, send to all
            if (receivers[0].equals("ALL")) {
                userArrayList.forEach(user -> user.addMessage(message));
                return "the message was successfully forwarded to all receivers";
            }

            //flag so that we can return an error if user not found
            boolean foundUser = false;

            //search for each receiver in userArray. if found, add message and return success
            for (int i = 0; i < receivers.length; i++) {
                for (int j = 0; j < userArrayList.size(); j++) {
                    if (userArrayList.get(j).getUserName().equals(receivers[i])) {
                        userArrayList.get(j).addMessage(message);
                        returnVal += "Successfully forwarded to " + userArrayList.get(j).getUserName()+" ";
                        foundUser = true;
                    }
                }
                //not found
                if (!foundUser) {
                    returnVal += "failed to forward to " + receivers[i] + ". This receiver does not exist.";
                }
            }
            return returnVal;
        }
        else
            return "Error in command format. please try again";
    }

    public static String deleteMessages(String userName) {
        //search for the user in userArrayList
        for (int i = 0; i < userArrayList.size(); i++) {
            if (Objects.equals(userArrayList.get(i).getUserName(), userName)) {
                userArrayList.get(i).clearMessages();
                return "Messages Deleted";
            }
        }
        return "This username does not exist";
    }

    //For each entry in the "userArrayList", print it out using a for loop
    public static String knowOthers() {
        String returnVal = "Registered users: ";
        for (int i = 0; i < userArrayList.size(); i++) {
            returnVal += userArrayList.get(i).getUserName()+", ";
        }

        if (returnVal.equals("Registered users: "))
            return "No current users exist";
        else
            return returnVal;
    }
}