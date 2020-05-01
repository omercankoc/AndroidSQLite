package com.omercankoc.androidsqlite

import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // Olusturulan menuyu baglama operasyonu.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        // Inflater : XML görünüm tanımını okuyan ve
        // bunları Kotlin tabanlı View nesnelerine dönüştüren bir sınıftır.
        val menuInflater : MenuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_add,menu)

        return super.onCreateOptionsMenu(menu)
    }

    // Menude secilen item'in hangisi oldugunu tanimlayip, secim sonrasi yapilacak operasyonu belirt.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.add_item){
            val intent = Intent(this,DetailActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }
}


