package mota.dev.happytesting.models;

import android.util.Log;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Slaush on 22/05/2017.
 */

public class Report extends RealmObject
{
    private int id;
    @PrimaryKey
    private String key; // es un Compound Key

    private String owner_id;
    private String name;
    private String appName;
    private String creado;
    private String username;
    private RealmList<Observation> observations;

    public Report()
    {
        name = null;
        id = -1;
        observations = new RealmList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreado() {
        return creado;
    }

    public void setCreado(String creado) {
        this.creado = creado;
    }

    public RealmList<Observation> getObservations() {
        return observations;
    }

    public void setObservations(RealmList<Observation> observations) {
        this.observations = observations;
    }

    public void setObservations(List<Observation> observations)
    {
        Log.d("MOTA--->","Add Observations:");
        for (Observation o : observations)
        {
            try
            {
                if (!this.observations.contains(o))
                    this.observations.add(o);
            }catch (Exception e)
            {
                Log.d("MOTA--->","Exception:"+e);
            }

        }
        Log.d("MOTA--->", "Added:"+this.observations.size());
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void copy(Report report)
    {
        this.id = report.id;
        this.name = report.name;
        this.owner_id = report.owner_id;
        this.username = report.username;
        this.creado = report.creado;
        this.appName = report.appName;
        this.observations.addAll(report.getObservations());
    }

    public void copyAll(Report report)
    {
        this.copy(report);
        this.key = report.key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String user_name) {
        this.username = user_name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Report)) return false;

        Report report = (Report) o;
        return key != null && report.key != null && report.key.equals(key) ||
                report.id != -1 && id == report.id ||
                report.name.equals(name) && report.appName.equals(appName);
    }

    public void fillEmptyFields(Report report) { // TODO add more if needed?
        if(id == -1)
            id = report.id;
        setObservations(report.observations);
        if(key == null)
            key = report.key;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", owner_id='" + owner_id + '\'' +
                ", name='" + name + '\'' +
                ", appName='" + appName + '\'' +
                ", creado='" + creado + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public void addObservation(Observation observation)
    {
        observations.add(observation);
    }
}
