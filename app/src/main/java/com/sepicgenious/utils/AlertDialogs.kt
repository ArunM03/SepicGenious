package com.sepicgenious.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.constraintlayout.widget.Constraints
import com.sepicgenious.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class AlertDialogs {


    companion object {
        var dialog: Dialog? = null

        fun AlertDialogWarningClose(
            context: Context?,
            message: String
        ) {
            if (dialog != null) {
                dialog!!.dismiss()
            }
            dialog = Dialog(context!!)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setCancelable(false)
            dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.setContentView(R.layout.dialog_warning)
            dialog!!.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val txtMessage =
                dialog!!.findViewById(R.id.txtMessage) as TextView
            val iv_close =
                dialog!!.findViewById(R.id.iv_close) as ImageView
            val btn_ok =
                dialog!!.findViewById(R.id.btn_ok) as Button
            txtMessage.text = message + ""
            btn_ok.setOnClickListener { dialog!!.dismiss() }
            iv_close.setOnClickListener { dialog!!.dismiss() }
            dialog!!.show()
        }


        fun AlertDialogWarning(
            context: Context?,
            message: String,
            onclick: AlertClick
        ) {
            if (dialog != null) {
                dialog!!.dismiss()
            }
            dialog = Dialog(context!!)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setCancelable(false)
            dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.setContentView(R.layout.dialog_warning)
            dialog!!.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val txtMessage =
                dialog!!.findViewById(R.id.txtMessage) as TextView
            val iv_close =
                dialog!!.findViewById(R.id.iv_close) as ImageView
            val btn_ok =
                dialog!!.findViewById(R.id.btn_ok) as Button
            txtMessage.text = message + ""
            btn_ok.setOnClickListener { onclick!!.alertClickListener(dialog!!) }
            iv_close.setOnClickListener { dialog!!.dismiss() }
            dialog!!.show()
        }


        fun AlertDialogDownloadBook(
            context: Context?,
            activity: Activity?,
            message: String,
            applink: String?,
            bookid: String?,
            forceCloseStatus: Boolean?,
            onClickListener: View.OnClickListener?
        ) {
            if (dialog != null) {
                dialog!!.dismiss()
            }
            dialog = Dialog(context!!)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setCancelable(false)
            dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.setContentView(
                R.layout.dialog_update
            )
            dialog!!.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val txtMessage =
                dialog!!.findViewById(R.id.txtMessage) as TextView
            val txtUpdate =
                dialog!!.findViewById(R.id.txtUpdate) as TextView
            val progressBar =
                dialog!!.findViewById(R.id.progressBar) as ProgressBar

            val btn_ok =
                dialog!!.findViewById(R.id.btn_ok) as Button
            val iv_close =
                dialog!!.findViewById(R.id.iv_close) as ImageView
            txtMessage.text = message + ""
            if (!forceCloseStatus!!) {
                iv_close.visibility = View.VISIBLE
            } else {
                iv_close.visibility = View.GONE
            }
            iv_close.setOnClickListener(onClickListener)
            btn_ok.setOnClickListener {
                btn_ok.isEnabled = false
                DownloadFileFromURL(
                    context,
                    btn_ok,
                    progressBar,
                    txtUpdate,
                    dialog!!
                ).execute(applink, bookid)
            }
            dialog!!.show()
        }


    }


    public interface AlertClick {
        public fun alertClickListener(dialog: Dialog)
    }


    class DownloadFileFromURL(
        var context: Context,
        var btn_ok: Button,
        var progressBar: ProgressBar,
        var txtUpdate: TextView,
        dialog: Dialog
    ) :
        AsyncTask<String?, Int?, Boolean>() {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        override fun onPreExecute() {
            super.onPreExecute()
            //            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        protected override fun doInBackground(vararg f_url: String?): Boolean {
            var flag = false
            try {
                val url = URL(f_url[0])
                val bookid = f_url[1] + ".epub"
                val c =
                    url.openConnection() as HttpURLConnection
                c.requestMethod = "GET"
                c.doOutput = false
                c.connect()
                val PATH =
                    Environment.getExternalStorageDirectory().toString() + "/.Sepik/"
                val file = File(PATH)
                file.mkdirs()

                val outputFile = File(file, bookid)
                if (outputFile.exists()) {
                    outputFile.delete()
                }
                val fos = FileOutputStream(outputFile)
                val `is` = c.inputStream
                val total_size = c.contentLength //size of apk
                val buffer = ByteArray(1024)
                var len1 = 0
                var per = 0
                var downloaded = 0
                while (`is`.read(buffer).also { len1 = it } != -1) {
                    fos.write(buffer, 0, len1)
                    downloaded += len1
                    per = (downloaded * 100 / total_size)
                    publishProgress(per)
                }
                fos.close()
                `is`.close()
                dialog!!.dismiss()
                flag = true
            } catch (e: MalformedURLException) {
                Log.e(
                    Constraints.TAG,
                    "Update Error: " + e.message
                )
                flag = false
            } catch (ex: IOException) {
                ex.printStackTrace()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return flag
        }

        /**
         * Updating progress bar
         */
        protected override fun onProgressUpdate(vararg progress: Int?) {
            // setting progress percentage
            progressBar.progress = progress[0]!!
            txtUpdate.text = progress[0].toString() + " %"
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         */
        override fun onPostExecute(file_url: Boolean) {
            // dismiss the dialog after the file was downloaded
//            dismissDialog(progress_bar_type);
            btn_ok.isEnabled = true
            // Displaying downloaded image into image view
            // Reading image path from sdcard
//            String file = Environment.getExternalStorageDirectory().toString() + "/dhan_games.apk";
//            Log.e("file", file);
            if (file_url) {
                Toast.makeText(
                    context, "Update Done",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context, "Error: Try Again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }


}