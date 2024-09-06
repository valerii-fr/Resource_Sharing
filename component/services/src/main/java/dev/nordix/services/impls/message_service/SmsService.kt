package dev.nordix.services.impls.message_service

import android.util.Log
import dev.nordix.services.NordixTcpService
import dev.nordix.services.domain.WssServerProvider
import dev.nordix.services.domain.model.ServiceContract
import dev.nordix.services.domain.model.ServiceDescriptor
import dev.nordix.services.impls.message_service.accessors.SmsRetrieveAccessor
import dev.nordix.services.impls.message_service.accessors.SmsSendAccessor
import dev.nordix.services.domain.model.actions.MessageAction
import dev.nordix.services.impls.message_service.model.MessageActionResult
import dev.nordix.settings.TerminalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class SmsService @Inject constructor(
    private val scope: CoroutineScope,
    private val messageAccessor: SmsRetrieveAccessor,
    private val smsSendAccessor: SmsSendAccessor,
    private val wssServerProvider: WssServerProvider,
    terminalRepository: TerminalRepository,
) : NordixTcpService<MessageActionResult, MessageAction<MessageActionResult>> {

    private val contract = ServiceContract(
        type = ServiceContract.ServiceType.SERVICE_PROXY,
        actions = listOfNotNull(
            MessageAction.SendMessage::class,
            MessageAction.GetMessages::class
        )
    )

    init {
        setupServer()
    }

    var serverJob: Job? = null

    override val descriptor: ServiceDescriptor = ServiceDescriptor(
        name = "Sms Service",
        owner = terminalRepository.terminal.id,
        contract = contract,
        port = 12337,
    )

    override suspend fun act(action: MessageAction<MessageActionResult>): MessageActionResult {
        Log.i(this::class.simpleName, "Received action $action")
        return action.react()
    }

    override fun setupServer() {
        serverJob = scope.launch {
            wssServerProvider.getServer(this@SmsService).start()
        }
    }

    override fun terminate() {
        serverJob?.cancel()
    }

    private inline fun <reified T: MessageAction<*>> T.react() : MessageActionResult {
        return when(val action = this) {
            is MessageAction.SendMessage -> try {
                smsSendAccessor.sendMessage(action.address, action.body)
                MessageActionResult.SendResult.Success
            } catch (e: Throwable) {
                MessageActionResult.SendResult.Failure(e.message)
            }
            is MessageAction.GetMessages -> try {
                MessageActionResult.GetResult.Success(messageAccessor.getAllSms())
            } catch (e: Throwable) {
                MessageActionResult.GetResult.Failure(cause = e.message.toString())
            }
            else -> throw IllegalArgumentException("Unknown action ${action::class.simpleName}")
        }
    }

}

