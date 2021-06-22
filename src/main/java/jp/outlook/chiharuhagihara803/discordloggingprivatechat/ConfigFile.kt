package jp.outlook.chiharuhagihara803.discordloggingprivatechat

import com.google.common.io.ByteStreams
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class ConfigFile(plugin: Plugin?) {
    private var plugin: Plugin? = null
    private val file: File
    private var config: Configuration? = null
    private val filePatch = "config.yml"
    private fun getResourceAsStream(patch: String): InputStream {
        return plugin!!.getResourceAsStream(patch)
    }

    fun getConfig(): Configuration? {
        return try {
            ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(file).also { config = it }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun setConfig(path: String, value: Any): Boolean {
        return try {
            val config = getConfig()

            config?.set(path, value)

            ConfigurationProvider.getProvider(YamlConfiguration::class.java).save(config, File(plugin!!.dataFolder, "config.yml"))

            true
        }catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration::class.java).save(config, File(plugin!!.dataFolder, "config.yml"))
        } catch (e: IOException) {
            e.printStackTrace()
            plugin!!.logger.severe("Could not save storage file!")
        }
    }

    fun dataFolder(): File {
        return plugin!!.dataFolder
    }

    init {
        this.plugin = plugin
        if (!plugin?.dataFolder?.exists()!!) plugin.dataFolder?.mkdir()
        file = File(plugin.dataFolder, filePatch)
        if (!file.exists()) {
            try {
                file.createNewFile()
                try {
                    val inputStream = plugin.getResourceAsStream("config.yml")
                    val fileOutputStream = FileOutputStream(file)
                    ByteStreams.copy(inputStream, fileOutputStream)
                } catch (e: IOException) {
                    e.printStackTrace()
                    plugin.logger.warning("Unable to create storage file.$filePatch")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                plugin.logger.warning("failed to create config.yml")
            }
        }
    }
}