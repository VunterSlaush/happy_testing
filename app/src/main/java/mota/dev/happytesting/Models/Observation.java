package mota.dev.happytesting.models;

import java.util.Collection;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Slaush on 22/05/2017.
 */

public class Observation extends RealmObject
{
    private String text;
    private RealmList<Image> images;
    private int id;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public RealmList<Image> getImages() {
        return images;
    }

    public void setImages(RealmList<Image> images) {
        this.images = images;
    }

    public void setImages(List<Image> images) {
        if(this.images == null)
            this.images = new RealmList<>();
        this.images.addAll(images);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
