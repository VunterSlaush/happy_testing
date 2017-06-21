package mota.dev.happytesting.Views.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import java.util.List;
import java.util.Observable;

import mota.dev.happytesting.R;
import mota.dev.happytesting.ViewModel.GalleryViewModel;
import mota.dev.happytesting.Views.adapters.ImageAdapter;
import mota.dev.happytesting.Views.interfaces.Selectable;
import mota.dev.happytesting.databinding.ActivityGalleryBinding;
import mota.dev.happytesting.models.Image;


/**
 * Created by Slaush on 07/05/2017.
 */

public class GalleryActivity extends BindeableActivity implements Selectable<Image>
{
    private GalleryViewModel viewModel;
    private ActivityGalleryBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initDataBinding();
        setupObserver(viewModel);
        setupAdapter();
    }

    private void setupAdapter()
    {
        ImageAdapter adapter = new ImageAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        binding.mosaicoRecyclerView.setLayoutManager(gridLayoutManager);
        binding.mosaicoRecyclerView.setAdapter(adapter);
    }

    @Override
    public void update(Observable observable, Object o)
    {
        if (observable instanceof GalleryViewModel)
        {
            ImageAdapter adapter = (ImageAdapter) binding.mosaicoRecyclerView.getAdapter();
            adapter.setList(viewModel.getImages());
            binding.mosaicoRecyclerView.getItemAnimator().endAnimations();
        }
    }

    @Override
    public void initDataBinding()
    {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery);
        viewModel = new GalleryViewModel(this,this);
        binding.setViewModel(viewModel);
    }

    @Override
    public List<Image> getSelected()
    {
        ImageAdapter adapter = (ImageAdapter) binding.mosaicoRecyclerView.getAdapter();
        return adapter.getSelectedImages();
    }
}
