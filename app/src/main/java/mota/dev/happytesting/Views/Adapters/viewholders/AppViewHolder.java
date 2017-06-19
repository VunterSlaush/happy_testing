package mota.dev.happytesting.Views.adapters.viewholders;

import android.support.v7.widget.RecyclerView;

import mota.dev.happytesting.ViewModel.items.ItemAppViewModel;
import mota.dev.happytesting.databinding.AppItemBinding;
import mota.dev.happytesting.models.App;

/**
 * Created by Slaush on 18/06/2017.
 */

public class AppViewHolder extends BaseViewHolder<App>
{
    AppItemBinding binding;

    private AppViewHolder(AppItemBinding binding)
    {
        super(binding.itemApp);
        this.binding = binding;
    }

    @Override
    public void onBind()
    {
        if(binding.getViewModel() == null)
            binding.setViewModel(new ItemAppViewModel(item, itemView.getContext()));
        else
            binding.getViewModel().setApp(item);
    }
}