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
import android.view.View;
import android.widget.TextView;

import java.util.Observable;

import mota.dev.happytesting.MyApplication;
import mota.dev.happytesting.R;
import mota.dev.happytesting.Views.fragments.AccountFragment;
import mota.dev.happytesting.Views.fragments.AppsFragment;
import mota.dev.happytesting.Views.fragments.CreateAppFragment;
import mota.dev.happytesting.Views.fragments.MyReportsFragment;
import mota.dev.happytesting.managers.UserManager;

/**
 * Created by Slaush on 28/05/2017.
 */

public class MainViewModel extends Observable // TODO Refactorizar esto?
{
    private Context context;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private View headerNav;
    private FragmentManager fragmentManager;
    private String tag;
    public MainViewModel(Context context)
    {
        this.context = context;

        toolbar = (Toolbar) ((Activity)context).findViewById(R.id.toolbar);
        ((AppCompatActivity)context).setSupportActionBar(toolbar);
        mDrawer = (DrawerLayout) ((Activity)context).findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) ((Activity)context).findViewById(R.id.nvView);
        headerNav = nvDrawer.getHeaderView(0);
        TextView userName = (TextView)headerNav.findViewById(R.id.user_name);
        userName.setText(UserManager.getInstance().getName());
        fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        setupDrawerContent(nvDrawer);
        goToMainFragment();
    }


    private void setupDrawerContent(NavigationView navigationView)
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

    private void selectDrawerItem(MenuItem menuItem)
    {

        Class fragmentClass = null;
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
            case R.id.my_reports:
                fragmentClass = MyReportsFragment.class;
                tag = MyReportsFragment.TAG;
                break;
            case R.id.sign_out:
                MyApplication.getInstance().logout(context);
                tag = null;
                break;
            default:
                fragmentClass = AppsFragment.class;
                tag = AppsFragment.TAG;
        }

        if(tag != null)
        {
            changeFragment(tag,fragmentClass);
            menuItem.setChecked(true);
            ((AppCompatActivity)context).setTitle(menuItem.getTitle());
        }

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

    private void changeFragment(String tag, Class fragmentClass)
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

    public void goToMainFragment()
    {
       selectDrawerItem(nvDrawer.getMenu().getItem(0));
    }

    public boolean onMainFragment() {
        return tag.equals(AppsFragment.TAG);
    }
}
