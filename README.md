# PeriodFriend Apps
Aplikasi yang digunakan untuk membantu para perempuan untuk tracking siklus menstruasi dan juga membantu memberitahu apakah siklus menstruasi normal atau tidak

## Features

- Menambahkan data bulanan menstruasi
- Mengubah nilai data yang salah
- Menghapus nilai data
- Melihat seluruh data mensturasi dan memberikan keterangan siklus haid

## Requirement
- Android Studio (disarankan menggunakan versi gariffe atau yang lebih tinggi)
- firebase (digunakan untuk meyimpan data menggunakan firestore)


## Tech
Aplikasi dibangun dengan menggunakan :
- [Android Studio](https://developer.android.com/) - Android Studio
- [Firebase](https://firebase.google.com/) - Firebase

## Installation
- download project ini
- buka android studio
- click open folder dan pilih direktori dimana anda menyimpan pada saat mendownload project ini
- Tambahkan file google-services.json ke direktori app/.
- sync project dengan gradle files
- sdk 34 dibutuhkan untuk menjalan project ini
- Pada emulator gunakan API 30 

## Demo Project 
- dashboard
  -- pada dashboard kita dapat memilih beberapa menu yang dapat digunakan seperti Add Data, Manage Data, Record Data
- add data
  -- pada add data kita dapat melakukan pembuatan data mensturasi yang disimpan pada database yang di sediakan
- manage data
  -- pada manage data kita mendapatkan beberapa fitur yaitu seperti : melakukan perubahan data (update data), melihat keseluruhan data (read data), dan juga dapat menghapus suatu data (delete data)
- record data
  -- pada record data kita dapat memmantau (tracking) siklus menstruasi setiap bulan secara berkala, pada record data ditambahkan sebuah logika yang otomatis akan terbuat untuk memberitahu user apakah siklus haid pada bulan pertama dan bulan kedua normal atau      tidak normal



