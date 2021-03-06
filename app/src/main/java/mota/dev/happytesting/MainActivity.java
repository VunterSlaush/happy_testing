package mota.dev.happytesting;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import mota.dev.happytesting.ViewModel.MainViewModel;
import mota.dev.happytesting.Views.interfaces.FragmentInteractionListener;
import mota.dev.happytesting.managers.UserManager;


public class MainActivity extends AppCompatActivity implements FragmentInteractionListener {


    private MainViewModel viewModel;


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

    @Override
    public void onBackPressed() {
        if(viewModel.onMainFragment())
            super.onBackPressed();
        else
            viewModel.goToMainFragment();
    }
}
