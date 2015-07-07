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
import com.facebook.drawee.view.SimpleDraweeView;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
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
import org.json.JSONException;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import br.com.luizcarlos.testinstaflickr.adapter.AdapterComments;
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

    AdapterComments adapterComments;

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
        Log.i( TAG, "id: " + photo.getId() );
        Log.i( TAG, "thumbnail: " + photo.getThumbnailUrl() );
        Log.i( TAG, "image: " + photo.getSmall320Url() );

        setSupportActionBar( toolbar );
        getSupportActionBar().setHomeButtonEnabled( true );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        tvTitle.setText( photo.getTitle().equals( "" ) ? "Without title" : photo.getTitle() );
        tvNameOwner.setText( photo.getOwner().getUsername() );
        tvTimeCreated.setText( TimeUtils.timePassed( photo.getDatePosted() ) );

        //load imagens
        sdPhoto.setImageURI( Uri.parse( String.format(getString( R.string.flickr_buddyicon ), photo.getOwner().getId() ) ) );
        sdPicture.setImageURI( Uri.parse( photo.getMedium640Url() ) );

        adapterComments = new AdapterComments();
        recyclerViewComments.setHasFixedSize( false );
        LinearLayoutManager layoutManager = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false );
        recyclerViewComments.setLayoutManager( layoutManager );
        recyclerViewComments.setAdapter( adapterComments );

        //id "18850487234" para testar comentários
        getPhotoComments( photo.getId() );
    }

    @Background
    void getPhotoComments( String idPhoto ){
        Flickr flickr = myApplication.getFlicker();
        CommentsInterface commentsInterface = flickr.getCommentsInterface();

        try {
            List<Comment> comments = commentsInterface.getList( idPhoto, null, null );
            createListComments( comments );
        } catch ( FlickrException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        } catch ( JSONException e ) {
            e.printStackTrace();
        }
    }

    @UiThread
    void createListComments( List<Comment> comments ){
        Log.i( TAG, "total-comments: " + comments.size() + " id: " + photo.getId() );

        int countComments = comments.size();
        String fmt = getString( R.string.comment );
        tvCountComment.setText( MessageFormat.format( fmt, countComments ) );

        // update ADAPTER
        adapterComments.setComments( comments );
    }
}
