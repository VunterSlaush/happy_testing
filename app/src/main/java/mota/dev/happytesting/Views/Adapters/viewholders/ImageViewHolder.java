package mota.dev.happytesting.Views.adapters.viewholders;

import android.view.View;

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
    }

    @Override
    public void onBind()
    {

    }
}
