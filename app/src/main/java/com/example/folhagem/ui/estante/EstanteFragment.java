package com.example.folhagem.ui.estante;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.folhagem.R;
import com.example.folhagem.database.DatabaseHelper;
import com.example.folhagem.database.EstanteDAO;
import com.example.folhagem.model.LivroEntity;

import java.util.ArrayList;
import java.util.List;

public class EstanteFragment extends Fragment {

    private RecyclerView recyclerView;
    private LivroEstanteAdapter livroAdapter;
    private TextView emptyMessage;
    private EditText inputBusca;
    private Spinner spinnerStatus;

    private List<LivroEntity> todosOsLivros = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_estante, container, false);

        recyclerView = view.findViewById(R.id.recycler_estante);
        emptyMessage = view.findViewById(R.id.text_estante_vazia);
        inputBusca = view.findViewById(R.id.input_busca);
        spinnerStatus = view.findViewById(R.id.spinner_status);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // ✅ Configura o adapter do Spinner antes de tudo
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.opcoes_status,
                R.layout.spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerAdapter);

        // Evento: busca por texto
        inputBusca.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarLista();
            }
        });

        // Evento: filtro de status
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtrarLista();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Carrega os livros
        carregarLivros();

        return view;
    }

    private void carregarLivros() {
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        EstanteDAO dao = new EstanteDAO(dbHelper.getReadableDatabase());
        todosOsLivros = dao.listarTodos(); // carrega todos e salva em memória
        filtrarLista();
    }

    private void filtrarLista() {
        if (spinnerStatus.getSelectedItem() == null) return;

        String termo = inputBusca.getText().toString().toLowerCase().trim();
        String statusSelecionado = spinnerStatus.getSelectedItem().toString();

        List<LivroEntity> filtrados = new ArrayList<>();

        for (LivroEntity livro : todosOsLivros) {
            boolean combinaComBusca = livro.getTitulo().toLowerCase().contains(termo);
            boolean combinaComStatus = statusSelecionado.equals("TODOS") || livro.getStatus().equalsIgnoreCase(statusSelecionado);

            if (combinaComBusca && combinaComStatus) {
                filtrados.add(livro);
            }
        }

        if (filtrados.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyMessage.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyMessage.setVisibility(View.GONE);

            livroAdapter = new LivroEstanteAdapter(filtrados, this::carregarLivros);
            recyclerView.setAdapter(livroAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarLivros();
    }
}
