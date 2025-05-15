package com.example.folhagem.ui.perfil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.folhagem.R;
import com.example.folhagem.database.DatabaseHelper;
import com.example.folhagem.database.EstanteDAO;
import com.example.folhagem.database.ResenhaDAO;
import com.example.folhagem.model.ResenhaEntity;
import com.example.folhagem.ui.login.LoginActivity;

import java.io.File;
import java.util.List;

public class PerfilFragment extends Fragment {

    private ImageView imagePerfil;
    private TextView textNome, textEmail, textTotalLivros, textMediaAvaliacao;
    private Button btnEditar, btnSair;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        imagePerfil = view.findViewById(R.id.image_perfil);
        textNome = view.findViewById(R.id.text_nome);
        textEmail = view.findViewById(R.id.text_email);
        textTotalLivros = view.findViewById(R.id.text_total_livros);
        textMediaAvaliacao = view.findViewById(R.id.text_media_avaliacao);
        btnEditar = view.findViewById(R.id.btn_editar_perfil);
        btnSair = view.findViewById(R.id.btn_sair);

        carregarDadosUsuario();
        calcularEstatisticas();

        btnEditar.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), EditarPerfilActivity.class)));

        btnSair.setOnClickListener(v -> {
            SharedPreferences.Editor editor = requireContext()
                    .getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE)
                    .edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarDadosUsuario(); // recarrega se tiver voltado do editar perfil
    }

    private void carregarDadosUsuario() {
        SharedPreferences prefs = requireContext()
                .getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE);

        String nome = prefs.getString("nome", "Usuário");
        String email = prefs.getString("email", "email@exemplo.com");
        String caminhoFoto = prefs.getString("foto", null);

        textNome.setText(nome);
        textEmail.setText(email);

        if (caminhoFoto != null) {
            File file = new File(caminhoFoto);
            if (file.exists()) {
                Glide.with(this).load(file).circleCrop().into(imagePerfil);
            } else {
                imagePerfil.setImageResource(R.drawable.ic_user);
            }
        } else {
            imagePerfil.setImageResource(R.drawable.ic_user);
        }
    }

    private void calcularEstatisticas() {
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        EstanteDAO estanteDAO = new EstanteDAO(dbHelper.getReadableDatabase());
        ResenhaDAO resenhaDAO = new ResenhaDAO(dbHelper.getReadableDatabase());

        int totalLivros = estanteDAO.contarLivros();
        textTotalLivros.setText(totalLivros + " livros");

        List<ResenhaEntity> resenhas = resenhaDAO.listarResenhas();
        if (resenhas.isEmpty()) {
            textMediaAvaliacao.setText("0.0");
            return;
        }

        float soma = 0;
        for (ResenhaEntity r : resenhas) {
            soma += r.getNota();
        }

        float media = soma / resenhas.size();
        textMediaAvaliacao.setText(String.format("%.1f", media));
    }
}
