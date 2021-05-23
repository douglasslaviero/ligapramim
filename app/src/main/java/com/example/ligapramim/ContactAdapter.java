package com.example.ligapramim;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<Contact> contatos;
    public ContactAdapter(List<Contact> contatos) {
        this.contatos = contatos;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout,viewGroup,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position)
    {
        viewHolder.setData(contatos.get(position));
    }
    @Override
    public int getItemCount() {
        return contatos.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private TextView txtContactName;
        private ImageView contactImageView;
        private TextView txtTitulo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            contactImageView = itemView.findViewById(R.id.contactImageView);
            txtContactName = itemView.findViewById(R.id.txtContactName);
        }
        private void setData(Contact contato) {
            txtContactName.setText(contato.getName());
            contactImageView.setImageBitmap(contato.getPhoto());
        }
        public void onClick(View view) {
            Toast.makeText(view.getContext(),"Você selecionou "
                    +
                    contatos.get(getLayoutPosition()).getName(),Toast.LENGTH_LONG).
                    show();
        }
    }
}