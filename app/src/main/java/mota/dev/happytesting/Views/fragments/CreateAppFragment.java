package mota.dev.happytesting.Views.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Observable;
import java.util.Observer;

import mota.dev.happytesting.R;
import mota.dev.happytesting.ViewModel.CreateAppViewModel;
import mota.dev.happytesting.Views.interfaces.BindeableActivity;
import mota.dev.happytesting.Views.interfaces.FragmentInteractionListener;
import mota.dev.happytesting.databinding.FragmentCreateAppBinding;


public class CreateAppFragment extends Fragment implements Observer
{

    private FragmentInteractionListener mListener;
    public static final String TAG = "CreateAppFragment";
    private CreateAppViewModel viewModel;
    public CreateAppFragment() {

    }

    public static CreateAppFragment newInstance()
    {
        CreateAppFragment fragment = new CreateAppFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentCreateAppBinding binding = DataBindingUtil.inflate(inflater,
                                           R.layout.fragment_create_app,container,false);

        viewModel = new CreateAppViewModel(getContext());
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void update(Observable observable, Object o) {

    }


    public void setupObserver(Observable observable) {
        observable.addObserver(this);
    }
}
