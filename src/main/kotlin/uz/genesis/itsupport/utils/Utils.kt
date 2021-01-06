package uz.genesis.itsupport.utils

import org.springframework.util.ResourceUtils
import java.io.File
import java.io.FileNotFoundException

/**
 * @created 10/12/2020 - 3:54 PM
 * @project it-support
 * @author Javohir Elmurodov
 */

class Utils {
    fun isEmpty(obj : Any?) : Boolean {
        return obj == null || "" == obj
    }

    fun getFile(path : String) : File? {
        var file : File? = null
        try {
//            file = ResourceUtils.getFile(path)
            file = ResourceUtils.getFile("classpath:static/images/$path")
        } catch (e : FileNotFoundException) {
            e.printStackTrace()
        } finally {
            return file
        }
    }
}