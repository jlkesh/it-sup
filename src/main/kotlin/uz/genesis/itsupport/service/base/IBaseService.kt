package uz.genesis.itsupport.service.base

/**
 * @created 10/12/2020 - 12:10 PM
 * @project it-support
 * @author Javohir Elmurodov
 */
interface IBaseService<T> {
    fun save(entity: T): T?
    fun delete(id: Long): Boolean
    fun update(entity: T): T?
    fun get(id: Long): T?
    fun getAll(): List<*>?
}