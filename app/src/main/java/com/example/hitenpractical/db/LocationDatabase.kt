package com.example.hitenpractical.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hitenpractical.model.LocationEntity


@Database(entities = [LocationEntity::class], version = 2)
abstract class LocationDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    companion object {
        private var INSTANCE: LocationDatabase ? = null

        fun getDatabase(context: Context): LocationDatabase? {

            if (INSTANCE == null) {
                synchronized(LocationDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LocationDatabase::class.java,
                        "locatindata.db"
                    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }
    }
}