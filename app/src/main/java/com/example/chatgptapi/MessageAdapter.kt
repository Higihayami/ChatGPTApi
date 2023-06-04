package com.example.chatgptapi

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.message_item.view.*


class MessageAdapter(private val context: Context): RecyclerView.Adapter<MessageAdapter.MessageHolder>() {


    private var messageList = emptyList<MessageModel>()
    class MessageHolder(item: View): RecyclerView.ViewHolder(item){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false )
        return MessageHolder(view)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        holder.itemView.tv_message_bot.text = messageList[position].response
        holder.itemView.tv_message_user.text = messageList[position].me
        holder.itemView.tv_time_bot.text = messageList[position].date
        holder.itemView.tv_time_user.text = messageList[position].date
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list : List<MessageModel>){
        messageList = list
        notifyDataSetChanged()
    }
}