package com.codemave.mobilecomputing.ui.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.codemave.mobilecomputing.ui.home.categoryReminder.getReminder
import com.google.accompanist.insets.systemBarsPadding
import com.codemave.mobilecomputing.R
import com.codemave.mobilecomputing.data.entity.Account
import com.codemave.mobilecomputing.data.entity.Category
import com.codemave.mobilecomputing.data.entity.Reminder
import com.codemave.mobilecomputing.data.room.ReminderToCategory
import com.codemave.mobilecomputing.ui.home.HomeViewModel
import com.codemave.mobilecomputing.ui.home.categoryReminder.getCategory
import com.codemave.mobilecomputing.ui.login.getProfile
import com.codemave.mobilecomputing.ui.reminder.ReminderViewModel
import com.codemave.mobilecomputing.ui.reminder.ReminderViewState
import kotlinx.coroutines.launch
import com.codemave.mobilecomputing.ui.reminder.getCategoryId
import com.codemave.mobilecomputing.ui.signup.SignUpViewModel
import com.codemave.mobilecomputing.util.viewModelProviderFactoryOf
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EditReminder  (
    navController: NavController,
    viewModel: ReminderViewModel = viewModel(),
    onBackPress: () -> Unit,
){
    val viewState by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val reminder = getReminder()
    val title = rememberSaveable{ mutableStateOf(reminder.reminderTitle)}
    val category = rememberSaveable {mutableStateOf(getCategory().name)}
    val user = getProfile()
    val creator = rememberSaveable{ mutableStateOf(user.name)}
    val reminderTime = rememberSaveable{ mutableStateOf(reminder.reminderTime)}
    val createTime = rememberSaveable { mutableStateOf(reminder.creationTime) }
    val message = rememberSaveable{ mutableStateOf(reminder.reminderMessage)}

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopAppBar {
                IconButton(onClick = onBackPress) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
                Text(text = "Reminder")
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = title.value,
                    onValueChange = {title.value = it},
                    label = {Text(text = "Reminder Title")},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                CategoryListDropdown(
                    viewState = viewState,
                    category = category
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = creator.value,
                    onValueChange = {creator.value = it},
                    label = {Text(text = "Creator Name")},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = reminderTime.value,
                    onValueChange = {reminderTime.value = it},
                    label = {Text(text = "Reminder Time")},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = createTime.value,
                    onValueChange = {createTime.value = it},
                    label = {Text(text = "Create Time")},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = message.value,
                    onValueChange = {message.value = it},
                    label = {Text(text = "Message")},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    enabled = true,
                    onClick = {
                        if ((title.value != reminder.reminderTitle)
                            || category.value != "${reminder.reminderCategoryId}"
                            || creator.value != "${reminder.creatorId}"
                            || reminderTime.value != reminder.reminderTime
                            || message.value != reminder.reminderMessage
                        ) {
                            coroutineScope.launch {
                                viewModel.editReminder(
                                    Reminder(
                                        reminderId = reminder.reminderId,
                                        reminderTitle = title.value,
                                        reminderCategoryId = getCategoryId(
                                            viewState.categories,
                                            category.value
                                        ),
                                        creatorId = user.id,
                                        reminderTime = reminderTime.value,
                                        creationTime = Date().time.toDateString(),
                                        reminderMessage = message.value,
                                    )
                                )
                                navController.navigate("home")
                            }
                            onBackPress()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(55.dp)
                ) {
                    Text ("Save Reminder")
                }
            }
        }
    }
}

@Composable
private fun CategoryListDropdown(
    viewState: ReminderViewState,
    category: MutableState<String>
) {
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded) {
        Icons.Filled.ArrowDropUp // requires androidx.compose.material:material-icons-extended dependency
    } else {
        Icons.Filled.ArrowDropDown
    }

    Column {
        OutlinedTextField(
            value = category.value,
            onValueChange = { category.value = it},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Category") },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            viewState.categories.forEach { dropDownOption ->
                DropdownMenuItem(
                    onClick = {
                        category.value = dropDownOption.name
                        expanded = false
                    }
                ) {
                    Text(text = dropDownOption.name)
                }

            }
        }
    }
}

fun Long.toDateString(): String {
    return SimpleDateFormat("MM dd, yyyy", Locale.getDefault()).format(Date(this))
}

