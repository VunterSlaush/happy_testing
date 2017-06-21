package mota.dev.happytesting.ViewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mota.dev.happytesting.Views.interfaces.Selectable;
import mota.dev.happytesting.models.Image;
import mota.dev.happytesting.useCases.GetGalleryImages;

/**
 * Created by Slaush on 18/06/2017.
 */

public class GalleryViewModel extends Observable {

    private Context context;
    private List<Image> imagenes;
    private GetGalleryImages useCase;
    private Selectable<Image> imageSelectable;
    public GalleryViewModel(Context context, Selectable<Image> selectable)
    {
        this.context = context;
        useCase = new GetGalleryImages();
        imagenes = new ArrayList<>();
        imageSelectable = selectable;
        fetchImages();
    }

    private void fetchImages()
    {
        useCase.fetchImages().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Image>>() {
                    @Override
                    public void accept(@NonNull List<Image> images) throws Exception {
                        updateImages(images);
                    }
                });
    }

    private void updateImages(List<Image> images)
    {
        imagenes.clear();
        imagenes.addAll(images);
        setChanged();
        notifyObservers();
    }

    public List<Image> getImages() {
        return imagenes;
    }

    public void select(View view)
    {
        List<Image> images = imageSelectable.getSelected();
        if(images.size() > 0)
        {
            String selectedImages = "";
            for (Image i: images)
                selectedImages+=i.getDir()+"|";
            Intent i = new Intent();
            i.putExtra("data", selectedImages);
            Activity a =(Activity) context;
            a.setResult(Activity.RESULT_OK, i);
            a.finish();
        }
        else
        {
            Toast.makeText(context,"selecciona al menos una imagen",Toast.LENGTH_SHORT).show();
        }
    }
}
