package dev.nordix.services.impls.message_service.model

import android.provider.Telephony.TextBasedSmsColumns.MESSAGE_TYPE_INBOX
import android.provider.Telephony.TextBasedSmsColumns.MESSAGE_TYPE_OUTBOX
import android.provider.Telephony.TextBasedSmsColumns.MESSAGE_TYPE_SENT
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class MessageEntity(
    val body: String,
    val address: String,
    @Contextual val date: Instant,
    val type: MessageType,
) {

    enum class MessageType(val typeIndex: Int) {
        SENT(MESSAGE_TYPE_SENT),
        INBOX(MESSAGE_TYPE_INBOX),
        OUTBOX(MESSAGE_TYPE_OUTBOX),
        ;
        companion object {
            fun getByTypeIndex(typeIndex: Int) : MessageType {
                return when (typeIndex) {
                    SENT.typeIndex -> SENT
                    INBOX.typeIndex -> INBOX
                    OUTBOX.typeIndex -> OUTBOX
                    else -> SENT
                }
            }
        }
    }

}
