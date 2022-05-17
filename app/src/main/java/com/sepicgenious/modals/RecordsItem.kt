package com.sepicgenious.modals

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RecordsItem(

    @field:SerializedName("author_name")
    var authorName: String? = null,

    @field:SerializedName("teacher_name")
    var teacherName: String? = null,

    @field:SerializedName("private")
    var jsonMemberPrivate: Boolean? = null,

    @field:SerializedName("description")
    var description: String? = null,

    @field:SerializedName("edition")
    var edition: String? = null,

    @field:SerializedName("created_at")
    var createdAt: String? = null,

    @field:SerializedName("publish_Date")
    var publishDate: String? = null,

    @field:SerializedName("title")
    var title: String? = null,

    @field:SerializedName("genre_id")
    var genreId: String? = null,

    @field:SerializedName("province_name")
    var provinceName: String? = null,

    @field:SerializedName("subTitle")
    var subTitle: String? = null,

    @field:SerializedName("province_id")
    var provinceId: String? = null,

    @field:SerializedName("price")
    var price: Int? = null,

    @field:SerializedName("bookSellCount")
    var bookSellCount: Int? = null,

    @field:SerializedName("grade")
    var grade: String? = null,

    @field:SerializedName("publisher")
    var publisher: String? = null,

    @field:SerializedName("_id")
    var id: String? = null,

    @field:SerializedName("iconURL")
    var iconURL: String? = null,

    @field:SerializedName("author_id")
    var authorId: String? = null,

    @field:SerializedName("genre_name")
    var genreName: String? = null,

    @field:SerializedName("Collabration")
    var collabration: String? = null,

    @field:SerializedName("bookURL" ,alternate = ["book_url"])
    var bookURL: String? = null,

    @field:SerializedName("open")
    var open: Boolean? = null,

    @field:SerializedName("timestamp")
    var timestamp: Int? = null,



    @field:SerializedName("book_price")
    val bookPrice: Int? = null,

    @field:SerializedName("book_icon")
    val bookIcon: String? = null,

    @field:SerializedName("buyer_name")
    val buyerName: String? = null,


    @field:SerializedName("book_id")
    val bookId: String? = null,

    @field:SerializedName("book_name")
    val bookName: String? = null,



    @field:SerializedName("buyer_id")
    val buyerId: String? = null




) : Serializable