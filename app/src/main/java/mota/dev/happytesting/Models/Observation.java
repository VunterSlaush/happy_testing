package mota.dev.happytesting.models;

import android.util.Log;

import java.util.Collection;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import mota.dev.happytesting.utils.Functions;

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
        //localId = Functions.generateRandomId();
        this.images = new RealmList<>();
    }

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

        for (Image i: images)
        {
            i.setObservationId(localId);
            Log.d("MOTA--->","SETTING IMAGE:"+i.getDir());
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

}
