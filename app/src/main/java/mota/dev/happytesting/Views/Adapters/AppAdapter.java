package mota.dev.happytesting.Views.adapters;

import android.database.DataSetObserver;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import mota.dev.happytesting.R;
import mota.dev.happytesting.ViewModel.ItemAppViewModel;
import mota.dev.happytesting.databinding.AppItemBinding;
import mota.dev.happytesting.models.App;

/**
 * Created by Slaush on 30/05/2017.
 */

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

    private List<App> appList;

    public AppAdapter()
    {
        appList = Collections.emptyList();
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        AppItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.app_item,
                parent, false);
        return new AppViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position)
    {
        Log.d("APPS","BINDEANDO:"+appList.get(position));
        holder.bindApp(appList.get(position));
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public void setAppList(List<App> apps)
    {
        appList = apps;
        notifyDataSetChanged();
    }

    public class AppViewHolder extends RecyclerView.ViewHolder
    {
        AppItemBinding binding;

        public AppViewHolder(AppItemBinding binding) {
            super(binding.itemApp);
            this.binding = binding;
        }

        public void bindApp(App app)
        {
            if(binding.getViewModel() == null)
                binding.setViewModel(new ItemAppViewModel(app, itemView.getContext()));
            else
                binding.getViewModel().setApp(app);
        }
    }
}
