package br.com.luizcarlos.testinstaflickr.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.googlecode.flickrjandroid.photos.comments.Comment;

import java.util.List;

import br.com.luizcarlos.testinstaflickr.R;
import br.com.luizcarlos.testinstaflickr.utils.TimeUtils;

/**
 * Created by luizcarlos on 06/07/15.
 */
public class AdapterComments extends RecyclerView.Adapter<AdapterComments.ViewHolder> {

    private List<Comment> comments;

    public AdapterComments( List<Comment> comments ) {
        this.comments = comments;
    }

    public AdapterComments() {

    }

    public void setComments( List<Comment> comments ) {
        this.comments = comments;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.card_comment, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position ) {
        holder.onBind( comments.get( position ) );
    }

    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView picture;
        TextView tvUsername;
        TextView tvComment;

        public ViewHolder( View itemView ) {
            super( itemView );

            picture = ( SimpleDraweeView ) itemView.findViewById( R.id.sdPhoto );
            tvUsername = ( TextView ) itemView.findViewById( R.id.tvNameUserComment );
            tvComment = ( TextView ) itemView.findViewById( R.id.tvCommentUser );
        }

        public void onBind( Comment comment ){
            String format = String.format( itemView.getContext().getString( R.string.format_author_name ),
                    comment.getAuthorName(),
                    TimeUtils.timePassed( comment.getDateCreate() ) );
            tvUsername.setText( Html.fromHtml( format ) );
            tvComment.setText( comment.getText() );
            picture.setImageURI(
                    Uri.parse( String.format( itemView.getContext().getString( R.string.flickr_buddyicon ), comment.getAuthor() ) )
            );
        }
    }
}
