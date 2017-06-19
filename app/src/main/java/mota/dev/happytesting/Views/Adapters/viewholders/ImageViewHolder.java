package mota.dev.happytesting.Views.adapters.viewholders;

import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;

import mota.dev.happytesting.databinding.ImageItemBinding;
import mota.dev.happytesting.models.Image;

/**
 * Created by Slaush on 18/06/2017.
 */

public class ImageViewHolder extends BaseViewHolder<Image> {

    private ImageItemBinding binding;
    public ImageViewHolder(ImageItemBinding itemView)
    {
        super(itemView.elementRootView);
        this.binding = itemView;
    }

    @Override
    public void onBind()
    {
        Log.d("MOTA--->","BIND IMAGE!!!");
        Glide.with(itemView.getContext()).load(item.getDir()).into(binding.imageItem);
    }
}
