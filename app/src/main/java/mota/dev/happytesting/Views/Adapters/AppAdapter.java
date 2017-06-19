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
import mota.dev.happytesting.Views.adapters.viewholders.AppViewHolder;
import mota.dev.happytesting.Views.adapters.viewholders.BaseViewHolder;
import mota.dev.happytesting.databinding.AppItemBinding;
import mota.dev.happytesting.models.App;

/**
 * Created by Slaush on 30/05/2017.
 */

public class AppAdapter extends BaseRecyclerAdapter<App> {

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        AppItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.app_item,
                parent, false);
        return new AppViewHolder(binding);
    }

}
