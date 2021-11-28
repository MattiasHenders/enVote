package com.envelopepushers.envote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private ArrayList<Representative> _repArrayList;
    private String[] captions;
    Button btnSubmitLocation;

    /**
     * Constructor.
     * @param context
     * @param repArrayList
     */
    public RecyclerAdapter(Context context, ArrayList<Representative> repArrayList) {
        _context = context;
        _repArrayList = repArrayList;
    }

    /**
     * Recycler View holder.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView _cardView;

        public ViewHolder(CardView v) {
            super(v);
            _cardView = v;
        }
    }

    /**
     * On view holder created use the inflater.
     * @param parent parent class
     * @param viewType Type of view (recycler)
     * @return View Holder object
     */
    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(_context)
                .inflate(R.layout.card_item, parent, false);
        return new ViewHolder(cv);
    }

    /**
     * On bind, populate cards.
     * @param holder Recycler view holder
     * @param position Position in arr
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        final CardView cardView = holder._cardView;
        Representative currentItem = _repArrayList.get(position);

        TextView tvName = cardView.findViewById(R.id.name);
        TextView tvEmail = cardView.findViewById(R.id.email);
        TextView tvParty = cardView.findViewById(R.id.party);
        TextView tvGovLevel = cardView.findViewById(R.id.government_level);
        Button btn = cardView.findViewById(R.id.next_page);

        ImageView ivPictureUrl = cardView.findViewById(R.id.image_view);
        tvName.setText(currentItem.getName());
        tvEmail.setText(currentItem.getEmail());
        tvParty.setText(currentItem.getParty());
        tvGovLevel.setText(currentItem.getGovernmentLevel());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent prevIntent = ((Activity) _context).getIntent();
                String issue = prevIntent.getStringExtra("issue");
                Intent intent = new Intent (view.getContext(), TemplateView.class);
                intent.putExtra("issue", issue);
                intent.putExtra("email", tvEmail.getText().toString());
                intent.putExtra("name", tvName.getText().toString());
                intent.putExtra("party", tvParty.getText().toString());
                ((Activity) _context).finish();
                _context.startActivity(intent);
            }
        });
            Picasso.with(_context)
                    .load(currentItem.getPictureUrl().isEmpty() ? null : currentItem.getPictureUrl())
                    .placeholder(R.drawable.ic_android_black_24dp)
                    .error(R.drawable.ic_android_black_24dp)
                    .fit()
                    .centerInside()
                    .into(ivPictureUrl);
    }

    /**
     * Gets the item count.
     * @return returns the size of the reps list
     */
    @Override
    public int getItemCount() {
        return _repArrayList.size();
    }
}

