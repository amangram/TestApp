package kg.amangram.testapp.data.local

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import kg.amangram.testapp.data.model.AlbumItem

@Dao
interface AlbumsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(album: AlbumItem): Completable

    @Query("SELECT * FROM albums")
    fun getAlbums(): Observable<List<AlbumItem>>

    @Delete
    fun delete(album: AlbumItem): Completable

    @Query("SELECT EXISTS (SELECT 1 FROM albums WHERE id = :id)")
    fun exists(id: Int): Observable<Boolean>
}