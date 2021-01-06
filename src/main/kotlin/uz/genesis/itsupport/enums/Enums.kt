package uz.genesis.itsupport.enums

import com.vdurmont.emoji.EmojiParser
import lombok.AllArgsConstructor

/**
 * @created 09/12/2020 - 4:19 PM
 * @project it-support
 * @author Javohir Elmurodov
 */
@AllArgsConstructor
enum class Emojis(private val code : String) {
    METRO(EmojiParser.parseToUnicode(":metro:")),
    BUS(EmojiParser.parseToUnicode(":bus:")),
    DOLLAR(EmojiParser.parseToUnicode(":dollar:")),
    PHONE_RECEIVER(EmojiParser.parseToUnicode(":telephone_receiver:")),
    CRY(EmojiParser.parseToUnicode(":cry:")),
    BLUSH(EmojiParser.parseToUnicode(":blush:")),
    PLUS(EmojiParser.parseToUnicode(":heavy_plus_sign:")),
    MINUS(EmojiParser.parseToUnicode(":heavy_minus_sign:")),
    X_NO(EmojiParser.parseToUnicode(":x:")),
    CHECK_BOX(EmojiParser.parseToUnicode(":ballot_box_with_check:")),
    WHITE_CHECK_MARK(EmojiParser.parseToUnicode(":white_check_mark:")),
    RED_CIRCLE(EmojiParser.parseToUnicode(":red_circle:")),
    BLUE_CIRCLE(EmojiParser.parseToUnicode(":blue_circle:")),
    WAVY_DASH(EmojiParser.parseToUnicode(":wavy_dash:")),
    SMILE(EmojiParser.parseToUnicode(":smile:")),
    UP(EmojiParser.parseToUnicode(":arrow_up:")),
    DOWN(EmojiParser.parseToUnicode(":arrow_down:")),
    PRINTER(EmojiParser.parseToUnicode(":printer:")),
    GLOBE_WITH_MERIDIANS(EmojiParser.parseToUnicode(":globe_with_meridians:")),
    COMPUTER(EmojiParser.parseToUnicode(":computer:")),
    JOYSTICK(EmojiParser.parseToUnicode(":joystick:")),
    TELEPHONE(EmojiParser.parseToUnicode(":telephone:")),
    REGISTER(EmojiParser.parseToUnicode(":writing_hand:")),
    WRENCH(EmojiParser.parseToUnicode(":wrench:")),
    FILE_CABINET(EmojiParser.parseToUnicode(":file_cabinet:")),
    SPIRAL_NOTE_PAD(EmojiParser.parseToUnicode(":spiral_note_pad:")),
    ARROW_FORWARD(EmojiParser.parseToUnicode(":arrow_forward:")),
    ARROW_BACKWARD(EmojiParser.parseToUnicode(":arrow_backward:")),
    ARROWS_COUNTERCLOCKWISE(EmojiParser.parseToUnicode(":arrows_counterclockwise:")),
    GB(EmojiParser.parseToUnicode(":gb:")),
    RU(EmojiParser.parseToUnicode(":ru:")),
    UZ(EmojiParser.parseToUnicode(":uz:")),
    MAN(EmojiParser.parseToUnicode(":man:")),
    WOMAN(EmojiParser.parseToUnicode(":woman:")),
    GIFT(EmojiParser.parseToUnicode(":gift:")),
    PARTY(EmojiParser.parseToUnicode(":party:")),
    BIRTHDAY(EmojiParser.parseToUnicode(":birthday:")),
    NOTEBOOK(EmojiParser.parseToUnicode(":notebook:")),
    LARGE_ORANGE_DIAMOND(EmojiParser.parseToUnicode(":large_orange_diamond:")),
    ID(EmojiParser.parseToUnicode(":id:")),
    BACK(EmojiParser.parseToUnicode(":back:")),
    MAN_NO(EmojiParser.parseToUnicode(":man_gesturing_no:")),
    MAN_TECHNOLOGIST(EmojiParser.parseToUnicode(":man_technologist:")),
    WASTEBASKET(EmojiParser.parseToUnicode(":wastebasket:")),
    POINT_DOWN(EmojiParser.parseToUnicode(":point_down:")),
    MEMO(EmojiParser.parseToUnicode(":memo:"));

    override fun toString() : String {
        return code
    }
}

enum class Step(private val code : String) {
    START("START"),
    DELETE_MESSAGE("DELETE_MESSAGE"),
    DO_NOTHING("DO_NOTHING"),

    MENU_ONE("MENU_ONE"),
    MENU_ONE_CHILD_ONE("MENU_ONE_CHILD_ONE"),
    MENU_ONE_CHILD_TWO("MENU_ONE_CHILD_TWO"),
    MENU_ONE_CHILD_THREE("MENU_ONE_CHILD_THREE"),
    MENU_ONE_CHILD_FOUR("MENU_ONE_CHILD_FOUR"),
    MENU_ONE_CHILD_FIVE("MENU_ONE_CHILD_FIVE"),

    MENU_TWO("MENU_TWO"),
    MENU_TWO_CHILD_ONE("MENU_TWO_CHILD_ONE"),
    MENU_TWO_CHILD_TWO("MENU_TWO_CHILD_TWO"),

    MENU_THREE("MENU_THREE"),

    APPEAL("APPEAL"),
    APPEAL_ASK_DESCRIPTION("APPEAL_ASK_DESCRIPTION"),
    APPEAL_DESCRIPTION_ENTERED("APPEAL_DESCRIPTION_ENTERED"),
    APPEAL_ASK_ATTACHMENT("APPEAL_ASK_ATTACHMENT"),
    APPEAL_ATTACHMENT_ENTERED("APPEAL_ATTACHMENT_ENTERED"),

    SETTINGS("SETTINGS"),
    CHANGE_LANGUAGE("CHANGE_LANGUAGE"),

    CREATE_ROOM("CREATE_ROOM");


    companion object {
        fun getValue(step : Step) : String {
            return "$step";
        }

        fun getStep(step : String) : Step {
            for (st in values()) {
                if ("$st" == step) {
                    return st
                }
            }
            return DELETE_MESSAGE
        }
    }

}

