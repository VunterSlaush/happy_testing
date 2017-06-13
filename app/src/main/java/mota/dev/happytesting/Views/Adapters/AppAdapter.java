package mota.dev.happytesting.Views.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mota.dev.happytesting.R;
import mota.dev.happytesting.ViewModel.items.ItemAppViewModel;
import mota.dev.happytesting.databinding.AppItemBinding;
import mota.dev.happytesting.models.App;

/**
 * Created by Slaush on 30/05/2017.
 */

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

    private List<App> appList;

    public AppAdapter()
    {
        appList = new ArrayList<>();
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
        appList.clear();
        appList.addAll(apps);
        notifyDataSetChanged();
    }


    public class AppViewHolder extends RecyclerView.ViewHolder
    {
        AppItemBinding binding;

        private AppViewHolder(AppItemBinding binding)
        {
            super(binding.itemApp);
            this.binding = binding;
        }

        private void bindApp(App app)
        {
            if(binding.getViewModel() == null)
                binding.setViewModel(new ItemAppViewModel(app, itemView.getContext()));
            else
                binding.getViewModel().setApp(app);
        }
    }
}
