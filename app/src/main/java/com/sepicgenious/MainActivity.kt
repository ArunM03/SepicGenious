package com.sepicgenious

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.Constraints
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.sepicgenious.apis.RetrofitClient
import com.sepicgenious.apis.Urlmanager
import com.sepicgenious.fragments.*
import com.sepicgenious.modals.Downloads
import com.sepicgenious.modals.GradeBooks
import com.sepicgenious.modals.RecordsItem
import com.sepicgenious.modals.StudentLogin
import com.sepicgenious.utils.AlertDialogs
import com.sepicgenious.utils.SharedPref
import kotlinx.android.synthetic.main.activity_main_layout.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class MainActivity : BaseActivity() {

    private var pagecount: Int? = 0
    private var studentloginData: StudentLogin? = null
    var studentString: String? = ""
    val records: ArrayList<RecordsItem?>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_layout)

        val studentString =
            SharedPref.getStringPreference(this@MainActivity, SharedPref.studentLogin)
        studentloginData = Gson().fromJson<StudentLogin>(
            studentString.toString(),
            StudentLogin::class.java
        )


        iv_home.setTint(this@MainActivity, R.color.colorPrimary)
        iv_fav.setTint(this@MainActivity, R.color.text_gray_2)
        iv_noti.setTint(this@MainActivity, R.color.text_gray_2)
        iv_private.setTint(this@MainActivity, R.color.text_gray_2)
        iv_sync.setTint(this@MainActivity, R.color.text_gray_2)
        iv_cart.setTint(this@MainActivity, R.color.text_gray_2)
        replaceFragment(
            HomeFragment.newInstance("", ""),
            R.id.main_frame
        )

        readDownloaded()
        AlertDialogSync(this@MainActivity, "")
    }


    fun replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.beginTransaction()
            .replace(frameId, fragment, fragment.javaClass.name).commit()
    }

    fun ImageView.setTint(context: Context, @ColorRes colorId: Int) {
        val color = ContextCompat.getColor(context, colorId)
        val colorStateList = ColorStateList.valueOf(color)
        ImageViewCompat.setImageTintList(this, colorStateList)
    }

    fun onHomeClick(view: View) {

        iv_home.setTint(this@MainActivity, R.color.colorPrimary)
        iv_fav.setTint(this@MainActivity, R.color.text_gray_2)
        iv_noti.setTint(this@MainActivity, R.color.text_gray_2)
        iv_private.setTint(this@MainActivity, R.color.text_gray_2)
        iv_cart.setTint(this@MainActivity, R.color.text_gray_2)
        iv_sync.setTint(this@MainActivity, R.color.text_gray_2)
        replaceFragment(
            HomeFragment.newInstance("", ""),
            R.id.main_frame
        )
    }

    fun onFavoriteClick(view: View) {
        iv_home.setTint(this@MainActivity, R.color.text_gray_2)
        iv_fav.setTint(this@MainActivity, R.color.colorPrimary)
        iv_noti.setTint(this@MainActivity, R.color.text_gray_2)
        iv_private.setTint(this@MainActivity, R.color.text_gray_2)
        iv_cart.setTint(this@MainActivity, R.color.text_gray_2)
        iv_sync.setTint(this@MainActivity, R.color.text_gray_2)
        replaceFragment(
            FavoriteFragment.newInstance("", ""),
            R.id.main_frame
        )
    }

    fun onNotificationClick(view: View) {
        iv_home.setTint(this@MainActivity, R.color.text_gray_2)
        iv_fav.setTint(this@MainActivity, R.color.text_gray_2)
        iv_noti.setTint(this@MainActivity, R.color.colorPrimary)
        iv_private.setTint(this@MainActivity, R.color.text_gray_2)
        iv_cart.setTint(this@MainActivity, R.color.text_gray_2)
        iv_sync.setTint(this@MainActivity, R.color.text_gray_2)
        replaceFragment(
            NotificationFragment.newInstance("", ""),
            R.id.main_frame
        )
    }

    fun onPrivateBooks(view: View) {
        iv_home.setTint(this@MainActivity, R.color.text_gray_2)
        iv_fav.setTint(this@MainActivity, R.color.text_gray_2)
        iv_noti.setTint(this@MainActivity, R.color.text_gray_2)
        iv_private.setTint(this@MainActivity, R.color.colorPrimary)
        iv_cart.setTint(this@MainActivity, R.color.text_gray_2)
        iv_sync.setTint(this@MainActivity, R.color.text_gray_2)

        replaceFragment(
            PrivateBooksFragment.newInstance("", ""),
            R.id.main_frame
        )
    }

    fun onSyncClick(view: View) {
        iv_home.setTint(this@MainActivity, R.color.text_gray_2)
        iv_fav.setTint(this@MainActivity, R.color.text_gray_2)
        iv_noti.setTint(this@MainActivity, R.color.text_gray_2)
        iv_private.setTint(this@MainActivity, R.color.text_gray_2)
        iv_cart.setTint(this@MainActivity, R.color.text_gray_2)
        iv_sync.setTint(this@MainActivity, R.color.colorPrimary)
        AlertDialogSync(this@MainActivity, "")
    }

    fun onCartClick(view: View) {
        iv_home.setTint(this@MainActivity, R.color.text_gray_2)
        iv_fav.setTint(this@MainActivity, R.color.text_gray_2)
        iv_noti.setTint(this@MainActivity, R.color.text_gray_2)
        iv_private.setTint(this@MainActivity, R.color.text_gray_2)
        iv_cart.setTint(this@MainActivity, R.color.colorPrimary)
        iv_sync.setTint(this@MainActivity, R.color.text_gray_2)

        replaceFragment(
            CartFragment.newInstance("", ""),
            R.id.main_frame
        )
    }


    fun AlertDialogSync(
        context: Context?,
        message: String
    ) {
        if (AlertDialogs.dialog != null) {
            AlertDialogs.dialog!!.dismiss()
        }
        AlertDialogs.dialog = Dialog(context!!)
        AlertDialogs.dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        AlertDialogs.dialog!!.setCancelable(false)
        AlertDialogs.dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        AlertDialogs.dialog!!.setContentView(R.layout.dialog_sync)
        AlertDialogs.dialog!!.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val txtMessage = AlertDialogs.dialog!!.findViewById(R.id.txtMessage) as TextView
        val iv_close = AlertDialogs.dialog!!.findViewById(R.id.iv_close) as ImageView
        iv_close.setOnClickListener { AlertDialogs.dialog!!.dismiss() }
        records!!.clear()

        loadNextSyncDataApi(1, txtMessage!!)
        AlertDialogs.dialog!!.show()
    }

    private fun loadNextSyncDataApi(page: Int, txtMessage: TextView) {

        val jsonObject = JSONObject()
        jsonObject.put("skipValue", page)
        jsonObject.put("type", "1")
        jsonObject.put("grade", studentloginData!!.studentData!!.grade)
        jsonObject.put("province_id", studentloginData!!.studentData!!.province)
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            (jsonObject).toString()
        )

        RetrofitClient.getResponse(this@MainActivity,
            null,
            RetrofitClient.getRegistrationApiService()!!.syncData(body),
            object : retrofit2.Callback<ResponseBody?> {
                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    AlertDialogs.dialog!!.dismiss()
                }

                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    AlertDialogs.dialog!!.dismiss()
                    val response1 = response?.body() as ResponseBody
                    val responseObject = JSONObject(response1.string())
                    Log.e("OnResponse", responseObject.toString())
                    try {

                        if (responseObject.getInt("status") == 1) {

                            val gradeBooks = Gson().fromJson<GradeBooks>(
                                responseObject.toString(),
                                GradeBooks::class.java
                            )
                            pagecount = gradeBooks.pages
                            gradeBooks!!.records?.let { records!!.addAll(it) }

                            if (page < gradeBooks.pages!!)
                                loadNextSyncDataApi(gradeBooks.current!! + 1, txtMessage!!)
                            if (page == gradeBooks.pages
                            ) {
                                loadPurchasedFromApi(1, txtMessage!!)


                            }
                        } else {
                            loadPurchasedFromApi(1, txtMessage!!)
                        }

                    } catch (e: Exception) {

                    }
                }

            })

    }

    private fun downloadSyncFiles(
        records: ArrayList<RecordsItem?>,
        txtMessage: TextView
    ) {

        val jsonObj = JsonObject()
        val jsonArray2: JsonArray = Gson().toJsonTree(records).asJsonArray
        jsonObj.add("records", jsonArray2)

        // Convert JsonObject to String Format

        // Convert JsonObject to String Format
        try {
            val userString: String = jsonObj.toString()
            val file = File(
                Environment.getExternalStorageDirectory().toString() + "/.Sepik/",
                "downloads.json"
            )
            if (!file!!.parentFile!!.exists())
                file!!.parentFile!!.mkdirs();
            if (!file.exists())
                file.createNewFile();
            val fileWriter = FileWriter(file)
            val bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(userString)
            bufferedWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        var donwloadedcount = 0
        for (i in 0 until records!!.size) {


            if (!records.get(i)!!.bookURL!!.contains(Urlmanager.ROOT_URL)) {
                val url: URL = URL(Urlmanager.ROOT_URL + records.get(i)!!.bookURL)

                val urlstr = url.toString().replace("\\", "/");
                records.get(i)!!.bookURL = urlstr
            }

            var id: String? = ""
            if (records.get(i)!!.bookId == null)
                id = records.get(i)!!.id
            else
                id = records.get(i)!!.bookId

            println("downlaod url : " + records.get(i)!!.bookURL)
            var urlfinal = records.get(i)!!.bookURL

                   val downloadId = PRDownloader.download(
                       urlfinal,
                       Environment.getExternalStorageDirectory().toString() + "/.Sepik/",
                       id + ".epub"
                   )
                       .build()
                       .setOnStartOrResumeListener { }
                       .setOnPauseListener { }
                       .setOnCancelListener { }
                       .setOnProgressListener { }
                       .start(object : OnDownloadListener {
                           override fun onDownloadComplete() {
                               ++donwloadedcount
                               if (donwloadedcount >= records!!.size) {

                                   txtMessage.text = "Completed Successfully"

                                   readDownloaded()

                               }
                           }

                           override fun onError(error: com.downloader.Error?) {

                               txtMessage.text = "URL $urlfinal  Something went wrong : ${error!!.serverErrorMessage}"
                               println("downlaod error : " + error!!.serverErrorMessage)
                           }

                       })




        }
    }


    private fun loadPurchasedFromApi(page: Int, txtMessage: TextView) {

        val jsonObject = JSONObject()
        jsonObject.put("skipValue", page)
        jsonObject.put("studentId", studentloginData!!.studentData!!.id)
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            (jsonObject).toString()
        )

        RetrofitClient.getResponse(this@MainActivity,
            null,
            RetrofitClient.getRegistrationApiService()!!.purchasedBook(body),
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

                            val purchased = Gson().fromJson<GradeBooks>(
                                responseObject.toString(),
                                GradeBooks::class.java
                            )

                            purchased!!.records?.let { records!!.addAll(it) }

                            if (page < purchased.pages!!)
                                loadPurchasedFromApi(purchased.current!! + 1, txtMessage!!)
                            if (page == purchased.pages
                            ) {

                                downloadSyncFiles(records!!, txtMessage!!)
                            }
                        } else {
                            downloadSyncFiles(records!!, txtMessage!!)
                        }

                    } catch (e: Exception) {

                    }
                }

            })

    }

    companion object {
        var downloadRecords: Downloads? = null

        fun readDownloaded(): Downloads? {
            try {
                val file = File(
                    Environment.getExternalStorageDirectory().toString() + "/.Sepik/",
                    "downloads.json"
                )
                val fileReader = FileReader(file)
                val bufferedReader = BufferedReader(fileReader)
                var stringBuilder = StringBuilder()
                var line: String? = bufferedReader.readLine()
                while (line != null) {
                    println("breakbreak")
                    stringBuilder.append(line).append("\n")
                    line = bufferedReader.readLine()
                }
                bufferedReader.close()
                val responce = stringBuilder.toString()
                println("output  : " + responce)


                downloadRecords =
                    Gson().fromJson<Downloads>(responce.toString(), Downloads::class.java)
                return downloadRecords
            } catch (e: Exception) {
                return Downloads()
            }
        }
    }



}
