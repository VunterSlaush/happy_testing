package mota.dev.happytesting.models;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Slaush on 22/05/2017.
 */

public class Report extends RealmObject
{
    private int id;
    private String name;
    private String appName;
    private String creado;
    private RealmList<Observation> observations;

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
        if(this.observations == null)
            this.observations = new RealmList<>();
        this.observations.addAll(observations);
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
