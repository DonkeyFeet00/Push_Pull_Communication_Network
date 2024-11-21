import java.util.ArrayList;

public class User {
    private String userName;
    private ArrayList<String> messages = new ArrayList<>();

    public void user(String userName) {
        this.userName = userName;
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public void clearMessages() {
        messages.clear();
    }

    public ArrayList getMessages() {
        return messages;
    }

}
