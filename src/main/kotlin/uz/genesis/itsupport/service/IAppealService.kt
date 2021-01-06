package uz.genesis.itsupport.service

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import uz.genesis.itsupport.entity.Appeal
import java.io.File

/**
 * @created 11/12/2020 - 3:37 PM
 * @project it-support
 * @author Javohir Elmurodov
 */
interface IAppealService {
    fun caption(appeal : Appeal, locale : String) : String
    fun messages(appeal : Appeal, locale : String) : List<SendMessage>
    fun photos(appeal : Appeal, locale : String,file:File) : List<SendPhoto>
    fun findByChatIdAndOrderByDateDesc(id : Long) : Appeal
}
