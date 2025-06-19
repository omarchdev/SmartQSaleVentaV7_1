package com.omarchdev.smartqsale.smartqsaleventas.Db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.omarchdev.smartqsale.smartqsaleventas.Dao.ProductDao
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.dto.mProductDto




@Database(entities = [mProductDto::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao?

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase::class.java, "smartqsale_db"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}