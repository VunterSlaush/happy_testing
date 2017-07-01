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
import mota.dev.happytesting.repositories.implementations.ObservationLocalImplementation;

/**
 * Created by Slaush on 26/06/2017.
 */

public class AddImages
{
    public Observable<Boolean> addImages(final List<Image> images, final String id)
    {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(final Observer<? super Boolean> observer)
            {
                final ObservationRepository repo = new ObservationLocalImplementation();
                repo.get(id).subscribe(new Consumer<Observation>() {
                    @Override
                    public void accept(@NonNull Observation observation) throws Exception
                    {
                        observation.setImages(images);
                        repo.modify(observation).subscribe(new Consumer<Observation>() {
                            @Override
                            public void accept(@NonNull Observation observation) throws Exception {
                                observer.onNext(true);
                                observer.onComplete();
                            }
                        });

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e("MOTA--->","Except>"+throwable.getMessage());
                        observer.onNext(false);
                        observer.onComplete();
                    }
                });
            }
        };
    }
}
