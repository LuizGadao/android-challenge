package br.com.luizcarlos.testinstaflickr.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import br.com.luizcarlos.testinstaflickr.R;

/**
 * Created by luizcarlos on 01/07/15.
 */
public class NetworkUtils {

    public static final int RESUTL_ACTIVITY_ENABLE_INTERNET = 123;

    public static boolean isNetworkAvailable( Context context )
    {
        ConnectivityManager connectivityManager = ( ConnectivityManager ) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if ( info != null && info.isConnected() )
            return true;

        return false;
    }

    public static boolean isNetworkAvailable( final AppCompatActivity context, final int resutl ){
        if ( ! isNetworkAvailable( context ) ){
            SnackbarManager.show(
                    Snackbar.with( context ).duration( Snackbar.SnackbarDuration.LENGTH_INDEFINITE )
                            .text( R.string.text_no_internet_connection )
                            .textColor( context.getResources().getColor( android.R.color.white ) ).actionLabel( R.string.action_connect )
                            .actionColor( context.getResources().getColor( R.color.accent ) )
                            .actionListener( new ActionClickListener() {
                                @Override
                                public void onActionClicked( Snackbar snackbar ) {
                                    context.startActivityForResult( new Intent( Settings.ACTION_WIFI_SETTINGS ), resutl );
                                }
                            } )
            );

            return false;
        }

        return true;
    }

}
