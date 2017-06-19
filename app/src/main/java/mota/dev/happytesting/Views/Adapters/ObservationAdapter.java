package mota.dev.happytesting.Views.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mota.dev.happytesting.R;
import mota.dev.happytesting.ViewModel.items.ItemObservationViewModel;
import mota.dev.happytesting.Views.adapters.viewholders.BaseViewHolder;
import mota.dev.happytesting.Views.adapters.viewholders.ObservationViewHolder;
import mota.dev.happytesting.databinding.ObservationItemBinding;
import mota.dev.happytesting.models.Observation;

/**
 * Created by Slaush on 18/06/2017.
 */


public class ObservationAdapter extends BaseRecyclerAdapter<Observation>
{
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        ObservationItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                                        R.layout.observation_item, parent, false);
        return new ObservationViewHolder(binding);
    }
}

