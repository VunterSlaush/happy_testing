package mota.dev.happytesting.useCases;



import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.managers.RequestManager;
import mota.dev.happytesting.models.Image;
import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.parsers.ImageParser;
import mota.dev.happytesting.repositories.ObservationRepository;
import mota.dev.happytesting.repositories.implementations.ImageLocalImplementation;
import mota.dev.happytesting.repositories.implementations.ObservationLocalImplementation;

/**
 * Created by Slaush on 26/06/2017.
 */

public class AddImages
{
    private static  final String TAG = AddImages.class.getSimpleName();
    public Observable<Observation> addImages(final List<Image> images, final int id, final String localId)
    {
        Log.d(TAG,"Init");
        return new Observable<Observation>() {
            @Override
            protected void subscribeActual(final Observer<? super Observation> observer)
            {
                final ObservationRepository repo = new ObservationLocalImplementation();
                repo.get(id, localId).subscribe(new Consumer<Observation>() {
                    @Override
                    public void accept(@NonNull Observation observation) throws Exception
                    {
                        final Observation o = new Observation();
                        o.copy(observation);
                        ImageLocalImplementation
                                .getInstance()
                                .createListOfImagesForObservation(images,observation)
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(@NonNull Boolean result) throws Exception
                                    {
                                        if(result)
                                        {
                                            o.setImages(images);
                                            sendImagesToServerIfNeeded(o,images, observer);
                                        }
                                        else
                                            observer.onError(new Throwable("No se pudieron añadir las imagenes"));
                                    }
                                });

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(TAG,"Except>"+throwable.getMessage());
                        observer.onError(new Throwable("No se pudieron añadir las imagenes"));
                    }
                });
            }
        };
    }

    private void sendImagesToServerIfNeeded(Observation o, List<Image> images, Observer<? super Observation> observer)
    {
        Log.d(TAG,"sendImagesToServerIfNeeded");
        if(o.getId() != -1 )
        {
            Log.d(TAG,"Yes Is Needed");
            sendImagesToServer(o,images,observer);
        }
        else
        {
            Log.d(TAG,"Isn't Needed");
            observer.onNext(o);
            observer.onComplete();
        }
    }

    private void sendImagesToServer(final Observation o,
                                    List<Image> images,
                                    final Observer<? super Observation> observer)
    {
        Log.d(TAG,"sendImagesToServer:"+images.size());
        HashMap<String, String> data = new HashMap<>();
        HashMap<String, String> files = new HashMap<>();
        HashMap<String, JSONArray> arrays = new HashMap<>();
        int imagesCount = 0;
        JSONArray imageArray = new JSONArray();
        data.put("observacion",o.getId()+"");
        for (Image im: images)
        {
            files.put("file"+imagesCount,im.getDir());
            imageArray.put(ImageParser.getInstance().generateImageJsonToSend(im, 0));
            imagesCount++;
        }
        arrays.put("images",imageArray);

        RequestManager.getInstance().sendImages(data,files,arrays).subscribe(new Consumer<JSONObject>()
        {
            @Override
            public void accept(@NonNull JSONObject jsonObject) throws Exception
            {
                Log.d(TAG,"send Images:"+jsonObject);
                if(jsonObject.has("success") && jsonObject.optBoolean("success"))
                {
                    observer.onNext(o);
                    observer.onComplete();
                }
                else
                {
                    observer.onError(new Throwable("No se pudieron Actualizar las imagenes en el servidor"));
                }
            }
        }, new Consumer<Throwable>()
        {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception
            {
                observer.onError(new Throwable("No se pudieron Actualizar las imagenes en el servidor"));
            }
        });
    }
}
