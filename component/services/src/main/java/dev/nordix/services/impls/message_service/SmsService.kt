package dev.nordix.services.impls.message_service

import dev.nordix.services.NordixTcpService
import dev.nordix.services.domain.model.ServiceContract
import dev.nordix.services.domain.model.ServiceDescriptor
import dev.nordix.services.impls.message_service.accessors.SmsRetrieveAccessor
import dev.nordix.services.impls.message_service.accessors.SmsSendAccessor
import dev.nordix.services.domain.model.actions.MessageAction
import dev.nordix.services.domain.model.results.MessageActionResult
import dev.nordix.settings.TerminalRepository
import kotlinx.coroutines.Job
import javax.inject.Inject

class SmsService @Inject constructor(
    private val messageAccessor: SmsRetrieveAccessor,
    private val smsSendAccessor: SmsSendAccessor,
    terminalRepository: TerminalRepository,
) : NordixTcpService<MessageAction<MessageActionResult>, MessageActionResult> {

    private val contract = ServiceContract(
        type = ServiceContract.ServiceType.SERVICE_PROXY,
        actions = listOfNotNull(
            MessageAction.SendMessage::class,
            MessageAction.GetMessages::class
        )
    )

    var serverJob: Job? = null

    override val descriptor: ServiceDescriptor = ServiceDescriptor(
        name = "Sms Service",
        owner = terminalRepository.terminal.id,
        contract = contract,
        port = 12337,
    )

    override suspend fun act(action: MessageAction<MessageActionResult>): MessageActionResult {
        return action.act()
    }

    override fun terminate() {
        serverJob?.cancel()
    }

    private inline fun <reified T: MessageAction<*>> T.act() : MessageActionResult {
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

