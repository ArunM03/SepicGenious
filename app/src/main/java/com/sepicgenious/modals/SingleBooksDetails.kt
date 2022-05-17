package com.sepicgenious.modals

import com.google.gson.annotations.SerializedName

data class SingleBooksDetails(

	@field:SerializedName("bookDetails")
	var bookDetails: ArrayList<BookDetailsItem?>? = ArrayList(),

	@field:SerializedName("purchaseStatus")
	var purchaseStatus: Boolean? = null,

	@field:SerializedName("message")
	var message: String? = null,

	@field:SerializedName("status")
	var status: Int? = null
)

data class BookDetailsItem(

	@field:SerializedName("author_name")
	var authorName: String? = null,

	@field:SerializedName("teacher_name")
	var teacherName: String? = null,

	@field:SerializedName("private")
	var private: Boolean? = null,

	@field:SerializedName("teacher_id")
	var teacherId: String? = null,

	@field:SerializedName("description")
	var description: String? = null,

	@field:SerializedName("edition")
	var edition: String? = null,

	@field:SerializedName("created_at")
	var createdAt: String? = null,

	@field:SerializedName("title")
	var title: String? = null,

	@field:SerializedName("genre_id")
	var genreId: String? = null,

	@field:SerializedName("subTitle")
	var subTitle: String? = null,

	@field:SerializedName("province_All")
	var provinceAll: Boolean? = null,

	@field:SerializedName("price")
	var price: Int? = null,

	@field:SerializedName("bookSellCount")
	var bookSellCount: Int? = null,

	@field:SerializedName("iconURL")
	var iconURL: String? = null,

	@field:SerializedName("genre_name")
	var genreName: String? = null,

	@field:SerializedName("bookURL")
	var bookURL: String? = null,

	@field:SerializedName("timestamp")
	var timestamp: Int? = null,

	@field:SerializedName("publish_Date")
	var publishDate: String? = null,

	@field:SerializedName("province_name")
	var provinceName: String? = null,

	@field:SerializedName("province_id")
	var provinceId: String? = null,

	@field:SerializedName("grade")
	var grade: String? = null,

	@field:SerializedName("publisher")
	var publisher: String? = null,

	@field:SerializedName("_id")
	var id: String? = null,

	@field:SerializedName("author_id")
	var authorId: String? = null,

	@field:SerializedName("Collabration")
	var collabration: String? = null,

	@field:SerializedName("open")
	var open: Boolean? = null
)
