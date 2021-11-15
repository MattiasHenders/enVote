package com.envelopepushers.envote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.envelopepushers.envote.Issue;
import com.envelopepushers.envote.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class IssueRecycler extends RecyclerView.Adapter<IssueRecycler.ViewHolder> {
    private Context _context;
    private ArrayList<Issue> _issueArrayList;
    private String[] captions;

    public IssueRecycler(Context context, ArrayList<Issue> issues) {
        _context = context;
        _issueArrayList = issues;
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
    public IssueRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(_context)
                .inflate(R.layout.issue_item, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueRecycler.ViewHolder holder, int position) {
        final CardView cardView = holder._cardView;
        Issue currentItem = _issueArrayList.get(position);
        TextView tvName = cardView.findViewById(R.id.issueName);
    }

    @Override
    public int getItemCount() {
        return _issueArrayList.size();
    }
}

