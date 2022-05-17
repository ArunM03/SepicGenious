package com.sepicgenious.modals

import com.google.gson.annotations.SerializedName

data class PurchasedBooks(

	@field:SerializedName("current")
	val current: Int? = null,

	@field:SerializedName("pages")
	val pages: Int? = null,

	@field:SerializedName("records")
	val records: List<RecordsItem?>? = null,

	@field:SerializedName("status")
	val status: Int? = null,

	@field:SerializedName("totalBooks")
	val totalBooks: Int? = null
)


