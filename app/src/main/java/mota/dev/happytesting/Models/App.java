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
    private User app_owner;
    private RealmList<Report> reports;
    private RealmList<User> modificar;

    public App()
    {
        id = -1;
        modificar = new RealmList<>();
        reports = new RealmList<>();
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


    public void setReports(List<Report> reports)
    {
        RealmList<Report> r = new RealmList<>();
        r.addAll(reports);
        this.reports = r;
        changeReportsAppName();
    }

    private void changeReportsAppName()
    {
        for (Report r: reports)
                r.setAppName(name);
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
                ", mod=" + modificar +
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
        modificar.addAll(users);
    }

    public User getApp_owner() {
        return app_owner;
    }

    public void setApp_owner(User app_owner) {
        this.app_owner = app_owner;
    }

    public RealmList<User> getModificar() {
        return modificar;
    }

    public void setModificar(RealmList<User> modificar) {
        this.modificar = modificar;
    }

    public void setModificar(List<User> modificar)
    {
        RealmList<User> users = new RealmList<>();
        users.addAll(modificar);
        this.modificar = users;
    }

    public void addReport(Report report)
    {
        reports.add(report);
    }

    public void copy(App app)
    {
        this.id = app.id;
        this.name = app.name;
        this.app_owner = app.app_owner;
        this.modificar.addAll(app.modificar);
        this.reports.addAll(app.reports);
    }
}
