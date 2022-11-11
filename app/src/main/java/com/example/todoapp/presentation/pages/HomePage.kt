package com.example.todoapp.presentation.pages

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.R
import com.example.todoapp.application.NoteViewModel
import com.example.todoapp.core.di.util.InputValidator
import com.example.todoapp.domain.entity.Note
import com.example.todoapp.ui.theme.Blue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomePage() {
    val vm: NoteViewModel = viewModel()
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar()
        },
        sheetContent = {
            CreateNewNoteBottomSheetContent(onCancel = {
                scope.launch {
                    sheetState.collapse()
                }
            }) { title, des ->
                vm.addNote(title, des)
            }
        },
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(16.dp),
        scaffoldState = scaffoldState
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Body(vm)
            AddNoteButton(Modifier.align(Alignment.BottomCenter)) {
                scope.launch {
                    sheetState.expand()
                }
            }
        }
    }

}

@Composable
fun CreateNewNoteBottomSheetContent(
    onCancel: () -> Unit,
    onSubmit: (title: String, des: String) -> Unit
) {

    var title by remember {
        mutableStateOf("")
    }

    var des by remember {
        mutableStateOf("")
    }

    Box(Modifier.padding(horizontal = 20.dp)) {
        Column {
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(4.dp)
                        .background(Color(0xffEFF3F4), RoundedCornerShape(10.dp))
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "New Task ToDo",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Divider(thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(30.dp))
            Text(text = "Note Title", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            CustomTextField(
                value = title,
                placeHolderString = "Add title...",
                isValid = InputValidator.isValidTitle(title)
            ) {
                title = it
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Description", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            CustomTextField(
                value = des,
                placeHolderString = "Add description...",
                isValid = InputValidator.isValidDes(des)
            ) {
                des = it
            }
            Spacer(modifier = Modifier.height(40.dp))
            Row() {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onCancel()
                        title = ""
                        des = ""
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Blue),
                    border = BorderStroke(1.dp, Blue),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(vertical = 15.dp)
                ) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = InputValidator.isValidEntry(title, des),
                    onClick = {
                        onSubmit(title, des)
                        onCancel()
                        title = ""
                        des = ""
                    },
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Blue),
                    contentPadding = PaddingValues(vertical = 15.dp)
                ) {
                    Text(text = "Create", color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    placeHolderString: String,
    isValid: Boolean,
    onValueChange: (value: String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeHolderString, color = Color.Gray, fontWeight = FontWeight.Light)
        },
        trailingIcon = {
            if (isValid) Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = "",
                tint = Blue
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color(0xffEFF3F4),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
fun Body(vm: NoteViewModel) {
    Column() {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "On Progress",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xff656768)
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "(${vm.state.value.data?.filter { !it.isDone }?.size})",
                    style = TextStyle(fontSize = 14.sp, color = Color.Gray)
                )
            }
            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    text = "View More",
                    style = TextStyle(color = Blue, fontSize = 10.sp)
                )
            }
        }
        if (vm.state.value.data!!.any { !it.isDone }) {
            LazyRow(contentPadding = PaddingValues(20.dp, 0.dp, 0.dp, 0.dp)) {
                itemsIndexed(vm.state.value.data!!.filter { !it.isDone }) { _, note ->
                    OnProgressNoteItem(note = note) {
                        vm.toggleCompleteNote(note, it)
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 20.dp)
            ) {
                Text(
                    text = "No Pending Notes 🤷",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Completed",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff656768)
                )
            )
            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    text = "View More",
                    style = TextStyle(color = Color(0xff2E86FC), fontSize = 10.sp)
                )
            }
        }
        if (vm.state.value.data!!.any { it.isDone }) {
            LazyColumn(contentPadding = PaddingValues(20.dp, 0.dp, 0.dp, 0.dp)) {
                itemsIndexed(vm.state.value.data!!.filter { it.isDone }) { _, note ->
                    CompletedNoteItem(note = note) {
                        vm.toggleCompleteNote(note, it)
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 20.dp)
            ) {
                Text(
                    text = "Complete pending tasks 👀",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun AddNoteButton(modifier: Modifier, onAddPressed: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        FloatingActionButton(
            onClick = onAddPressed, backgroundColor = Color(0xff3085FE),
            shape = RoundedCornerShape(10.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(Modifier.size(14.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            tint = Color.White,
                            contentDescription = "Add"
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Create New", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun AppBar() {
    TopAppBar(
        backgroundColor = Color.White,
        contentPadding = PaddingValues(20.dp, 20.dp, 20.dp, 10.dp),
        elevation = 0.dp,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Card(
                    Modifier
                        .size(40.dp),
                    shape = CircleShape,
                    backgroundColor = Color(0xffF8D5A3),
                    elevation = 20.dp,
                    border = BorderStroke(1.dp, Color.White)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_avatar),
                        contentDescription = "Avatar"
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column() {
                    Text(
                        text = "Hello,",
                        style = TextStyle(fontSize = 12.sp, color = Color.Gray)
                    )
                    Text(
                        text = "James Bond", style = TextStyle(
                            fontSize = 14.sp, fontWeight = FontWeight.Bold,
                        )
                    )
                }
            }
            Row() {
                AppbarActionItem(R.drawable.ic_calendar)
                Spacer(modifier = Modifier.width(10.dp))
                AppbarActionItem(R.drawable.ic_notifications)
            }
        }
    }
}

@Composable
fun AppbarActionItem(@DrawableRes res: Int) {
    Card(
        shape = CircleShape,
    ) {
        Box(modifier = Modifier.padding(6.dp)) {
            Icon(
                painter = painterResource(res),
                contentDescription = "Icon",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun CompletedNoteItem(note: Note, onToggleComplete: (value: Boolean) -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp, 14.dp, 2.dp),
    ) {
        Box(Modifier.drawBehind {
            drawRect(
                color = Color(0xffF9B5BE),
                topLeft = Offset(0f, 0f),
                size = Size(15f, size.height)
            )
        }) {
            Column(
                Modifier
                    .padding(
                        20.dp,
                        10.dp,
                        10.dp,
                        10.dp,
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = note.title, fontSize = 12.sp,
                        modifier = Modifier.weight(0.7f)
                    )
                    Checkbox(
                        checked = note.isDone,
                        onCheckedChange = onToggleComplete,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Blue,
                            uncheckedColor = Color.Gray
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Description:", style = TextStyle(color = Color.Gray, fontSize = 10.sp),
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = note.des, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun OnProgressNoteItem(note: Note, onToggleComplete: (value: Boolean) -> Unit) {
    Card(
        modifier = Modifier
            .padding(0.dp, 10.dp, 14.dp, 0.dp),
    ) {
        Box(Modifier.drawBehind {
            drawRect(
                color = Color(0xffFDE7C6),
                topLeft = Offset(0f, (center.y * 2 - 15f)),
                size = Size(size.width, 15f)
            )
        }) {
            Column(
                Modifier
                    .padding(
                        10.dp,
                        10.dp,
                        10.dp,
                        20.dp
                    )
            ) {
                Row(
                    modifier = Modifier.widthIn(
                        0.dp,
                        (LocalConfiguration.current.screenWidthDp * 0.5).dp
                    ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = note.title, fontSize = 12.sp,
                        modifier = Modifier.widthIn(
                            0.dp,
                            (LocalConfiguration.current.screenWidthDp * 0.4).dp
                        )
                    )
                    Checkbox(
                        checked = note.isDone, onCheckedChange = onToggleComplete,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Blue,
                            uncheckedColor = Color.Gray
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Description:", style = TextStyle(color = Color.Gray, fontSize = 10.sp),
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = note.des, fontSize = 12.sp)
            }
        }
    }
}