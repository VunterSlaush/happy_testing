package mota.dev.happytesting.models;


import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created by Slaush on 22/05/2017.
 */

public class User extends RealmObject {

    private String name;
    private String username;
    private String password;
    private int id;

    public String getName() {
        return name != null ? name : "" ;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
