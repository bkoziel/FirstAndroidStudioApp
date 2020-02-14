package com.example.wastic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.wastic.Activity.Comments;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context context;
    private List<Comments> list;
    private String addCommentReportURL = "https://wasticelo.000webhostapp.com/addCommentReport.php";
    RequestQueue requestQueue;

    public CommentAdapter(Context context, List<Comments> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_comment, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Comments comments = list.get(position);

        holder.textTitle.setText(comments.getDescription());
        holder.username.setText(comments.getUsername());
        Picasso.get().load(comments.getImageUser()).into(holder.imageView);
        holder.textRating.setText(String.valueOf(comments.getRating()));
        holder.textDate.setText(String.valueOf(comments.getDate()));
        holder.buttonReport.setImageResource(R.drawable.ic_error_black_24dp);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle, textRating, textDate,username;
        public ImageView imageView, buttonReport;
        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.commentText);
            textRating = itemView.findViewById(R.id.textViewRating);
            textDate = itemView.findViewById(R.id.comment_date);
            username = itemView.findViewById(R.id.comment_username);
            imageView = itemView.findViewById(R.id.comment_user_img);
            buttonReport = itemView.findViewById(R.id.buttonCommentReport);

        }
    }

}


