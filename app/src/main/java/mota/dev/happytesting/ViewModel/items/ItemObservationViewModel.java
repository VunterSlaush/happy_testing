package mota.dev.happytesting.ViewModel.items;

import android.content.Context;
import android.databinding.ObservableField;

import java.util.List;
import java.util.Observable;

import mota.dev.happytesting.models.Image;
import mota.dev.happytesting.models.Observation;

/**
 * Created by Slaush on 18/06/2017.
 */

public class ItemObservationViewModel extends Observable
{
    private Context context;
    private Observation observation;
    public ObservableField<String> text;

    public ItemObservationViewModel(Observation o, Context context)
    {
        this.context = context;
        this.observation = o;
        this.text = new ObservableField<>();
        updateObservationData();
    }

    private void updateObservationData() {
        this.text.set(observation.getText());
        setChanged();
        notifyObservers();
    }

    public void setObservation(Observation o)
    {
        this.observation = o;
        updateObservationData();
    }

    public List<Image> getImages() {
        return this.observation.getImages();
    }
}
