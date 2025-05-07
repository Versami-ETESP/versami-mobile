package com.example.prjversami.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prjversami.R;
import com.example.prjversami.controllers.PublicacaoController;
import com.example.prjversami.entities.Publicacao;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecyclerPostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecyclerPostsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PublicacaoController controller;
    ProgressBar carregando;

    public RecyclerPostsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecyclerPostsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecyclerPostsFragment newInstance(String param1, String param2) {
        RecyclerPostsFragment fragment = new RecyclerPostsFragment();
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
        return inflater.inflate(R.layout.fragment_recycler_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = new PublicacaoController(view.getContext());
        RecyclerView recyclerView = view.findViewById(R.id.posts_recycler);
        TextView txtSemPost = view.findViewById(R.id.posts_semposts);
        carregando = view.findViewById(R.id.loading_spinner);

        carregando.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        txtSemPost.setVisibility(View.GONE);

        //carrega os posts antes de exibir na view

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = getArguments();
                List<Publicacao> publicacoes = new ArrayList<>();

                if(bundle != null){
                    String frag = bundle.getString("fragment");
                    int user = bundle.getInt("idUsuario");

                    if(frag.equals("profile")){

                        publicacoes = controller.listarPublicacoes(user);

                    }else if(frag.equals("home")){
                        publicacoes = controller.listarPublicacoes();
                    }
                    carregando.setVisibility(View.GONE);

                    if(publicacoes.isEmpty()){
                        txtSemPost.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        txtSemPost.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(new AdapterPublicacoes(publicacoes, view.getContext()));
                    }
                }
            }
        },200);
    }
}