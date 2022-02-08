package com.example.finalproject.adaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.R;
import com.example.finalproject.models.Message;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class messagesadatpot extends RecyclerView.Adapter{

    Context context;
    ArrayList<Message> messages;
    final  int itemsent=1;
    final  int itemrec=2;
    String senderroom;
    String recroom;


    public messagesadatpot(Context context, ArrayList<Message> messages, String senderroom, String recroom){
        this.context=context;
        this.messages=messages;
        this.senderroom=senderroom;
        this.recroom=recroom;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==itemsent)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.itemsent,parent,false);
            return new sentviewholder(view);
        }else
        {
            View view= LayoutInflater.from(context).inflate(R.layout.itemrec,parent,false);
            return  new recviewholder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message=messages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderid()))
            return itemsent;
        else
            return itemrec;

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message= messages.get(position);
        int reactions[]=new int[]{
                R.drawable.ic_fb_like,
                R.drawable.ic_fb_love,
                R.drawable.ic_fb_laugh,
                R.drawable.ic_fb_wow,
                R.drawable.ic_fb_sad,
                R.drawable.ic_fb_angry
        };
        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build();
        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
            if(holder.getClass()==sentviewholder.class)
            {
                sentviewholder sentviewholder=(sentviewholder)holder;
                sentviewholder.reaction.setImageResource(reactions[pos]);
                sentviewholder.reaction.setVisibility(View.VISIBLE);

            }else
            {
                recviewholder recviewholder=(recviewholder)holder;
                recviewholder.reaction.setImageResource(reactions[pos]);
                recviewholder.reaction.setVisibility(View.VISIBLE);
            }

            message.setFeeling(pos);
            FirebaseDatabase.getInstance().getReference().child("chats").child(senderroom).child("message").child(message.getMsgid()).setValue(message);
            FirebaseDatabase.getInstance().getReference().child("chats").child(recroom).child("message").child(message.getMsgid()).setValue(message);
            return true; // true is closing popup, false is requesting a new selection
        });

        if(holder.getClass()==sentviewholder.class)
        {
            sentviewholder sentviewholder=(sentviewholder)holder;
            sentviewholder.sentmsg.setText(message.getMsg());
            sentviewholder.timeofmsgs.setText(message.getTimestamp());
            if(message.getFeeling()>=0)
            {
                sentviewholder.reaction.setImageResource(reactions[(int) message.getFeeling()]);
                sentviewholder.reaction.setVisibility(View.VISIBLE);
            }else
                sentviewholder.reaction.setVisibility(View.GONE);
                sentviewholder.sentmsg.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popup.onTouch(v,event);
                        return false;
                    }
                });

        }
        else
        {
            recviewholder recviewholder=(recviewholder)holder;
            recviewholder.sentmsg.setText(message.getMsg());
            recviewholder.timeofmsgr.setText(message.getTimestamp());
            if(message.getFeeling()>=0)
            {
                recviewholder.reaction.setImageResource(reactions[(int) message.getFeeling()]);
                recviewholder.reaction.setVisibility(View.VISIBLE);
            }else
                recviewholder.reaction.setVisibility(View.GONE);
            recviewholder.sentmsg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v,event);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public  class sentviewholder extends RecyclerView.ViewHolder{
        ImageView reaction;
        TextView sentmsg;
        TextView timeofmsgs;
        public sentviewholder(@NonNull View itemView) {
            super(itemView);
             reaction=itemView.findViewById(R.id.reactions);
             sentmsg=itemView.findViewById(R.id.sentmsgs);
             timeofmsgs=itemView.findViewById(R.id.timeofmsgs);


        }
    }
    public  class recviewholder extends RecyclerView.ViewHolder{
        ImageView reaction;
        TextView sentmsg;
        TextView timeofmsgr;
        public recviewholder(@NonNull View itemView) {
            super(itemView);
             reaction=itemView.findViewById(R.id.reaction);
             sentmsg=itemView.findViewById(R.id.sentmsg);
             timeofmsgr=itemView.findViewById(R.id.timeofmsgr);

        }
    }

}
