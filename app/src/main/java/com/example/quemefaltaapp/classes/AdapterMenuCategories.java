package com.example.quemefaltaapp.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quemefaltaapp.R;

import java.util.List;

public class AdapterMenuCategories extends ArrayAdapter<String> {
    private OnCategoryDeleteListener onCategoryDeleteListener;

    public interface OnCategoryDeleteListener {
        void onCategoryDelete(int position);
    }

    public void setOnCategoryDeleteListener(OnCategoryDeleteListener listener) {
        this.onCategoryDeleteListener = listener;
    }

    public AdapterMenuCategories(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_categories_layout, parent, false);
        }

        TextView textViewNombre = convertView.findViewById(R.id.textViewNombre);
        ImageButton botonTresPuntos = convertView.findViewById(R.id.botonTresPuntos);

        textViewNombre.setText(getItem(position));
        botonTresPuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view, position);
            }
        });

        return convertView;
    }

    private void showMenu(View view, final int position) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.categories_manu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu_copiar) {
                    Toast.makeText(getContext(), "Copiando Link", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.menu_eliminar) {
                    if (onCategoryDeleteListener != null) {
                        onCategoryDeleteListener.onCategoryDelete(position);
                    }
                    return true;
                } else {
                    return false;
                }

            }
        });

        popupMenu.show();
    }


}
