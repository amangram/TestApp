package kg.amangram.testapp

import android.app.Application
import android.content.Context
import kg.amangram.testapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }

    override fun getApplicationContext(): Context {
        return super.getApplicationContext()
    }
}