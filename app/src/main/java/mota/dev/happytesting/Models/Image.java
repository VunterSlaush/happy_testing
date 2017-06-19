package mota.dev.happytesting.models;

import io.realm.RealmObject;

/**
 * Created by Slaush on 22/05/2017.
 */

public class Image extends RealmObject
{
    private String dir;
    private int id;

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

        return id == image.id;
    }


}
