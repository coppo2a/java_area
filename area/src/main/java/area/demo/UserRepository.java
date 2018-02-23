package area.demo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    public List<User> findByLogin(String login);
    public List<User> findByPassword(String password);
    public User findByPasswordAndAndLogin(String password, String login);
}