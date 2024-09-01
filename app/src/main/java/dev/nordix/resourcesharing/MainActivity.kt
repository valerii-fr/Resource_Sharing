package dev.nordix.resourcesharing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dev.nordix.publish.ServicePublisher
import dev.nordix.resourcesharing.ui.theme.ResourceSharingTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var publisher: ServicePublisher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ResourceSharingTheme {

            }
        }
    }

    override fun onDestroy() {
        publisher.removeAll()
        super.onDestroy()
    }
}