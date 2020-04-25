package com.omercankoc.androidsqlite

class Tutorial {
    /*
    try{

            // bu veritabanını var ise ac yok ise olustur.
            val database = this.openOrCreateDatabase("Users", Context.MODE_PRIVATE, null)
            // SQL Kodu çalıştırır.
            // tablo oluştur
            database.execSQL("CREATE TABLE IF NOT EXISTS users(id INTEGER PRIMARY KEY,name VARCHAR, surname VARCHAR)")
            // tabloya veri ekle
            database.execSQL("INSERT INTO users(name,surname) VALUES('Ömer','Koç')")

            // Veri Çekmek
            val cursor : Cursor = database.rawQuery("SELECT * FROM users",null)
            // Index degerlerini alma
            val idIndex : Int = cursor.getColumnIndex("id")
            val nameIndex : Int = cursor.getColumnIndex("name")
            val surnameIndex :Int = cursor.getColumnIndex("surname")

            while(cursor.moveToNext()){
                println("ID" + cursor.getString(idIndex))
                println("Name" + cursor.getString(nameIndex))
                println("Surname" + cursor.getString(surnameIndex))
            }

            cursor.close()

            // Filtering
            val cursor2 : Cursor = database.rawQuery("SELECT * FROM users WHERE name = 'Omer'",null)
            // Index degerlerini alma
            val idIndex2 : Int = cursor.getColumnIndex("id")
            val nameIndex2 : Int = cursor.getColumnIndex("name")
            val surnameIndex2 :Int = cursor.getColumnIndex("surname")

            while(cursor.moveToNext()){
                println("ID" + cursor2.getString(idIndex2))
                println("Name" + cursor2.getString(nameIndex2))
                println("Surname" + cursor2.getString(surnameIndex2))
            }

            cursor.close()

            // update
            database.execSQL("UPDATE users SET name = 'Omer Can' WHERE name = 'Omer'")

            // delete
            database.execSQL("DELETE FROM users WHERE name = 'Omer Can'")

        } catch (e:Exception){
            e.printStackTrace()
        }
     */
}