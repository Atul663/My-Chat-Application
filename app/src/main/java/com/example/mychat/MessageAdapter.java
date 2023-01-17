package com.example.mychat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.Layout;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    List<ModelClass> list;
    String userName;
    String otherName;

    boolean status;
    int send, received;

    public MessageAdapter(List<ModelClass> list, String userName, String otherName) {
        this.list = list;
        this.userName = userName;
        this.otherName = otherName;

        send = 1;
        received = 2;
        status = true;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == send)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_send_messages,parent,false);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_received_messages,parent,false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        int x = position;
        holder.textView.setText(list.get(x).getMessage());
        Long timeStamp = list.get(x).getTimeStamp();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String time = sdf.format(timeStamp);
        holder.time.setText(time);


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete")
                        .setMessage("Are you sure to delete this message")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference();

                                System.out.println(list.get(x).getMessageId());
                                reference.child("Message").child(userName).child(otherName).child(list.get(x).getMessageId())
                                        .setValue(null);
                                reference.child("Message").child(otherName).child(userName).child(list.get(x).getMessageId())
                                        .setValue(null);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textView, time;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            if(status)
            {
                textView = itemView.findViewById(R.id.textViewSend);
                time = itemView.findViewById(R.id.textViewTime);
            }
            else
            {
                textView = itemView.findViewById(R.id.textViewreceived);
                time = itemView.findViewById(R.id.textViewTime);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getFrom().equals(userName))
        {
            status = true;
            return send;
        }

        else
        {
            status = false;
            return received;
        }
    }
}
