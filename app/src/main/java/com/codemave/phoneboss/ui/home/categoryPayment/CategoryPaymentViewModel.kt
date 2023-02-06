package com.codemave.phoneboss.ui.home.categoryPayment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemave.phoneboss.Graph
import com.codemave.phoneboss.data.repository.PaymentRepository
import com.codemave.phoneboss.data.room.PaymentToCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CategoryPaymentViewModel(
    private val categoryId: Long,
    private val paymentRepository: PaymentRepository = Graph.paymentRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CategoryPaymentViewState())

    val state: StateFlow<CategoryPaymentViewState>
        get() = _state

    init {
        /*
        val paymentList = mutableListOf<Payment>()
        for (x in 1..20) {
                Payment (
                    paymentId = x.toLong(),
                    paymentTitle = "$x payment",
                    paymentDate = Date().time,
                    paymentCategoryId = 0,
                    paymentAmount = 5.0
                )
            paymentList.forEach { payment -> paymentRepository.addPayment(payment) }
        }*/
        viewModelScope.launch {
            //paymentList.forEach { payment -> paymentRepository.addPayment(payment) }
            paymentRepository.paymentsInCategory(categoryId).collect { list ->
                _state.value = CategoryPaymentViewState(
                    payments = list
                )
            }
        }
    }
}

data class CategoryPaymentViewState(
    val payments: List<PaymentToCategory> = emptyList()
)