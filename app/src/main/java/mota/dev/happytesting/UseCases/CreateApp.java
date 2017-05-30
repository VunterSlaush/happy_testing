package mota.dev.happytesting.useCases;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import mota.dev.happytesting.MainActivity;
import mota.dev.happytesting.managers.ErrorManager;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.repositories.AppRepository;
import mota.dev.happytesting.repositories.implementations.AppLocalImplementation;
import mota.dev.happytesting.repositories.implementations.AppRemoteImplementation;

/**
 * Created by Slaush on 29/05/2017.
 */

public class CreateApp
{
    private AppRepository remoteRepository, localRepository;
    private String app_name;
    private Context context;
    public CreateApp(Context context)
    {
        this.context = context;
        remoteRepository = new AppRemoteImplementation();
        localRepository = new AppLocalImplementation();
    }

    public void createApp(String name)
    {
        this.app_name = name;
        remoteRepository.create(name)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(remoteObserver);
    }


    private Observer<? super App> remoteObserver = new Observer<App>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull App app) {
            System.out.println("MODIFICANDO");
            saveAppToLocalStorage(app);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            if(!(e instanceof ErrorManager.Error))
            {
                Toast.makeText(context,"La aplicacion no pudo ser creada en el servidor", Toast.LENGTH_LONG).show();
                createAppOnLocalStorage();
            }
            else
            {
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onComplete() {

        }
    };

    private Observer<? super App> localObserver = new Observer<App>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull App app) {
            Toast.makeText(context,"Aplicacion Creada Satisfactoriamente", Toast.LENGTH_LONG).show();
            finishCreate();
            
        }

        @Override
        public void onError(@NonNull Throwable e) {
            Toast.makeText(context,"La aplicacion no pudo ser creada", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onComplete() {

        }
    };

    private void finishCreate()
    {
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
    }

    private void saveAppToLocalStorage(App app)
    {
        localRepository.modifiy(app)
                       .subscribeOn(Schedulers.io())
                       .observeOn(AndroidSchedulers.mainThread())
                       .subscribe(localObserver);
    }

    private void createAppOnLocalStorage()
    {
        localRepository.create(app_name)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(localObserver);
    }

}