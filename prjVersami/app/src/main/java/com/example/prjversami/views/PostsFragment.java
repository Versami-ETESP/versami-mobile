package com.example.prjversami.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prjversami.R;
import com.example.prjversami.controllers.PerfilController;
import com.example.prjversami.entities.Livro;
import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.ImagensUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText txtpublicacao;
    private Button btnPublicar, btnProcurarLivro;
    private ImageView imgPerfil;
    private TextView lblNome, lblArroba;
    private Usuario usuario;
    private Livro livro;


    public PostsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostsFragment newInstance(String param1, String param2) {
        PostsFragment fragment = new PostsFragment();
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
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.lblNome = view.findViewById(R.id.addpost_name);
        this.lblArroba = view.findViewById(R.id.addpost_username);
        this.imgPerfil = view.findViewById(R.id.addpost_image);
        this.txtpublicacao = view.findViewById(R.id.addpost_txtPublicacao);
        this.btnPublicar = view.findViewById(R.id.addpost_btnpublicar);
        this.btnProcurarLivro = view.findViewById(R.id.addpost_btnLivro);

        SharedPreferences pref = view.getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        int id = pref.getInt("id", 0);

        PerfilController pfc = new PerfilController(getContext());
        usuario = pfc.obtemPerfil(id);

        lblNome.setText(usuario.getUserName());
        lblArroba.setText("@"+usuario.getUserLogin());

        if(usuario.getUserImage() != null)
            imgPerfil.setImageBitmap(ImagensUtil.converteParaBitmap(usuario.getUserImage()));
        else
            imgPerfil.setImageResource(R.drawable.user_icon_placeholder2);
    }
}