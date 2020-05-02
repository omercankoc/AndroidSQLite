package com.omercankoc.androidsqlite

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val languages = ArrayList<String>()
        val ids = ArrayList<Int>()

        // Array Adapter : UI elemanı ile veri setini baglayan yapi.
        val arrayAdapter = ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,languages)
        listView.adapter = arrayAdapter

        try {
            // DB ac.
            val database = this.openOrCreateDatabase("Languages",Context.MODE_PRIVATE,null)
            // Verileri getir.
            val cursor = database.rawQuery("SELECT * FROM languages",null)
            val languageIndex = cursor.getColumnIndex("language")
            val idIndex = cursor.getColumnIndex("id")

            while(cursor.moveToNext()){
                languages.add(cursor.getString(languageIndex))
                ids.add(cursor.getInt(idIndex))
            }
            cursor.close()
            arrayAdapter.notifyDataSetChanged()
        } catch (e : Exception){
            e.printStackTrace()
        }

        // List View'da bir item'e tiklandiginda o item'e ait verilere DetailActivity'de goruntule.
        listView.onItemClickListener = AdapterView.OnItemClickListener {
            parent, view, position, id ->
            val intent = Intent(this, DetailActivity::class.java)
            // Detaylari getir.
            intent.putExtra("detail", "get")
            intent.putExtra("id", ids[position])
            startActivity(intent)
        }
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
            // Yeni olustur.
            intent.putExtra("detail","create")
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }
}


