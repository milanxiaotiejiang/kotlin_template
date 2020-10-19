package com.seabreeze.robot.base.common

import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.annotation.StringDef
import java.util.*

/**
 * <pre>
 * author : 76515
 * time   : 2020/6/30
 * desc   :
 * </pre>
 */

object LanguageHelper {

    @StringDef(
        LANGUAGE_SYSTEM,//系统
        LANGUAGE_GERMAN,//德语
        LANGUAGE_ENGLISH,//英语
        LANGUAGE_SPANISH,//西班牙语
        LANGUAGE_FRENCH,//法语
        LANGUAGE_ITALIAN,//意大利语
        LANGUAGE_JAPANESE,//日本语
        LANGUAGE_SIMPLIFIED_CHINESE//简体中文
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class LanguageStatus

    const val LANGUAGE_SYSTEM = "system"
    const val LANGUAGE_GERMAN = "de"
    const val LANGUAGE_ENGLISH = "en"
    const val LANGUAGE_SPANISH = "es"
    const val LANGUAGE_FRENCH = "fr"
    const val LANGUAGE_ITALIAN = "it"
    const val LANGUAGE_JAPANESE = "ja"
    const val LANGUAGE_SIMPLIFIED_CHINESE = "zh"

    private val LOCALE_TYPE_GERMAN = Locale.GERMAN
    private val LOCALE_TYPE_ENGLISH = Locale.ENGLISH
    private val LOCALE_TYPE_SPANISH = Locale(LANGUAGE_SPANISH)
    private val LOCALE_TYPE_FRENCH = Locale.FRENCH
    private val LOCALE_TYPE_ITALIAN = Locale.ITALIAN
    private val LOCALE_TYPE_JAPANESE = Locale.JAPANESE
    private val LOCALE_TYPE_SIMPLIFIED_CHINESE = Locale.SIMPLIFIED_CHINESE

    fun switchLanguage(
        context: Context,
        @LanguageStatus language: String,
        isForce: Boolean = false,
        isSeniorForce: Boolean = false
    ): Context {
        return if (isSeniorForce) {

            languageCompat(language, context)
        } else {
            if (isForce) {
                languageCompat(language, context)
            } else {
                if (Settings.language_status == language) {
                    context
                } else {
                    languageCompat(language, context)
                }
            }
        }

    }

    private fun languageCompat(language: String, context: Context): Context {
        Settings.language_status = language
        return when (language) {
            LANGUAGE_SYSTEM -> languageCompat(context, systemLanguage())
            LANGUAGE_GERMAN -> languageCompat(context, LOCALE_TYPE_GERMAN)
            LANGUAGE_ENGLISH -> languageCompat(context, LOCALE_TYPE_ENGLISH)
            LANGUAGE_SPANISH -> languageCompat(context, LOCALE_TYPE_SPANISH)
            LANGUAGE_FRENCH -> languageCompat(context, LOCALE_TYPE_FRENCH)
            LANGUAGE_ITALIAN -> languageCompat(context, LOCALE_TYPE_ITALIAN)
            LANGUAGE_JAPANESE -> languageCompat(context, LOCALE_TYPE_JAPANESE)
            LANGUAGE_SIMPLIFIED_CHINESE -> languageCompat(context, LOCALE_TYPE_SIMPLIFIED_CHINESE)
            else -> context
        }
    }

    private fun languageCompat(context: Context, locale: Locale): Context {
        val resources = context.resources ?: return context
        val config = resources.configuration ?: return context
        config.setLocale(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.createConfigurationContext(config)
        } else {
            val dm = resources.displayMetrics ?: return context
            @Suppress("DEPRECATION")
            resources.updateConfiguration(config, dm)
            return context
        }
    }

    private fun systemLanguage(): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 解决了获取系统默认错误的问题
            Resources.getSystem().configuration.locales.get(0)
        } else {
            Locale.getDefault()
        }
    }

    fun getLocal(): Locale = when (Settings.language_status) {
        LANGUAGE_SYSTEM -> LOCALE_TYPE_SIMPLIFIED_CHINESE
        LANGUAGE_GERMAN -> LOCALE_TYPE_GERMAN
        LANGUAGE_ENGLISH -> LOCALE_TYPE_ENGLISH
        LANGUAGE_SPANISH -> LOCALE_TYPE_SPANISH
        LANGUAGE_FRENCH -> LOCALE_TYPE_FRENCH
        LANGUAGE_ITALIAN -> LOCALE_TYPE_ITALIAN
        LANGUAGE_JAPANESE -> LOCALE_TYPE_JAPANESE
        LANGUAGE_SIMPLIFIED_CHINESE -> LOCALE_TYPE_SIMPLIFIED_CHINESE
        else -> LOCALE_TYPE_SIMPLIFIED_CHINESE
    }
}