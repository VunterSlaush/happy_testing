package mota.dev.happytesting.Views.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mota.dev.happytesting.R;
import mota.dev.happytesting.ViewModel.items.ItemUserViewModel;
import mota.dev.happytesting.Views.adapters.viewholders.BaseViewHolder;
import mota.dev.happytesting.Views.adapters.viewholders.UserViewHolder;
import mota.dev.happytesting.databinding.UserItemBinding;
import mota.dev.happytesting.models.User;

/**
 * Created by Slaush on 11/06/2017.
 */

public class UserAdapter extends BaseRecyclerAdapter<User> {

    private List<UserViewHolder> holders;
    public UserAdapter()
    {
        super();
        holders = new ArrayList<>();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        UserItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.user_item,
                parent, false);

        UserViewHolder holder = new UserViewHolder(binding);
        holders.add(holder);
        return holder;
    }

    public void setSelectedUsers(List<User> selected)
    {
        Log.d("MOTA--->","setSelectedUsers H:"+holders.size() + " S:"+selected.size());
        for (int i = 0; i< holders.size(); i++)
        {
            if (holders.get(i).binding.getViewModel() != null && selected.contains(holders.get(i).binding.getViewModel().getUser()))
            {
                Log.d("MOTA--->","HACIENDO CHECKED:"+i);
                holders.get(i).binding.getViewModel().checked.set(true);
            }

        }
    }


    public List<User> getSelectedUsers()
    {
        List<User> selected = new ArrayList<>();
        for (int i = 0; i< holders.size(); i++)
        {
            if(holders.get(i).binding.getViewModel().checked.get())
                selected.add(holders.get(i).binding.getViewModel().getUser());
        }
        return selected;
    }
}
