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
import mota.dev.happytesting.ViewModel.ReportsViewModel;
import mota.dev.happytesting.Views.adapters.ReportAdapter;
import mota.dev.happytesting.Views.interfaces.FragmentInteractionListener;
import mota.dev.happytesting.databinding.FragmentMyReportsBinding;


public class MyReportsFragment extends Fragment implements Observer {

    private FragmentInteractionListener mListener;
    public static final String TAG = "MyReportsFragment";

    private ReportsViewModel viewModel;
    FragmentMyReportsBinding binding;

    public MyReportsFragment() {
        // Required empty public constructor
    }

    public static MyReportsFragment newInstance() {
        MyReportsFragment fragment = new MyReportsFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_reports, container, false);
        viewModel = new ReportsViewModel(getContext());
        binding.setViewModel(viewModel);
        setupListView();
        setupObserver(viewModel);
        return binding.getRoot();
    }

    private void setupListView()
    {
        ReportAdapter reportAdapter = new ReportAdapter();
        reportAdapter.setHasStableIds(true);
        binding.reportsList.setAdapter(reportAdapter);
        binding.reportsList.setLayoutManager(new LinearLayoutManager(this.getContext()));
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
        if (observable instanceof ReportsViewModel)
        {
            binding.swipeContainer.setRefreshing(false);
            ReportAdapter adapter = (ReportAdapter) binding.reportsList.getAdapter();
            ReportsViewModel reportsViewModel = (ReportsViewModel) observable;
            binding.reportsList.getItemAnimator().endAnimations();
            adapter.setList(reportsViewModel.getReportList());
        }
    }
}
