package dev.nordix.services.impls.flash_service

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.nordix.core.Constants.ROOT_SERVICE_PORT
import dev.nordix.services.NordixTcpService
import dev.nordix.services.domain.model.ServiceContract
import dev.nordix.services.domain.model.ServiceDescriptor
import dev.nordix.services.domain.model.actions.FlashAction
import dev.nordix.services.domain.model.results.FlashActionResult
import dev.nordix.settings.TerminalRepository
import javax.inject.Inject

class FlashService @Inject constructor(
    @ApplicationContext private val context: Context,
    terminalRepository: TerminalRepository,
) : NordixTcpService<FlashAction<FlashActionResult>, FlashActionResult> {

    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val isFlashAvailable = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

    private val contract = ServiceContract(
        type = ServiceContract.ServiceType.DEVICE_PROXY,
        actions = listOfNotNull(
            FlashAction.SwitchOn::class,
            FlashAction.SwitchOff::class
        )
    )

    override val descriptor: ServiceDescriptor = ServiceDescriptor(
        name = "Flash Service",
        owner = terminalRepository.terminal.id,
        contract = contract,
        port = ROOT_SERVICE_PORT,
    )

    override fun terminate() { }

    override suspend fun act(action: FlashAction<FlashActionResult>): FlashActionResult {
        return when(action) {
            FlashAction.SwitchOn -> toggleFlash(true)
            FlashAction.SwitchOff -> toggleFlash(false)
        }
    }

    fun toggleFlash(enabled: Boolean) : FlashActionResult {
        if (!isFlashAvailable) return FlashActionResult.Failure("Flash not available")
        return try {
            getCameraId()?.let { cameraId ->
                cameraManager.setTorchMode(cameraId, enabled)
                when (enabled) {
                    true -> FlashActionResult.SwitchedOn
                    false -> FlashActionResult.SwitchedOff
                }
            } ?: FlashActionResult.Failure("Flash not available")
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            FlashActionResult.Failure(e.message)
        }
    }

    private fun getCameraId(): String? {
        val cameraIdList = cameraManager.cameraIdList
        for (cameraId in cameraIdList) {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            if (characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true) {
                return cameraId
            }
        }
        return null
    }

}
