package com.sepicgenious.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.sepicgenious.BookDetailActivity
import com.sepicgenious.R
import com.sepicgenious.apis.Urlmanager
import com.sepicgenious.modals.RecordsItem
import java.net.URL


public class GradeBooksAdapter(
    val context: Context?,
    val records: ArrayList<RecordsItem?>?
) :
    RecyclerView.Adapter<GradeBooksAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tv_Edition = itemView.findViewById<TextView>(R.id.tv_Edition)
        val tv_title = itemView.findViewById<TextView>(R.id.tv_title)
        val tv_writer = itemView.findViewById<TextView>(R.id.tv_writer)
        val tv_subject = itemView.findViewById<TextView>(R.id.tv_subject)
        val iv_image = itemView.findViewById<RoundedImageView>(R.id.iv_image)
        val tv_price = itemView.findViewById<TextView>(R.id.tv_price)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_grade_book_thumbnail,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return records!!.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val recordItem = records!!.get(position)
        holder.tv_Edition.text = "Edition : " + recordItem!!.edition
        holder.tv_title.text = recordItem.title
        if(recordItem.title==null)
        holder.tv_title.text = recordItem.bookName
        holder.tv_writer.text = "By : " + recordItem.authorName
        holder.tv_subject.text = recordItem.genreName
        holder.tv_price.text = "Price ($): " + recordItem.price
        if(recordItem.price==null)
        holder.tv_price.text = "Price ($): " + recordItem.bookPrice
        try {
            var urlImage : URL
            if (recordItem.bookIcon!=null)
            urlImage = URL(Urlmanager.ROOT_URL + recordItem.bookIcon!!)
            else
            urlImage = URL(Urlmanager.ROOT_URL + recordItem.iconURL!!)

            val urlstrImage = urlImage.toString().replace("\\", "/");

            Glide.with(context!!).load(urlstrImage).into(holder.iv_image);
        } catch (e: Exception) {

        }



        holder.itemView.setOnClickListener {

            var id : String?= ""
            if(recordItem!!.bookId==null)
              id =   recordItem!!.id
            else
               id =  recordItem!!.bookId

            context!!.startActivity(
                Intent(
                    context,
                    BookDetailActivity::class.java
                ).putExtra("bookId",id)
                .putExtra("book", recordItem)
            )
        }


    }
}