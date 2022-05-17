package com.sepicgenious.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sepicgenious.BookDetailActivity
import com.sepicgenious.R


public class DashBoardBooksAdapter(val context: Context?) :
    RecyclerView.Adapter<DashBoardBooksAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_book_thumbnail,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            context!!.startActivity(
                Intent(
                    context,
                    BookDetailActivity::class.java
                )
            )
        }


    }
}