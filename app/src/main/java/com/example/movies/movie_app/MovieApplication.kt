package com.example.movies.movie_app

import android.app.Application
import android.content.Context
import com.example.movies.modules.ModulesMovie
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MovieApplication : Application() {

    companion object {
        var context: Context? = null
    }


    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Android context
            androidContext(applicationContext)
            // modules
            modules(ModulesMovie().createModules(applicationContext))
        }

        context = applicationContext

        Realm.init(this)

        val realmConfiguration = RealmConfiguration.Builder()
            .name("movi.realm")
            .encryptionKey(ByteArray(64))
            .schemaVersion(1)
            .build()

        Realm.setDefaultConfiguration(realmConfiguration)

    }

}