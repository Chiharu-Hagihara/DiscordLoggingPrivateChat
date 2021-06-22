package jp.outlook.chiharuhagihara803.discordloggingprivatechat.command

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Plugin


open class ReplyCommand (val plugin: Plugin, name: String?) : Command(name) {

    override fun execute(sender: CommandSender, args: Array<String>) {
        val recieverName = TellCommand.getHistory(sender.name)

        if (args.isEmpty()) {
            if (recieverName != null) {
                sendMessage(sender, "§dCurrent Conversation Partner： $recieverName")
            } else {
                sendMessage(sender, "§c現在の会話相手はいません。\n§cThere is no current conversation partner.")
            }
            return
        }

        if (recieverName == null) {
            sendMessage(sender, "§cメッセージ送信先が見つかりません。\n§cThe destination for the message was not found.")
            return
        }
        val reciever: ProxiedPlayer = plugin.proxy.getPlayer(
            TellCommand.getHistory(sender.name))

        val str = StringBuilder()
        for (i in args.indices) {
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
        TellCommand.putHistory(reciever.name, sender.name)
    }


    fun sendMessage(reciever: CommandSender, message: String?) {
        if (message == null) return
        reciever.sendMessage(*TextComponent.fromLegacyText(message))
    }
}