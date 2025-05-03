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
import com.example.prjversami.controllers.PerfilController;
import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.NavigationUtil;
import com.example.prjversami.util.ImagensUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TabLayout opcoesPerfil;
    private ImageView profileImg, coverImg;
    private TextView seguidores, seguindo, bioUser, arroba, nomeUser;
    private Button btnSeguir;
    private ProgressBar progressBar;
    private FrameLayout frame;
    private PerfilController perCon = new PerfilController(getContext());

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //criando obj usuario e pegando o id do usuario logado
                Usuario user;
                SharedPreferences pref = view.getContext().getSharedPreferences("login", Context.MODE_PRIVATE);

                // se tiver android ele vai setar as informações do usuario nos devidos campos
                if (pref.getInt("id", 0) > 0) {
                    user = perCon.obtemPerfil(pref.getInt("id", 0));

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

                    visibilityItens(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        },200);



        //Cria um bundle para passar a informação de qual fragment de origem e carrega o recycler na view
        Bundle bundle = new Bundle();
        bundle.putString("fragment", "profile");
        NavigationUtil.carregarFragment(getChildFragmentManager(), R.id.profile_framelayout, new RecyclerPostsFragment(), bundle);

        // define qual fragment colocar no lugar do framelayout de acordo com a opçao do tablayout
        opcoesPerfil.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (opcoesPerfil.getSelectedTabPosition()) {
                    case 0:
                        bundle.putString("fragment", "profile");
                        fragment = new RecyclerPostsFragment();
                        if (fragment != null) {
                            NavigationUtil.carregarFragment(getChildFragmentManager(), R.id.profile_framelayout, new RecyclerPostsFragment(), bundle);
                        }
                        break;
                    case 1:
                        fragment = null; // todo fazer fragment de favoritos
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile_edit:
                Toast.makeText(getActivity(), "Editar Perfil Clicado", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_profile_exit:
                SharedPreferences pref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                editor.putInt("id", -1);
                editor.apply();
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
        this.btnSeguir.setVisibility(value);
    }
}