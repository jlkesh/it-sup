package uz.genesis.itsupport.repository

import org.springframework.data.jpa.repository.JpaRepository
import uz.genesis.itsupport.entity.Appeal
import uz.genesis.itsupport.entity.Room
import uz.genesis.itsupport.entity.User

/**
 * @created 09/12/2020 - 12:56 PM
 * @project it-support
 * @author Javohir Elmurodov
 */

interface AppealRepository : JpaRepository<Appeal?, Long?>{
    fun findFirstByCreatorChatIDOrderByIdDesc(id : Long) : Appeal?
}

interface RoomRepository : JpaRepository<Room?, Long?> {
    fun findRoomByChatId(id : Long) : Room?
    fun findAllByActive(active : Boolean) : List<Room>
}

interface UserRepository : JpaRepository<User?, Long?> {
    fun findUserByChatId(id : Long) : User?
}