package com.sepicgenious

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sepicgenious.adapters.SearchBooksAdapter
import com.sepicgenious.apis.RetrofitClient
import com.sepicgenious.modals.SearchBooks
import com.sepicgenious.modals.SearchDataItem
import com.sepicgenious.utils.AlertDialogs
import kotlinx.android.synthetic.main.activity_book_detail.toolbar
import kotlinx.android.synthetic.main.activity_search.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    val records: ArrayList<SearchDataItem?>? = ArrayList()
    private lateinit var gradeBookAdapter: SearchBooksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar);
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setHomeButtonEnabled(true);

        supportActionBar!!.setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar!!.setTitle("Search Books");



        gradeBookAdapter =
            SearchBooksAdapter(this@SearchActivity, records)
        rv_searchbooks.adapter = gradeBookAdapter


        val linearLayoutManager = LinearLayoutManager(this@SearchActivity)
        rv_searchbooks.layoutManager = linearLayoutManager


        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                loadNextGradeFromApi(query!!)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.length >= 2 || newText.isEmpty())
                    loadNextGradeFromApi(newText!!)

                return true
            }
        })
    }


    private fun loadNextGradeFromApi(
        page: String?
    ) {

        val jsonObject = JSONObject()
        jsonObject.put("string", page)
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            (jsonObject).toString()
        )

        RetrofitClient.getResponse(this@SearchActivity,
            null,
            RetrofitClient.getRegistrationApiService()!!.searchBook(body),
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

                        if (responseObject.getInt("status") == 0) {
                            records!!.clear()
                            val gradeBooks = Gson().fromJson<SearchBooks>(
                                responseObject.toString(),
                                SearchBooks::class.java
                            )
                            val curSize: Int = gradeBookAdapter.itemCount
                            gradeBooks!!.searchData?.let { records!!.addAll(it) }
                            gradeBookAdapter.notifyDataSetChanged()
                        }else{
                            AlertDialogs.AlertDialogWarningClose(
                                this@SearchActivity,
                                responseObject.getString("message")
                            )
                        }

                    } catch (e: Exception) {
                        AlertDialogs.AlertDialogWarningClose(
                            this@SearchActivity,
                            e.message.toString()
                        )
                    }
                }

            })

    }
}
