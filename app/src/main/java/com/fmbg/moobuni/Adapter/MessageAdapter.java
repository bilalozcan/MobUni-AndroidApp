package com.fmbg.moobuni.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fmbg.moobuni.MessageZoomPhotoActivity;
import com.fmbg.moobuni.Model.Chat;
import com.fmbg.moobuni.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    public static final int PHOTO_TYPE_LEFT = 2;
    public static final int PHOTO_TYPE_RIGHT = 3;

    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;
    DatabaseReference mDatabase;

    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl) {
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);

        }else if (viewType == MSG_TYPE_LEFT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);


        }else if (viewType == PHOTO_TYPE_RIGHT) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_photo_right, parent, false);
            return new MessageAdapter.ViewHolder(view);

        } else {

            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_photo_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.ViewHolder holder, final int position) {
        final Chat chat = mChat.get(position);
        if(chat.getMessagetype().equals("message")){
            holder.show_message.setText(chat.getMessage());
            holder.show_message_time.setText(chat.getMessagetime());
        } else{
            Glide.with(mContext).load(chat.getMessagephotourl()).into(holder.show_image_message);
            holder.show_message_time.setText(chat.getMessagetime());
        }

        /*if (imageurl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);

        } else{
            Glide.with(mContext).load(imageurl).into(holder.profile_image);

        }*/

        if(chat.getMessagetype().equals("photo")) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MessageZoomPhotoActivity.class);
                    intent.putExtra("url", chat.getMessagephotourl());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message, show_message_time;
        public ImageView profile_image, show_image_message;

        public ViewHolder(View itemView){
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            show_message_time = itemView.findViewById(R.id.show_message_time);
            profile_image = itemView.findViewById(R.id.profile_image);
            show_image_message = itemView.findViewById(R.id.show_image_message);

        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getMessagetype().equals("message")){
            if (mChat.get(position).getSender().equals(fuser.getUid())){
                return MSG_TYPE_RIGHT;
            } else{
                return MSG_TYPE_LEFT;
            }
        } else {
            if (mChat.get(position).getSender().equals(fuser.getUid())){
                return PHOTO_TYPE_RIGHT;
            } else{
                return PHOTO_TYPE_LEFT;
            }
        }

    }
}
