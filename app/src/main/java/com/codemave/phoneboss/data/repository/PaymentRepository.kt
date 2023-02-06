package com.codemave.phoneboss.data.repository

import com.codemave.phoneboss.data.entity.Payment
import com.codemave.phoneboss.data.room.PaymentDao
import com.codemave.phoneboss.data.room.PaymentToCategory
import kotlinx.coroutines.flow.Flow

// A data repository for [Payment] instances
class PaymentRepository(
    private val paymentDao: PaymentDao
) {
    //Returns a flow containing the list of payments associated with the category with the
    //given [categoryId]
    fun paymentsInCategory(categoryId: Long) : Flow<List<PaymentToCategory>> {
        return paymentDao.paymentsFromCategory(categoryId)
    }

    // Add a new [Payment] to the payment store
     suspend fun addPayment(payment: Payment) = paymentDao.insert(payment)
}