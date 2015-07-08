package br.com.luizcarlos.testinstaflickr;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.comments.Comment;
import com.googlecode.flickrjandroid.photos.comments.CommentsInterface;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.text.MessageFormat;
import java.util.List;

import br.com.luizcarlos.testinstaflickr.adapter.CommentsAdapter;
import br.com.luizcarlos.testinstaflickr.utils.TimeUtils;

@EActivity
public class DetailsPhoto extends AppCompatActivity {


    private static final String TAG = DetailsPhoto.class.getSimpleName();

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_NAME_OWNER = "name-owner";
    public static final String EXTRA_URL_PICTURE = "url-picture";
    public static final String EXTRA_OBJ_PHOTO = "obj-photo";

    @Extra(EXTRA_TITLE)
    String title;
    @Extra(EXTRA_NAME_OWNER)
    String nameOwner;
    @Extra(EXTRA_URL_PICTURE)
    String urlPicture;
    @Extra(EXTRA_OBJ_PHOTO)
    Photo photo;

    @ViewById
    TextView tvTitle;
    @ViewById
    TextView tvNameOwner;
    @ViewById
    SimpleDraweeView sdPicture;
    @ViewById
    TextView tvCountComment;
    @ViewById
    SimpleDraweeView sdPhoto;
    @ViewById
    RecyclerView recyclerViewComments;
    @ViewById
    TextView tvTimeCreated;
    @ViewById
    Toolbar toolbar;

    @App
    MyApplication myApplication;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            TransitionInflater transitionInflater = TransitionInflater.from( this );
            Transition transition = transitionInflater.inflateTransition( R.transition.transition );
            getWindow().setSharedElementExitTransition( transition );
        }

        super.onCreate( savedInstanceState );
        Fresco.initialize( this );
        setContentView( R.layout.activity_details_photo );
    }

    @AfterViews
    void init(){
        setSupportActionBar( toolbar );
        getSupportActionBar().setHomeButtonEnabled( true );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        tvTitle.setText( photo.getTitle().equals( "" ) ? "Without title" : photo.getTitle() );
        tvNameOwner.setText( photo.getOwner().getUsername() );
        tvTimeCreated.setText( TimeUtils.timePassed( photo.getDatePosted() ) );

        //load imagens
        sdPhoto.setImageURI( Uri.parse( String.format(getString( R.string.flickr_buddyicon ), photo.getOwner().getId() ) ) );
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest( ImageRequest.fromUri( photo.getThumbnailUrl() ) )
                .setImageRequest( ImageRequest.fromUri( photo.getMedium640Url() ) )
                .setOldController( sdPicture.getController() )
                .build();
        sdPicture.setController( draweeController );

        recyclerViewComments.setHasFixedSize( false );
        recyclerViewComments.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );

        //id "18850487234" para testar comentários
        getPhotoComments( "18850487234" );
    }

    @Background
    void getPhotoComments( String idPhoto ){
        CommentsInterface commentsInterface = myApplication.getFlicker().getCommentsInterface();

        try {
            List<Comment> comments = commentsInterface.getList( idPhoto, null, null );
            createListComments( comments );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @UiThread
    void createListComments( List<Comment> comments ){
        Log.i( TAG, "total-comments: " + comments.size() + " id: " + photo.getId() );

        String fmt = getString( R.string.comment );
        tvCountComment.setText( MessageFormat.format( fmt, comments.size() ) );

        CommentsAdapter adapter = new CommentsAdapter( this );
        adapter.addAll( comments );
        recyclerViewComments.setAdapter( adapter );
    }
}
