package mota.dev.happytesting.Views.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Observable;
import java.util.Observer;

import mota.dev.happytesting.R;
import mota.dev.happytesting.ViewModel.AccountViewModel;
import mota.dev.happytesting.Views.interfaces.FragmentInteractionListener;
import mota.dev.happytesting.databinding.FragmentAccountBinding;


public class AccountFragment extends Fragment implements Observer {

    private FragmentInteractionListener mListener;
    public static final String TAG = "AccountFragment";
    private AccountViewModel viewModel;
    private FragmentAccountBinding binding;

    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        viewModel = new AccountViewModel(getContext());
        binding.setViewModel(viewModel);
        setupObserver(viewModel);
        return binding.getRoot();
    }

    private void setupObserver(Observable o)
    {
        o.addObserver(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            mListener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void update(Observable observable, Object o)
    {
        if (observable instanceof AccountViewModel)
        {
            //do somenthing!
        }
    }


}
