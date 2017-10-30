package br.com.findfer.findfer.adapter;

/**
 * Created by infsolution on 30/09/17.
 */



import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import br.com.findfer.findfer.R;
import br.com.findfer.findfer.extras.DataUrl;
import br.com.findfer.findfer.interfaces.RecyclerViewOnClickListenerHack;
import br.com.findfer.findfer.model.Poster;

import java.util.List;

/**
 * Created by infsolution on 25/09/17.
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PostViewHolder> {
    private Context mContext;
    private List<Poster> posters;
    private LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    public PosterAdapter(Context context, List<Poster> postList){
        mContext = context;
        this.posters = postList;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_poster,viewGroup,false);
        PostViewHolder pvh = new PostViewHolder(view);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        //holder.imagCapa.setImageResource(posters.get(position).getPhoto());
        holder.title.setText(posters.get(position).getTitle());
        holder.value.setText(posters.get(position).getStringValue());
        ControllerListener listener = new BaseControllerListener(){
            @Override
            public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
                super.onIntermediateImageFailed(id, throwable);
            }

            @Override
            public void onIntermediateImageSet(String id, Object imageInfo) {
                super.onIntermediateImageSet(id, imageInfo);
            }

            @Override
            public void onRelease(String id) {
                super.onRelease(id);
            }

            @Override
            public void onSubmit(String id, Object callerContext) {
                super.onSubmit(id, callerContext);
            }
        };
        int w =0;
        if(holder.imageCapa.getLayoutParams().width == FrameLayout.LayoutParams.MATCH_PARENT
                || holder.imageCapa.getLayoutParams().width== FrameLayout.LayoutParams.WRAP_CONTENT){
            Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            try{
                w = size.x;
            }catch (Exception e){
                w = display.getWidth();
            }
        }
        //Uri uri = Uri.parse(DataUrl.getUrlCostom(posters.get(position).getUrlImage(),w));
        Uri uri = Uri.parse(posters.get(position).getUrlImage());
        DraweeController dc = Fresco.newDraweeControllerBuilder().setUri(uri).setControllerListener(listener).setOldController(holder.imageCapa.getController()).build();
        holder.imageCapa.setController(dc);


    }

    @Override
    public int getItemCount() {
        return posters.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack rV){
        recyclerViewOnClickListenerHack = rV;
    }

    public void addListItem(Poster poster, int position){
        posters.add(poster);
        notifyItemInserted(position);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imagCapa;
        SimpleDraweeView imageCapa ;
        TextView title;
        TextView value;
        public PostViewHolder (View itemView){
            super(itemView);
            //imagCapa = (ImageView)itemView.findViewById(R.id.image_capa);
            imageCapa = (SimpleDraweeView)itemView.findViewById(R.id.image_capa_fresco);
            title = (TextView)itemView.findViewById(R.id.title_poster);
            value = (TextView)itemView.findViewById(R.id.value_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(recyclerViewOnClickListenerHack != null){
                recyclerViewOnClickListenerHack.onClickListner(view,getPosition());
            }
        }
    }
}
