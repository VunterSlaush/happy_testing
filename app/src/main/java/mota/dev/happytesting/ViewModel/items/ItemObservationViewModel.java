package mota.dev.happytesting.ViewModel.items;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mota.dev.happytesting.Views.activities.GalleryActivity;
import mota.dev.happytesting.Views.interfaces.Hideable;
import mota.dev.happytesting.Views.interfaces.Selectable;
import mota.dev.happytesting.models.Image;
import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.Views.dialogs.SimpleInputDialog;
import mota.dev.happytesting.repositories.implementations.ImageLocalImplementation;
import mota.dev.happytesting.repositories.implementations.ObservationLocalImplementation;
import mota.dev.happytesting.useCases.DeleteObservation;
import mota.dev.happytesting.useCases.RemoveImages;
import mota.dev.happytesting.utils.Functions;
import mota.dev.happytesting.utils.Pnotify;

/**
 * Created by Slaush on 18/06/2017.
 */

public class ItemObservationViewModel extends Observable
{
    private static final String TAG = ItemObservationViewModel.class.getSimpleName();

    private Context context;
    private Observation observation;
    private List<Image> images;
    public ObservableField<String> text;
    private Selectable<Image> selectable;
    private Hideable hideable;

    public ItemObservationViewModel(Context context, Selectable<Image> selectable, Hideable hideable)
    {
        this.context = context;
        this.text = new ObservableField<>();
        this.selectable = selectable;
        this.hideable = hideable;
        this.observation = new Observation();
        this.images = new ArrayList<>();
    }

    private void updateObservationData(){
        this.text.set(observation.getText());
        this.images.clear();
        this.images.addAll(observation.getImages());
        setChanged();
        notifyObservers();
    }

    public void setObservation(Observation o)
    {
        this.observation.copy(o);
        fetchImagenes();
    }

    public List<Image> getImages() {
        return images;
    }

    public void editarTexto(View view)
    {
        new SimpleInputDialog(context, "Editar Observacion", text.get(), new SimpleInputDialog.OnGetText() {
            @Override
            public void get(String textResult)
            {
                text.set(textResult);
                observation.setText(textResult);
                new ObservationLocalImplementation().modify(observation)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Observation>() {
                            @Override
                            public void accept(@NonNull Observation observation) throws Exception {
                                Pnotify.makeText(context,"Actualizacion Satisfactoria",Toast.LENGTH_SHORT,Pnotify.INFO).show();
                            }
                        });
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
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean result) throws Exception
                            {

                                if (result) {
                                    Pnotify.makeText(context, "Eliminado Satisfactoriamente", Toast.LENGTH_SHORT,Pnotify.INFO).show();
                                    hideable.hide();
                                }
                                else
                                {
                                    Pnotify.makeText(context,"No pudo ser eliminado", Toast.LENGTH_SHORT,Pnotify.ERROR).show();
                                }



                            }
                        });
            }
        });
    }

    public void removerImagenes(View view)
    {
        final List<Image> selected = selectable.getSelected();

        RemoveImages.getInstance().removeImages(selected, observation)
                                  .subscribeOn(Schedulers.io())
                                  .observeOn(AndroidSchedulers.mainThread())
                                  .subscribe(new Consumer<Boolean>()
                                  {
                                    @Override
                                    public void accept(@NonNull Boolean result) throws Exception
                                    {
                                        if(result)
                                        {
                                            observation.removeImages(selected);
                                            updateObservationData();
                                        }
                                        else
                                        {
                                            Pnotify.makeText(context,"No se pudieron eliminar las imagenes", Toast.LENGTH_SHORT,Pnotify.ERROR).show();
                                        }
                                    }
                                });

    }

    private void fetchImagenes()
    {
        ImageLocalImplementation.getInstance()
                .getObservationImages(observation)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Image>>() {
                    @Override
                    public void accept(@NonNull List<Image> imgs) throws Exception
                    {
                        Log.d(TAG, "OB Images:"+observation.getImages().size() + " Imgs:"+imgs.size());
                        observation.clearImages();
                        observation.setImages(imgs);
                        updateObservationData();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        // Do Nothing??
                    }
                });
    }

    public void seleccionarImagenes(View view)
    {
        Intent i = new Intent(context, GalleryActivity.class);
        i.putExtra("observation_id",observation.getId());
        i.putExtra("observation_local_id", observation.getLocalId());
        i.putExtra("report_name",observation.getReportName());
        ((Activity)context).startActivityForResult(i,1234);
    }

}
