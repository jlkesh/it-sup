package uz.genesis.itsupport.entity

import uz.genesis.itsupport.enums.Step
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * @created 09/12/2020 - 12:51 PM
 * @project it-support
 * @author Javohir Elmurodov
 */

@Entity
class Appeal {
    @Id
    @GeneratedValue
    var id : Long? = null
    var type : String? = null
    @Column(length = 600)
    var description : String? = null
    var creatorUsername : String? = null
    var replied : Boolean = false
    var repliedBy : String = ""
    var creatorChatID : Long? = null
    var filepath : String? = null
    var date : LocalDateTime? = LocalDateTime.now()
}

@Entity
class Room {
    @Id
    @GeneratedValue
    val id : Long? = null
    var type : String? = null
    var title : String? = null

    @Column(unique = true)
    var chatId : Long? = null
    var active : Boolean = false
    var date : LocalDateTime? = LocalDateTime.now()
}

@Entity
class User {
    @Id
    @GeneratedValue
    var id : Long? = null
    var lang : String = "ru"
    var step : Step = Step.DELETE_MESSAGE

    @Column(unique = true)
    var chatId : Long? = null
    var username : String? = null
    var active : Boolean = false
    var date : LocalDateTime? = LocalDateTime.now()
}