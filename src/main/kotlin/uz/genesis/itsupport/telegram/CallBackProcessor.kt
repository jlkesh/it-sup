package uz.genesis.itsupport.telegram

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard
import uz.genesis.itsupport.config.LocaleMessageService
import uz.genesis.itsupport.entity.Appeal
import uz.genesis.itsupport.entity.User
import uz.genesis.itsupport.enums.Emojis
import uz.genesis.itsupport.enums.Step
import uz.genesis.itsupport.service.AppealService
import uz.genesis.itsupport.service.UserService

/**
 * @created 11/12/2020 - 3:38 PM
 * @project it-support
 * @author Javohir Elmurodov
 */
@Service
class CallBackProcessor @Autowired constructor(
    private val userService : UserService,
    private val appealService : AppealService,
    private val lms : LocaleMessageService,
    @Lazy
    private val tgBotService : TgBotService,
    private val mBtn : MarkupButtons) {


    fun process(update : Update) : List<Any> {
        val callbackQuery = update.callbackQuery;
        val from = update.callbackQuery.from
        val message = callbackQuery.message
        val response : MutableList<Any> =
            mutableListOf(DeleteMessage().setChatId(message.chatId).setMessageId(message.messageId))
        val data = callbackQuery.data
        var user = User()
        if (message.chat.isUserChat) {
            user = userService.getUserByChatId(message.chatId)
        }

        when (data.substringBefore("_")) {

            "ru", "uz" -> {
                user.lang = data
                user.active = true
                user.chatId = message.chatId
                user.username = "@" + (from.userName ?: from.firstName)
                user.step = Step.DELETE_MESSAGE
                response.add(sendMessage(message.chatId, "choose.menu", data, mBtn.mainMenu(data), Emojis.POINT_DOWN))
                userService.save(user)
            }

            "7151263A9" -> {
                val appeal = Appeal();
                appeal.type = lms.getMessage(data.substringAfter("_"), user.lang)
                appeal.creatorUsername = "@" + (from.userName ?: from.firstName)
                appeal.creatorChatID = message.chatId
                appealService.save(appeal);
                user.step = Step.APPEAL_ASK_DESCRIPTION
                response.add(sendMessage(message.chatId, "appeal.description", user.lang, null))
                userService.save(user)
            }

            "reply" -> {
                val appealId = data.substringAfter("_").toLong()
                val tempAppeal : Appeal? = appealService.get(appealId)
                if (tempAppeal != null) {
                    tempAppeal.replied = true
                    tempAppeal.repliedBy = "@" + (from.userName ?: from.firstName)
                    val appeal = appealService.save(tempAppeal)
                    response.add(editMessage(message, user.lang, appeal))
                }

            }

            "addFile" -> {
                response.add(sendMessage(message.chatId, "upload.file", user.lang, null))
                user.step = Step.APPEAL_ASK_ATTACHMENT
                userService.save(user)
            }

            "noFile" -> {
                user.step = Step.DELETE_MESSAGE
                response.add(sendMessage(message.chatId, "appeal.sent", user.lang, null))
                response.addAll(appealService.messages(appealService.findByChatIdAndOrderByDateDesc(message.chatId), user.lang))
                userService.save(user)
            }

        }

        return response
    }

    private fun <T : ReplyKeyboard?> sendMessage(chatId : Long, message : String, locale : String, btn : T?, vararg params : Any?) : SendMessage {
        val response = SendMessage(chatId, lms.getMessage(message, locale, *params))
        return if (btn == null) response else response.setReplyMarkup(btn)
    }

    private fun editMessage(message : Message, lang : String, appeal : Appeal?) : Any {
        val files = tgBotService.getFiles(message)
        if (files.isNotEmpty()) {
            val res = SendPhoto();
            res.chatId = "${message.chatId}"
            res.caption = appealService.caption(appeal!!, lang)
            res.setPhoto(files[0])
            res.replyMarkup = message.replyMarkup
            res.parseMode = "HTML"
            return res;
        }
        val sendMessage = SendMessage()
        sendMessage.chatId = "${message.chatId}"
        sendMessage.text = appealService.caption(appeal!!, lang)
        sendMessage.replyMarkup = message.replyMarkup
        sendMessage.setParseMode("HTML")
        sendMessage.enableHtml(true)
        return sendMessage
    }


}