package mota.dev.happytesting.ViewModel.items;

import android.content.Context;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

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

    public ItemObservationViewModel(Context context)
    {
        this.context = context;
        this.text = new ObservableField<>();
    }

    private void updateObservationData(){
        Log.d("MOTA--->","Observation update images:"+observation.getImages().size());
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

    public void editarTexto(View view)
    {

    }

    public void removerImagenes(View view)
    {

    }

    public void seleccionarImagenes(View view)
    {

    }
}
