package br.com.luizcarlos.testinstaflickr.utils;

import android.content.Context;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import br.com.luizcarlos.testinstaflickr.R;

/**
 * Created by luizcarlos on 08/07/15.
 */
public class Utils {

    public static void snackBarErrorFlickrApi( Context context, final CallbackErrorFlickr callback ){
        SnackbarManager.show(
                Snackbar.with( context ).duration( Snackbar.SnackbarDuration.LENGTH_INDEFINITE )
                        .text( R.string.error_flicker_api )
                        .textColor( context.getResources().getColor( android.R.color.white ) ).actionLabel( R.string.action_try_again )
                        .actionColor( context.getResources().getColor( R.color.accent ) )
                        .actionListener( new ActionClickListener() {
                            @Override
                            public void onActionClicked( Snackbar snackbar ) {
                                callback.doSomethingErrorFlickrApi();
                            }
                        } )
        );
    }

    public interface CallbackErrorFlickr{
        void doSomethingErrorFlickrApi();
    }

}
