package com.ribakova.contactsviewer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ribakova.contactsviewer.domain.Contact

@Database(
    entities = [Contact::class],
    version = 1
)
abstract class ContactsDatabase: RoomDatabase() {
    abstract fun getContactsDao(): ContactsDao

    companion object {
        @Volatile
        private var INSTANCE: ContactsDatabase? = null

        fun getInstance(context: Context): ContactsDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room
                        .databaseBuilder(
                            context.applicationContext,
                            ContactsDatabase::class.java,
                            "contacts.db"
                        )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}