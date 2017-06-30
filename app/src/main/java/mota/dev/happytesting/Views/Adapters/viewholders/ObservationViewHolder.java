package mota.dev.happytesting.Views.adapters.viewholders;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import mota.dev.happytesting.ViewModel.items.ItemObservationViewModel;
import mota.dev.happytesting.Views.adapters.ImageAdapter;
import mota.dev.happytesting.Views.adapters.ObservationAdapter;
import mota.dev.happytesting.Views.interfaces.Hideable;
import mota.dev.happytesting.Views.interfaces.Selectable;
import mota.dev.happytesting.databinding.ObservationItemBinding;
import mota.dev.happytesting.models.Image;
import mota.dev.happytesting.models.Observation;

/**
 * Created by Slaush on 18/06/2017.
 */


public class ObservationViewHolder extends BaseViewHolder<Observation> implements Observer, Selectable<Image>, Hideable
{
    private ObservationItemBinding binding;

    public ObservationViewHolder(ObservationItemBinding binding)
    {
        super(binding.itemObservation);
        this.binding = binding;
        setupAdapter();
    }

    private void setupAdapter()
    {
        ImageAdapter adapter = new ImageAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(itemView.getContext(), 3);
        binding.mosaicoRecyclerView.setLayoutManager(gridLayoutManager);
        binding.mosaicoRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onBind()
    {
        if(binding.getViewModel() == null) {
            ItemObservationViewModel viewModel = new ItemObservationViewModel(itemView.getContext(),this, this);
            viewModel.addObserver(this);
            viewModel.setObservation(item);
            binding.setViewModel(viewModel);
        }else
        {
            binding.getViewModel().setObservation(item);
        }
    }

    @Override
    public void update(Observable observable, Object o)
    {
        if(observable instanceof ItemObservationViewModel)
        {
            ImageAdapter adapter = (ImageAdapter) binding.mosaicoRecyclerView.getAdapter();
            ItemObservationViewModel viewModel = (ItemObservationViewModel) observable;
            adapter.setList(viewModel.getImages());
            binding.mosaicoRecyclerView.getItemAnimator().endAnimations();
        }
    }

    @Override
    public List<Image> getSelected()
    {
        ImageAdapter adapter = (ImageAdapter) binding.mosaicoRecyclerView.getAdapter();
        return adapter.getSelectedImages();
    }

    @Override
    public void hide()
    {
        binding.getRoot().setVisibility(View.GONE);
    }

    @Override
    public void show()
    {
        binding.getRoot().setVisibility(View.VISIBLE);
    }

}
