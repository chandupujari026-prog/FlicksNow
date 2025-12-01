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

data class BookingHistoryItem(
    val movieTitle: String,
    val cinema: String,
    val date: String,
    val time: String,
    val seats: String,
    val bookingId: String,
    val status: String // Upcoming, Completed, Cancelled
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

// booking history sample
private val sampleBookings = listOf(
    BookingHistoryItem(
        movieTitle = "The Last Stand",
        cinema = "PVR Phoenix MarketCity",
        date = "Mon, 03 Feb 2025",
        time = "07:30 PM",
        seats = "A5, A6",
        bookingId = "FN-2025-001",
        status = "Upcoming"
    ),
    BookingHistoryItem(
        movieTitle = "Love in Paris",
        cinema = "INOX City Centre",
        date = "Sun, 02 Feb 2025",
        time = "04:15 PM",
        seats = "C10, C11",
        bookingId = "FN-2025-000",
        status = "Completed"
    ),
    BookingHistoryItem(
        movieTitle = "Galaxy Quest",
        cinema = "SPI Sathyam",
        date = "Fri, 31 Jan 2025",
        time = "09:45 PM",
        seats = "B2, B3, B4",
        bookingId = "FN-2025-998",
        status = "Completed"
    ),
    BookingHistoryItem(
        movieTitle = "Comedy Night",
        cinema = "PVR VR Mall",
        date = "Thu, 30 Jan 2025",
        time = "06:00 PM",
        seats = "D7, D8",
        bookingId = "FN-2025-997",
        status = "Cancelled"
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

                1 -> MyBookingsScreen(bookings = sampleBookings)
                2 -> ProfileScreen()
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

// ----- MY BOOKINGS TAB (HISTORY) -----

@Composable
fun MyBookingsScreen(bookings: List<BookingHistoryItem>) {
    val upcoming = bookings.filter { it.status == "Upcoming" }
    val others = bookings.filter { it.status != "Upcoming" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "My Bookings",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF4E342E)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "View your upcoming shows and booking history.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF5D4037)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (upcoming.isNotEmpty()) {
            Text(
                text = "Upcoming",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF4E342E)
            )
            Spacer(modifier = Modifier.height(8.dp))
            upcoming.forEach { booking ->
                BookingCard(booking)
                Spacer(modifier = Modifier.height(10.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = "History",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color(0xFF4E342E)
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (others.isEmpty()) {
            Text(
                text = "You have no past bookings yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5D4037)
            )
        } else {
            others.forEach { booking ->
                BookingCard(booking)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun BookingCard(booking: BookingHistoryItem) {
    val statusColor = when (booking.status) {
        "Upcoming" -> Color(0xFF2E7D32)
        "Completed" -> Color(0xFF1565C0)
        "Cancelled" -> Color(0xFFC62828)
        else -> Color(0xFF5D4037)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF8E1)
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = booking.movieTitle,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF4E342E)
                    )
                    Text(
                        text = booking.cinema,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6D4C41)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(50),
                    color = statusColor
                ) {
                    Text(
                        text = booking.status,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${booking.date} • ${booking.time}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF5D4037)
            )
            Text(
                text = "Seats: ${booking.seats}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF5D4037)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Booking ID: ${booking.bookingId}",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF8D6E63)
            )
        }
    }
}

// ----- PROFILE TAB -----

@Composable
fun ProfileScreen() {
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

        Spacer(modifier = Modifier.height(16.dp))

        // Basic user info
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFFFF8E1),
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Account",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Color(0xFF4E342E)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Name: FlicksNow User")
                Text("Email: user@example.com")
                Text("Phone: +91 98765 43210")
                Text("Preferred City: Chennai")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Membership info
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFFFE0B2),
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Membership",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Color(0xFF4E342E)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tier: Gold Member")
                Text("Points: 2,450")
                Text("Benefits: No booking fee, priority support, birthday offers")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Preferences
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFFFF8E1),
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Preferences",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Color(0xFF4E342E)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Favourite Genres:")
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PreferenceChip("Action")
                    PreferenceChip("Sci-Fi")
                    PreferenceChip("Romance")
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Preferred Cinemas:")
                Spacer(modifier = Modifier.height(6.dp))
                Column {
                    Text("• PVR Phoenix MarketCity")
                    Text("• SPI Sathyam")
                    Text("• INOX City Centre")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Payment info (sample)
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFFFE0B2),
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Saved Payment",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Color(0xFF4E342E)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Primary Card: **** 4821 (Visa)")
                Text("UPI: flicksnow@upi")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun PreferenceChip(label: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color(0xFFFFE0B2)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF4E342E)
        )
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
