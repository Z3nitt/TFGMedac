package com.example.gamebox

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

object LocaleHelper {
    private const val PREFS_NAME = "settings"
    private const val KEY_LANG   = "app_language"

    fun applyLocale(context: Context): Context {
        val lang = context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANG, Locale.getDefault().language)!!
        return updateResources(context, lang)
    }

    fun setLocale(context: Context, language: String): Context {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_LANG, language).apply()
        return updateResources(context, language)
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        val res    = context.resources
        val cfg    = Configuration(res.configuration).apply {
            setLocale(locale)
        }
        return context.createConfigurationContext(cfg)
    }
}


open class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase))
    }
}