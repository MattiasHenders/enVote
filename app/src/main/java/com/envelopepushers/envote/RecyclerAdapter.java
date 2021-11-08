package com.envelopepushers.envote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.envelopepushers.envote.R;
import com.envelopepushers.envote.Representative;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context _context;
    private ArrayList<Representative> _toonArrayList;
    private String[] captions;

    public RecyclerAdapter(Context context, ArrayList<Representative> toonArrayList) {
        _context = context;
        _toonArrayList = toonArrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView _cardView;

        public ViewHolder(CardView v) {
            super(v);
            _cardView = v;
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(_context)
                .inflate(R.layout.card_item, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        final CardView cardView = holder._cardView;

        Representative currentItem = _toonArrayList.get(position);

        TextView tvId = cardView.findViewById(R.id.id);
        TextView tvName = cardView.findViewById(R.id.name);
        TextView tvOccupation = cardView.findViewById(R.id.occupation);
        ImageView ivPictureUrl = cardView.findViewById(R.id.image_view);

        tvId.setText(String.valueOf(currentItem.getId()));
        tvName.setText(currentItem.getFirstName() + " " + currentItem.getLastName());
        tvOccupation.setText(currentItem.getOccupation());

        if (currentItem.getPictureUrl() != null) {
//            new ImageDownloaderTask(ivPictureUrl).execute(currentItem.getPictureUrl());
            Picasso.with(_context)
                    .load(currentItem.getPictureUrl())
                    .fit()
                    .centerInside()
                    .into(ivPictureUrl);
        }
    }

    @Override
    public int getItemCount() {
        return _toonArrayList.size();
    }
}

