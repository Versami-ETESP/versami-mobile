package com.example.prjversami.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prjversami.R;
import com.example.prjversami.controllers.NotificacaoController;
import com.example.prjversami.controllers.PerfilController;
import com.example.prjversami.entities.Notificacao;
import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.NavigationUtil;
import com.example.prjversami.util.ImagensUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String ARG_ID_USUARIO = "idUsuario";

    private TabLayout opcoesPerfil;
    private ImageView profileImg, coverImg;
    private TextView seguidores, seguindo, bioUser, arroba, nomeUser;
    private Button btnSeguir;
    private ProgressBar progressBar;
    private FrameLayout frame;
    private PerfilController perCon;
    private int idUsuarioVisualizado;
    private int idUsuarioLogado;
    private String arrobaUser;
    private Usuario user;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(int idUsuario) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID_USUARIO, idUsuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idUsuarioVisualizado = getArguments().getInt(ARG_ID_USUARIO, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fazendo a similaridade com o xml
        this.nomeUser = view.findViewById(R.id.profile_name);
        this.bioUser = view.findViewById(R.id.profile_bio);
        this.arroba = view.findViewById(R.id.profile_username);
        this.seguindo = view.findViewById(R.id.profile_seguindo);
        this.seguidores = view.findViewById(R.id.profile_seguidores);
        this.profileImg = view.findViewById(R.id.profile_image);
        this.coverImg = view.findViewById(R.id.profile_cover);
        this.opcoesPerfil = view.findViewById(R.id.profile_tablayout);
        this.frame = view.findViewById(R.id.profile_framelayout);
        this.progressBar = view.findViewById(R.id.profile_progress);
        this.btnSeguir = view.findViewById(R.id.profile_buttonSeguir);

        visibilityItens(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        this.perCon = new PerfilController(view.getContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //criando obj usuario e pegando o id do usuario logado
                SharedPreferences pref = view.getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                idUsuarioLogado = pref.getInt("id", 0);
                arrobaUser = pref.getString("arroba","");

                if(idUsuarioVisualizado == 0)
                    idUsuarioVisualizado = idUsuarioLogado;

                user = perCon.obtemPerfil(idUsuarioVisualizado);
                if (user == null) return;

                    nomeUser.setText(user.getUserName());
                    arroba.setText("@" + user.getUserLogin());
                    seguidores.setText(user.getSeguidores().toString() + "\nSeguidores");
                    seguindo.setText(user.getSeguindo().toString() + "\nSeguindo");

                    // As imagens só são definidas se o objeto nao estiver nulo, para evitar nullpointer exception

                    if (user.getUserImage() != null)
                        profileImg.setImageBitmap(ImagensUtil.converteParaBitmap(user.getUserImage()));

                    if (user.getUserCover() != null)
                        coverImg.setImageBitmap(ImagensUtil.converteParaBitmap(user.getUserCover()));

                    if (user.getUserBio() != null || user.getUserBio() != "")
                        bioUser.setText(user.getUserBio());
                    else
                        bioUser.setVisibility(View.GONE);

                    if(idUsuarioVisualizado == idUsuarioLogado)
                        btnSeguir.setVisibility(View.GONE);
                    else
                        btnSeguir.setVisibility(View.VISIBLE);

                    if(user.isSeguidor())
                        btnSeguir.setText("Seguindo");

                    visibilityItens(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

            }
        },1000);


        //Cria um bundle para passar a informação de qual fragment de origem e carrega o recycler na view
        Bundle bundle = new Bundle();
        bundle.putString("fragment", "profile");
        bundle.putInt("idUsuario", idUsuarioVisualizado);
        NavigationUtil.carregarFragment(getChildFragmentManager(), R.id.profile_framelayout, new RecyclerPostsFragment(), bundle);

        // define qual fragment colocar no lugar do framelayout de acordo com a opçao do tablayout
        opcoesPerfil.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                bundle.putInt("idUsuario", idUsuarioVisualizado);

                switch (opcoesPerfil.getSelectedTabPosition()) {
                    case 0:
                        bundle.putString("fragment", "profile");
                        fragment = new RecyclerPostsFragment();
                        break;
                    case 1:
                        fragment = new RecyclerFavoritosFragment();
                        break;
                }

                if(fragment != null)
                    NavigationUtil.carregarFragment(getChildFragmentManager(),R.id.profile_framelayout, fragment, bundle);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btnSeguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idUsurioLogado = view.getContext().getSharedPreferences("login", Context.MODE_PRIVATE).getInt("id", 0);
                if(!user.isSeguidor()){
                    perCon.seguirUsuario(idUsurioLogado, idUsuarioVisualizado);
                    user.setSeguidor(true);
                    user.setSeguidores(user.getSeguidores() + 1);
                    seguidores.setText(user.getSeguidores().toString()+"\nSeguidores");
                    btnSeguir.setText("Seguindo");
                    if(idUsuarioLogado != idUsuarioVisualizado)
                        NotificacaoController.notificarAcao(Notificacao.SEGUIU,idUsuarioVisualizado,arrobaUser,getContext());
                }else{
                    perCon.deixarDeSeguirUsuario(idUsurioLogado, idUsuarioVisualizado);
                    user.setSeguidor(false);
                    user.setSeguidores(user.getSeguidores() - 1);
                    seguidores.setText(user.getSeguidores().toString()+"\nSeguidores");
                    btnSeguir.setText("Seguir");
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        int idUsuarioLogado = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE).getInt("id", 0);

        if(idUsuarioVisualizado == idUsuarioLogado){
            inflater.inflate(R.menu.menu_fragment_profile, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile_edit:
                startActivity(new Intent(getContext(), EditarPerfil.class));
                return true;
            case R.id.menu_profile_exit:
                SharedPreferences pref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                editor.putInt("id", -1);
                editor.apply();
                ImagensUtil.apagarImagem("imagemPerfil.jpeg", getContext());
                startActivity(new Intent(getContext(), splash.class));
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void visibilityItens(int value){
        this.nomeUser.setVisibility(value);
        this.bioUser.setVisibility(value);
        this.arroba.setVisibility(value);
        this.seguindo.setVisibility(value);
        this.seguidores.setVisibility(value);
        this.profileImg.setVisibility(value);
        this.coverImg.setVisibility(value);
        this.opcoesPerfil.setVisibility(value);
        this.frame.setVisibility(value);
    }
}