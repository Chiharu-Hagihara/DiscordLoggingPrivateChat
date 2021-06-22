package jp.outlook.chiharuhagihara803.discordloggingprivatechat.command

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command

class TellCommand(name: String) : Command(name) {

    override fun execute(sender: CommandSender?, args: Array<out String>?) {
        if (sender == null) return

        if (args.isNullOrEmpty()) {
            sendMessage(sender, "§c/$name <player> <message> : Send private message.")
            sendMessage(sender, "§c/$name ignore [player名] : Hide/show private chats from [player name].")
            return
        }

        if (args.size <= 1) {
            sendMessage(sender, "§c/$name <player> <message> : Send private message.")
            sendMessage(sender, "§c/$name ignore [player名] : Hide/show private chats from [player name].")
            return
        }

        if (args[0] == sender.name) {
            sendMessage(sender, "§c自分自身にはプライベートメッセージを送信することができません。\n§cI cannot send a private message to myself.")
            return
        }

        val reciever = ProxyServer.getInstance().getPlayer(args[0])
        if (reciever == null) {
            sendMessage(sender, "§cメッセージ送信先が見つかりません。\n§cThe destination for the message was not found.")
            return
        }

        val str = StringBuilder()
        for (i in 1 until args.size) {
            str.append(args[i] + " ")
        }
        val message = str.toString().trim { it <= ' ' }

        sendPrivateMessage(sender, reciever, message)
    }

    fun sendPrivateMessage(sender: CommandSender, reciever: ProxiedPlayer, message: String?) {

        val message = ChatColor.translateAlternateColorCodes('&', message)

        var senderServer: String? = "console"
        if (sender is ProxiedPlayer) {
            senderServer = sender.server.info.name
        }
        val endmsg = "§7[${sender.name}@${senderServer}> ${reciever.name}@${reciever.server.info.name}] §f${message}"

        sendMessage(sender, endmsg)
        sendMessage(reciever, endmsg)

    }

    fun sendMessage(reciever: CommandSender, message: String?) {
        if (message == null) return
        reciever.sendMessage(*TextComponent.fromLegacyText(message))
    }

    companion object {

        lateinit var history: MutableMap<String, String>

        fun putHistory(reciever: String?, sender: String?) {
            history[reciever!!] = sender!!
        }

        fun getHistory(reciever: String?): String? {
            return history[reciever]
        }
    }
}