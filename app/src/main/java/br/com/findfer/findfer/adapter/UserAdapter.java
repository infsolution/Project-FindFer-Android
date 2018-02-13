package br.com.findfer.findfer.adapter;

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
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import br.com.findfer.findfer.R;
import br.com.findfer.findfer.interfaces.RecyclerViewOnClickListenerHack;
import br.com.findfer.findfer.model.Poster;
import br.com.findfer.findfer.model.User;

/**
 * Created by infsolution on 03/11/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
    private Context mContext;
    private List<User> users;
    private LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    public UserAdapter(Context context, List<User> users){
        mContext = context;
        this.users = users;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_user,viewGroup,false);
        UserAdapter.UserViewHolder pvh = new UserAdapter.UserViewHolder(view);
        return pvh;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.name.setText(users.get(position).getName());
       // holder.description.setText(users.get(position).getNameUser());
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
        if(holder.imgUser.getLayoutParams().width == FrameLayout.LayoutParams.MATCH_PARENT
                || holder.imgUser.getLayoutParams().width== FrameLayout.LayoutParams.WRAP_CONTENT){
            Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            try{
                w = size.x;
            }catch (Exception e){
                w = display.getWidth();
            }
        }
        Uri uri = Uri.parse(users.get(position).getImage());
        DraweeController dc = Fresco.newDraweeControllerBuilder().setUri(uri).setControllerListener(listener).setOldController(holder.imgUser.getController()).build();
        holder.imgUser.setController(dc);
    }




    @Override
    public int getItemCount() {
        return users.size();
    }
    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack rV){
        recyclerViewOnClickListenerHack = rV;
    }
    public void addListItem(User user, int position){
        users.add(user);
        notifyItemInserted(position);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        SimpleDraweeView imgUser;
        TextView name;
        TextView description;
        public UserViewHolder(View itemView) {
            super(itemView);
            imgUser = (SimpleDraweeView)itemView.findViewById(R.id.img_profile_user);
            name = (TextView)itemView.findViewById(R.id.tv_name);
            //description = (TextView)itemView.findViewById(R.id.tv_detail);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if(recyclerViewOnClickListenerHack != null){
                recyclerViewOnClickListenerHack.onClickListner(view,getAdapterPosition());
            }
        }
    }

}
