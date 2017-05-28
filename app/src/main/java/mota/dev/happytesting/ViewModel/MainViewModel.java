package mota.dev.happytesting.ViewModel;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.Observable;

import mota.dev.happytesting.R;
import mota.dev.happytesting.Views.fragments.AccountFragment;
import mota.dev.happytesting.Views.fragments.AppsFragment;
import mota.dev.happytesting.Views.fragments.CreateAppFragment;

/**
 * Created by Slaush on 28/05/2017.
 */

public class MainViewModel extends Observable // TODO Refactorizar esto?
{
    private Context context;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private FragmentManager fragmentManager;
    public MainViewModel(Context context)
    {
        this.context = context;

        toolbar = (Toolbar) ((Activity)context).findViewById(R.id.toolbar);
        ((AppCompatActivity)context).setSupportActionBar(toolbar);
        mDrawer = (DrawerLayout) ((Activity)context).findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) ((Activity)context).findViewById(R.id.nvView);
        fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        setupDrawerContent(nvDrawer);

        changeFragment(AppsFragment.TAG,AppsFragment.class);
    }


    public void setupDrawerContent(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem)
    {

        Class fragmentClass;
        String tag;
        switch(menuItem.getItemId())
        {
            case R.id.apps:
                fragmentClass = AppsFragment.class;
                tag = AppsFragment.TAG;
                break;
            case R.id.create_app:
                fragmentClass = CreateAppFragment.class;
                tag = CreateAppFragment.TAG;
                break;
            case R.id.my_account:
                fragmentClass = AccountFragment.class;
                tag = AccountFragment.TAG;
                break;
            /*case R.id.sign_out:
                // Aqui Cierro Session !
                break;*/
            default:
                fragmentClass = AppsFragment.class;
                tag = AppsFragment.TAG;
        }
        changeFragment(tag,fragmentClass);
        menuItem.setChecked(true);
        // Set action bar title
        ((AppCompatActivity)context).setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }


    public void onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                break;
        }

    }

    public void changeFragment(String tag, Class fragmentClass)
    {
        try
        {
            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            if(fragment == null)
                fragment = (Fragment) fragmentClass.newInstance();
            fragmentManager.beginTransaction().replace(R.id.content, fragment,tag).commit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
