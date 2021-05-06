package com.omercankoc.sqlite

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import java.lang.Exception

class ListViewActivity : AppCompatActivity() {

    private lateinit var listView : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)
        listView = findViewById(R.id.listView)

        val languagesList = ArrayList<String>()
        val idsList = ArrayList<Int>()

        // Olusturulan languageList ile array Adapter'i birbirine bagla.
        val arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,languagesList)
        listView.adapter = arrayAdapter

        try {
            val database = this.openOrCreateDatabase("Languages",Context.MODE_PRIVATE,null)
            val cursor = database.rawQuery("SELECT * FROM languages",null)
            val idIndex = cursor.getColumnIndex("id")
            val languageIndex = cursor.getColumnIndex("language")

            while (cursor.moveToNext()){
                idsList.add(cursor.getInt(idIndex))
                languagesList.add(cursor.getString(languageIndex))
            }

            // Guncelle.
            arrayAdapter.notifyDataSetChanged()

            cursor.close()
        } catch (e : Exception){
            e.printStackTrace()
        }

        // ListView'da bir kayida tiklandiginda o kayida ait DetailsActivity'e git.
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(this,DetailsActivity::class.java)
            intent.putExtra("info","old")
            intent.putExtra("id",idsList[position])
            startActivity(intent)
        }
    }

    // XML ile olusturulan menuyu bagla...
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        // Inflater : XML dosyasini arayuze baglama islemine inflater denir.
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add,menu)

        return super.onCreateOptionsMenu(menu)
    }

    // Menuden secilen itemi anla ve operasyonunu isle...
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // Add Item ise DetailsActivity'e yonlendir.
        if(item.itemId == R.id.add_item){
            val intent = Intent(this,DetailsActivity::class.java)
            // Yeni kayit olusturmasi icin gerekli bilgiyi gonder.
            intent.putExtra("info","new")
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }
}