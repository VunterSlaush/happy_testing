package mota.dev.happytesting.ViewModel.items;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.Observable;

import mota.dev.happytesting.Views.activities.GalleryActivity;
import mota.dev.happytesting.Views.interfaces.Selectable;
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
    private Selectable<Image> selectable;

    public ItemObservationViewModel(Context context, Selectable<Image> selectable)
    {
        this.context = context;
        this.text = new ObservableField<>();
        this.selectable = selectable;
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
        List<Image> selected = selectable.getSelected();
        observation.removeImages(selected);
        setChanged();
        notifyObservers();
    }

    public void seleccionarImagenes(View view)
    {
        Intent i = new Intent(context, GalleryActivity.class);
        ((Activity)context).startActivityForResult(i,1234);
    }

    public void onActivityResult(Intent result)
    {
        Log.d("MOTA--->","result:"+result.getStringExtra("data"));
    }
}
