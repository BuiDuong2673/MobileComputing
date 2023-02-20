package com.codemave.mobilecomputing.ui.reminder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codemave.mobilecomputing.R
import com.codemave.mobilecomputing.data.entity.Account
import com.codemave.mobilecomputing.data.entity.Category
import com.codemave.mobilecomputing.data.entity.Reminder
import com.codemave.mobilecomputing.ui.login.getProfile
import com.codemave.mobilecomputing.ui.signup.SignUpViewModel
import com.google.accompanist.insets.systemBarsPadding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Reminder (
    onBackPress: () -> Unit,
    viewModel: ReminderViewModel = viewModel()
) {
    val viewState by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val title = rememberSaveable{ mutableStateOf("") }
    val category = rememberSaveable{ mutableStateOf("") }
    val creator = rememberSaveable { mutableStateOf(getProfile().name) }
    val remindDay = rememberSaveable{ mutableStateOf("") }
    val remindMonth = rememberSaveable {mutableStateOf("")}
    val remindYear = rememberSaveable{ mutableStateOf("") }
    val remindHour = rememberSaveable{ mutableStateOf("") }
    val remindMin = rememberSaveable{ mutableStateOf("") }
    val reminderMessage = rememberSaveable{ mutableStateOf("") }
    val viewModelSignUp: SignUpViewModel = viewModel()
    val viewStateSignUp by viewModelSignUp.state.collectAsState()
    Surface {
        Column(modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()) {
            TopAppBar {
                IconButton(onClick = onBackPress) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back))
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
                    label = { Text(text = "Reminder Title")},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                CategoryListDropDown(
                    viewState = viewState,
                    category = category
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = creator.value,
                    onValueChange = {creator.value = it},
                    label = { Text(text = "Creator Name")},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Choose the remind date",
                    maxLines = 1,
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = remindDay.value,
                        onValueChange = {remindDay.value = it},
                        label = {Text(text = "Day")},
                        keyboardOptions = KeyboardOptions (
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(end = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = remindMonth.value,
                        onValueChange = {remindMonth.value = it},
                        label = {Text(text = "Month")},
                        keyboardOptions = KeyboardOptions (
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(end = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = remindYear.value,
                        onValueChange = {remindYear.value = it},
                        label = {Text(text = "Year")},
                        keyboardOptions = KeyboardOptions (
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.weight(0.5f)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Divider()
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Choose the remind time",
                    maxLines = 1,
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = remindHour.value,
                        onValueChange = {remindHour.value = it},
                        label = {Text(text = "Hour")},
                        keyboardOptions = KeyboardOptions (
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = remindMin.value,
                        onValueChange = {remindMin.value = it},
                        label = {Text(text = "Minute")},
                        keyboardOptions = KeyboardOptions (
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = reminderMessage.value,
                    onValueChange = {reminderMessage.value = it},
                    label = {Text(text = "Message")},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        if (category.value.isNotEmpty()) {
                            coroutineScope.launch {
                                viewModel.saveReminder(
                                    Reminder(
                                        reminderTitle = title.value,
                                        reminderCategoryId = getCategoryId(viewState.categories, category.value),
                                        creatorId = getCreatorId(viewStateSignUp.accounts, creator.value),
                                        reminderTime = dateToString(remindDay.value, remindMonth.value, remindYear.value, remindHour.value, remindMin.value),
                                        creationTime = Date().time.toDateString(),
                                        reminderMessage = reminderMessage.value
                                    )
                                )
                            }
                        }
                        onBackPress()
                    },
                    enabled = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(55.dp)
                ) {
                    Text("Save Reminder")
                }
            }
        }
    }
}

@Composable
private fun CategoryListDropDown(
    viewState: ReminderViewState,
    category: MutableState<String>
) {
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded) {
        Icons.Filled.ArrowDropUp
    } else {
        Icons.Filled.ArrowDropDown
    }
    Column {
        OutlinedTextField(
            value = category.value,
            onValueChange = {category.value = it},
            modifier = Modifier.fillMaxWidth(),
            label = {Text("Category")},
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.clickable {expanded = !expanded}
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
                    Text(dropDownOption.name)
                }
            }
        }
    }
}

private fun dateToString(day: String, month: String, year: String, hour: String, min: String): String {
    return "$month $day, $year ${hour}:${min}"
}

fun Long.toDateString(): String {
    return SimpleDateFormat("MM dd, yyyy", Locale.getDefault()).format(Date(this))
}

fun getCategoryId(categories: List<Category>, categoryName: String): Long {
    val category: Category? = categories.first{category -> category.name == categoryName}
    return category?.id?:-1
}

fun getCreatorId(accounts: List<Account>, userName: String): Long {
    val account: Account? = accounts.first { account -> account.name == userName }
    return account?.id?: -1
}