package com.example.prjversami.views;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prjversami.R;
import com.example.prjversami.controllers.SearchController;
import com.example.prjversami.entities.Livro;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecyclerExolorarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecyclerExolorarFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private List<Livro> livros;

    public RecyclerExolorarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecyclerExolorarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecyclerExolorarFragment newInstance(String param1, String param2) {
        RecyclerExolorarFragment fragment = new RecyclerExolorarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_exolorar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recycler = view.findViewById(R.id.toplivro_recycler);
        ProgressBar progressBar = view.findViewById(R.id.topLivro_progress);
        TextView label = view.findViewById(R.id.toplivro_label);

        SearchController sc = new SearchController(getContext());

        progressBar.setVisibility(View.VISIBLE);
        recycler.setVisibility(View.GONE);
        label.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                livros = sc.retornaTopLivros();
                progressBar.setVisibility(View.GONE);
                recycler.setVisibility(View.VISIBLE);
                label.setVisibility(View.VISIBLE);

                recycler.setAdapter(new AdapterTopLivros(livros, getContext()));
                recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
            }
        },200);
    }
}