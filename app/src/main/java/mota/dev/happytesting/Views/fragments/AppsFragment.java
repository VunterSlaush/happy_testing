package mota.dev.happytesting.Views.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Observable;
import java.util.Observer;

import mota.dev.happytesting.R;
import mota.dev.happytesting.ViewModel.AppsViewModel;
import mota.dev.happytesting.Views.adapters.AppAdapter;
import mota.dev.happytesting.Views.interfaces.FragmentInteractionListener;
import mota.dev.happytesting.databinding.FragmentAppsBinding;


public class AppsFragment extends Fragment implements Observer {

    private FragmentInteractionListener mListener;
    public static final String TAG = "AppsFragment";

    private AppsViewModel viewModel;
    FragmentAppsBinding binding;

    public AppsFragment() {
        // Required empty public constructor
    }

    public static AppsFragment newInstance() {
        AppsFragment fragment = new AppsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_apps, container, false);
        viewModel = new AppsViewModel(getContext());
        binding.setViewModel(viewModel);
        setupListView();
        setupObserver(viewModel);
        return binding.getRoot();
    }

    private void setupListView()
    {
        AppAdapter appAdapter = new AppAdapter();
        appAdapter.setHasStableIds(true);
        binding.appsList.setAdapter(appAdapter);
        binding.appsList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh();
            }
        });
    }

    private void setupObserver(Observable observable)
    {
        observable.addObserver(this);
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
        if (observable instanceof AppsViewModel)
        {
            binding.swipeContainer.setRefreshing(false);
            AppAdapter adapter = (AppAdapter) binding.appsList.getAdapter();
            AppsViewModel appViewModel = (AppsViewModel) observable;
            binding.appsList.getItemAnimator().endAnimations();
            adapter.setList(appViewModel.getAppList());
        }
    }
}
