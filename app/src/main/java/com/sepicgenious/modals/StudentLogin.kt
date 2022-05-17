package com.sepicgenious.modals

import com.google.gson.annotations.SerializedName

data class StudentLogin(

    @field:SerializedName("securityToken")
    val securityToken: String? = null,

    @field:SerializedName("studentData")
    val studentData: StudentData? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
)

data class StudentData(

    @field:SerializedName("teacher_name")
    val teacherName: String? = null,

    @field:SerializedName("teacher_id")
    val teacherId: String? = null,

    @field:SerializedName("sex")
    val sex: String? = null,

    @field:SerializedName("student_id")
    val studentId: String? = null,

    @field:SerializedName("mother_village")
    val motherVillage: String? = null,

    @field:SerializedName("school_name")
    val schoolName: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("Grade")
    val grade: String? = null,

    @field:SerializedName("sur_name")
    val surName: String? = null,

    @field:SerializedName("deviceID")
    val deviceID: String? = null,

    @field:SerializedName("Province")
    val province: String? = null,

    @field:SerializedName("province_name")
    val provinceName: String? = null,

    @field:SerializedName("disablity")
    val disablity: String? = null,

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("School")
    val school: String? = null,

    @field:SerializedName("balance")
    val balance: Int? = null,

    @field:SerializedName("DOB")
    val dOB: String? = null,

    @field:SerializedName("father_village")
    val fatherVillage: String? = null,

    @field:SerializedName("talent")
    val talent: String? = null,

    @field:SerializedName("second_name")
    val secondName: String? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("first_name")
    val firstName: String? = null,

    @field:SerializedName("timestamp")
    val timestamp: Int? = null
)
