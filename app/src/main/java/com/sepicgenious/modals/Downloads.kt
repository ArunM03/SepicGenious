package com.sepicgenious.modals

import com.google.gson.annotations.SerializedName

data class Downloads(

	@field:SerializedName("records")
	val records: ArrayList<RecordsItem?>? = ArrayList()
)


