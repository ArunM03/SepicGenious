package com.sepicgenious


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.sepicgenious.apis.RetrofitClient
import com.sepicgenious.modals.GradeBooks
import com.sepicgenious.modals.StudentLogin
import com.sepicgenious.utils.AlertDialogs
import com.sepicgenious.utils.ConnectivityReceiver
import com.sepicgenious.utils.Helper
import com.sepicgenious.utils.SharedPref
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response


class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login.setOnClickListener {
            val username = et_username.text.toString()
            val password = et_password.text.toString()


            when {
                username.isEmpty() -> AlertDialogs.AlertDialogWarningClose(
                    this@LoginActivity,
                    "Your Username Should Not Be Empty!"
                )
                password.isEmpty() -> AlertDialogs.AlertDialogWarningClose(
                    this@LoginActivity,
                    "Your Password Should Not Be Empty!"
                )
                password.length < 6 -> AlertDialogs.AlertDialogWarningClose(
                    this@LoginActivity,
                    "Your Password Should Have 6 Digits!"
                )

                else -> {

                    doUserLogin(username, password)

                }
            }
        }

    }


    private fun doUserLogin(username: String, password: String) {
        val deviceId = Helper(this@LoginActivity).getDeviceId();
        val jsonObject = JSONObject()
        jsonObject.put("studentID", username)
        jsonObject.put("password", password)
        jsonObject.put("device", deviceId)
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            (jsonObject).toString()
        )

        if (ConnectivityReceiver.isConnectedOrConnecting(this@LoginActivity!!)) {
            RetrofitClient.getResponse(this@LoginActivity,
                Dialog(this@LoginActivity),
                RetrofitClient.getRegistrationApiService()!!.login(body),
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

                                SharedPref.setStringPreference(
                                    this@LoginActivity,
                                    SharedPref.username,
                                    username.trim()
                                )
                                SharedPref.setStringPreference(
                                    this@LoginActivity,
                                    SharedPref.password,
                                    password.trim()
                                )
                                SharedPref.setStringPreference(
                                    this@LoginActivity,
                                    SharedPref.studentLogin,
                                    responseObject.toString()
                                )

                                startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        MainActivity::class.java
                                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                )
                            } else {
                                AlertDialogs.AlertDialogWarningClose(
                                    this@LoginActivity,
                                    responseObject.getString("message")
                                )
                            }

                        } catch (e: Exception) {
                            AlertDialogs.AlertDialogWarningClose(
                                this@LoginActivity,
                                e.message.toString()
                            )
                        }
                    }

                })

        } else {
            if (!username.equals(
                    SharedPref.getStringPreference(
                        this@LoginActivity,
                        SharedPref.username
                    )
                )
            ) {
                AlertDialogs.AlertDialogWarningClose(
                    this@LoginActivity,
                    "Invalid Username"
                )
            } else if (!password.equals(
                    SharedPref.getStringPreference(
                        this@LoginActivity,
                        SharedPref.password
                    )
                )
            ) {
                AlertDialogs.AlertDialogWarningClose(
                    this@LoginActivity,
                    "Invalid Password"
                )
            } else {
                startActivity(
                    Intent(
                        this@LoginActivity,
                        MainActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }

    fun onForgetPassword(view: View) {

        startActivity(
            Intent(
                this@LoginActivity,
                ForgetPasswordActivity::class.java
            )
        )
    }
}
