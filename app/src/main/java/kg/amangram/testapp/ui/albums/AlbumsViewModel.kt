package kg.amangram.testapp.ui.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.amangram.testapp.data.State
import kg.amangram.testapp.data.model.AlbumResponse
import kg.amangram.testapp.data.remote.Api
import org.json.JSONObject

class AlbumsViewModel(private val apiService: Api) : ViewModel() {

    private var disposable = CompositeDisposable()

    val albums: LiveData<State<AlbumResponse>>
        get() = _albums

    private val _albums = MutableLiveData<State<AlbumResponse>>()

    fun getAlbums(){
        _albums.postValue(State.loading())
        disposable.add(
            apiService.getAlbums()
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _albums.postValue(State.success(it))
                        }
                    } else {
                        try {
                            val jsonObject = JSONObject(response.errorBody()?.string())
                            _albums.postValue(
                                State.failed(
                                    jsonObject.getJSONObject("error").getString("message")
                                )
                            )
                        } catch (e: Exception) {
                        }

                    }
                }, {
                    _albums.postValue(State.failed(it.localizedMessage))
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        disposable.dispose()
    }
}