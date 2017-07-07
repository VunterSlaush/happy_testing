package mota.dev.happytesting.repositories.implementations;

import android.util.Log;

import java.util.List;


import io.reactivex.Observable;
import io.reactivex.Observer;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import mota.dev.happytesting.models.Image;
import mota.dev.happytesting.repositories.ImageRepository;
import mota.dev.happytesting.repositories.RealmRepository;
import mota.dev.happytesting.utils.RealmTransactionHelper;

/**
 * Created by Slaush on 02/07/2017.
 */

public class ImageLocalImplementation extends RealmRepository<Image> implements ImageRepository {

    private static ImageLocalImplementation instance;

    private ImageLocalImplementation() {
    }

    public static ImageLocalImplementation getInstance() {
        if (instance == null)
            instance = new ImageLocalImplementation();
        return instance;
    }

    @Override
    public Observable<List<Image>> getAll() {
        return new Observable<List<Image>>() {
            @Override
            protected void subscribeActual(Observer<? super List<Image>> observer) {

            }
        };
    }

    @Override
    public Observable<List<Image>> getObservationImages(final String observationId) {
        return new Observable<List<Image>>() {
            @Override
            protected void subscribeActual(Observer<? super List<Image>> observer) {
                Realm realm = Realm.getDefaultInstance();
                List<Image> images = getObservationImages(realm, observationId);
                observer.onNext(images);
                observer.onComplete();
                realm.close();
            }
        };
    }

    private List<Image> getObservationImages(Realm realm, String observationId)
    {
        RealmResults<Image> results = getObservationImagesResult(realm,observationId);
        return realm.copyFromRealm(results);
    }

    private RealmResults<Image> getObservationImagesResult(Realm realm, String observationId)
    {
       return  realm.where(Image.class)
                .equalTo("observationName", observationId)
                .findAll();
    }

    public Observable<Boolean> createListOfImagesForObservation(final List<Image> images, final String observationId)
    {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(final Observer<? super Boolean> observer)
            {
                RealmTransactionHelper.executeTransaction(new RealmTransactionHelper.OnTransaction() {
                    @Override
                    public void action(Realm realm)
                    {
                        List<Image> oBimages = getObservationImages(realm, observationId);
                        int imageCount = oBimages.size();
                        for (Image i : images)
                        {
                            i.setObservationId(observationId);
                            if(!oBimages.contains(i))
                                oBimages.add(i);
                        }
                        if(imageCount != oBimages.size())
                        {
                            realm.copyToRealmOrUpdate(oBimages);
                            observer.onNext(true);
                            observer.onComplete();
                        }
                        else
                        {
                            observer.onNext(false);
                            observer.onComplete();
                        }

                    }

                    @Override
                    public void error(Exception e) {
                        observer.onNext(false);
                        observer.onComplete();
                    }
                });

            }
        };
    }


    public Observable<Boolean> deleteObservationImages(final List<Image> images, final String observationId)
    {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(final Observer<? super Boolean> observer) {

                RealmTransactionHelper.executeTransaction(new RealmTransactionHelper.OnTransaction() {
                    @Override
                    public void action(Realm realm)
                    {
                        for (Image i: images)
                        {
                            realm.where(Image.class)
                                    .equalTo("dir",i.getDir())
                                    .findAll()
                                    .deleteAllFromRealm();
                        }
                        observer.onNext(true);
                        observer.onComplete();
                    }

                    @Override
                    public void error(Exception e) {
                        observer.onNext(false);
                        observer.onComplete();
                    }
                });

            }
        };
    }
}
