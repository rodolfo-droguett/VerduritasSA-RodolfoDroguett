package rodolfo.droguett.verduritassa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import rodolfo.droguett.verduritassa.model.CultivoVista;

public class HomeActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_CULTIVO = 1;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private CultivoAdapter cultivoAdapter;
    private List<CultivoVista> cultivoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cultivoList = new ArrayList<>();
        cultivoAdapter = new CultivoAdapter(cultivoList, this);
        recyclerView.setAdapter(cultivoAdapter);

        loadCultivos();

        ImageButton logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(HomeActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            navigateToLogin();
        });

        ImageButton addCultivoButton = findViewById(R.id.addCultivoButton);
        addCultivoButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddEditCultivoActivity.class);
            startActivityForResult(intent, REQUEST_ADD_CULTIVO);
        });
    }

    private void loadCultivos() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            Log.d("DEBUG", "Usuario autenticado: " + userId);

            db.collection("cultivos")
                    .whereEqualTo("uidUsuario", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            cultivoList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CultivoVista cultivo = document.toObject(CultivoVista.class);
                                cultivo.setDocumentId(document.getId());
                                cultivoList.add(cultivo);
                                Log.d("DEBUG", "Documento cargado: " + document.getData());
                            }
                            cultivoAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("ERROR", "Error al cargar cultivos: " + task.getException().getMessage());
                            Toast.makeText(HomeActivity.this, "Error al cargar cultivos.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Log.e("ERROR", "Usuario no autenticado. No se pueden cargar cultivos.");
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_CULTIVO && resultCode == RESULT_OK) {
            loadCultivos();
        }
    }
}
