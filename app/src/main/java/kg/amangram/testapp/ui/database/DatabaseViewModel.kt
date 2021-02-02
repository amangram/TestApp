package kg.amangram.testapp.ui.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.amangram.testapp.data.local.AlbumsDAO
import kg.amangram.testapp.data.model.AlbumItem

class DatabaseViewModel(private val db: AlbumsDAO) : ViewModel() {

    private val disposable = CompositeDisposable()
    val albums: LiveData<List<AlbumItem>> = getAlbums()

    private fun getAlbums(): MutableLiveData<List<AlbumItem>> {
        val albums = MutableLiveData<List<AlbumItem>>()
        disposable.add(
            db.getAlbums()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    albums.postValue(it)
                }, {})
        )
        return albums
    }

    fun delete(album: AlbumItem) {
        disposable.add(
            db.delete(album)
                .subscribeOn(Schedulers.io())
                .subscribe()
        )
        getAlbums()
    }
}