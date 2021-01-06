package uz.genesis.itsupport.service

import uz.genesis.itsupport.entity.Room

/**
 * @created 11/12/2020 - 3:36 PM
 * @project it-support
 * @author Javohir Elmurodov
 */
interface IRoomService {
    fun getByChatId(id : Long) : Room?
    fun getAllActiveRooms() : List<Room>
}