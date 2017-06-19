package mota.dev.happytesting.ViewModel;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mota.dev.happytesting.models.Image;
import mota.dev.happytesting.useCases.GetGalleryImages;

/**
 * Created by Slaush on 18/06/2017.
 */

public class GalleryViewModel extends Observable {

    private Context context;
    private List<Image> imagenes;
    private GetGalleryImages useCase;
    public GalleryViewModel(Context context)
    {
        this.context = context;
        useCase = new GetGalleryImages();
        imagenes = new ArrayList<>();
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

    }
}
