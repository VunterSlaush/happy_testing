package mota.dev.happytesting.Views.activities;

import android.support.v7.app.AppCompatActivity;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Slaush on 18/06/2017.
 */

public abstract class BindeableActivity extends AppCompatActivity implements Observer
{
    /* TODO Evaluar si esto es viable ...
    protected T viewModel;
    protected Class<T> clazz;
    public void update(Observable observable, Object o)
    {
        if(clazz.isInstance(observable))
            update();
    }*/
    public abstract  void update(Observable observable, Object o);
    public abstract void initDataBinding();
    void setupObserver(Observable observable)
    {
        observable.addObserver(this);
    }
}
