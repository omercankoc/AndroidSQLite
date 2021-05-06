package com.omercankoc.sqlite

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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream

class DetailsActivity : AppCompatActivity() {

    var selectedLogo : Uri? = null
    var selectedBitmap : Bitmap? = null

    private lateinit var imageViewLogo : ImageView
    private lateinit var editTextLanguage : EditText
    private lateinit var editTextDeveloper : EditText
    private lateinit var editTextYear : EditText
    private lateinit var buttonSave : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        imageViewLogo = findViewById(R.id.imageViewLogo)
        editTextLanguage = findViewById(R.id.editTextLanguage)
        editTextDeveloper = findViewById(R.id.editTextDeveloper)
        editTextYear = findViewById(R.id.editTextYear)
        buttonSave = findViewById(R.id.buttonSave)
    }

    fun select(view : View){
        // Kullanici EXTERNAL_STORAGE erisimine izin vermemis ise izin iste.
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }

        // Kullanici EXTERNAL_STORAGE erisimine izin vermis ise
        else {
            // Galeri'ye yonlendir.
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,2)
        }
    }

    // Kullanici izin verdikten sonra yine Galeri'ye yonlendir.
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array <out String>, grantResults: IntArray) {

        if(requestCode == 1){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent,2)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // Galeriye gittikten sonra secilen image icin bitmap decode islemini yap ve ilgili view'a aktar.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            selectedLogo = data.data

            try {

                // Selected Logo Null degil ise...
                if(selectedLogo != null){

                    // Versiyon 28'den buyuk ise...
                    if(Build.VERSION.SDK_INT >= 28){
                        val source = ImageDecoder.createSource(this.contentResolver,selectedLogo!!)
                        selectedBitmap = ImageDecoder.decodeBitmap(source)
                        imageViewLogo.setImageBitmap(selectedBitmap)
                    }

                    // Versiyon 28'den kucuk ise...
                    else {
                        selectedBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,selectedLogo)
                        imageViewLogo.setImageBitmap(selectedBitmap)
                    }
                }

            } catch (e : Exception){
                e.printStackTrace()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    // Resimi sikistirma islemini yap...
    fun compression(image : Bitmap, maximumSize : Int) : Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio : Double = width.toDouble() / height.toDouble()

        if(bitmapRatio > 1){
            // Gorsel yatay...
            width = maximumSize
            val scaledHeight = width / bitmapRatio
            height = scaledHeight.toInt()
        } else {
            // Gorsel dikey...
            height = maximumSize
            val scaledWidth = height * bitmapRatio
            width = scaledWidth.toInt()
        }

        return Bitmap.createScaledBitmap(image,width,height,true)
    }

    // Kaydetme islemini yap.
    fun save(view: View){
        val language = editTextLanguage.text.toString()
        val developer = editTextDeveloper.text.toString()
        val year = editTextYear.text.toString()

        // Image sikistirip encode et.
        if(selectedBitmap != null){
            val compressBitmap = compression(selectedBitmap!!,300)

            val outputStream = ByteArrayOutputStream()
            compressBitmap!!.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val byteArray = outputStream.toByteArray()

            try {
                val database = this.openOrCreateDatabase("Languages",Context.MODE_PRIVATE,null)
                database.execSQL("CREATE TABLE IF NOT EXISTS languages (id INTEGER PRIMARY KEY, language VARCHAR, developer VARCHAR, year VARCHAR, image BLOB)")

                // SQL kodu ile degiskenleri bagla.
                val sqlString = "INSERT INTO languages(language,developer,year,image) VALUES (?,?,?,?)"
                val statement = database.compileStatement(sqlString)
                statement.bindString(1,language)
                statement.bindString(2,developer)
                statement.bindString(3,year)
                statement.bindBlob(4,byteArray)
                // SQL kodunu isle.
                statement.execute()
            } catch (e : Exception){
                e.printStackTrace()
            }
            finish()
        }

    }
}