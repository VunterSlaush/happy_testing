package mota.dev.happytesting.useCases;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.managers.RequestManager;
import mota.dev.happytesting.models.Report;

/**
 * Created by Slaush on 08/07/2017.
 */

public class PublishReport
{
    private static  PublishReport instance;
    private PublishReport(){}
    public static PublishReport getInstance()
    {
        if(instance == null)
            instance = new PublishReport();
        return instance;
    }


    public Observable<String> publicarReporte(final Report reporte)
    {
        return new Observable<String>() {
            @Override
            protected void subscribeActual(final Observer<? super String> observer)
            {
                final JSONObject obj = new JSONObject();
                try {
                    obj.put("reporte", reporte.getId());
                }catch (Exception e) {}
                RequestManager.getInstance().publishReport(obj).subscribe(new Consumer<JSONObject>() {
                    @Override
                    public void accept(@NonNull JSONObject jsonObject) throws Exception
                    {
                        if(jsonObject.optBoolean("success"))
                        {
                            observer.onNext(jsonObject.optString("link"));
                            observer.onComplete();
                        }
                        else
                        {
                            observer.onError(new Throwable("No se pudo publicar el reporte"));
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception
                    {
                        observer.onError(new Throwable("No se pudo publicar el reporte"));
                    }
                });

            }
        };

    }
}
