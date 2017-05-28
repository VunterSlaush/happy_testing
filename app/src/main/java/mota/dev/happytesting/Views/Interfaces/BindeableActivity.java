package mota.dev.happytesting.Views.interfaces;

import java.util.Observable;

/**
 * Created by Slaush on 22/05/2017.
 */

public interface BindeableActivity
{
    void initDataBinding();
    void setupObserver(Observable observable);

}
