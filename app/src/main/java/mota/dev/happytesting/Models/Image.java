package mota.dev.happytesting.models;

import java.io.File;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Slaush on 22/05/2017.
 */

public class Image extends RealmObject
{

    private String dir;
    private int id;
    private String observationName;

    @PrimaryKey
    private String key;

    public Image()
    {
        id = -1;
    }

    public Image(String dir)
    {
        id = -1;
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;
        if(id != -1 && id == image.id) return true;

        String str1 = dir.substring(dir.lastIndexOf("\\")+1, dir.length());
        String str2 = image.dir.substring(image.dir.lastIndexOf("\\")+1, image.dir.length());
        return str1.equals(str2) ;
    }

    public String getKey()
    {
        return key;
    }

    public void setObservationId(String observationId) {
        this.observationName = observationId;

        key = observationName + "-" + dir;
    }

    @Override
    public String toString() {
        return "Image{" +
                "dir='" + dir + '\'' +
                ", id=" + id +
                ", observationName='" + observationName + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    public void copy(Image i)
    {
        this.dir = i.dir;
        this.id = i.id;
        this.key = i.key;
        this.observationName = i.observationName;
    }
}
