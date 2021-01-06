package uz.genesis.itsupport.telegram

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard
import uz.genesis.itsupport.config.LocaleMessageService
import uz.genesis.itsupport.entity.Appeal
import uz.genesis.itsupport.entity.Room
import uz.genesis.itsupport.enums.Emojis
import uz.genesis.itsupport.enums.Step
import uz.genesis.itsupport.service.AppealService
import uz.genesis.itsupport.service.RoomService
import uz.genesis.itsupport.service.UserService
import uz.genesis.itsupport.utils.Utils
import java.io.File


/**
 * @created 10/12/2020 - 1:53 PM
 * @project it-support
 * @author Javohir Elmurodov
 */

@Service
class MessageReplier @Autowired constructor(
    private val lms : LocaleMessageService,
    private val userService : UserService,
    private val roomService : RoomService,
    private val appealService : AppealService,
    @Lazy
    private val tgBotService : TgBotService,
    private val iBtn : InlineButtons,
    private val mBtn : MarkupButtons) {

    fun reply(update : Update, st : Step? = Step.DELETE_MESSAGE) : List<Any> {

        val message = update.message
        val user = userService.getUserByChatId(message.chatId);
        val step = st ?: user.step
        val replyToUser : MutableList<Any> = mutableListOf()

        when (step) {
            Step.START -> {
                if (!user.active)
                    replyToUser.addAll(listOf(
                        sendPhoto(message.chatId, "welcome", Emojis.BLUSH, message.from.firstName),
                        sendMessage(message.chatId, "choose", user.lang, iBtn.langButtons(user.lang))))
                else
                    replyToUser.add(sendMessage(message.chatId, "choose.menu", user.lang, mBtn.mainMenu(user.lang), Emojis.POINT_DOWN))
                user.step = Step.DELETE_MESSAGE
            }
            Step.MENU_ONE ->
                replyToUser.add(sendMessage(message.chatId, "type.appeal", user.lang, iBtn.menuOne(user.lang), Emojis.POINT_DOWN))
            Step.MENU_TWO -> {
                user.step = Step.DELETE_MESSAGE
                replyToUser.add(sendMessage(message.chatId, "type.appeal", user.lang, iBtn.menuTwo(user.lang), Emojis.POINT_DOWN))
            }
            Step.MENU_THREE -> {
                val appeal = Appeal();
                appeal.type = message.text.substringAfter(" ")
                appeal.creatorUsername = "@" + (message.from.userName ?: message.from.firstName)
                appeal.creatorChatID = message.chatId

                replyToUser.add(sendMessage(message.chatId, "appeal.description", user.lang, null))

                user.step = Step.APPEAL_ASK_DESCRIPTION
                appealService.save(appeal);
                userService.save(user)
            }
            Step.APPEAL_ASK_DESCRIPTION -> {
                replyToUser.add(sendMessage(message.chatId, "want.to.upload.file", user.lang, iBtn.addFile(user.lang)))
                val appeal = appealService.findByChatIdAndOrderByDateDesc(message.chatId)
                appeal.description = message.text;
                appealService.save(appeal)
                user.step = Step.DELETE_MESSAGE
            }
            Step.APPEAL_ASK_ATTACHMENT -> {
                val appeal = appealService.findByChatIdAndOrderByDateDesc(message.chatId)
                if (message.hasPhoto()) {
                    for (file in processPhoto(message)) {
                        replyToUser.add(sendMessage(message.chatId, "appeal.sent", user.lang, null))
                        replyToUser.addAll(appealService.photos(appeal, user.lang, file))
                        appealService.save(appeal.apply { filepath = "/api/uploads/" + file.name })
                    }
                    user.step = Step.DELETE_MESSAGE
                } else {
                    replyToUser.add(DeleteMessage(message.chatId, message.messageId))
                    user.step = Step.APPEAL_ASK_ATTACHMENT
                }
            }
            Step.CHANGE_LANGUAGE -> {
                replyToUser.add(sendMessage(message.chatId, "choose", user.lang, iBtn.langButtons(user.lang)))
                user.step = Step.DELETE_MESSAGE
            }
            Step.CREATE_ROOM -> {
                var room = roomService.getByChatId(message.chatId)
                room = room(room, message)
                roomService.save(room);
                user.step = Step.DELETE_MESSAGE
            }
            Step.DELETE_MESSAGE ->
                replyToUser.add(DeleteMessage(message.chatId, message.messageId))
        }


        if (user.id != null) userService.save(user)
        return replyToUser
    }


    private fun sendPhoto(chatId : Long, locale : String, vararg params : Any?) : SendPhoto {
        return SendPhoto().setChatId(chatId).setCaption(lms.getMessage("welcome", locale, *params)).setPhoto(Utils().getFile("1.jpg"));
    }

    private fun <T : ReplyKeyboard?> sendMessage(chatId : Long, message : String, locale : String, btn : T, vararg params : Any?) : SendMessage {
        val response = SendMessage(chatId, lms.getMessage(message, locale, *params))
        return if (btn == null) response else response.setReplyMarkup(btn)
    }

    private fun room(roomIn : Room?, message : Message) : Room {
        return if (roomIn?.chatId != null) roomIn
        else Room().apply {
            title = "@" + message.chat.title
            type = "${message.chat.isSuperGroupChat ?: "super"}group"
            chatId = message.chatId
        }
    }

    fun processPhoto(message : Message) : List<File> {
        return tgBotService.getFiles(message)
    }


}

