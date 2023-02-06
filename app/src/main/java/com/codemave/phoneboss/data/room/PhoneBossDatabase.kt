package com.codemave.phoneboss.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codemave.phoneboss.data.entity.Category
import com.codemave.phoneboss.data.entity.Payment


//The [RoomDatabase] for this app
@Database(
    entities = [Category::class, Payment::class],
    version = 2,
    exportSchema = false
)
abstract class PhoneBossDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun paymentDao(): PaymentDao
}