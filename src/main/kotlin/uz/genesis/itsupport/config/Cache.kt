package uz.genesis.itsupport.config

import org.springframework.stereotype.Component
import uz.genesis.itsupport.entity.Appeal
import uz.genesis.itsupport.enums.Emojis
import uz.genesis.itsupport.enums.Step

/**
 * @created 14/12/2020 - 10:08 AM
 * @project it-support
 * @author Javohir Elmurodov
 */
@Component
class Cache {
    private final val steps : HashMap<String, Step> = HashMap()
    private final val appeals : HashMap<Long, Appeal> = HashMap()

    init {
        steps["/start"] = Step.START

        steps["${Emojis.JOYSTICK} Неполадки в работе АРМ сотрудников"] = Step.MENU_ONE
        steps["${Emojis.JOYSTICK} Xodimlarning ish joyidagi nosozliklar"] = Step.MENU_ONE

        steps["${Emojis.GLOBE_WITH_MERIDIANS} Доступ к базам и серверам"] = Step.MENU_TWO
        steps["${Emojis.GLOBE_WITH_MERIDIANS} Baza hamda serverlarga dostup"] = Step.MENU_TWO

        steps["${Emojis.COMPUTER} Ulanishda xatolik"] = Step.MENU_THREE
        steps["${Emojis.COMPUTER} Ошибка при подключении"] = Step.MENU_THREE

        steps["${Emojis.WRENCH} Sozlanmalar"] = Step.SETTINGS
        steps["${Emojis.WRENCH} Настройки"] = Step.SETTINGS

        steps["${Emojis.GLOBE_WITH_MERIDIANS} Tilni o`zgartirish"] = Step.CHANGE_LANGUAGE
        steps["${Emojis.GLOBE_WITH_MERIDIANS} Изменить язык"] = Step.CHANGE_LANGUAGE
    }

    fun getStep(key : String) : Step? {
        return steps[key];
    }

    fun getAppeal(chatId : Long) : Appeal {
        return appeals[chatId] ?: Appeal();
    }

    fun saveAppeal(chatId : Long, appeal : Appeal) {
        appeals[chatId] = appeal
    }

    fun removeAppeal(chatId : Long) {
        appeals.remove(chatId)
    }
}