package uz.genesis.itsupport.service.base

import org.springframework.data.jpa.repository.JpaRepository
import java.io.Serializable

/**
 * @created 10/12/2020 - 12:38 PM
 * @project it-support
 * @author Javohir Elmurodov
 */


abstract class BaseService<T, R : JpaRepository<T?, Long?>>(private val repository : R?) : IBaseService<T>, Serializable {

    override fun save(entity : T) : T? {
        return repository?.save(entity);
    }

    override fun delete(id : Long) : Boolean {
        return try {
            repository?.deleteById(id);
            true
        } catch (e : Exception) {
            e.printStackTrace()
            false;
        }
    }

    override fun update(entity : T) : T? {
        return save(entity);
    }

    override fun get(id : Long) : T? {
        return repository!!.findById(id).orElse(null)!!
    }

    override fun getAll() : List<*>? {
        return repository?.findAll() as List<*>
    }
}

