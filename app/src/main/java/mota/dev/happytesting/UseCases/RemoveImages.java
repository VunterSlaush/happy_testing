package mota.dev.happytesting.useCases;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.managers.RequestManager;
import mota.dev.happytesting.models.Image;
import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.repositories.implementations.ImageLocalImplementation;
import mota.dev.happytesting.utils.RxHelper;

/**
 * Created by Slaush on 08/07/2017.
 */

public class RemoveImages {
    private static RemoveImages instance;

    private RemoveImages() {
    }

    public static RemoveImages getInstance() {
        if (instance == null)
            instance = new RemoveImages();
        return instance;
    }

    public Observable<Boolean> removeImages(final List<Image> images, final Observation o) {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer) {
                if (o.getId() != 1)
                    removeImagesOnRemote(images, o, observer);
                else
                    removeImagesOnLocal(images, o, observer);
            }
        };
    }

    private void removeImagesOnLocal(List<Image> images, Observation o, final Observer<? super Boolean> observer) {
        ImageLocalImplementation.getInstance()
                .deleteObservationImages(images, o.getLocalId())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean result) throws Exception {
                        RxHelper.nextAndComplete(observer, result);
                    }
                });
    }

    private void removeImagesOnRemote(final List<Image> images,
                                      final Observation o,
                                      final Observer<? super Boolean> observer)
    {
        JSONArray array = new JSONArray();
        JSONObject jsn = new JSONObject();
        for (Image i : images)
            array.put(i.getId());
        try {
            jsn.put("observacion", o.getId());
            jsn.put("images", array);
        } catch (Exception e) {

        }

        RequestManager.getInstance().deleteImages(jsn).subscribe(
                new Consumer<JSONObject>() {
                    @Override
                    public void accept(@NonNull JSONObject jsonObject) throws Exception {
                        if (jsonObject.optBoolean("success"))
                            removeImagesOnLocal(images, o, observer);
                        else
                            RxHelper.nextAndComplete(observer, false);

                    }
                },
                RxHelper.getErrorThrowable(observer, false));
    }
}
