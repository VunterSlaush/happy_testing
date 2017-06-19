package mota.dev.happytesting.useCases;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.models.Image;
import mota.dev.happytesting.repositories.ImageRepository;
import mota.dev.happytesting.repositories.implementations.ImagenAndroidImplementation;

/**
 * Created by Slaush on 18/06/2017.
 */

public class GetGalleryImages // TODO, hay algo mas que hacer aqui?? ..
{

    public Observable<List<Image>> fetchImages() {

        return new Observable<List<Image>>() {
            @Override
            protected void subscribeActual(final Observer<? super List<Image>> observer)
            {
                ImageRepository repo = new ImagenAndroidImplementation();
                repo.getAll().subscribe(new Consumer<List<Image>>() {
                    @Override
                    public void accept(@NonNull List<Image> images) throws Exception {
                        observer.onNext(images);
                        observer.onComplete();
                    }
                });
            }
        };
    }
}
