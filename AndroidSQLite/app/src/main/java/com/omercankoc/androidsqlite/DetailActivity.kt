package com.omercankoc.androidsqlite

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_detail.*
import java.io.ByteArrayOutputStream
import java.util.jar.Manifest
import androidx.core.content.ContextCompat as ContextCompat1

class DetailActivity : AppCompatActivity() {

    var selectedPicture : Uri? = null
    var selectedBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    }

    fun onClickSelectImage(view : View){

        // Kullanicidan erisim izini alinip alinmadiginin kontrol et.
        // Izin yok ise izin al.
        if(ContextCompat1.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }

        // Izin var ise kullanicinin Media Store'una eris.
        else{
            // Kullanicinin Media'sina ait URı'ye git.
            val intentToGallery =  Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intentToGallery,2)
        }
    }

    // Izin veridigi an kullaniciya ait galeri'ye git. (!Ikinci kez tiklanmamasi icin!)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1){
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intentToGallery =  Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intentToGallery,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    //
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            selectedPicture = data.data

            // Bitmap cevirme operasyonu.
            if(selectedPicture != null) {
                // API Level'e gore ozellestirilmis operasyonlar.
                if (Build.VERSION.SDK_INT >= 28) {
                    val source =
                        ImageDecoder.createSource(this.contentResolver, selectedPicture!!)
                    val selectedBitmap = ImageDecoder.decodeBitmap(source)
                    imageView.setImageBitmap(selectedBitmap)
                }
                else {
                    val selectedBitmap =
                        MediaStore.Images.Media.getBitmap(this.contentResolver, selectedPicture)
                    imageView.setImageBitmap(selectedBitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun onClickSave(view : View){

        val language = editTextLanguage.text.toString()
        val creator = editTextCreator.text.toString()
        val year = editTextYear.text.toString()

        if(selectedBitmap != null){
            // Image sikistirip bir veri setine cevirir.
            var smallBitmap = makeSmallerBitmap(selectedBitmap!!,300)

            val outputStream = ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val byteArray = outputStream.toByteArray()

            try {
                // DB olustur veya olusmus ise ac.
                val database = this.openOrCreateDatabase("Languages",Context.MODE_PRIVATE,null)
                database.execSQL("CREATE TABLE IF NOT EXISTS languages (id INTEGER PRIMARY KEY, language VARCHAR, creator VARCHAR, year VARCHAR, image BLOB)")
                // Olusturulan DB'ye kayit ekle.
                val sqlString = "INSERT INTO languages (language, creator, year, image) VALUES (?,?,?,?)"
                val statement = database.compileStatement(sqlString)
                statement.bindString(1,language)
                statement.bindString(2,creator)
                statement.bindString(3,year)
                statement.bindBlob(4,byteArray)

                statement.execute()
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
        finish()
    }

    // Bitmap Kucultme Operasyonu.
    fun makeSmallerBitmap(image : Bitmap, maximumSize : Int) : Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio : Double = width.toDouble() / height.toDouble()

        if(bitmapRatio > 1){
            // Gorsel Yatay!
            width = maximumSize
            val scaledHeight = width / bitmapRatio
            height = scaledHeight.toInt()
        } else{
            // Gorsel Dikey!
            height = maximumSize
            val scaledWidth = height * bitmapRatio
            width = scaledWidth.toInt()
        }

        return Bitmap.createScaledBitmap(image,width,height,true)
    }
}
