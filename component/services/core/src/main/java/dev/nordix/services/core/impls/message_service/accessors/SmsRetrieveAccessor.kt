package dev.nordix.services.impls.message_service.accessors

import android.content.Context
import android.provider.Telephony
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.nordix.services.impls.message_service.model.MessageEntity
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SmsRetrieveAccessor @Inject constructor(
    @ApplicationContext context: Context
) {
    private val contentResolver = context.contentResolver

    fun getAllSms() : List<MessageEntity> {

        val smsList = mutableListOf<MessageEntity>()

        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            null,
            null,
            null,
            "${Telephony.Sms.DATE} DESC"
        )

        cursor?.use {
            val bodyIndex = it.getColumnIndex(Telephony.Sms.BODY)
            val addressIndex = it.getColumnIndex(Telephony.Sms.ADDRESS)
            val dateIndex = it.getColumnIndex(Telephony.Sms.DATE)
            val typeIndex = it.getColumnIndex(Telephony.Sms.TYPE)

            while (it.moveToNext()) {
                val body = it.getString(bodyIndex)
                val address = it.getString(addressIndex)
                val date = it.getLong(dateIndex)
                val formattedDate = Instant.ofEpochMilli(date)
                val type = it.getInt(typeIndex)
                smsList.add(
                    MessageEntity(
                        address = address,
                        body = body,
                        date = formattedDate,
                        type = MessageEntity.MessageType.getByTypeIndex(type)
                    )
                )
            }
        }
        Log.i("${this::class.simpleName}", "got messages \n${smsList.joinToString("\n")}")
        return smsList
    }

}

