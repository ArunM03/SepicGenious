package com.sepicgenious.apis

import android.app.Dialog
import android.content.Context

import com.sepicgenious.apis.Urlmanager.Companion.ROOT_URL
import com.sepicgenious.utils.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClient {

    companion object {

        private fun getRetrofitInstance(): Retrofit {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging);

            return Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        private fun getRetrofitRegistrationInstance(): Retrofit {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging);

            val interceptorReg =
                Interceptor { chain: Interceptor.Chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                        .header(
                            "x-api-key",
                            Urlmanager.XAPI
                        ) /*.method(original.method(), original.body())*/
                        .build()
                    chain.proceed(request)
                }

            httpClient.addInterceptor(interceptorReg)

            return Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        public fun getApiService(): RetrofitApis? {
            return getRetrofitInstance().create(RetrofitApis::class.java)
        }

        public fun getRegistrationApiService(): RetrofitApis? {
            return getRetrofitRegistrationInstance().create(RetrofitApis::class.java)
        }

        public fun getResponse(
            context: Context?,
            dialog: Dialog?,
            method: Call<ResponseBody?>,
            callback: Callback<ResponseBody?>
        ) {
            if (ConnectivityReceiver.isConnectedOrConnecting(context!!)) {

                if (dialog != null)
                    ProgressDialog.show(dialog!!)



                method.enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {
                        if (dialog != null)
                            ProgressDialog.hide(dialog)
                        callback.onResponse(call, response)
                    }

                    override fun onFailure(
                        call: Call<ResponseBody?>,
                        throwable: Throwable
                    ) {
                        if (dialog != null)
                            ProgressDialog.hide(dialog)
                        callback.onFailure(call, throwable)
                    }
                })
            } else {
                AlertDialogs.AlertDialogWarningClose(
                    context,
                    "Internet Connection Not Found!"
                )

            }
        }
    }

}