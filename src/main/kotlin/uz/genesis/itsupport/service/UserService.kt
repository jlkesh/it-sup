package uz.genesis.itsupport.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uz.genesis.itsupport.entity.User
import uz.genesis.itsupport.repository.UserRepository
import uz.genesis.itsupport.service.base.BaseService


/**
 * @created 10/12/2020 - 12:25 PM
 * @project it-support
 * @author Javohir Elmurodov
 */

@Service
class UserService @Autowired constructor(private val userRepository: UserRepository?) : BaseService<User, UserRepository>(repository = userRepository), IUserService {
    override fun getUserByChatId(id: Long): User {
        return userRepository?.findUserByChatId(id) ?: User()
    }
}