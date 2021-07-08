package com.example.ridecellassignment

import android.R
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

object Utils {

    const val MY_PERMISSIONS_REQUEST_LOCATION = 101

    fun showToast(context: Context?, msg: String?) {
        val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun isDataConnected(context: Context?): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            Log.d("1st : ", "connectivityManager")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                Log.d("2nd : ", "connectivityManager")
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true
                    }
                }
            } else {
                Log.d("3rd : ", "else")
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return true
                }
            }
        }
        return false
    }

    fun showSNACK_BAR_NO_INTERNET(activity: Activity, str_class_name: String) {
        Snackbar.make(
            activity.findViewById<View>(R.id.content),
            "No internet connection !",
            Snackbar.LENGTH_INDEFINITE
        ).setAction(
            "Retry"
        ) { v: View? ->
            try {
                activity.startActivity(
                    Intent(
                        activity,
                        Class.forName(str_class_name)
                    )
                )
                activity.finish()
            } catch (e: ClassNotFoundException) {
                // Toast.makeText(activity, s + " does not exist yet", Toast.LENGTH_SHORT).show();
            }
        }.show()
    }

    fun generateProgressDialog(context: Context?): Dialog? {
        val pDialog = Dialog(context!!, com.example.ridecellassignment.R.style.ProgressTheme)
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view: View =
            LayoutInflater.from(context)
                .inflate(com.example.ridecellassignment.R.layout.progress_layout, null)
        pDialog.setContentView(view)
        pDialog.setCancelable(false)
        pDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return pDialog
    }

    fun showMessageOKDialog(
        c: Context?,
        message: String?,
        okListener: DialogInterface.OnClickListener?,
    ) {
        AlertDialog.Builder(c!!)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK", okListener)
            .create()
            .show()
    }

    fun vectorToBitmap(resources: Resources, @DrawableRes id: Int): BitmapDescriptor? {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)!!
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        //    DrawableCompat.setTint(vectorDrawable, color);
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    @ExperimentalTime
    fun getDateDiff(format: SimpleDateFormat, oldDate: String, newDate: String): Long {
        return try {
            DurationUnit.DAYS.convert(
                format.parse(newDate).time - format.parse(oldDate).time,
                DurationUnit.MILLISECONDS
            )
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
}