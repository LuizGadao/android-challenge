package br.com.luizcarlos.testinstaflickr;

import android.app.Application;

import com.googlecode.flickrjandroid.Flickr;

import org.androidannotations.annotations.EApplication;

/**
 * Created by luizcarlos on 04/07/15.
 */
@EApplication
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    Flickr getFlicker(){
        return new Flickr( getString( R.string.flickr_api_key, R.string.flickr_secret_key ) );
    }
}
