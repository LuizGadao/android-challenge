package br.com.luizcarlos.testinstaflickr.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.googlecode.flickrjandroid.photos.Photo;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import br.com.luizcarlos.testinstaflickr.view.RecentPhotoItemView;
import br.com.luizcarlos.testinstaflickr.view.RecentPhotoItemView_;

/**
 * Created by luizcarlos on 07/07/15.
 */
@EBean
public class RecentPhotosAdapter extends RecyclerViewAdapterBase<Photo, RecentPhotoItemView> {

    @RootContext
    Context context;

    public RecentPhotosAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected RecentPhotoItemView onCreateItemView( ViewGroup parent, int viewType ) {
        return RecentPhotoItemView_.build( context );
    }
}
