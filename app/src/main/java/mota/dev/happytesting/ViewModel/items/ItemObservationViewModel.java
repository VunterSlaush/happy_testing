package mota.dev.happytesting.ViewModel.items;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mota.dev.happytesting.Views.activities.GalleryActivity;
import mota.dev.happytesting.Views.interfaces.Selectable;
import mota.dev.happytesting.models.Image;
import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.Views.dialogs.SimpleInputDialog;
import mota.dev.happytesting.repositories.implementations.ObservationLocalImplementation;
import mota.dev.happytesting.useCases.DeleteObservation;
import mota.dev.happytesting.utils.Functions;

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
        new SimpleInputDialog(context, "Inserta tu Observacion", new SimpleInputDialog.OnGetText() {
            @Override
            public void get(String textResult)
            {
                text.set(textResult);
                observation.setText(textResult);
                new ObservationLocalImplementation().modify(observation)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }
        });
    }

    public void eliminar(View view)
    {
        Functions.showConfirmDialog(context, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                new DeleteObservation().delete(observation)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean result) throws Exception
                            {
                                //TODO TOTAL!
                                if (result)
                                    Toast.makeText(context,"Eliminado Satisfactoriamente",Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(context,"No pudo ser eliminado", Toast.LENGTH_SHORT).show();

                                setChanged();
                                notifyObservers();
                            }
                        });
            }
        });
    }

    public void removerImagenes(View view)
    {
        List<Image> selected = selectable.getSelected();
        observation.removeImages(selected);
        new ObservationLocalImplementation()
                .modify(observation)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Observation>() {
            @Override
            public void accept(@NonNull Observation observation) throws Exception
            {
                setChanged();
                notifyObservers();
            }
        });

    }

    public void seleccionarImagenes(View view)
    {
        Intent i = new Intent(context, GalleryActivity.class);
        i.putExtra("observation_id",observation.getLocalId());
        i.putExtra("report_name",observation.getReportName());
        ((Activity)context).startActivityForResult(i,1234); // TODO cambiar a interfaz !
    }

}
