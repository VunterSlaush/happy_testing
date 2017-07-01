package mota.dev.happytesting.models;


import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * Created by Slaush on 22/05/2017.
 */

public class Observation extends RealmObject
{
    private String text;
    private RealmList<Image> images;
    private int id;
    private String reportName;

    @PrimaryKey
    private String localId;

    public Observation()
    {
        id = -1;
        this.images = new RealmList<>();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {

        for (Image i: images)
        {
            i.setObservationId(localId);
            if(!this.images.contains(i))
                this.images.add(i);
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void removeImages(List<Image> selected)
    {
        int index;
        for (int i = 0; i < selected.size(); i++)
        {
            index = images.indexOf(selected.get(i));
            images.remove(index);
        }
    }

    public String getReportName()
    {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Observation)) return false;

        Observation that = (Observation) o;

        if(id != -1 && id == that.id) return true;

        if(text.equals(that.text) && images.size() == that.images.size())
        {
            for (Image i : images)
            {
                if(!that.images.contains(i))
                    return false;
            }
            return true;
        }
        return false;
    }

    public String getLocalId() {
        return localId;
    }

    public void copy(Observation o)
    {
        this.images.addAll(o.getImages());
        this.localId = o.localId;
        this.text = o.getText();
        this.id = o.id;
    }
}
