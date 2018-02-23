package area.demo;

import org.springframework.data.annotation.Id;

public class User {
    @Id
    public String id;

    public String login;
    public String password;

    public User() {}

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%s, login='%s', password='%s']",
                id, login, password);
    }
}
