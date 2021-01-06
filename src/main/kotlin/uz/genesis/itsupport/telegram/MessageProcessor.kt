package uz.genesis.itsupport.telegram

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Update
import uz.genesis.itsupport.config.Cache
import uz.genesis.itsupport.enums.Step

/**
 * @created 11/12/2020 - 3:39 PM
 * @project it-support
 * @author Javohir Elmurodov
 */

@Service
class MessageProcessor @Autowired constructor(
    private val messageReplier : MessageReplier,
    private val cache : Cache) {

    fun process(update : Update) : List<Any> {
        var step : Step? = null
        if (update.message.hasText()) {
            step = cache.getStep(update.message.text)
        }
        val chat = update.message.chat
        when {
            chat.isSuperGroupChat || chat.isGroupChat -> step = Step.CREATE_ROOM
            chat.isChannelChat -> step = Step.DELETE_MESSAGE
        }
        return messageReplier.reply(update, step)
    }

}