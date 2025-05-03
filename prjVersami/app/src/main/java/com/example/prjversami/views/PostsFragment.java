package com.example.prjversami.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prjversami.R;
import com.example.prjversami.controllers.CriarPostController;
import com.example.prjversami.controllers.PerfilController;
import com.example.prjversami.entities.Livro;
import com.example.prjversami.entities.Publicacao;
import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.Compartilha;
import com.example.prjversami.util.ImagensUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private ImageView imgPerfil, imgCapa;
    private TextView lblNome, lblArroba, lblTitulo;
    private LinearLayout dadosLivro;
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
        this.dadosLivro = view.findViewById(R.id.addpost_bookinfo);
        this.imgCapa = view.findViewById(R.id.addpost_bookcover);
        this.lblTitulo = view.findViewById(R.id.addpost_bookname);

        //RESGATANDO DADOS DO USUARIO DO SHARED PREFERENCES
        SharedPreferences pref = view.getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        this.usuario = new Usuario();
        usuario.setUserID(pref.getInt("id", 0));
        usuario.setUserName(pref.getString("nome","Usuário"));
        usuario.setUserLogin(pref.getString("arroba", "Usuário"));
        Bitmap fotoPerfil = ImagensUtil.pegarImagem("imagemPerfil.jpeg",view.getContext());


        CriarPostController postController = new CriarPostController(getContext());
        PerfilController pfc = new PerfilController(getContext());


        lblNome.setText(usuario.getUserName());
        lblArroba.setText("@" + usuario.getUserLogin());

        if (fotoPerfil != null)
            imgPerfil.setImageBitmap(fotoPerfil);
        else
            imgPerfil.setImageResource(R.drawable.user_icon_placeholder2);

        btnProcurarLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialogFragment bottomDialog = new BuscaLivrosFragment();
                bottomDialog.show(getChildFragmentManager(), "Busca livros");
            }
        });

        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Publicacao publicacao = new Publicacao();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date agora = new Date();
                String conteudo = txtpublicacao.getText().toString(), data = df.format(agora);


                publicacao.setUsuario(usuario);
                publicacao.setPostDate(data);

                if(conteudo.isEmpty()){
                    txtpublicacao.setError("Campo vazio");
                }else if(conteudo.length() > 1000){
                    txtpublicacao.setError("A publicação deve ter no máximo 1000 caracteres");
                }else{
                    publicacao.setContent(conteudo);
                }

                if(livro != null){
                    publicacao.setLivro(livro);
                }



                if(postController.postarPublicação(publicacao)){
                    Snackbar.make(view, "Postagem publicada",Snackbar.LENGTH_LONG).show();
                }else{
                    Snackbar.make(view, "Não foi possivel compartilhar publicação. Tente mais tarde!",Snackbar.LENGTH_LONG).show();
                }

            }
        });
    }

    /**
     * Esse método é chamado dentro do BottomSheetDialogFragment para aparecer a tag
     * do livro na pagina de escrever publicação.
     */
   public void selecionaLivro(){
       if (Compartilha.getLivro() != null) {
           livro = Compartilha.getLivro();
           Compartilha.setLivro(null);

           lblTitulo.setText(livro.getTitle());
           if (livro.getCover() != null)
               imgCapa.setImageBitmap(ImagensUtil.converteParaBitmap(livro.getCover()));

           dadosLivro.setVisibility(View.VISIBLE);
       } else {
           Toast.makeText(getContext(), "Livro Não Selecionado", Toast.LENGTH_LONG).show();
       }
   }
}