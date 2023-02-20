package com.codemave.mobilecomputing.ui.signup

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemave.mobilecomputing.Graph
import com.codemave.mobilecomputing.R
import com.codemave.mobilecomputing.data.entity.Account
import com.codemave.mobilecomputing.data.repository.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel (
    private val accountRepository: AccountRepository = Graph.accountRepository
): ViewModel(){
    private val _state = MutableStateFlow(SignUpViewState())

    val state: StateFlow<SignUpViewState>
        get() = _state

    suspend fun saveAccount(account: Account): Long {
        return if (account.name != account.password) {
            createSuccessNotification()
            accountRepository.addAccount(account)
        } else {
            createErrorNotification()
            404
        }
    }

    suspend fun editAccount(account: Account) {
        accountRepository.editAccount(account)
    }

    init {
        createNotificationChannel(context = Graph.appContext)
        viewModelScope.launch {
            accountRepository.accounts().collect { accounts ->
                _state.value= SignUpViewState(accounts)
            }
        }
    }
}

private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "NotificationChannelName"
        val descriptionText = "NotificationChannelDescriptionText"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
            description = descriptionText
        }
        // register the channel with the system
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

private fun createSuccessNotification() {
    val notificationId = 1
    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_stat_success)
        .setContentTitle("Create Account Success!")
        .setContentText("Your account has been added")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    with (NotificationManagerCompat.from(Graph.appContext)) {
        notify(notificationId, builder.build())
    }
}

private fun createErrorNotification() {
    val notificationId = 2
    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_stat_name)
        .setContentTitle("Create Account Failed")
        .setContentText("Your username is not unique.\nAlso, password must different than username.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    with(NotificationManagerCompat.from(Graph.appContext)) {
        notify(notificationId, builder.build())
    }
}


data class SignUpViewState(
    val accounts: List<Account> = emptyList()
)