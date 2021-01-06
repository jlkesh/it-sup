package uz.genesis.itsupport.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import uz.genesis.itsupport.config.LocaleMessageService
import uz.genesis.itsupport.entity.Appeal
import uz.genesis.itsupport.enums.Emojis
import uz.genesis.itsupport.repository.AppealRepository
import uz.genesis.itsupport.service.base.BaseService
import uz.genesis.itsupport.telegram.InlineButtons
import java.io.File

/**
 * @created 10/12/2020 - 12:36 PM
 * @project it-support
 * @author Javohir Elmurodov
 */

@Service
class AppealService @Autowired constructor(
    private val lms : LocaleMessageService,
    private val roomService : RoomService,
    private val iBtn : InlineButtons,
    private val appealsRepository : AppealRepository) : BaseService<Appeal, AppealRepository>(repository = appealsRepository), IAppealService {
    override fun caption(appeal : Appeal, locale : String) : String {
        val builder = StringBuilder()
        builder.append("<b>" + lms.getMessage("type", locale) + "</b> : " + appeal.type + "\n")
        builder.append("<b>" + lms.getMessage("appeal", locale) + "</b> : " + appeal.description + "\n")
        builder.append("<b>" + lms.getMessage("sender", locale) + "</b> : " + appeal.creatorUsername + "\n")
        builder.append("<b>" + lms.getMessage("status", locale) + "</b> : " + appeal.replied + "\n")
        builder.append("<b>" + lms.getMessage("created.date", locale) + "</b> : " + appeal.date + "\n")
        if (!appeal.repliedBy.isEmpty()) {
            builder.append(lms.getMessage("accepted.by", locale, Emojis.MAN_TECHNOLOGIST, appeal.repliedBy) + "\n")
        }
        return String(stringBuilder = builder)
    }

    override fun messages(appeal : Appeal, locale : String) : List<SendMessage> {
        val rooms = roomService.getAllActiveRooms()
        val rpl = mutableListOf<SendMessage>()
        for (room in rooms) {
            val message = SendMessage()
            message.text = caption(appeal, locale)
            message.chatId = "${room.chatId}"
            message.replyMarkup = iBtn.replied(locale, appeal.id!!)
            message.enableHtml(true).setParseMode("HTML")
            rpl.add(message)
        }
        return rpl
    }

    override fun photos(appeal : Appeal, locale : String, file : File) : List<SendPhoto> {
        val rooms = roomService.getAllActiveRooms()
        val rpl = mutableListOf<SendPhoto>()
        for (room in rooms) {
            val message = SendPhoto()
            message.caption = caption(appeal, locale)
            message.chatId = "${room.chatId}"
            message.replyMarkup = iBtn.replied(locale, appeal.id!!)
            message.parseMode = "HTML"
            message.setPhoto(file)
            rpl.add(message)
        }
        return rpl
    }

    override fun findByChatIdAndOrderByDateDesc(id : Long) : Appeal {
        return appealsRepository.findFirstByCreatorChatIDOrderByIdDesc(id) ?: Appeal()
    }
}