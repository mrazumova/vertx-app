package app;

import java.util.HashMap;
import java.util.Map;

public class UserStorage {

    private Map<Integer, User> userList = new HashMap<>();

    UserStorage(){
        userList.put(1, new User(1, "firstUser", 18));
        userList.put(2, new User(2, "secondUser", 39));
        userList.put(3, new User(3, "thirdUser", 43));
        userList.put(4, new User(4, "fourthUser", 13));
        userList.put(5, new User(5, "fifthUser", 16));
        userList.put(6, new User(6, "fiveUser", 18));
        userList.put(7, new User(7, "seventhUser", 28));
        userList.put(8, new User(8, "eighthUser", 19));
        userList.put(9, new User(9, "ninthUser", 31));
        userList.put(10, new User(10, "tenthUser", 74));
    }

    public void create(int id, String nick, int age) {
        User user = new User(id, nick, age);
        userList.put(id, user);
    }

    public User read(int id){
        return userList.get(id);
    }

    public void update(int id, String nick) {
        User user = userList.get(id);
        user.setNickname(nick);
    }

    public void update(int id, int age) {
        User user = userList.get(id);
        user.setAge(age);
    }
    public void update(int id, String nick, int age) {
        User user = userList.get(id);
        user.setNickname(nick);
        user.setAge(age);
    }

    public void delete(int id) {
        userList.remove(id);
    }
}
