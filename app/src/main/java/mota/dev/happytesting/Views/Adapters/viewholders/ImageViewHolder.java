package mota.dev.happytesting.Views.adapters.viewholders;

import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;

import java.io.File;

import mota.dev.happytesting.ViewModel.items.ItemImageViewModel;
import mota.dev.happytesting.databinding.ImageItemBinding;
import mota.dev.happytesting.models.Image;

/**
 * Created by Slaush on 18/06/2017.
 */

public class ImageViewHolder extends BaseViewHolder<Image> {

    public ImageItemBinding binding;
    public ImageViewHolder(ImageItemBinding itemView)
    {
        super(itemView.elementRootView);
        this.binding = itemView;
    }

    @Override
    public void onBind()
    {
        if(binding.getViewModel() == null)
            binding.setViewModel(new ItemImageViewModel(item, itemView.getContext()));
        else
            binding.getViewModel().setImage(item);

        if(item.getDir().contains("http"))
            Glide.with(itemView.getContext()).load(item.getDir()).into(binding.imageItem);
        else
            Glide.with(itemView.getContext()).load(new File(item.getDir())).into(binding.imageItem);
    }
}
