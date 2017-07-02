package mota.dev.happytesting.useCases;



import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.models.Image;
import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.repositories.ObservationRepository;
import mota.dev.happytesting.repositories.implementations.ImageLocalImplementation;
import mota.dev.happytesting.repositories.implementations.ObservationLocalImplementation;

/**
 * Created by Slaush on 26/06/2017.
 */

public class AddImages
{
    public Observable<Observation> addImages(final List<Image> images, final String id)
    {

        return new Observable<Observation>() {
            @Override
            protected void subscribeActual(final Observer<? super Observation> observer)
            {
                final ObservationRepository repo = new ObservationLocalImplementation();
                repo.get(id).subscribe(new Consumer<Observation>() {
                    @Override
                    public void accept(@NonNull Observation observation) throws Exception
                    {
                        final Observation o = new Observation();
                        o.copy(observation);
                        ImageLocalImplementation
                                .getInstance()
                                .createListOfImagesForObservation(images,observation.getLocalId())
                                .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean result) throws Exception
                            {
                                if(result)
                                {
                                    o.setImages(images);
                                    observer.onNext(o);
                                    observer.onComplete();
                                }
                                else
                                    observer.onError(new Throwable("No se pudieron añadir las imagenes"));
                            }
                        });

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e("MOTA--->","Except>"+throwable.getMessage());
                        observer.onError(new Throwable("No se pudieron añadir las imagenes"));
                    }
                });
            }
        };
    }
}
