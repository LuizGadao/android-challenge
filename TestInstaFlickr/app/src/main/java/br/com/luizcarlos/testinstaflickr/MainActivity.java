package br.com.luizcarlos.testinstaflickr;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import br.com.luizcarlos.testinstaflickr.adapter.RecentPhotosAdapter;
import br.com.luizcarlos.testinstaflickr.event.ItemRecyclerViewClick;
import br.com.luizcarlos.testinstaflickr.load.LoadFlicker;
import br.com.luizcarlos.testinstaflickr.utils.EndlessRecyclerOnScrollListener;
import br.com.luizcarlos.testinstaflickr.utils.NetworkUtils;
import br.com.luizcarlos.testinstaflickr.utils.Utils;

//import android.support.annotation.UiThread;

@EActivity
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int RESUTL_ACTIVITY_ENABLE_INTERNET = 123;

    @ViewById
    RecyclerView recyclerView;
    @ViewById
    SwipeRefreshLayout swipeRefresh;

    @App
    MyApplication myApplication;

    @InstanceState
    PhotoList photosRecent;
    @InstanceState
    int firtVisibleItemRecyclerView;
    @InstanceState
    int currentPage = 1;

    RecentPhotosAdapter adapter;

    LoadFlicker loadFlicker;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            TransitionInflater transitionInflater = TransitionInflater.from( this );
            Transition transition = transitionInflater.inflateTransition( R.transition.transition );
            getWindow().setSharedElementExitTransition( transition );
        }

        super.onCreate( savedInstanceState );
        Fresco.initialize(this);
        setContentView( R.layout.activity_main );

        swipeRefresh.setColorSchemeResources( R.color.accent, R.color.primary, R.color.green );
        loadFlicker = new LoadFlicker();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myApplication.getBus().unregister( this );
    }

    @AfterViews
    void init(){
        myApplication.getBus().register( this );

        adapter = new RecentPhotosAdapter( this );
        recyclerView.setHasFixedSize( false );
        LinearLayoutManager layoutManager = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter( adapter );
        recyclerView.addOnScrollListener( new EndlessRecyclerOnScrollListener( layoutManager ) {
            @Override
            public void onLoadMore( int current_page ) {
                if ( currentPage < loadFlicker.getMaxPage() ){
                    loadPhotos();
                    currentPage++;
                }
            }
        } );

        if ( photosRecent == null )
            loadPhotos();
        else {
            addPhotosInAdapter( photosRecent );
        }
    }

    private void loadPhotos() {
        if ( NetworkUtils.isNetworkAvailable( this, NetworkUtils.RESUTL_ACTIVITY_ENABLE_INTERNET ) ){
            swipeRefresh.setOnRefreshListener( this );
            swipeRefresh.post( new Runnable() {
                @Override
                public void run() {
                    swipeRefresh.setRefreshing( true );
                }
            } );
            getPhotosFlickr();
        }
    }

    @Background
    void getPhotosFlickr(){
        try {
            PhotoList recentPhotos = loadFlicker.getRecentPhotos( currentPage );
            addPhotosInAdapter( recentPhotos );
        } catch ( Exception e ) {
            e.printStackTrace();
            swipeRefresh.post( new Runnable() {
                @Override
                public void run() {
                    swipeRefresh.setRefreshing( false );
                }
            } );
            Utils.snackBarErrorFlickrApi( this, new Utils.CallbackErrorFlickr() {
                @Override
                public void doSomethingErrorFlickrApi() {
                    getPhotosFlickr();
                }
            } );
        }
    }

    @UiThread
    void addPhotosInAdapter( PhotoList recents ){
        adapter.addAll( recents );
        swipeRefresh.setRefreshing( false );

        //set position in recyclerview when device orientation change
        if ( firtVisibleItemRecyclerView != 0 ) {
            recyclerView.scrollToPosition( firtVisibleItemRecyclerView );
            firtVisibleItemRecyclerView = 0;
        }
    }

    @Override
    public void onRefresh() {
        loadPhotos();
    }

    private void openDetailsPhotoActivity( Photo photo, View view ){
        Intent intent = new Intent( this, DetailsPhotoActivity_.class );
        intent.putExtra( DetailsPhotoActivity.EXTRA_OBJ_PHOTO, photo );

        //FRESCO NOT SUPPORT ANDROID TRANSITION VERY WELL
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            TransitionInflater transitionInflater = TransitionInflater.from( this );
            Transition transition = transitionInflater.inflateTransition( R.transition.transition );
            getWindow().setSharedElementExitTransition( transition );

            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this, //Pair.create( view.findViewById( R.id.sdPicture ), "element1" ),
                    Pair.create( view.findViewById( R.id.tvTitlePhoto ), "element2" ),
                    Pair.create( view.findViewById( R.id.tvNameOwner ), "element3" ) );

            startActivity( intent, activityOptions.toBundle() );
        }else {
            startActivity( intent );
        }
    }

    @Subscribe
    public void onItemRecyclerViewClick( ItemRecyclerViewClick event ){
        openDetailsPhotoActivity( adapter.getItems().get( event.getPosition() ), event.getView() );
    }

    @OnActivityResult( NetworkUtils.RESUTL_ACTIVITY_ENABLE_INTERNET )
    void onResult(){
        loadPhotos();
    }

    @Override
    protected void onSaveInstanceState( Bundle outState ) {
        super.onSaveInstanceState( outState );

        //set values for save
        if ( photosRecent == null )
            photosRecent = new PhotoList();
        photosRecent.clear();
        photosRecent.addAll( adapter.getItems() );
        firtVisibleItemRecyclerView = ( (LinearLayoutManager)recyclerView.getLayoutManager() ).findFirstVisibleItemPosition();
    }


}
