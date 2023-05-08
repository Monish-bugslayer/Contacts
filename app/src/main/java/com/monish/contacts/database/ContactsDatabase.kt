package com.monish.contacts.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database([ContactsEntity::class], version = 2, exportSchema = false)
abstract class ContactsDatabase: RoomDatabase() {
    abstract fun contactDao(): ContactsDao

    companion object {
        @Volatile
        private var instance: ContactsDatabase? = null
        fun getInstance(context: Context): ContactsDatabase {
            val tempInstance = instance
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val newInstance =Room.databaseBuilder(
                    context,
                    ContactsDatabase::class.java,
                    "Contacts DataBase").fallbackToDestructiveMigration().build()
                instance = newInstance
                return newInstance
            }
        }
    }
}