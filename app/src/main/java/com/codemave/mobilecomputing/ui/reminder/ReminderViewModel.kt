package com.codemave.mobilecomputing.ui.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.codemave.mobilecomputing.Graph
import com.codemave.mobilecomputing.data.entity.Category
import com.codemave.mobilecomputing.data.entity.Reminder
import com.codemave.mobilecomputing.data.repository.CategoryRepository
import com.codemave.mobilecomputing.data.repository.ReminderRepository
import com.codemave.mobilecomputing.util.NotificationWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import com.codemave.mobilecomputing.R

class ReminderViewModel(
    private val reminderRepository: ReminderRepository = Graph.reminderRepository,
    private val categoryRepository: CategoryRepository = Graph.categoryRepository
): ViewModel() {
    private val _state = MutableStateFlow(ReminderViewState())

    val state: StateFlow<ReminderViewState>
        get() = _state

    suspend fun saveReminder(reminder: Reminder): Long {
        return if (reminder.reminderCategoryId.equals(-1)) {
            createReminderMadeErrorNoti()
            0
        } else {
            createReminderMadeNotification(reminder)
            reminderRepository.addReminder(reminder)
        }
    }

    suspend fun deleteReminder(reminder: Reminder): Int {
        return reminderRepository.deleteReminder(reminder)
    }

    suspend fun editReminder(reminder: Reminder) {
        reminderRepository.editReminder(reminder)
    }

    init {
        createNotificationChannel(context = Graph.appContext)
        setOneTimeNotification()
        viewModelScope.launch {
            categoryRepository.categories().collect { categories ->
                _state.value = ReminderViewState(categories)
            }
        }
    }
}

/**
 * Create the [NotificationChannel], but only on API 26+ because
 * the NotificationChannel class is new and not in the support library
 */
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

private fun setOneTimeNotification(){
    val workManager = WorkManager.getInstance(Graph.appContext)
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
    val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(10, TimeUnit.SECONDS)
        .setConstraints(constraints)
        .build()
    workManager.enqueue(notificationWorker)

    //Monitoring for state of work
    workManager.getWorkInfoByIdLiveData(notificationWorker.id)
        .observeForever { workInfo ->
            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                createSuccessNotification()
            } else if (workInfo.state == WorkInfo.State.FAILED) {
                createErrorNotification()
            }
        }
}

private fun createSuccessNotification() {
    val notificationId = 1
    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_stat_success)
        .setContentTitle("Open Success!")
        .setContentText("Your countdown complete successfully!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    with (NotificationManagerCompat.from(Graph.appContext)) {
        // notificationId is unique for each notification that you define
        notify (notificationId, builder.build())
    }
}

private fun createErrorNotification() {
    val notificationId = 3
    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_stat_name)
        .setContentTitle("Error Occur")
        .setContentText("There is some error in the opening process!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    with (NotificationManagerCompat.from(Graph.appContext)) {
        notify (notificationId, builder.build())
    }
}

private fun createReminderMadeNotification(reminder: Reminder) {
    val notificationId = 2
    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_stat_add)
        .setContentTitle("New reminder made")
        .setContentText("You have saved ${reminder.reminderTitle} on ${reminder.reminderTime}")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    with (NotificationManagerCompat.from(Graph.appContext)) {
        notify(notificationId, builder.build())
    }
}

private fun createReminderMadeErrorNoti() {
    val notificationId = 4
    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_stat_name)
        .setContentTitle("Error Occur")
        .setContentText("Please enter all of the value to add the reminder!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    with (NotificationManagerCompat.from(Graph.appContext)) {
        notify (notificationId, builder.build())
    }
}

data class ReminderViewState(
    val categories: List<Category> = emptyList()
)