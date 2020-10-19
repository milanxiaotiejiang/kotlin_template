package com.seabreeze.robot.data.utils.audio

import android.os.Environment
import com.elvishew.xlog.XLog
import com.seabreeze.robot.data.common.Common.Companion.SYNTHESIZER_AUE_PARAM
import okhttp3.ResponseBody
import java.io.File

/**
 * User: milan
 * Time: 2020/8/6 21:16
 * Des:
 */

fun ResponseBody.parseAudioAndPlay(function: AudioTracker.OnAudioTrackerListener) {
    val contentType = contentType()
    contentType?.let { mediaType ->
        if (mediaType.type.contains("audio")) {
            val bytes = bytes()

            val ttsFile = File(getVideoDirectory().toString(), "tts." + SYNTHESIZER_AUE_PARAM.getFormat())
                    .apply {
                        if (exists())
                            delete()
                        createNewFile()
                        writeBytes(bytes)
                    }

            XLog.e(ttsFile.absolutePath)
            AudioTracker.getInstance().start(ttsFile, function)
        } else {
            XLog.e(string())
        }
    }
}

private fun getVideoDirectory(): File? {
    val sdRootFile = getSDRootFile()
    var file: File? = null
    if (sdRootFile != null && sdRootFile.exists()) {
        file = File(sdRootFile, "video")
        if (!file.exists()) {
            file.mkdirs()
        }
    }
    return file
}

private fun getSDRootFile(): File? {
    return if (isSdCardAvailable()) {
        Environment.getExternalStorageDirectory()
    } else {
        null
    }
}

private fun isSdCardAvailable(): Boolean {
    return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
}

fun Int.getFormat(): String {
    val formats = arrayOf("mp3", "pcm", "pcm", "wav")
    return formats[this - 3]
}