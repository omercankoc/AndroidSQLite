## Sample of using SQLite with Android

This repository includes android and sqlite project sample. First, let's create a database and examine the 4 basic operations later.

If not, create a table and name it 'language':
```kotlin
database.execSQL("CREATE TABLE IF NOT EXISTS languages (id INTEGER PRIMARY KEY, name VARCHAR, year INT)")
```
'execSQL' runs SQL code.

Insert Data in DB:
```kotlin
database.execSQL("INSERT INTO languages (name,year) VALUES ('Kotlin',2017)")
database.execSQL("INSERT INTO languages (name,year) VALUES ('Swift',2014)")
database.execSQL("INSERT INTO languages (name,year) VALUES ('Go',2009)")
database.execSQL("INSERT INTO languages (name,year) VALUES ('Rust',2010)")
database.execSQL("INSERT INTO languages (name,year) VALUES ('Dart',2010)")
```
Get All Datas:
```kotlin
val cursor = database.rawQuery("SELECT * FROM languages", null)
val idIndex = cursor.getColumnIndex("id")
val nameIndex = cursor.getColumnIndex("name")
val yearIndex = cursor.getColumnIndex("year")
while (cursor.moveToNext()){
    println(
        " ID : " + cursor.getInt(idIndex) +
        " Name : " + cursor.getString(nameIndex) +
        " Year : " + cursor.getInt(yearIndex))
} cursor.close()
```
'rawQuery' runs queries.

Get the Requested Data:
```kotlin
val cursor = database.rawQuery("SELECT * FROM languages WHERE name ='Kotlin'", null)
val idIndex = cursor.getColumnIndex("id")
val nameIndex = cursor.getColumnIndex("name")
val yearIndex = cursor.getColumnIndex("year")
while (cursor.moveToNext()){
    println(
        " ID : " + cursor.getInt(idIndex) +
        " Name : " + cursor.getString(nameIndex) +
        " Year : " + cursor.getInt(yearIndex))
} cursor.close()
```

Update:
```kotlin
database.execSQL("UPDATE languages SET year = 2011 WHERE name = 'Dart'")
val cursor = database.rawQuery("SELECT * FROM languages", null)
val idIndex = cursor.getColumnIndex("id")
val nameIndex = cursor.getColumnIndex("name")
val yearIndex = cursor.getColumnIndex("year")
while (cursor.moveToNext()){
    println(
         " ID : " + cursor.getInt(idIndex) +
         " Name : " + cursor.getString(nameIndex) +
         " Year : " + cursor.getInt(yearIndex))
} cursor.close()
```

Remove:
```kotlin
database.execSQL("DELETE FROM languages WHERE name = 'Dart'")
val cursor = database.rawQuery("SELECT * FROM languages", null)
val idIndex = cursor.getColumnIndex("id")
val nameIndex = cursor.getColumnIndex("name")
val yearIndex = cursor.getColumnIndex("year")
while (cursor.moveToNext()){
    println(
         " ID : " + cursor.getInt(idIndex) +
         " Name : " + cursor.getString(nameIndex) +
         " Year : " + cursor.getInt(yearIndex))
} cursor.close()
```


