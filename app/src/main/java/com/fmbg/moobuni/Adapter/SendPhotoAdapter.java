package com.fmbg.moobuni.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fmbg.moobuni.Model.SendPhoto;
import com.fmbg.moobuni.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SendPhotoAdapter extends RecyclerView.Adapter<SendPhotoAdapter.ViewHolder> {


    public static final int SENDPHOTO_TYPE_LEFT = 0;
    public static final int SENDPHOTO_TYPE_RIGHT = 1;

    private Context mContext;
    private List<SendPhoto> mSendPhoto;
    private String imageurl;

    FirebaseUser firebaseUser;

    public SendPhotoAdapter(Context mContext, List<SendPhoto> mSendPhoto, String imageurl) {
        this.mContext = mContext;
        this.mSendPhoto = mSendPhoto;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override


    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDPHOTO_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_photo_right, parent, false);
            return new SendPhotoAdapter.ViewHolder(view);
        } else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_photo_left, parent, false);
            return new SendPhotoAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        SendPhoto sendPhoto = mSendPhoto.get(position);
        Picasso.get().load(sendPhoto.getImageurl()).into(holder.profile_image);

        if (imageurl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else{
            Glide.with(mContext).load(imageurl).into(holder.profile_image);
        }



    }

    @Override
    public int getItemCount() {
        return mSendPhoto.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView show_image_message, profile_image;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            show_image_message = itemView.findViewById(R.id.show_image_message);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mSendPhoto.get(position).getSender().equals(firebaseUser.getUid())){
            return SENDPHOTO_TYPE_RIGHT;
        } else{
            return SENDPHOTO_TYPE_LEFT;
        }
    }
}
