package uz.genesis.itsupport.telegram

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import uz.genesis.itsupport.config.LocaleMessageService
import uz.genesis.itsupport.enums.Emojis
import java.util.*

/**
 * @created 10/12/2020 - 4:11 PM
 * @project it-support
 * @author Javohir Elmurodov
 */
@Component
class InlineButtons @Autowired constructor(
    private val lms : LocaleMessageService) {

    fun langButtons(locale : String) : InlineKeyboardMarkup? {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        val ru = InlineKeyboardButton(lms.getMessage("ru.with.logo", locale, Emojis.RU))
        val uz = InlineKeyboardButton(lms.getMessage("uz.with.logo", locale, Emojis.UZ))
        ru.callbackData = "ru"
        uz.callbackData = "uz"
        val kbr1 : MutableList<InlineKeyboardButton> = ArrayList()
        val kbr2 : MutableList<InlineKeyboardButton> = ArrayList()
        kbr1.add(uz)
        kbr2.add(ru)
        val rowList : MutableList<List<InlineKeyboardButton>> = ArrayList()
        rowList.add(kbr1)
        rowList.add(kbr2)
        inlineKeyboardMarkup.keyboard = rowList
        return inlineKeyboardMarkup
    }

    fun menuOne(locale : String) : InlineKeyboardMarkup? {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()

        val child1 = InlineKeyboardButton(lms.getMessage("main.menu1.child1", locale))
        val child2 = InlineKeyboardButton(lms.getMessage("main.menu1.child2", locale))
        val child3 = InlineKeyboardButton(lms.getMessage("main.menu1.child3", locale))
        val child4 = InlineKeyboardButton(lms.getMessage("main.menu1.child4", locale))
        val child5 = InlineKeyboardButton(lms.getMessage("main.menu1.child5", locale))

        child1.callbackData = "7151263A9_main.menu1.child1"
        child2.callbackData = "7151263A9_main.menu1.child2"
        child3.callbackData = "7151263A9_main.menu1.child3"
        child4.callbackData = "7151263A9_main.menu1.child4"
        child5.callbackData = "7151263A9_main.menu1.child5"

        inlineKeyboardMarkup.keyboard =
            listOf(listOf(child1), listOf(child2), listOf(child3), listOf(child4), listOf(child5))

        return inlineKeyboardMarkup
    }

    fun menuTwo(locale : String) : InlineKeyboardMarkup? {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()

        val child1 = InlineKeyboardButton(lms.getMessage("main.menu2.child1", locale))
        val child2 = InlineKeyboardButton(lms.getMessage("main.menu2.child2", locale))

        child1.callbackData = "7151263A9_main.menu2.child1";
        child2.callbackData = "7151263A9_main.menu2.child2";

        inlineKeyboardMarkup.keyboard = listOf(listOf(child1), listOf(child2))
        return inlineKeyboardMarkup
    }

    fun replied(lang : String, id : Long) : InlineKeyboardMarkup? {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()

        val replied = InlineKeyboardButton(lms.getMessage("appeal.replied", lang, Emojis.WHITE_CHECK_MARK))

        replied.callbackData = "reply_$id"

        inlineKeyboardMarkup.keyboard = listOf(listOf(replied))
        return inlineKeyboardMarkup
    }

    fun addFile(lang : String) : InlineKeyboardMarkup? {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()

        val yes = InlineKeyboardButton(lms.getMessage("yes", lang, Emojis.WHITE_CHECK_MARK))
        val no = InlineKeyboardButton(lms.getMessage("no", lang, Emojis.RED_CIRCLE))

        yes.callbackData = "addFile"
        no.callbackData = "noFile"

        inlineKeyboardMarkup.keyboard = listOf(listOf(yes, no))
        return inlineKeyboardMarkup
    }
}

@Component
class MarkupButtons @Autowired constructor(
    private val lms : LocaleMessageService) {

    fun mainMenu(locale : String) : ReplyKeyboardMarkup? {
        val replyKeyboardMarkup = ReplyKeyboardMarkup()

        replyKeyboardMarkup.selective = true
        replyKeyboardMarkup.resizeKeyboard = true
        replyKeyboardMarkup.oneTimeKeyboard = true

        val btn1 = KeyboardButton(lms.getMessage("main.menu1", locale, Emojis.JOYSTICK))
        val btn2 = KeyboardButton(lms.getMessage("main.menu2", locale, Emojis.GLOBE_WITH_MERIDIANS))
        val btn3 = KeyboardButton(lms.getMessage("main.menu3", locale, Emojis.COMPUTER))
        val btn4 = KeyboardButton("${Emojis.GLOBE_WITH_MERIDIANS} " + lms.getMessage("choose", locale))
        val keyboard : MutableList<KeyboardRow> = ArrayList()

        var row1 = KeyboardRow()
        row1.add(btn1)
        keyboard.add(row1)

        row1 = KeyboardRow()
        row1.add(btn2)
        keyboard.add(row1)

        row1 = KeyboardRow()
        row1.add(btn3)
        keyboard.add(row1)

        row1 = KeyboardRow()
        row1.add(btn4)
        keyboard.add(row1)

        replyKeyboardMarkup.keyboard = keyboard

        return replyKeyboardMarkup
    }

}