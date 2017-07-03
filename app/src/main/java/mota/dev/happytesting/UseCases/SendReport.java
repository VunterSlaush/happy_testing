package mota.dev.happytesting.useCases;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.realm.Realm;
import io.realm.RealmResults;
import mota.dev.happytesting.MyApplication;
import mota.dev.happytesting.managers.RequestManager;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.Image;
import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.parsers.ImageParser;
import mota.dev.happytesting.parsers.ObservationParser;
import mota.dev.happytesting.utils.CustomMultipartRequest;
import mota.dev.happytesting.utils.Functions;

/**
 * Created by Slaush on 02/07/2017.
 */

public class SendReport
{

    private static SendReport instance;
    private SendReport(){}

    public static SendReport getInstance()
    {
        if(instance == null)
            instance = new SendReport();
        return instance;
    }

    public Observable<Report> send(final Report report)
    {
        return new Observable<Report>() {
            @Override
            protected void subscribeActual(final Observer<? super Report> observer)
            {
                Realm realm = Realm.getDefaultInstance();
                App app = realm.where(App.class)
                               .equalTo("name",report.getAppName())
                               .findFirst();
                List<Observation> observationList = findReportObservations(realm,report);

                if(app.getId() != -1)
                {
                    sendReportCall(app,report,observationList,observer);
                }
                else
                {
                    //TODO CREATEAPP SINGLETON Y QUE RETORNE UN OBSERVABLE!
                   //new CreateApp(MyApplication.getInstance()).createApp(app.getName(),app.getModificar());
                }

            }
        };
    }


    private void sendReportCall(final App app,
                                final Report report,
                                final List<Observation> observations,
                                final Observer<? super Report> observer)
    {
        HashMap<String, String> data = new HashMap<>();
        HashMap<String, JSONArray> arrays = new HashMap<>();
        HashMap<String, String> files = new HashMap<>();
        data.put("aplicacion",app.getId()+"");
        data.put("nombre",report.getName());
        arrays.put("observaciones",ObservationParser.getInstance().generateObservationArray(report.getObservations()));
        int imagesCount = 0;
        JSONArray imageArray = new JSONArray();
        for (int i = 0; i< observations.size(); i++)
        {
            for (Image im: observations.get(i).getImages())
            {
                files.put("file"+imagesCount,im.getDir());
                imageArray.put(ImageParser.getInstance().generateImageJsonToSend(im, i));
                imagesCount++;
            }
        }
        arrays.put("images",imageArray);
        RequestManager.getInstance().sendReport(data,files,arrays,null).subscribe(new Consumer<JSONObject>() {
            @Override
            public void accept(@NonNull JSONObject jsonObject) throws Exception {
                observer.onNext(report);
                observer.onComplete();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                observer.onError(new Throwable());
            }
        });
    }

    private List<Observation> findReportObservations(Realm realm,Report report)
    {
        RealmResults<Observation> obResult = realm.where(Observation.class)
                                                  .equalTo("reportName",report.getName())
                                                  .findAll();
        List<Observation> obs = realm.copyFromRealm(obResult);
        for(int i = 0; i< obs.size(); i++)
        {
            findImagesForObservation(realm, obs.get(i));
        }
        return obs;
    }

    private void findImagesForObservation(Realm realm, Observation o)
    {
        RealmResults<Image> results = realm.where(Image.class)
                                            .equalTo("observationName", o.getLocalId())
                                           .findAll();
        List<Image> images = realm.copyFromRealm(results);
        o.setImages(images);
    }
}
