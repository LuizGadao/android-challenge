package br.com.luizcarlos.testinstaflickr.view;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.googlecode.flickrjandroid.photos.comments.Comment;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import br.com.luizcarlos.testinstaflickr.R;
import br.com.luizcarlos.testinstaflickr.adapter.ViewWrapper;
import br.com.luizcarlos.testinstaflickr.utils.TimeUtils;

/**
 * Created by luizcarlos on 07/07/15.
 */
@EViewGroup(R.layout.card_comment)
public class CommentItemView extends RelativeLayout implements ViewWrapper.Binder<Comment> {

    @ViewById
    SimpleDraweeView sdPhoto;
    @ViewById
    TextView tvNameUserComment;
    @ViewById
    TextView tvCommentUser;

    public CommentItemView( Context context ) {
        super( context );
    }

    @Override
    public void onBind( Comment data, int position ) {
        String format = String.format( getContext().getString( R.string.format_author_name ),
                data.getAuthorName(),
                TimeUtils.timePassed( data.getDateCreate() ) );
        tvNameUserComment.setText( Html.fromHtml( format ) );
        tvCommentUser.setText( data.getText() );
        sdPhoto.setImageURI(
                Uri.parse( String.format( getContext().getString( R.string.flickr_buddyicon ), data.getAuthor() ) )
        );
    }
}
