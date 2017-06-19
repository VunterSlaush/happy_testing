package mota.dev.happytesting.Views.adapters.viewholders;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import mota.dev.happytesting.ViewModel.items.ItemObservationViewModel;
import mota.dev.happytesting.Views.adapters.ImageAdapter;
import mota.dev.happytesting.databinding.ObservationItemBinding;
import mota.dev.happytesting.models.Observation;

/**
 * Created by Slaush on 18/06/2017.
 */


public class ObservationViewHolder extends BaseViewHolder<Observation>
{
    private ObservationItemBinding binding;

    public ObservationViewHolder(ObservationItemBinding binding)
    {
        super(binding.itemObservation);
        this.binding = binding;
    }

    public void setupAdapter()
    {
        //ImageAdapter adapter = new ImageAdapter(itemView.getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(itemView.getContext(), 3);
        binding.mosaicoRecyclerView.setLayoutManager(gridLayoutManager);
        //binding.mosaicoRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onBind()
    {
        if(binding.getViewModel() == null)
            binding.setViewModel(new ItemObservationViewModel(item, itemView.getContext()));
        else
            binding.getViewModel().setObservation(item);
    }
}
