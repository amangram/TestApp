package kg.amangram.testapp.ui.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.amangram.testapp.data.State
import kg.amangram.testapp.data.local.AlbumsDAO
import kg.amangram.testapp.data.model.AlbumDetailResponse
import kg.amangram.testapp.data.model.AlbumItem
import kg.amangram.testapp.data.remote.Api
import org.json.JSONObject

class PhotosViewModel(
    private val albumId: Int,
    private val apiService: Api,
    private val db: AlbumsDAO
) : ViewModel() {

    private var disposable = CompositeDisposable()
    val isExist: LiveData<Boolean> = exists()


    fun getPhotos(): LiveData<State<AlbumDetailResponse>> {
        val photos = MutableLiveData<State<AlbumDetailResponse>>()
        photos.postValue(State.loading())
        disposable.add(
            apiService.getAlbum(albumId)
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            photos.postValue(State.success(it))
                        }
                    } else {
                        try {
                            val jsonObject = JSONObject(response.errorBody()?.string())
                            photos.postValue(
                                State.failed(
                                    jsonObject.getJSONObject("error").getString("message")
                                )
                            )
                        } catch (e: Exception) {
                        }
                    }

                }, {
                    photos.postValue(State.failed(it.localizedMessage))
                })
        )
        return photos
    }

    fun save(album: AlbumItem) {
        disposable.add(
            db.add(album)
                .subscribeOn(Schedulers.io())
                .subscribe({

                }, {

                })
        )
        exists()
    }

    private fun exists(): MutableLiveData<Boolean> {
        val exists = MutableLiveData<Boolean>()
        disposable.add(
            db.exists(albumId)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    exists.postValue(it)
                }, {})
        )
        return exists
    }

    fun delete(album: AlbumItem) {
        disposable.add(
            db.delete(album)
                .subscribeOn(Schedulers.io())
                .subscribe()
        )
        exists()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        disposable.dispose()
    }
}