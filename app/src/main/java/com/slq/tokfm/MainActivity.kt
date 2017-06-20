package com.slq.tokfm

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView


class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE = 1
    private var bitmap: Bitmap? = null
    private var imageView: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById (R.id.result) as ImageView?;
    }

    fun listPodcasts(view: View) {
        Log.d("LIST PODCASTS", "listPodcasts called!")
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var stream = null;
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)

        // recyle unused bitmaps
            if (bitmap != null) {
                bitmap?.recycle();
            }
        val openInputStream = getContentResolver().openInputStream(data?.getData())
        stream = openInputStream as Nothing?
        bitmap = BitmapFactory.decodeStream(stream)
        imageView?.setImageBitmap(bitmap);

    }
}
