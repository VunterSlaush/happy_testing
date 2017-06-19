package mota.dev.happytesting.repositories.implementations;

import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import mota.dev.happytesting.MyApplication;
import mota.dev.happytesting.models.Image;
import mota.dev.happytesting.repositories.ImageRepository;

/**
 * Created by Slaush on 18/06/2017.
 */

public class ImagenAndroidImplementation implements ImageRepository {
    @Override
    public Observable<List<Image>> getAll() {
        return new Observable<List<Image>>() {
            @Override
            protected void subscribeActual(Observer<? super List<Image>> observer) {
                observer.onNext(getGalleryImages());
                observer.onComplete();
            }
        };
    }

    @Override
    public Observable<List<Image>> getObservationImages(int observationId) {
        return null;
    }

    private Cursor generateGalleryCursor() {
        final String orderBy = MediaStore.Images.Media._ID;
        Cursor cursor = MyApplication.getInstance().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, orderBy);
        if (cursor == null)
            Log.d(getClass().getName(), "CURSOR NULO!");
        return cursor;

    }

    private List<Image> getGalleryImages()
    {
        Cursor cursor = generateGalleryCursor();
        cursor.moveToFirst();
        int count = cursor.getCount();
        ArrayList<Image> images = new ArrayList<>();

        for (int i = 0; i < count; i++)
        {
            cursor.moveToPosition(i);
            Image im = new Image();
            im.setDir(cursor.getString(3));
            images.add(im);
        }
        return images;
    }
}