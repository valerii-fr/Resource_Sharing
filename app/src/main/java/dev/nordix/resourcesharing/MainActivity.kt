package dev.nordix.resourcesharing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dev.nordix.discovery.domain.DiscoveryService
import dev.nordix.homescreen.composable.HomeScreen
import dev.nordix.publish.ServicePublisher
import dev.nordix.resourcesharing.ui.theme.ResourceSharingTheme
import dev.nordix.services.NordixTcpService
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var publisher: ServicePublisher

    @Inject
    lateinit var discoveryService: DiscoveryService

    @Inject
    lateinit var services: Set<@JvmSuppressWildcards NordixTcpService<*,*>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ResourceSharingTheme {
                HomeScreen()
            }
        }
    }

    override fun onDestroy() {
        publisher.removeAll()
        discoveryService.stopDiscovery()
        services.forEach(NordixTcpService<*,*>::terminate)
        super.onDestroy()
    }
}