package br.com.luizcarlos.testinstaflickr.load;

import android.util.Log;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotosInterface;
import com.googlecode.flickrjandroid.photos.comments.Comment;
import com.googlecode.flickrjandroid.photos.comments.CommentsInterface;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.luizcarlos.testinstaflickr.MyApplication_;

/**
 * Created by luizcarlos on 08/07/15.
 */
public class LoadFlicker {

    private static final String TAG = LoadFlicker.class.getSimpleName();

    //max recent photos
    double maxRecentPhoto = 1000.0;
    //total photos por page
    int itemPerPage = 100;
    //max page
    int maxPage = ( int ) Math.ceil( maxRecentPhoto / itemPerPage );

    public PhotoList getRecentPhotos( int currentPage ) throws JSONException, FlickrException, IOException {
        Set<String> params = new HashSet();
        params.add( "url_z" );//z medium 640, 640 on longest side
        params.add( "url_n" );//n	small, 320 on longest side
        params.add( "url_q" );//q	large square 150x150
        params.add( "owner_name" );
        params.add( "date_taken" );
        params.add( "date_upload" );
        params.add( "last_update" );

        PhotosInterface photosInterface = MyApplication_.getInstance().getFlicker().getPhotosInterface();

        Log.i( TAG, "load-page: " + currentPage );
        PhotoList photosRecent = photosInterface.getRecent( params, itemPerPage, currentPage );
        return photosRecent;
    }

    public List<Comment> getComments( String idPhoto ) throws JSONException, FlickrException, IOException {
        Flickr flickr = MyApplication_.getInstance().getFlicker();
        CommentsInterface commentsInterface = flickr.getCommentsInterface();

        List<Comment> comments = commentsInterface.getList( idPhoto, null, null );
        return comments;
    }

    public double getMaxPage() {
        return maxPage;
    }
}
