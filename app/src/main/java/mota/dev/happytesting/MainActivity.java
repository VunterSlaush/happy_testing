package mota.dev.happytesting;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import mota.dev.happytesting.ViewModel.MainViewModel;
import mota.dev.happytesting.Views.interfaces.FragmentInteractionListener;


public class MainActivity extends AppCompatActivity implements FragmentInteractionListener {


    private MainViewModel viewModel;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new MainViewModel(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        viewModel.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
