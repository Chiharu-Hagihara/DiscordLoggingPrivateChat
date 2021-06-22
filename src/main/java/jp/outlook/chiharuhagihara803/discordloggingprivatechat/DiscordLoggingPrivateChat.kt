package jp.outlook.chiharuhagihara803.discordloggingprivatechat

import jp.outlook.chiharuhagihara803.discordloggingprivatechat.command.ReplyCommand
import jp.outlook.chiharuhagihara803.discordloggingprivatechat.command.TellCommand
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin

class DiscordLoggingPrivateChat : Plugin(), Listener{
    override fun onEnable() {
        // Plugin startup logic
        //tell commandを置き換える
        for (command in arrayOf(
            "tell", "msg", "message", "m", "w", "t")) {
            proxy.pluginManager.registerCommand(this, TellCommand(command))
        }
        //reply commandを置き換える
        for (command in arrayOf("reply", "r")) {
            proxy.pluginManager.registerCommand(this, ReplyCommand(this, command))
        }

        val config = ConfigFile(this).getConfig()
        Discord.token = config?.getString("token")
        Discord.guildID = config?.getLong("guild")
        Discord.channelID = config?.getLong("channel")

        Discord.startUp()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}