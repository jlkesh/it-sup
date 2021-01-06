package uz.genesis.itsupport

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.ApplicationListener
import uz.genesis.itsupport.telegram.TgBotService

@SpringBootApplication
class ItSupportApplication : SpringBootServletInitializer(), ApplicationListener<ApplicationReadyEvent> {
    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        val botService = event.applicationContext.getBean("tgBotService") as TgBotService;
        botService.run()
    }

    override fun configure(applicationBuilder: SpringApplicationBuilder): SpringApplicationBuilder {
        return applicationBuilder.sources(ItSupportApplication::class.java)
    }
}
fun main(args: Array<String>) {
    runApplication<ItSupportApplication>(*args)
}
