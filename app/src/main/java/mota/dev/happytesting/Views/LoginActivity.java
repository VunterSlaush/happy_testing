package mota.dev.happytesting.Views;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Observable;
import java.util.Observer;

import mota.dev.happytesting.R;
import mota.dev.happytesting.ViewModel.LoginViewModel;
import mota.dev.happytesting.Views.Interfaces.BindeableView;
import mota.dev.happytesting.databinding.LoginActivityBinding;

/**
 * Created by Slaush on 22/05/2017.
 */

public class LoginActivity extends Activity implements Observer, BindeableView
{
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initDataBinding();
        setupObserver(loginViewModel);
    }

    @Override public void initDataBinding()
    {
        LoginActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.login_activity);
        loginViewModel = new LoginViewModel(this);
        binding.setViewModel(loginViewModel);
    }

    @Override public void setupObserver(Observable observable)
    {
        observable.addObserver(this);
    }


    @Override public void update(Observable observable, Object o)
    {
        if(observable instanceof LoginViewModel)
        {
            LoginViewModel viewModel = (LoginViewModel) observable;
            Log.d(getClass().getName(),"UPDATE Object:"+o);
        }
    }
}
