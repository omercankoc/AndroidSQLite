package com.omercankoc.sqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class ListViewActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)
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
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }
}