package com.example.flicksnow

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.flicksnow.ui.theme.FlicksNowTheme

class BookingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlicksNowTheme {
                BookingScreen(
                    onShowToast = { msg ->
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

// ----- DATA MODELS -----

data class MovieInfo(
    val title: String,
    val genre: String,
    val duration: String,
    val rating: String,
    val language: String,
    val showTimes: List<String>,
    val certificate: String
)

data class BookingDate(
    val label: String,
    val day: String
)

// ----- SAMPLE DATA -----

private val sampleDates = listOf(
    BookingDate("Today", "Mon 03"),
    BookingDate("Tomorrow", "Tue 04"),
    BookingDate("Wed", "Wed 05"),
    BookingDate("Thu", "Thu 06"),
    BookingDate("Fri", "Fri 07")
)

private val sampleMovies = listOf(
    MovieInfo(
        title = "The Last Stand",
        genre = "Action • Thriller",
        duration = "2h 15m",
        rating = "⭐ 4.5",
        language = "English",
        showTimes = listOf("10:30", "13:15", "16:00", "20:30"),
        certificate = "U/A"
    ),
    MovieInfo(
        title = "Love in Paris",
        genre = "Romance • Drama",
        duration = "2h 02m",
        rating = "⭐ 4.2",
        language = "English",
        showTimes = listOf("11:00", "14:30", "18:15"),
        certificate = "U"
    ),
    MovieInfo(
        title = "Galaxy Quest",
        genre = "Sci-Fi • Adventure",
        duration = "2h 20m",
        rating = "⭐ 4.7",
        language = "English",
        showTimes = listOf("09:45", "13:00", "17:40", "21:10"),
        certificate = "U/A"
    )
)

// ----- MAIN BOOKING SCREEN -----

@Composable
fun BookingScreen(
    onShowToast: (String) -> Unit
) {
    var selectedTab by rememberSaveable { mutableStateOf(0) }
    var selectedDateIndex by rememberSaveable { mutableStateOf(0) }

    // Light brown gradient background
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFF3E0),
            Color(0xFFD7CCC8)
        )
    )

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            FlicksBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(padding)
        ) {
            when (selectedTab) {
                0 -> BookingHomeContent(
                    selectedDateIndex = selectedDateIndex,
                    onDateSelected = { selectedDateIndex = it },
                    movies = sampleMovies,
                    onBookClicked = { movieTitle, time ->
                        onShowToast("Booked $movieTitle at $time")
                    }
                )

                1 -> MyBookingsPlaceholder()
                2 -> ProfilePlaceholder()
            }
        }
    }
}

// ----- HOME TAB CONTENT (MOVIE LIST) -----

@Composable
fun BookingHomeContent(
    selectedDateIndex: Int,
    onDateSelected: (Int) -> Unit,
    movies: List<MovieInfo>,
    onBookClicked: (movieTitle: String, time: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Book Tickets",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF4E342E)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Choose a date, pick your movie, and book seats.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF5D4037)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Location chip (static for now)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Location:",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF4E342E)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                color = Color(0xFFFFE0B2),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "Chennai, IN",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF4E342E)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Date selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            sampleDates.forEachIndexed { index, date ->
                val selected = index == selectedDateIndex
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (selected) Color(0xFF6D4C41) else Color(0xFFFFE0B2),
                    tonalElevation = if (selected) 4.dp else 0.dp,
                    modifier = Modifier
                        .width(90.dp)
                        .clickable { onDateSelected(index) }
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = date.label,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (selected) Color.White else Color(0xFF5D4037)
                        )
                        Text(
                            text = date.day,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (selected) Color.White else Color(0xFF4E342E)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Now Showing",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color(0xFF4E342E)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(movies) { movie ->
                MovieCard(
                    movie = movie,
                    onBookClicked = { time -> onBookClicked(movie.title, time) }
                )
            }
        }
    }
}

// ----- MOVIE CARD -----

@Composable
fun MovieCard(
    movie: MovieInfo,
    onBookClicked: (time: String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF8E1)
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF4E342E)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = movie.genre,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6D4C41)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${movie.duration} • ${movie.language} • ${movie.certificate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6D4C41)
                    )
                }

                Text(
                    text = movie.rating,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Color(0xFFFF6F00),
                    modifier = Modifier.align(Alignment.Top)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Showtimes",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF4E342E)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                movie.showTimes.forEach { time ->
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color(0xFFFFE0B2),
                        modifier = Modifier.clickable { onBookClicked(time) }
                    ) {
                        Text(
                            text = time,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF4E342E)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    val time = movie.showTimes.firstOrNull() ?: "N/A"
                    onBookClicked(time)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Book Now",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

// ----- MY BOOKINGS TAB PLACEHOLDER -----

@Composable
fun MyBookingsPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "My Bookings",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF4E342E)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your upcoming and past tickets will appear here.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5D4037),
                textAlign = TextAlign.Center
            )
        }
    }
}

// ----- PROFILE TAB PLACEHOLDER -----

@Composable
fun ProfilePlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF4E342E)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "In a real app, you can show user details, preferences, and payment methods here.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF5D4037),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFFFF8E1),
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Sample user:",
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF4E342E)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("Name: FlicksNow User")
                Text("Email: user@example.com")
                Text("Preferred City: Chennai")
            }
        }
    }
}

// ----- BOTTOM NAVIGATION BAR -----

@Composable
fun FlicksBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFF4E342E),
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Bookings") },
            label = { Text("Bookings") }
        )

        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}
