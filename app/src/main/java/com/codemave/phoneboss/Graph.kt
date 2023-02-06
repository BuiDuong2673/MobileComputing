package com.codemave.phoneboss

import android.content.Context
import androidx.room.Room
import com.codemave.phoneboss.data.repository.CategoryRepository
import com.codemave.phoneboss.data.repository.PaymentRepository
import com.codemave.phoneboss.data.room.PhoneBossDatabase

/**
 * A simple singleton dependency graph
 *
 * For a real app, please use something like Koin/Dagger/Hilt instead
 */
object Graph {
    lateinit var database: PhoneBossDatabase

    val categoryRepository by lazy {
        CategoryRepository(
            categoryDao = database.categoryDao()
        )
    }

    val paymentRepository by lazy {
        PaymentRepository(
            paymentDao = database.paymentDao()
        )
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, PhoneBossDatabase::class.java, "mcData.db")
            .fallbackToDestructiveMigration() // don't use this in production app
            .build()
    }
}