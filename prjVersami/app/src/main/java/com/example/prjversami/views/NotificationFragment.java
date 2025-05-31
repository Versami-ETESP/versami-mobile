package com.example.prjversami.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prjversami.R;
import com.example.prjversami.controllers.NotificacaoController;
import com.example.prjversami.entities.Notificacao;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private ProgressBar progress;
    private TextView msgSemNotificacoes;
    private RecyclerView recyclerNotificacoes;

    private int userLogado;
    private List<Notificacao> notificacoes;
    private AdapterNotificacoes adapter;
    private NotificacaoController nc;

    public NotificationFragment() {
        // Required empty public constructor
    }


    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progress = view.findViewById(R.id.notificacao_progress);
        msgSemNotificacoes = view.findViewById(R.id.notificao_labelSemNotificacao);
        recyclerNotificacoes = view.findViewById(R.id.notificacao_recycler);

        progress.setVisibility(View.VISIBLE);
        msgSemNotificacoes.setVisibility(View.GONE);
        recyclerNotificacoes.setVisibility(View.GONE);

        this.userLogado = getContext().getSharedPreferences("login", Context.MODE_PRIVATE).getInt("id",0);
        NotificacaoController nc = new NotificacaoController(getContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notificacoes = nc.listarNotificacoes(userLogado);
                recyclerNotificacoes.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new AdapterNotificacoes(notificacoes,getContext());
                recyclerNotificacoes.setAdapter(adapter);

                progress.setVisibility(View.GONE);

                if(notificacoes == null || notificacoes.isEmpty()){
                    msgSemNotificacoes.setVisibility(View.VISIBLE);
                    recyclerNotificacoes.setVisibility(View.GONE);
                }else{
                    msgSemNotificacoes.setVisibility(View.GONE);
                    recyclerNotificacoes.setVisibility(View.VISIBLE);
                }
            }
        },200);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHandler(0,ItemTouchHelper.LEFT));
        helper.attachToRecyclerView(recyclerNotificacoes);
    }

    private class ItemTouchHandler extends ItemTouchHelper.SimpleCallback{

        public ItemTouchHandler(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            int posicao = viewHolder.getAdapterPosition();
            Notificacao notificacao = adapter.getNotificacoes().get(posicao);
            nc = new NotificacaoController(getContext());
            nc.marcarVisualizada(notificacao);
            adapter.getNotificacoes().remove(posicao);
            adapter.notifyItemRemoved(posicao);
            Snackbar.make(getView(),"Tudo certo! Notificação marcada como visualizada.", Snackbar.LENGTH_LONG).show();
            if(adapter.getNotificacoes().size() <= 0){
                recyclerNotificacoes.setVisibility(View.GONE);
                msgSemNotificacoes.setVisibility(View.VISIBLE);
            }
        }
    }
}