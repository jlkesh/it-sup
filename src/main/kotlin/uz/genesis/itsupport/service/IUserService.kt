package uz.genesis.itsupport.service

import uz.genesis.itsupport.entity.User

interface IUserService {
    fun getUserByChatId(id: Long): User
}
