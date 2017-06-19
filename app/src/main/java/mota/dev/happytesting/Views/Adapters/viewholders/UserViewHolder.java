package mota.dev.happytesting.Views.adapters.viewholders;

import android.support.v7.widget.RecyclerView;

import mota.dev.happytesting.ViewModel.items.ItemUserViewModel;
import mota.dev.happytesting.databinding.UserItemBinding;
import mota.dev.happytesting.models.User;

/**
 * Created by Slaush on 18/06/2017.
 */

public class UserViewHolder extends BaseViewHolder<User>
{
    public UserItemBinding binding;

    public UserViewHolder(UserItemBinding binding)
    {
        super(binding.itemUser);
        this.binding = binding;
    }

    @Override
    public void onBind()
    {
        if(binding.getViewModel() == null)
            binding.setViewModel(new ItemUserViewModel(item, itemView.getContext()));
        else
            binding.getViewModel().setUser(item);
    }
}