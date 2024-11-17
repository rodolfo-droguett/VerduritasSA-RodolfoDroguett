package rodolfo.droguett.verduritassa;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import rodolfo.droguett.verduritassa.model.CultivoVista;

public class CultivoAdapter extends RecyclerView.Adapter<CultivoAdapter.CultivoViewHolder> {

    private final List<CultivoVista> cultivos;
    private final Context context;

    public CultivoAdapter(List<CultivoVista> cultivos, Context context) {
        this.cultivos = cultivos;
        this.context = context;
    }

    @NonNull
    @Override
    public CultivoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cultivo, parent, false);
        return new CultivoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CultivoViewHolder holder, int position) {
        CultivoVista cultivo = cultivos.get(position);
        holder.aliasTextView.setText(cultivo.getAlias());
        holder.fechaCosechaTextView.setText(cultivo.getFechaCosecha());

        holder.settingsButton.setOnClickListener(v -> {
            showBottomSheetMenu(cultivo, position);
        });
    }

    @Override
    public int getItemCount() {
        return cultivos.size();
    }

    public static class CultivoViewHolder extends RecyclerView.ViewHolder {
        TextView aliasTextView;
        TextView fechaCosechaTextView;
        ImageButton settingsButton;

        public CultivoViewHolder(@NonNull View itemView) {
            super(itemView);
            aliasTextView = itemView.findViewById(R.id.aliasTextView);
            fechaCosechaTextView = itemView.findViewById(R.id.fechaCosechaTextView);
            settingsButton = itemView.findViewById(R.id.editButton);
        }
    }

    private void showBottomSheetMenu(CultivoVista cultivo, int position) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View bottomSheetView = LayoutInflater.from(context).inflate(
                R.layout.bottom_sheet_cultivo_menu,
                null
        );

        bottomSheetView.findViewById(R.id.editButton).setOnClickListener(v -> {
            Intent editIntent = new Intent(context, AddEditCultivoActivity.class);
            editIntent.putExtra("alias", cultivo.getAlias());
            editIntent.putExtra("planta", cultivo.getPlanta());
            editIntent.putExtra("fechaSiembra", cultivo.getFechaSiembra());
            editIntent.putExtra("documentId", cultivo.getDocumentId());
            if (context instanceof HomeActivity) {
                ((HomeActivity) context).startActivityForResult(editIntent, HomeActivity.REQUEST_ADD_CULTIVO);
            } else {
                context.startActivity(editIntent);
            }
            bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.deleteButton).setOnClickListener(v -> {
            FirebaseFirestore.getInstance()
                    .collection("cultivos")
                    .document(cultivo.getDocumentId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Cultivo eliminado.", Toast.LENGTH_SHORT).show();
                        cultivos.remove(position);
                        notifyItemRemoved(position);
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Error al eliminar cultivo.", Toast.LENGTH_SHORT).show());
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    public void updateData(List<CultivoVista> newCultivos) {
        cultivos.clear();
        cultivos.addAll(newCultivos);
        notifyDataSetChanged();
    }
}
