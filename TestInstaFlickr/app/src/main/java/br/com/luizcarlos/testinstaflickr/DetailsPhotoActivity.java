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
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.comments.Comment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import br.com.luizcarlos.testinstaflickr.adapter.CommentsAdapter;
import br.com.luizcarlos.testinstaflickr.load.LoadFlicker;
import br.com.luizcarlos.testinstaflickr.utils.NetworkUtils;
import br.com.luizcarlos.testinstaflickr.utils.TimeUtils;
import br.com.luizcarlos.testinstaflickr.utils.Utils;

@EActivity
public class DetailsPhotoActivity extends AppCompatActivity {

    private static final String TAG = DetailsPhotoActivity.class.getSimpleName();
    public static final String EXTRA_OBJ_PHOTO = "obj-photo";

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

    LoadFlicker loadFlicker;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            TransitionInflater transitionInflater = TransitionInflater.from( this );
            Transition transition = transitionInflater.inflateTransition( R.transition.transition );
            getWindow().setSharedElementExitTransition( transition );
        }

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_details_photo );
        loadFlicker = new LoadFlicker();
    }

    @AfterViews
    void init(){
        setSupportActionBar( toolbar );
        getSupportActionBar().setHomeButtonEnabled( true );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        tvTitle.setText( photo.getTitle().equals( "" ) ? getString( R.string.with_out_title) : photo.getTitle() );
        tvNameOwner.setText( photo.getOwner().getUsername() );
        tvTimeCreated.setText( TimeUtils.timePassed( photo.getDatePosted() ) );

        recyclerViewComments.setHasFixedSize( false );
        recyclerViewComments.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );

        //load images and comments
        load();
    }

    private void loadImagens() {
        sdPhoto.setImageURI( Uri.parse( String.format( getString( R.string.flickr_buddyicon ), photo.getOwner().getId() ) ) );
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest( ImageRequest.fromUri( photo.getThumbnailUrl() ) )
                .setImageRequest( ImageRequest.fromUri( photo.getMedium640Url() ) )
                .setOldController( sdPicture.getController() )
                .build();
        sdPicture.setController( draweeController );
    }

    private void load(){
        if ( NetworkUtils.isNetworkAvailable( this, NetworkUtils.RESUTL_ACTIVITY_ENABLE_INTERNET ) ){
            loadImagens();
            //id "18850487234" para testar comentários
            getPhotoComments();
        }
    }

    @Background
    void getPhotoComments(){

        String id = photo.getId();
        List<Comment> comments = null;
        try {
            comments = loadFlicker.getComments( id );
            createListComments( comments );
        } catch ( JSONException e ) {
            e.printStackTrace();
            snackBarError();
        } catch ( FlickrException e ) {
            e.printStackTrace();
            snackBarError();
        } catch ( IOException e ) {
            e.printStackTrace();
            snackBarError();
        } catch ( NullPointerException e ){
            getPhotoComments();
        } catch ( Exception e ){
            getPhotoComments();
        }
    }

    private void snackBarError(){
        Utils.snackBarErrorFlickrApi( this, new Utils.CallbackErrorFlickr() {
            @Override
            public void doSomethingErrorFlickrApi() {
                getPhotoComments();
            }
        } );
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

    @OnActivityResult( NetworkUtils.RESUTL_ACTIVITY_ENABLE_INTERNET )
    void onResult( int resultCode ){
        load();
    }
}
