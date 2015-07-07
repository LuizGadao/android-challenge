package br.com.luizcarlos.testinstaflickr.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by luizcarlos on 07/07/15.
 */
public abstract class RecyclerViewAdapterBase<T, V extends View & ViewWrapper.Binder<T>> extends RecyclerView.Adapter<ViewWrapper<T, V>>{

    protected List<T> items = new ArrayList<T>();

    @Override
    public ViewWrapper<T, V> onCreateViewHolder( ViewGroup parent, int viewType ) {
        return new ViewWrapper<T, V>( onCreateItemView( parent, viewType ) );
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder( ViewWrapper<T, V> holder, int position ) {
        V view = holder.getView();
        T data = items.get( position );
        view.onBind( data );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void add( T items ) {
        this.items.add( items );
        notifyDataSetChanged();
    }

    public void addAll( Collection<T> collection ){
        this.items.addAll( collection );
        notifyDataSetChanged();
    }

    public void clear(){
        items.clear();
    }
}
