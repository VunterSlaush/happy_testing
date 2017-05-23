package mota.dev.happytesting.Views.Interfaces;

import java.util.Observable;

/**
 * Created by Slaush on 22/05/2017.
 */

public interface BindeableView
{
    void initDataBinding();
    void setupObserver(Observable observable);

}
