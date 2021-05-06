package com.omercankoc.sqlite

import android.content.Intent
import android.content.pm.PackageManager
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

class DetailsActivity : AppCompatActivity() {

    var selectedLogo : Uri? = null

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
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        imageViewLogo.setImageBitmap(bitmap)
                    }

                    // Versiyon 28'den kucuk ise...
                    else {
                        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,selectedLogo)
                        imageViewLogo.setImageBitmap(bitmap)
                    }
                }

            } catch (e : Exception){
                e.printStackTrace()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun save(view: View){

    }
}