package ac.rs.singidunum.chatclient.messaging.dtos;

import java.util.List;

public class ActiveUsers {
    private List<String> users;

    public ActiveUsers(List<String> users) {
        this.users = users;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "ActiveUsers{" +
                "users=" + users +
                '}';
    }
}
