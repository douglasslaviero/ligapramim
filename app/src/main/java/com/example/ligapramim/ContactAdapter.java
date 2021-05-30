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
    private  OnContactListener onContactListener;
    public ContactAdapter(List<Contact> contatos,  OnContactListener onContactListener) {
        this.contatos = contatos;
        this.onContactListener = onContactListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout,viewGroup,false);
        return new ViewHolder(view, onContactListener);
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
        OnContactListener onContactListener;
        public ViewHolder(@NonNull View itemView,  OnContactListener onContactListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.onContactListener = onContactListener;
            contactImageView = itemView.findViewById(R.id.contactImageView);
            txtContactName = itemView.findViewById(R.id.txtContactName);
        }
        private void setData(Contact contato) {
            txtContactName.setText(contato.getName());
            contactImageView.setImageBitmap(contato.getPhoto());
        }
        public void onClick(View view) {
            onContactListener.onContactClick(getAdapterPosition());
        }
    }

    public interface OnContactListener{
        void onContactClick(int position);
    }
}