package br.com.luizcarlos.testinstaflickr.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by luizcarlos on 07/07/15.
 */
public class ViewWrapper<T, V extends View & ViewWrapper.Binder<T>> extends RecyclerView.ViewHolder {

    private V view;

    public ViewWrapper( V itemView ) {
        super( itemView );
        view = itemView;
    }

    public V getView() {
        return view;
    }

    public interface Binder<T>{
        void onBind( T data, int position );
    }
}
