package mota.dev.happytesting.Views.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mota.dev.happytesting.R;
import mota.dev.happytesting.ViewModel.items.ItemUserViewModel;
import mota.dev.happytesting.databinding.UserItemBinding;
import mota.dev.happytesting.models.User;

/**
 * Created by Slaush on 11/06/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users;
    private List<UserViewHolder> holders;
    public UserAdapter()
    {
        users = new ArrayList<>();
        holders = new ArrayList<>();
        this.setHasStableIds(true);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        UserItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.user_item,
                parent, false);

        UserViewHolder holder = new UserViewHolder(binding);
        holders.add(holder);

        return holder;
    }

    public List<User> getSelectedUsers()
    {
        List<User> selected = new ArrayList<>();
        for (int i = 0; i< holders.size(); i++)
        {
            if(holders.get(i).binding.getViewModel().checked.get())
                selected.add(users.get(i));
        }
        return selected;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position)
    {
        holder.bindUser(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsersList(List<User> users)
    {
        this.users.clear();
        this.users.addAll(users);
        notifyDataSetChanged();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder
    {
        UserItemBinding binding;

        private UserViewHolder(UserItemBinding binding)
        {
            super(binding.itemUser);
            this.binding = binding;
        }

        private void bindUser(User user)
        {
            if(binding.getViewModel() == null)
                binding.setViewModel(new ItemUserViewModel(user, itemView.getContext()));
            else
                binding.getViewModel().setUser(user);
        }
    }
}
