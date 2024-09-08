package dev.nordix.services.impls.message_service.accessors

import android.content.Context
import android.telephony.SmsManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SmsSendAccessor @Inject constructor(
    @ApplicationContext context: Context,
) {

    private val smsManager = context.getSystemService(SmsManager::class.java)

    fun sendMessage(
        address: String,
        body: String,
    ) {
        smsManager.sendTextMessage(
            address,
            null,
            body,
            null,
            null
        )
    }

}
