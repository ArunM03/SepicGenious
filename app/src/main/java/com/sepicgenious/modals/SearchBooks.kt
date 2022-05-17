package com.sepicgenious.modals

import com.google.gson.annotations.SerializedName

data class SearchBooks(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("searchData")
	val searchData: ArrayList<SearchDataItem?>? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class SearchDataItem(

	@field:SerializedName("author_name")
	val authorName: String? = null,

	@field:SerializedName("teacher_name")
	val teacherName: String? = null,

	@field:SerializedName("private")
	val jsonMemberPrivate: Boolean? = null,

	@field:SerializedName("teacher_id")
	val teacherId: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("edition")
	val edition: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("genre_id")
	val genreId: String? = null,

	@field:SerializedName("subTitle")
	val subTitle: String? = null,

	@field:SerializedName("province_All")
	val provinceAll: Boolean? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("bookSellCount")
	val bookSellCount: Int? = null,

	@field:SerializedName("iconURL")
	val iconURL: String? = null,

	@field:SerializedName("genre_name")
	val genreName: String? = null,

	@field:SerializedName("bookURL")
	val bookURL: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: Int? = null,

	@field:SerializedName("publish_Date")
	val publishDate: String? = null,

	@field:SerializedName("province_name")
	val provinceName: String? = null,

	@field:SerializedName("province_id")
	val provinceId: String? = null,

	@field:SerializedName("grade")
	val grade: String? = null,

	@field:SerializedName("publisher")
	val publisher: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("author_id")
	val authorId: String? = null,

	@field:SerializedName("Collabration")
	val collabration: String? = null,

	@field:SerializedName("open")
	val open: Boolean? = null
)
