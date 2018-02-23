package area.demo;

import area.demo.UserRepository;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.xml.crypto.Data;
import java.util.Iterator;
import java.util.List;

public class Database {

    private UserRepository repository;

    public Database(UserRepository repository)
    {
        this.repository = repository;
    }

    public void affDatabase()
    {
        List<User> allUsers = repository.findAll();
        for (User allUser : allUsers) {
            System.out.println(allUser);
        }
    }

    public void AffFindByPassword(String pass)
    {
        for (User customer : repository.findByPassword(pass)) {
            System.out.println(customer);
        }
    }

    public void affFindByLogin(String login)
    {
        for (User customer : repository.findByLogin(login)) {
            System.out.println(customer);
        }
    }

    public boolean IsUserValide(String login, String pass)
    {
        User user = repository.findByPasswordAndAndLogin(pass, login);
        return user != null;
    }

    public UserRepository getRepository() {
        return repository;
    }

    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }
}
