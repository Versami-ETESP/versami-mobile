package com.example.prjversami.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prjversami.R;
import com.example.prjversami.controllers.SearchController;
import com.example.prjversami.entities.Livro;
import com.example.prjversami.entities.Usuario;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link resultadoPesquisaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class resultadoPesquisaFragment extends Fragment {

    private static final String ARG_PESQUISA = "PESQUISA";

    private String textoPesquisa;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private TextView txtSemResultado;
    private ProgressBar progressBar;
    private List<Livro> livros;
    private List<Usuario> usuarios;

    public resultadoPesquisaFragment() {
        // Required empty public constructor
    }


    public static resultadoPesquisaFragment newInstance(String pesquisa) {
        resultadoPesquisaFragment fragment = new resultadoPesquisaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PESQUISA, pesquisa);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            textoPesquisa = getArguments().getString(ARG_PESQUISA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resultado_pesquisa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.resultado_progress);
        tabLayout = view.findViewById(R.id.resultado_tablayout);
        txtSemResultado = view.findViewById(R.id.resultado_lblSemResultado);
        recyclerView = view.findViewById(R.id.resultado_recycler);

        SearchController sc = new SearchController(getContext());
        this.livros = sc.pesquisarLivros(textoPesquisa);

        if(this.livros.isEmpty()){
            txtSemResultado.setVisibility(View.VISIBLE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new AdapaterResultadoLivros(this.livros, getContext()));

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tabLayout.getSelectedTabPosition()){
                    case 0:
                        txtSemResultado.setVisibility(View.GONE);
                        livros = sc.pesquisarLivros(textoPesquisa);
                        if(livros.isEmpty()){
                            txtSemResultado.setVisibility(View.VISIBLE);
                        }
                        recyclerView.setAdapter(new AdapaterResultadoLivros(livros, getContext()));
                        break;
                    case 1:
                        txtSemResultado.setVisibility(View.GONE);
                        usuarios = sc.pesquisarUsuarios(textoPesquisa);
                        if(usuarios.isEmpty()){
                            txtSemResultado.setVisibility(View.VISIBLE);
                        }
                        recyclerView.setAdapter(new AdapterResultadoUsuarios(usuarios,getContext()));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}