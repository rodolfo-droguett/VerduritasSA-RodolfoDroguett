package rodolfo.droguett.verduritassa;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import rodolfo.droguett.verduritassa.model.Cultivo;

public class AddEditCultivoActivity extends AppCompatActivity {

    private EditText aliasEditText, fechaSiembraEditText;
    private Spinner plantaSpinner;
    private FirebaseFirestore db;

    private String documentId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_cultivo);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();

        aliasEditText = findViewById(R.id.aliasEditText);
        fechaSiembraEditText = findViewById(R.id.fechaSiembraEditText);
        plantaSpinner = findViewById(R.id.plantaSpinner);
        Button guardarButton = findViewById(R.id.guardarButton);
        ImageButton backButton = findViewById(R.id.backButton);

        if (getIntent() != null) {
            String alias = getIntent().getStringExtra("alias");
            String fechaSiembra = getIntent().getStringExtra("fechaSiembra");
            String planta = getIntent().getStringExtra("planta");
            documentId = getIntent().getStringExtra("documentId");

            if (alias != null) aliasEditText.setText(alias);
            if (fechaSiembra != null) fechaSiembraEditText.setText(fechaSiembra);
            if (planta != null) {
                String[] opcionesPlantas = getResources().getStringArray(R.array.planta_options);
                for (int i = 0; i < opcionesPlantas.length; i++) {
                    if (opcionesPlantas[i].equals(planta)) {
                        plantaSpinner.setSelection(i);
                        break;
                    }
                }
            }
        }

        guardarButton.setOnClickListener(v -> guardarCultivo());

        backButton.setOnClickListener(v -> finish());
    }

    private void guardarCultivo() {
        String alias = aliasEditText.getText().toString().trim();
        String fechaSiembra = fechaSiembraEditText.getText().toString().trim();
        String planta = plantaSpinner.getSelectedItem().toString();

        if (alias.isEmpty() || fechaSiembra.isEmpty() || planta.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        String fechaCosecha = calcularFechaCosecha(fechaSiembra, planta);

        if (fechaCosecha.isEmpty()) {
            Toast.makeText(this, "Error al calcular la fecha de cosecha.", Toast.LENGTH_SHORT).show();
            return;
        }

        String uidUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String documentId = getIntent().getStringExtra("documentId");
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        if (documentId != null) {
            firestore.collection("cultivos").document(documentId)
                    .update("alias", alias,
                            "fechaSiembra", fechaSiembra,
                            "planta", planta,
                            "fechaCosecha", fechaCosecha)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Cultivo actualizado exitosamente.", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al actualizar cultivo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Cultivo cultivo = new Cultivo(uidUsuario, alias, planta, fechaSiembra, fechaCosecha);

            firestore.collection("cultivos")
                    .add(cultivo)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Cultivo guardado exitosamente.", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al guardar cultivo: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        }
    }

    private String calcularFechaCosecha(String fechaSiembra, String planta) {
        int diasCultivo = 0;

        switch (planta) {
            case "Tomates":
                diasCultivo = 80;
                break;
            case "Cebollas":
                diasCultivo = 120;
                break;
            case "Lechugas":
                diasCultivo = 85;
                break;
            case "Apio":
                diasCultivo = 150;
                break;
            case "Choclo":
                diasCultivo = 90;
                break;
            default:
                return "";
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date fecha = sdf.parse(fechaSiembra);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fecha);
            calendar.add(Calendar.DAY_OF_YEAR, diasCultivo);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
