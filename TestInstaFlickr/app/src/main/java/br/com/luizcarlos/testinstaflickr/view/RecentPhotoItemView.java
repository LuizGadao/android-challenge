package br.com.luizcarlos.testinstaflickr.view;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.view.SimpleDraweeView;
import com.googlecode.flickrjandroid.photos.Photo;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import br.com.luizcarlos.testinstaflickr.R;
import br.com.luizcarlos.testinstaflickr.adapter.ViewWrapper;

/**
 * Created by luizcarlos on 07/07/15.
 */
@EViewGroup(R.layout.card_photo_recent)
public class RecentPhotoItemView extends CardView implements ViewWrapper.Binder<Photo>, View.OnClickListener {

    @ViewById
    SimpleDraweeView picture;
    @ViewById
    TextView titlePhoto;
    @ViewById
    TextView tvNameOwner;

    int lastPositionBind = 0;
    private Photo photo;
    EventPhotosItemClick event;

    public RecentPhotoItemView( Context context ) {
        super( context );
    }

    public RecentPhotoItemView( Context context, EventPhotosItemClick event ) {
        super( context );
        this.event = event;
        this.setOnClickListener( this );
    }

    @Override
    public void onBind( Photo data, int position ) {
        this.photo = data;
        titlePhoto.setText( data.getTitle().equals( "" ) ? "without title" : data.getTitle() );
        tvNameOwner.setText( data.getOwner().getUsername() );

        //load picture with fresco
        picture.setImageURI( Uri.parse( data.getThumbnailUrl() ) );

        //animation card
        boolean reverse = lastPositionBind > position;
        YoYo.with( reverse ? Techniques.FadeInDown : Techniques.FadeInUp ).duration( 250 ).playOn( this );
    }

    @Override
    public void onClick( View view ) {
        if ( event != null )
            event.onItemClick( photo, this );
    }

    public interface EventPhotosItemClick {
        void onItemClick( Photo photo, View view );
    }
}
