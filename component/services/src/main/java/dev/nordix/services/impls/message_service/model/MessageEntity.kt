package dev.nordix.services.impls.message_service.model

import android.provider.Telephony.TextBasedSmsColumns.MESSAGE_TYPE_INBOX
import android.provider.Telephony.TextBasedSmsColumns.MESSAGE_TYPE_OUTBOX
import android.provider.Telephony.TextBasedSmsColumns.MESSAGE_TYPE_SENT
import java.io.Serializable
import java.time.Instant

data class MessageEntity(
    val body: String,
    val address: String,
    val date: Instant,
    val type: MessageType,
) : Serializable {

    enum class MessageType(val typeIndex: Int) {
        SENT(MESSAGE_TYPE_SENT),
        INBOX(MESSAGE_TYPE_INBOX),
        OUTBOX(MESSAGE_TYPE_OUTBOX),
        ;
        companion object {
            fun getByTypeIndex(typeIndex: Int) : MessageType {
                return when (typeIndex) {
                    MessageEntity.MessageType.SENT.typeIndex -> MessageEntity.MessageType.SENT
                    MessageEntity.MessageType.INBOX.typeIndex -> MessageEntity.MessageType.INBOX
                    MessageEntity.MessageType.OUTBOX.typeIndex -> MessageEntity.MessageType.OUTBOX
                    else -> MessageEntity.MessageType.SENT
                }
            }
        }
    }

}
