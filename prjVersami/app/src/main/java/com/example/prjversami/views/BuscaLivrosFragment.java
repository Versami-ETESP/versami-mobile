package com.example.prjversami.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.prjversami.R;
import com.example.prjversami.controllers.CriarPostController;
import com.example.prjversami.entities.Livro;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuscaLivrosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuscaLivrosFragment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText txtBusca;
    private Button btnBusca, btnFecha;
    private RecyclerView rclLivros;

    public BuscaLivrosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuscaLivrosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuscaLivrosFragment newInstance(String param1, String param2) {
        BuscaLivrosFragment fragment = new BuscaLivrosFragment();
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
        return inflater.inflate(R.layout.fragment_busca_livros, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBusca = view.findViewById(R.id.livros_btnbusca);
        btnFecha = view.findViewById(R.id.livros_fechabusca);
        txtBusca = view.findViewById(R.id.livros_txtbusca);
        rclLivros = view.findViewById(R.id.livros_recycler);

        rclLivros.setLayoutManager(new LinearLayoutManager(getContext()));

        CriarPostController controller = new CriarPostController(getContext());

        btnBusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtBusca.getText().toString().isEmpty()){
                    txtBusca.setError("Digite o nome de um livros");
                }else{
                    List<Livro> livros = controller.buscarLivros(txtBusca.getText().toString());
                    rclLivros.setAdapter(new AdapterBuscaLivro(livros, getContext()));
                }

            }
        });

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Fragment parent = getParentFragment();
        if(parent instanceof PostsFragment)
            ((PostsFragment)parent).selecionaLivro(); // chama o metodo da fragment principal para marcar a tag do livro
    }
}