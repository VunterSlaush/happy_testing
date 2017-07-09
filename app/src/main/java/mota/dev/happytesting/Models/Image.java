package mota.dev.happytesting.models;

import android.util.Log;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

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

        if(image.key != null && key != null && key.equals(image.key)) return true;


        if(dir.equals(image.dir))
        {
            return true;
        }
        else
        {
            String str1 = FilenameUtils.getName(dir);
            String str2 = FilenameUtils.getName(image.dir);
            Log.d("MOTA COMP", str1 + " VS " + str2);
            return str1.equals(str2) ;
        }

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
