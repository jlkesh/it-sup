package uz.genesis.itsupport.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uz.genesis.itsupport.entity.Room
import uz.genesis.itsupport.repository.RoomRepository
import uz.genesis.itsupport.service.base.BaseService

/**
 * @created 10/12/2020 - 12:10 PM
 * @project it-support
 * @author Javohir Elmurodov
 */

@Service
class RoomService @Autowired constructor(private val roomRepository : RoomRepository?) : BaseService<Room, RoomRepository>(repository = roomRepository), IRoomService {
    override fun getByChatId(id : Long) : Room? {
        return roomRepository?.findRoomByChatId(id)
    }

    override fun getAllActiveRooms() : List<Room> {
        val rooms = roomRepository?.findAllByActive(true)
        return rooms!!
    }
}