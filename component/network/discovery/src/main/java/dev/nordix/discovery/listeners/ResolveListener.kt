package dev.nordix.discovery.listeners

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import dev.nordix.client_provider.domain.WssClientProvider
import dev.nordix.client_provider.domain.model.ClientTarget
import dev.nordix.service_manager.domain.model.mapper.toServiceInfo
import dev.nordix.service_manager.holder.NsdServicesStateProvider
import dev.nordix.settings.TerminalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class ResolveListener(
    private val nsdServicesStateProvider: NsdServicesStateProvider,
    private val wssClientProvider: WssClientProvider,
    private val scope: CoroutineScope,
    private val terminalRepository: TerminalRepository,
    private val onFinished : () -> Unit,
) : NsdManager.ResolveListener {

    override fun onResolveFailed(
        serviceInfo: NsdServiceInfo?,
        errorCode: Int
    ) {
        nsdServicesStateProvider.onResolveFailed(serviceInfo, errorCode)
        onFinished()
    }

    override fun onServiceResolved(serviceInfo: NsdServiceInfo?) {
        nsdServicesStateProvider.onServiceResolved(serviceInfo)
        scope.launch {
            val target = ClientTarget(
                host = serviceInfo?.host?.hostAddress ?: "",
            )
            if (serviceInfo?.toServiceInfo()?.deviceId != terminalRepository.terminal.id.value) {
                Log.i("ResolveListener", "onServiceResolved: launching client for $target")
                wssClientProvider.launchClient(
                    ClientTarget(
                        host = serviceInfo?.host?.hostAddress ?: "",
                        port = serviceInfo?.port ?: 0,
                    )
                )
            }
        }
        onFinished()
    }

}
