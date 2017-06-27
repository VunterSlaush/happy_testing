package mota.dev.happytesting.useCases;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.repositories.implementations.ObservationLocalImplementation;

/**
 * Created by Slaush on 26/06/2017.
 */

public class DeleteObservation {

    public Observable<Boolean> delete(final Observation observation)
    {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer)
            {
                    if(observation.getId() == -1)
                        deleteOnLocal(observation,observer);
                    else
                        deleteOnRemote(observation,observer);
            }
        };
    }

    private void deleteOnRemote(Observation observation, Observer<? super Boolean> observer)
    {
        // TODO
        observer.onError(new Throwable("NOT IMPLEMENTED"));
    }

    private void deleteOnLocal(Observation o, final Observer<? super Boolean> observer)
    {
        new ObservationLocalImplementation().delete(o).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean result) throws Exception {
                observer.onNext(result);
                observer.onComplete();
            }
        });
    }
}
