package com.example.qurannative

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qurannative.design.QuranTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            QuranTheme {
                QuranHomeScreen(
                    onReadClick = {
                        // سيتم ربط شاشة المصحف هنا
                    },
                    onAudioClick = {
                        // سيتم ربط مشغل القرآن هنا
                    }
                )
            }
        }
    }
}

@Composable
fun QuranHomeScreen(
    onReadClick: () -> Unit,
    onAudioClick: () -> Unit
) {
    val background = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF102C25),
            Color(0xFF071A16),
            Color(0xFF06110F)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "﷽",
                fontSize = 48.sp,
                color = Color(0xFFD9B66F),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "القرآن الكريم",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "نورٌ لقلبك وطمأنينةٌ لروحك",
                fontSize = 16.sp,
                color = Color(0xFFD5DCCF)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onReadClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD9B66F),
                    contentColor = Color(0xFF102C25)
                )
            ) {
                Text(
                    text = "📖  قراءة القرآن الكريم",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onAudioClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF244A3D),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "🎧  الاستماع للقرآن",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(35.dp))

            Text(
                text = "﴿ وَاذْكُر رَّبَّكَ إِذَا نَسِيتَ ﴾",
                fontSize = 18.sp,
                color = Color(0xFFD9B66F),
                textAlign = TextAlign.Center
            )
        }
    }
}
