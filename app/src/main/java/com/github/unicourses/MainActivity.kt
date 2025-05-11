package com.github.unicourses

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.unicourses.ui.theme.UniCoursesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniCoursesTheme {
                MyApp()
            }
        }
    }
}

data class Course(
    val title: String,
    val code: String,
    val creditHours: Int,
    val description: String,
    val prerequisites: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    var showOnboarding by rememberSaveable { mutableStateOf(true) }

    val courses = listOf<Course>(
        Course("Web Development", "CS101", 3, "Learn the basics of HTML, CSS, and JavaScript.", "None"),
        Course("Mobile App Development", "CS102", 4, "Build Android apps using Kotlin and Jetpack Compose.", "CS101"),
        Course("User Interface Design", "CS201", 3, "Design interfaces with usability and aesthetics.", "CS101"),
        Course("Backend Development", "CS202", 3, "Server-side programming using Node.js and databases.", "CS101"),
        Course("Cloud Computing", "CS301", 3, "Introduction to cloud platforms like AWS and Azure.", "CS202"),
        Course("Database Systems", "CS203", 3, "Learn relational databases, SQL, and normalization.", "None"),
        Course("Artificial Intelligence", "CS401", 4, "Fundamentals of AI, ML, and neural networks.", "CS203"),
        Course("DevOps", "CS402", 3, "CI/CD, containerization, and cloud automation.", "CS301")
    )

    if (showOnboarding){
        OnboardingScreen(onContinue = {showOnboarding = false})
    }
    else{
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Course List",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            items(courses.size) { index ->
                ListCourse(course = courses[index])
            }
        }
    }
    }

}

@Composable
fun ListCourse(course: Course, modifier: Modifier = Modifier) {
    var expanded by remember{ mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 6.dp)) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 8.dp),
        color = MaterialTheme.colorScheme.secondary,
        shape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp)) {
        //Colored title part for the Course title
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
                ) {
                Text(course.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    color = Color.White)
                TextButton(
                    onClick = { expanded = !expanded },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                ) {
                    Text(if (expanded) "Less" else "Details")
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) "less" else "Expand"
                    )
                }
            }
    }
        // The code and Credits part (Initially can be viewed)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
                .background(color = MaterialTheme.colorScheme.background)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(
                        bottomEnd = if (expanded) 0.dp else 12.dp,
                        bottomStart = if (expanded) 0.dp else 12.dp
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp),
            ) {
            Column(modifier
                .background(color = MaterialTheme.colorScheme.background)
                .animateContentSize()){
                Text("Code: ${course.code}", style = MaterialTheme.typography.bodyMedium)
                Text("Credits: ${course.creditHours}", style = MaterialTheme.typography.bodyMedium)
//                AnimatedVisibility(
//                    visible = expanded,
//                    enter = fadeIn() + expandVertically(),
//                    exit = fadeOut() + shrinkVertically()
//                    // we can add duration like animationSpec = tween(durationMillis = 250)
//                )
                if (expanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                    {
                        Spacer(modifier.padding(4.dp))
                        Text(
                            "Prerequisites: ${course.prerequisites}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier.padding(4.dp))

                        Text(
                            "Description",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            course.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

            }
        }
        }
    }
}

@Composable
fun OnboardingScreen(modifier: Modifier = Modifier,
                     onContinue: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to UniCourses!")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onContinue
            ) {
                Text(
                    "Continue",
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UniCoursesTheme{
    MyApp()
    }
}