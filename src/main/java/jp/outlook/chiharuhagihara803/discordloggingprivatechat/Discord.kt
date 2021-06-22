package jp.outlook.chiharuhagihara803.discordloggingprivatechat

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color
import javax.security.auth.login.LoginException

object Discord : ListenerAdapter() {
    var token: String? = null
    var guildID: Long? = null
    private var channel: TextChannel? = null
    var channelID: Long? = null
    fun startUp() {
        try {
            val jda = JDABuilder
                .createDefault(token)
                .build()
            jda.awaitReady()
            val guild = jda.getGuildById(guildID!!)!!
            channel = guild.getTextChannelById(channelID!!)
        } catch (e: LoginException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun sendLog(sender: String, receiver: String, message: String) {
        val eb = EmbedBuilder()
        eb.setColor(Color.CYAN)
        eb.setDescription(
            """
    ``Sender: $sender, Receiver: $receiver``
    ```Message: $message```
    """.trimIndent()
        )
        channel!!.sendMessageEmbeds(eb.build()).queue()
    }
}