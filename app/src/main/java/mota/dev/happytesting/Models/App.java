package mota.dev.happytesting.models;

import java.util.Collection;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Slaush on 22/05/2017.
 */

public class App extends RealmObject
{
    @PrimaryKey
    private String name;

    private int id;
    private RealmList<Report> reports;

    private RealmList<User> modificar;

    public App()
    {
        id = -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RealmList<Report> getReports() {
        return reports;
    }

    public void setReports(RealmList<Report> reports) {
        this.reports = reports;
    }

    @Override
    public String toString() {
        return "App{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", reports=" + reports +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass()) return false;

        App app = (App) o;

        return name.equals(app.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public void setUsers(List<User> users)
    {
        if(modificar == null)
            modificar = new RealmList<>();

        modificar.addAll(users);
    }
}
