package com.sepicgenious.fragments


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.sepicgenious.BookDetailActivity
import com.sepicgenious.MainActivity
import com.sepicgenious.adapters.DashBoardBooksAdapter
import com.sepicgenious.adapters.GradeBooksAdapter
import com.sepicgenious.R
import com.sepicgenious.SearchActivity
import com.sepicgenious.apis.RetrofitClient
import com.sepicgenious.modals.GradeBooks
import com.sepicgenious.modals.RecordsItem
import com.sepicgenious.modals.StudentLogin
import com.sepicgenious.utils.AlertDialogs
import com.sepicgenious.utils.EndlessRecyclerViewScrollListener
import com.sepicgenious.utils.SharedPref
import kotlinx.android.synthetic.main.fragment_home.*

import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    val records: ArrayList<RecordsItem?>? = ArrayList()
    private lateinit var gradeBookAdapter: GradeBooksAdapter
    private lateinit var downloadBookAdapter: GradeBooksAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val studentString =
            SharedPref.getStringPreference(requireContext(), SharedPref.studentLogin)
        val studentLogin = Gson().fromJson<StudentLogin>(
            studentString.toString(),
            StudentLogin::class.java
        )
        tv_student.text =
            studentLogin!!.studentData!!.firstName + " " + studentLogin!!.studentData!!.surName
        tv_school.text = studentLogin!!.studentData!!.schoolName
        tv_grade.text = "Grade " + studentLogin!!.studentData!!.grade.toString()
        tv_balance.text = "Balance " + studentLogin!!.studentData!!.balance.toString()
        tv_grade_books.text = "My Grade " + studentLogin!!.studentData!!.grade.toString() + " Books"

//        rv_books.adapter =
//            DashBoardBooksAdapter(context)
        gradeBookAdapter = GradeBooksAdapter(context, records)
       downloadBookAdapter = GradeBooksAdapter(context,   MainActivity.readDownloaded()!!.records!!)
        rv_grade_books.adapter = gradeBookAdapter
        rv_download_books.adapter = downloadBookAdapter

        loadNextGradeFromApi(
            1,
            studentLogin!!.studentData!!.grade!!,
            studentLogin!!.studentData!!.province!!
        )
        val linearLayoutManager = LinearLayoutManager(context)
        rv_grade_books.layoutManager = linearLayoutManager
        // Retain an instance so that you can call `resetState()` for fresh searches
        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextGradeFromApi(
                    page,
                    studentLogin!!.studentData!!.grade!!,
                    studentLogin!!.studentData!!.province!!
                )
            }
        }
        // Adds the scroll listener to RecyclerView
        // Adds the scroll listener to RecyclerView
        rv_grade_books.addOnScrollListener(scrollListener)




        tv_search.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    SearchActivity::class.java
                )
            )
        }
    }

    private fun loadNextGradeFromApi(
        page: Int,
        grade: String,
        province: String
    ) {

        val jsonObject = JSONObject()
        jsonObject.put("skipValue", page)
        jsonObject.put("garde", grade.trim())
        jsonObject.put("provinceId", province.trim())
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            (jsonObject).toString()
        )

        RetrofitClient.getResponse(context,
            null,
            RetrofitClient.getRegistrationApiService()!!.landingpageGrade(body),
            object : retrofit2.Callback<ResponseBody?> {
                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    val response1 = response?.body() as ResponseBody
                    val responseObject = JSONObject(response1.string())
                    Log.e("OnResponse", responseObject.toString())
                    try {

                        if (responseObject.getInt("status") == 1) {

                            val gradeBooks = Gson().fromJson<GradeBooks>(
                                responseObject.toString(),
                                GradeBooks::class.java
                            )
                            val curSize: Int = gradeBookAdapter.itemCount
                            gradeBooks!!.records?.let { records!!.addAll(it) }
                            gradeBookAdapter.notifyItemRangeInserted(curSize, records!!.size)
                        } else {
                            /* AlertDialogs.AlertDialogWarningClose(
                                 context,
                                 responseObject.getString("message")
                             )*/
                        }

                    } catch (e: Exception) {
                        /* AlertDialogs.AlertDialogWarningClose(
                             context,
                             e.message.toString()
                         )*/
                    }
                }

            })

    }

    fun yourMethod() {

        records!!.addAll(MainActivity.downloadRecords!!.records!!)
        gradeBookAdapter.notifyDataSetChanged()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
