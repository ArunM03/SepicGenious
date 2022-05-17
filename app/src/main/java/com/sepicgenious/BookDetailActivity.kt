package com.sepicgenious


import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.sepicgenious.apis.RetrofitClient
import com.sepicgenious.apis.Urlmanager
import com.sepicgenious.epub_reader.OpenBookActivity
import com.sepicgenious.modals.BookDetailsItem
import com.sepicgenious.modals.RecordsItem
import com.sepicgenious.modals.SingleBooksDetails
import com.sepicgenious.modals.StudentLogin
import com.sepicgenious.utils.AlertDialogs
import com.sepicgenious.utils.ConnectivityReceiver
import com.sepicgenious.utils.SharedPref
import kotlinx.android.synthetic.main.activity_book_detail.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.net.URL


class BookDetailActivity : BaseActivity() {


    private var downloadbook: RecordsItem? = null
    private lateinit var recordItem: BookDetailsItem
    private var singleBooksDetails: SingleBooksDetails? = null

    private var bookId: String? = null

    private val LOG_TAG: String =
        BookDetailActivity::class.java.getSimpleName()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        setSupportActionBar(toolbar);
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setHomeButtonEnabled(true);

        supportActionBar!!.setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener { onBackPressed() }


        if (intent != null) {
            bookId = intent.getStringExtra("bookId")
            downloadbook = intent.getSerializableExtra("book") as RecordsItem?

        }
        if (ConnectivityReceiver.isConnectedOrConnecting(this@BookDetailActivity!!)) {
            if (bookId != null)
                loadSingleBokkApi(bookId!!)
        } else {
            if (downloadbook != null) {

                try {
                    btn_buybook.visibility = View.GONE
                    btn_readbook.visibility = View.VISIBLE
                    val gson = Gson()
                    val tmp = gson.toJson(downloadbook)
                    recordItem = gson.fromJson(tmp, BookDetailsItem::class.java)

                    if(downloadbook!!.bookId!=null)
                    {
                        recordItem.id = downloadbook!!.bookId
                    }

                    tv_des.text =
                        downloadbook!!.description
                    tv_title.text =
                        downloadbook!!.title
                    tv_writer.text =
                        "By : " + downloadbook!!.authorName
                    tv_price.text =
                        "$" + downloadbook!!.price
                    tv_publisher.text =
                        downloadbook!!.publisher
                    tv_Edition.text =
                        downloadbook!!.edition

                    val urlImage: URL = URL(
                        Urlmanager.ROOT_URL + downloadbook!!.iconURL!!
                    )
                    val urlstrImage = urlImage.toString().replace("\\", "/");

                    Glide.with(this@BookDetailActivity)
                        .load(urlstrImage)
                        .into(header);

                    if (!downloadbook!!.bookURL!!.contains(
                            Urlmanager.ROOT_URL
                        )
                    ) {
                        val url: URL = URL(
                            Urlmanager.ROOT_URL + downloadbook!!.bookURL
                        )
                        val urlstr = url.toString().replace("\\", "/");
                        downloadbook!!.bookURL = urlstr
                    }


                    btn_buybook.visibility = View.GONE
                    btn_readbook.visibility = View.VISIBLE


                } catch (e: Exception) {

                }

            }
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }

    fun onReadBook(view: View) {

        readBook()
    }

    private fun readBook() {
        if (isStoragePermissionGranted()) {
            openFile()
        }
    }


    private fun openFile() {


        if (check()) {

            val PATH = Environment.getExternalStorageDirectory().toString() + "/.Sepik/"

            val filename: String? = recordItem!!.id + ".epub"
            val file = File(PATH + filename)

            val resultIntent = Intent(this@BookDetailActivity, OpenBookActivity::class.java)
            resultIntent.putExtra("bpath", file.absolutePath)
            resultIntent.putExtra("name", recordItem.title)
            startActivity(resultIntent)
        } else {
            AlertDialogs.AlertDialogDownloadBook(this@BookDetailActivity,
                this@BookDetailActivity,
                "Downloading Book",
                recordItem!!.bookURL,
                recordItem!!.id,
                false,
                View.OnClickListener { AlertDialogs.dialog!!.dismiss() })
        }
    }


    fun check(): Boolean {

        val PATH = Environment.getExternalStorageDirectory().toString() + "/.Sepik/"
//        val filename: String = recordItem!!.bookURL!!.substring(
//            recordItem!!.bookURL!!.lastIndexOf("/") + 1,
//            recordItem!!.bookURL!!.length
//        )
        val file = File(PATH + recordItem!!.id + ".epub")
        return file.exists()
    }

    override fun onDestroy() {
        super.onDestroy()

    }


    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(
                    LOG_TAG,
                    "Permission is granted"
                )
                true
            } else {
                Log.v(
                    LOG_TAG,
                    "Permission is revoked"
                )
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(LOG_TAG, "Permission is granted")
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            readBook()
        }
    }


    private fun loadSingleBokkApi(
        bookId: String
    ) {
        val studentString =
            SharedPref.getStringPreference(this@BookDetailActivity, SharedPref.studentLogin)
        val studentloginData = Gson().fromJson<StudentLogin>(
            studentString.toString(),
            StudentLogin::class.java
        )
        val jsonObject = JSONObject()
        jsonObject.put("bookId", bookId)
        jsonObject.put("studentId", studentloginData!!.studentData!!.id)
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            (jsonObject).toString()
        )

        RetrofitClient.getResponse(this@BookDetailActivity,
            Dialog(this@BookDetailActivity),
            RetrofitClient.getRegistrationApiService()!!.singleBook(body),
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

                            singleBooksDetails = Gson().fromJson<SingleBooksDetails>(
                                responseObject.toString(),
                                SingleBooksDetails::class.java
                            )

                            recordItem = singleBooksDetails!!.bookDetails!!.get(0)!!
                            if (singleBooksDetails != null) {
                                supportActionBar!!.setTitle(singleBooksDetails!!.bookDetails!!.get(0)!!.title);
                                try {

                                    tv_des.text =
                                        singleBooksDetails!!.bookDetails!!.get(0)!!.description
                                    tv_title.text =
                                        singleBooksDetails!!.bookDetails!!.get(0)!!.title
                                    tv_writer.text =
                                        "By : " + singleBooksDetails!!.bookDetails!!.get(0)!!.authorName
                                    tv_price.text =
                                        "$" + singleBooksDetails!!.bookDetails!!.get(0)!!.price
                                    tv_publisher.text =
                                        singleBooksDetails!!.bookDetails!!.get(0)!!.publisher
                                    tv_Edition.text =
                                        singleBooksDetails!!.bookDetails!!.get(0)!!.edition

                                    val urlImage: URL = URL(
                                        Urlmanager.ROOT_URL + singleBooksDetails!!.bookDetails!!.get(
                                            0
                                        )!!.iconURL!!
                                    )
                                    val urlstrImage = urlImage.toString().replace("\\", "/");

                                    Glide.with(this@BookDetailActivity)
                                        .load(urlstrImage)
                                        .into(header);

                                    if (!singleBooksDetails!!.bookDetails!!.get(0)!!.bookURL!!.contains(
                                            Urlmanager.ROOT_URL
                                        )
                                    ) {
                                        val url: URL = URL(
                                            Urlmanager.ROOT_URL + singleBooksDetails!!.bookDetails!!.get(
                                                0
                                            )!!!!.bookURL
                                        )
                                        val urlstr = url.toString().replace("\\", "/");
                                        singleBooksDetails!!.bookDetails!!.get(0)!!.bookURL = urlstr
                                    }

                                    if (singleBooksDetails!!.bookDetails!!.get(0)!!.private!!) {
                                        if (singleBooksDetails!!.purchaseStatus!!) {
                                            btn_buybook.visibility = View.GONE
                                            btn_readbook.visibility = View.VISIBLE
                                        } else {
                                            btn_buybook.visibility = View.VISIBLE
                                            btn_readbook.visibility = View.GONE
                                        }
                                    } else {
                                        btn_buybook.visibility = View.GONE
                                        btn_readbook.visibility = View.VISIBLE
                                    }

                                } catch (e: Exception) {

                                }
                            }
                        } else {
                            AlertDialogs.AlertDialogWarningClose(
                                this@BookDetailActivity,
                                responseObject.getString("message")
                            )
                        }

                    } catch (e: Exception) {
                        AlertDialogs.AlertDialogWarningClose(
                            this@BookDetailActivity,
                            e.message.toString()
                        )
                    }
                }

            })

    }

    private fun buyBookApi(
        singleBooksDetails: SingleBooksDetails
    ) {
        val studentString =
            SharedPref.getStringPreference(this@BookDetailActivity, SharedPref.studentLogin)
        val studentloginData = Gson().fromJson<StudentLogin>(
            studentString.toString(),
            StudentLogin::class.java
        )
        val jsonObject = JSONObject()
        jsonObject.put("book_name", singleBooksDetails!!.bookDetails!!.get(0)!!.title)
        jsonObject.put("genre_name", singleBooksDetails!!.bookDetails!!.get(0)!!.genreName)
        jsonObject.put("edition", singleBooksDetails!!.bookDetails!!.get(0)!!.edition)
        jsonObject.put("book_url", singleBooksDetails!!.bookDetails!!.get(0)!!.bookURL)
        jsonObject.put("book_id", singleBooksDetails!!.bookDetails!!.get(0)!!.id)
        jsonObject.put("author_name", singleBooksDetails!!.bookDetails!!.get(0)!!.authorName)
        jsonObject.put("author_id", singleBooksDetails!!.bookDetails!!.get(0)!!.authorId)
        jsonObject.put("book_price", singleBooksDetails!!.bookDetails!!.get(0)!!.price)
        jsonObject.put("book_icon", singleBooksDetails!!.bookDetails!!.get(0)!!.iconURL)
        jsonObject.put(
            "buyer_name",
            studentloginData!!.studentData!!.firstName + " " + studentloginData!!.studentData!!.surName
        )
        jsonObject.put("buyer_id", studentloginData!!.studentData!!.id)
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            (jsonObject).toString()
        )

        RetrofitClient.getResponse(this@BookDetailActivity,
            Dialog(this@BookDetailActivity),
            RetrofitClient.getRegistrationApiService()!!.buyBook(body),
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

                            onResume()
                        }
                        AlertDialogs.AlertDialogWarningClose(
                            this@BookDetailActivity,
                            responseObject.getString("message")
                        )


                    } catch (e: Exception) {
                        AlertDialogs.AlertDialogWarningClose(
                            this@BookDetailActivity,
                            e.message.toString()
                        )
                    }
                }

            })

    }

    fun onBuyBook(view: View) {
        AlertDialogs.AlertDialogWarning(
            this@BookDetailActivity,
            "Are You Sure to Buy this Book?", object : AlertDialogs.AlertClick {
                override fun alertClickListener(dialog: Dialog) {
                    buyBookApi(singleBooksDetails!!)
                    dialog.dismiss()
                }
            }
        )
    }
}
