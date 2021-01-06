package uz.genesis.itsupport.telegram

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.ApiContext
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatPhoto
import org.telegram.telegrambots.meta.api.methods.send.*
import org.telegram.telegrambots.meta.api.methods.stickers.AddStickerToSet
import org.telegram.telegrambots.meta.api.methods.stickers.CreateNewStickerSet
import org.telegram.telegrambots.meta.api.methods.stickers.SetStickerSetThumb
import org.telegram.telegrambots.meta.api.methods.stickers.UploadStickerFile
import org.telegram.telegrambots.meta.api.methods.updatingmessages.*
import org.telegram.telegrambots.meta.api.objects.File
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.PhotoSize
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.annotation.PostConstruct

/**
 * @created 09/12/2020 - 4:18 PM
 * @project it-support
 * @author Javohir Elmurodov
 */


@Service
class TgBotService @Autowired constructor(
    private val processor : UpdateProcessor,
    @Value("\${tg.bot.token}") private val token : String,
    @Value("\${tg.bot.username}") private val username : String,
    @Value("\${tg.bot.download.url}") private val fileDownloadUrl : String,
    @Value("\${upload.url}") private val upload : String) {

    private var rootLocation : Path = Paths.get(upload)

    @PostConstruct
    fun init() {
        try {
            Files.createDirectories(rootLocation)
        } catch (e : IOException) {
            throw RuntimeException("Could not initialize dir", e)
        }
    }


    fun run() {
        ApiContextInitializer.init()
        val botsApi = TelegramBotsApi()

        val botOptions = ApiContext.getInstance(DefaultBotOptions::class.java)
        botOptions.proxyHost = "172.17.9.31"
        botOptions.proxyPort = 8080
        botOptions.proxyType = DefaultBotOptions.ProxyType.HTTP
        try {
            botsApi.registerBot(UpdateHandler(botOptions, token, username, processor))
        } catch (e : TelegramApiException) {
            e.printStackTrace()
        }
    }

    fun execute(message : Any) {
        val botOptions = ApiContext.getInstance(DefaultBotOptions::class.java)
        botOptions.proxyHost = "172.17.9.31"
        botOptions.proxyPort = 8080
        botOptions.proxyType = DefaultBotOptions.ProxyType.HTTP
        UpdateHandler(botOptions, token, username, processor).executeMessage(message)
    }

    fun filePath(message : GetFile) : File {
        val botOptions = ApiContext.getInstance(DefaultBotOptions::class.java)
        botOptions.proxyHost = "172.17.9.31"
        botOptions.proxyPort = 8080
        botOptions.proxyType = DefaultBotOptions.ProxyType.HTTP
        return UpdateHandler(botOptions, token, username, processor).filePath(message)
    }

    private fun downloadFile(fileUrl : String) : java.io.File? {
        var file : java.io.File? = null
        try {
            val url = URL(String.format(fileDownloadUrl, token, fileUrl))
            val inps = url.openStream()
            val pathToFile =
                rootLocation.resolve("${System.currentTimeMillis()}.${fileUrl.substringAfter(".")}").toString()
            file = java.io.File(pathToFile)
            file.createNewFile()

            val outputStream = FileOutputStream(file)
            var read : Int
            val bytes = ByteArray(10000)
            while (inps.read(bytes).also { read = it } != -1) {
                outputStream.write(bytes, 0, read)
            }
            outputStream.flush()
            outputStream.close()
        } catch (ex : Exception) {
            ex.printStackTrace()
        }
        return file
    }

    fun getFiles(message : Message) : List<java.io.File> {
        val files = mutableListOf<java.io.File>()
        if (!message.hasPhoto()) return files
        val photoSize = message.photo.stream()
                .max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null);
        if (photoSize != null) {
            val filePath = getFilePath(photoSize)
            if (!filePath.isNullOrBlank()) {
                val downloadPhoto = downloadPhoto(filePath)
                if (downloadPhoto != null) {
                    files.add(downloadPhoto);
                }
            }
        }
        return files
    }

    fun getFilePath(photo : PhotoSize) : String? {
        if (photo.hasFilePath()) {
            return photo.filePath
        }
        val getFileMethod = GetFile()
        getFileMethod.fileId = photo.fileId
        try {
            var filePath = filePath(getFileMethod)
            return filePath.filePath
        } catch (e : TelegramApiException) {
            e.printStackTrace()
        }
        return null;
    }

    fun downloadPhoto(filePath : String) : java.io.File? {
        try {
            return downloadFile(filePath)
        } catch (e : TelegramApiException) {
            e.printStackTrace()
        }
        return null
    }
}


class UpdateHandler(
    options : DefaultBotOptions,
    private var token : String,
    private var username : String,
    private var updateProcessor : UpdateProcessor) : TelegramLongPollingBot(options) {

    override fun getBotUsername() : String? {
        return this.username
    }

    override fun getBotToken() : String {
        return this.token
    }

    override fun onUpdateReceived(update : Update) {
        val sendMessage : List<Any> = updateProcessor.process(update)
        for (any in sendMessage) {
            executeMessage(any)
        }
    }

    fun executeMessage(message : Any) {
        when (message) {
            is SendDocument -> execute(message as SendDocument?)
            is SendPhoto -> execute(message as SendPhoto?)
            is SendVideo -> execute(message as SendVideo?)
            is SendVideoNote -> execute(message as SendVideoNote?)
            is SendSticker -> execute(message as SendSticker?)
            is SendAudio -> execute(message as SendAudio?)
            is SendVoice -> execute(message as SendVoice?)
            is SendMediaGroup -> execute(message as SendMediaGroup?)
            is SetChatPhoto -> execute(message as SetChatPhoto?)
            is AddStickerToSet -> execute(message as AddStickerToSet?)
            is SetStickerSetThumb -> execute(message as SetStickerSetThumb?)
            is CreateNewStickerSet -> execute(message as CreateNewStickerSet?)
            is UploadStickerFile -> execute(message as UploadStickerFile?)
            is EditMessageMedia -> execute(message as EditMessageMedia?)
            is SendAnimation -> execute(message as SendAnimation?)
            is DeleteMessage -> execute(message as DeleteMessage?)
            is SendMessage -> execute(message as SendMessage?)
            is EditMessageReplyMarkup -> execute(message as EditMessageReplyMarkup?)
            is AnswerCallbackQuery -> execute(message as AnswerCallbackQuery?)
            is EditMessageCaption -> execute(message as EditMessageCaption?)
            is EditMessageText -> execute(message as EditMessageText?)
            is GetFile -> execute(message as GetFile?)
            else -> {
            }
        }
    }

    fun filePath(path : GetFile) : File {
        return execute(path)
    }
}

@Service
class UpdateProcessor @Autowired constructor(
    private val callBackProcessor : CallBackProcessor,
    private val messageProcessor : MessageProcessor) {

    fun process(update : Update) : List<Any> {
        return when {
            update.hasCallbackQuery() -> callBackProcessor.process(update)
            update.hasMessage() -> messageProcessor.process(update)
            else -> emptyList()
        }
    }
}