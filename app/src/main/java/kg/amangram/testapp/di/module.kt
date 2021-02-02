package kg.amangram.testapp.di

import android.app.Application
import androidx.room.Room
import kg.amangram.testapp.ui.photos.PhotosViewModel
import kg.amangram.testapp.data.local.AlbumsDB
import kg.amangram.testapp.data.remote.Api
import kg.amangram.testapp.ui.database.DatabaseViewModel
import kg.amangram.testapp.ui.albums.AlbumsViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    single { provideDB(androidApplication()) }
    single { provideDAO(get()) }

    single { httpLoggingInterceptor() }
    single { okHttpClient(get()) }
    single { provideRxAdapter() }
    single { retrofit(get(), get()) }
    single { getApiService(get()) }

    viewModel { AlbumsViewModel(get()) }
    viewModel { (albumId: Int) -> PhotosViewModel(albumId, get(), get()) }
    viewModel { DatabaseViewModel(get()) }
}

fun provideDB(application: Application): AlbumsDB {
    return Room.databaseBuilder(application, AlbumsDB::class.java, "albums").build()
}

fun provideDAO(db: AlbumsDB) = db.getSaved()

fun httpLoggingInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.HEADERS
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
}

fun okHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient().newBuilder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .build()
}

fun getApiService(retrofit: Retrofit): Api {
    return retrofit.create(Api::class.java)
}

fun provideRxAdapter(): RxJava2CallAdapterFactory {
    return RxJava2CallAdapterFactory.create()
}

fun retrofit(okHttpClient: OkHttpClient, provideRxAdapter: RxJava2CallAdapterFactory): Retrofit {
    return Retrofit.Builder()
        .addCallAdapterFactory(provideRxAdapter)
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .client(okHttpClient)
        .build()
}