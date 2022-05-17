package com.sepicgenious

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.sepicgenious.apis.RetrofitClient
import com.sepicgenious.utils.AlertDialogs
import com.sepicgenious.utils.Helper
import com.sepicgenious.utils.SharedPref
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btn_login
import kotlinx.android.synthetic.main.activity_login.et_password
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class ForgetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        btn_login.setOnClickListener {
            val password = et_password.text.toString()
            val cpassword = et_confirmpassword.text.toString()


            when {
                password.isEmpty() -> AlertDialogs.AlertDialogWarningClose(
                    this@ForgetPasswordActivity,
                    "Your Password Should Not Be Empty!"
                )
                password.length < 6 -> AlertDialogs.AlertDialogWarningClose(
                    this@ForgetPasswordActivity,
                    "Your Password Should Have 6 Digits!"
                )

                cpassword != password -> AlertDialogs.AlertDialogWarningClose(
                    this@ForgetPasswordActivity,
                    "Your Password is not same as above!"
                )


                else -> {

                    doUserLogin(password)

                }
            }
        }
    }

    private fun doUserLogin(password: String) {
        val deviceId = Helper(this@ForgetPasswordActivity).getDeviceId();
        val jsonObject = JSONObject()
        jsonObject.put("password", password)
        jsonObject.put("device", deviceId)
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            (jsonObject).toString()
        )

        RetrofitClient.getResponse(this@ForgetPasswordActivity,
            Dialog(this@ForgetPasswordActivity),
            RetrofitClient.getRegistrationApiService()!!.changePass(body),
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


                        AlertDialogs.AlertDialogWarningClose(
                            this@ForgetPasswordActivity,
                            responseObject.getString("message")
                        )


                    } catch (e: Exception) {
                        AlertDialogs.AlertDialogWarningClose(
                            this@ForgetPasswordActivity,
                            e.message.toString()
                        )
                    }
                }

            })
    }
}
