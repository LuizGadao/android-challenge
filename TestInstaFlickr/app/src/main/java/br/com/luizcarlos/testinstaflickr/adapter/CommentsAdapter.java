package br.com.luizcarlos.testinstaflickr.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.googlecode.flickrjandroid.photos.comments.Comment;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import br.com.luizcarlos.testinstaflickr.view.CommentItemView;
import br.com.luizcarlos.testinstaflickr.view.CommentItemView_;

/**
 * Created by luizcarlos on 07/07/15.
 */
@EBean
public class CommentsAdapter extends RecyclerViewAdapterBase<Comment, CommentItemView> {

    @RootContext
    Context context;

    public CommentsAdapter( Context context ) {
        this.context = context;
    }

    @Override
    protected CommentItemView onCreateItemView( ViewGroup parent, int viewType ) {
        return CommentItemView_.build( context );
    }
}
