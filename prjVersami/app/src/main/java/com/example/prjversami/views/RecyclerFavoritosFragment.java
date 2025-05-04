package com.example.prjversami.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prjversami.R;
import com.example.prjversami.controllers.PerfilController;
import com.example.prjversami.entities.Livro;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecyclerFavoritosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecyclerFavoritosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecyclerFavoritosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecyclerFavoritosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecyclerFavoritosFragment newInstance(String param1, String param2) {
        RecyclerFavoritosFragment fragment = new RecyclerFavoritosFragment();
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
        return inflater.inflate(R.layout.fragment_recycler_favoritos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ProgressBar progressBar = view.findViewById(R.id.livroFavorito_progresso);
        TextView textView = view.findViewById(R.id.livroFavorito_sem_favoritos);
        RecyclerView recyclerView = view.findViewById(R.id.livroFavorito_Recycler);

        textView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PerfilController pc = new PerfilController(view.getContext());

                SharedPreferences pref = view.getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                int user = pref.getInt("id", 0);
                List<Livro> livros = pc.obterLivrosFavoritos(user);

                progressBar.setVisibility(View.GONE);

                if(!livros.isEmpty()){
                    textView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(new AdapterLivrosFavoritos(livros, view.getContext()));
                }else {
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }
            }
        },200);
    }
}