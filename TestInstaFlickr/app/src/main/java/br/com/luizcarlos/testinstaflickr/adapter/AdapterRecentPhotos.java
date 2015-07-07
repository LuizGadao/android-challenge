package br.com.luizcarlos.testinstaflickr.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.view.SimpleDraweeView;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;

import br.com.luizcarlos.testinstaflickr.R;

/**
 * Created by luizcarlos on 30/06/15.
 */
public class AdapterRecentPhotos extends RecyclerView.Adapter<AdapterRecentPhotos.ViewHolder> {

    EventPhotosItemClick event;
    PhotoList recentPhotos;
    int lastPostitionBind = 0;

    public AdapterRecentPhotos() {
    }

    public AdapterRecentPhotos( PhotoList recentPhotos ) {
        this.recentPhotos = recentPhotos;
    }

    public AdapterRecentPhotos( EventPhotosItemClick event, PhotoList recentPhotos ) {
        this.event = event;
        this.recentPhotos = recentPhotos;
    }

    public AdapterRecentPhotos( EventPhotosItemClick event ) {
        this.event = event;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.card_photo_recent, parent, false );
        return new ViewHolder( view, event );
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position ) {
        holder.bind( recentPhotos.get( position ), lastPostitionBind > position );
        lastPostitionBind = position;
    }

    @Override
    public int getItemCount() {
        return recentPhotos != null ? recentPhotos.size() : 0;
    }

    public void setRecentPhotos( PhotoList recentPhotos ) {
        this.recentPhotos = recentPhotos;
    }

    public void appendPhotos( PhotoList morePhotos ) {
        if ( this.recentPhotos == null )
            this.recentPhotos = new PhotoList();

        this.recentPhotos.addAll( morePhotos );
        this.notifyDataSetChanged();
    }

    public PhotoList getRecentPhotos() {
        return recentPhotos;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String TAG = ViewHolder.class.getSimpleName();

        SimpleDraweeView picture;
        TextView titlePhoto;
        TextView nameOwner;
        EventPhotosItemClick event;

        Photo photo;

        public ViewHolder( View itemView, EventPhotosItemClick e ) {
            super( itemView );

            this.event = e;
            picture = ( SimpleDraweeView ) itemView.findViewById( R.id.picture );
            titlePhoto = ( TextView ) itemView.findViewById( R.id.titlePhoto );
            nameOwner = ( TextView ) itemView.findViewById( R.id.tvNameOwner );

            itemView.setOnClickListener( this );
        }

        public void bind( Photo photo, boolean reverse ) {
            this.photo = photo;
            titlePhoto.setText( photo.getTitle().equals( "" ) ? "without title" : photo.getTitle() );
            nameOwner.setText( photo.getOwner().getUsername() );

            //load picture with fresco
            picture.setImageURI( Uri.parse( photo.getThumbnailUrl() ) );

            //animation card
            YoYo.with( reverse ? Techniques.FadeInDown : Techniques.FadeInUp ).duration( 250 ).playOn( itemView );
        }

        @Override
        public void onClick( View view ) {
            if ( event != null )
                event.onItemClick( photo, itemView );
        }
    }

    public interface EventPhotosItemClick {
        void onItemClick( Photo photo, View view );
    }
}


