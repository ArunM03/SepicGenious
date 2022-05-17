package com.sepicgenious.modals

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GradeBooks(

    @field:SerializedName("current")
    var current: Int? = null,

    @field:SerializedName("pages")
    var pages: Int? = null,

    @field:SerializedName("records")
    var records: ArrayList<RecordsItem?>? = null,

    @field:SerializedName("status")
    var status: Int? = null,

    @field:SerializedName("totalBooks")
    var totalBooks: Int? = null
) : Serializable


