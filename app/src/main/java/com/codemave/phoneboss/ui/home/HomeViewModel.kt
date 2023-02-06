package com.codemave.phoneboss.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemave.phoneboss.Graph
import com.codemave.phoneboss.Graph.paymentRepository
import com.codemave.phoneboss.data.entity.Category
import com.codemave.phoneboss.data.entity.Payment
import com.codemave.phoneboss.data.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(
    private val categoryRepository: CategoryRepository = Graph.categoryRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeViewState())
    private val _selectedCategory = MutableStateFlow<Category?>(null)

    val state: StateFlow<HomeViewState>
        get() = _state

    fun onCategorySelected(category: Category) {
        _selectedCategory.value = category
    }

    init {
        viewModelScope.launch {
            combine(
                categoryRepository.categories().onEach { list ->
                    if (list.isNotEmpty() && _selectedCategory.value == null) {
                        _selectedCategory.value = list[0]
                    }
                },
                _selectedCategory
            ) { categories, selectedCategory ->
                HomeViewState(
                    categories = categories,
                    selectedCategory = selectedCategory
                )
            }.collect { _state.value = it }
        }
        loadCategoriesFromDb()
        loadPaymentDb()
    }

    private fun loadCategoriesFromDb() {
        val list = mutableListOf(
            Category(name = "Food"),
            Category(name = "Health"),
            Category(name = "Savings"),
            Category(name = "Drinks"),
            Category(name = "Clothing"),
            Category(name = "Investment"),
            Category(name = "Travel"),
            Category(name = "Fuel"),
            Category(name = "Repairs"),
            Category(name = "Coffee")
        )
        viewModelScope.launch {
            list.forEach { category -> categoryRepository.addCategory(category) }
        }
    }

    private fun loadPaymentDb() {
        val paymentList = mutableListOf(
            Payment (paymentId = 1, paymentTitle = "1st payment",
                paymentDate = Date().time, paymentCategoryId = 1, paymentAmount = 5.0),
            Payment (paymentId = 2, paymentTitle = "2nd payment",
            paymentDate = Date().time, paymentCategoryId = 1, paymentAmount = 5.0),
            Payment (paymentId = 3, paymentTitle = "3rd payment",
                paymentDate = Date().time, paymentCategoryId = 1, paymentAmount = 5.0),
            Payment (paymentId = 4, paymentTitle = "4th payment",
                paymentDate = Date().time, paymentCategoryId = 1, paymentAmount = 5.0),
            Payment (paymentId = 5, paymentTitle = "5th payment",
                paymentDate = Date().time, paymentCategoryId = 1, paymentAmount = 5.0),
            Payment (paymentId = 6, paymentTitle = "6th payment",
                paymentDate = Date().time, paymentCategoryId = 1, paymentAmount = 5.0),
            Payment (paymentId = 7, paymentTitle = "7th payment",
                paymentDate = Date().time, paymentCategoryId = 1, paymentAmount = 5.0),
            Payment (paymentId = 8, paymentTitle = "8th payment",
                paymentDate = Date().time, paymentCategoryId = 1, paymentAmount = 5.0),
            Payment (paymentId = 9, paymentTitle = "9th payment",
                paymentDate = Date().time, paymentCategoryId = 1, paymentAmount = 5.0),
            Payment (paymentId = 10, paymentTitle = "10th payment",
                paymentDate = Date().time, paymentCategoryId = 1, paymentAmount = 5.0),
        )
        viewModelScope.launch {
            paymentList.forEach { payment -> paymentRepository.addPayment(payment) }
        }
    }
}

data class HomeViewState(
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null
)