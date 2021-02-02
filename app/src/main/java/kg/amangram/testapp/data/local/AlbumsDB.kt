package kg.amangram.testapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import kg.amangram.testapp.data.model.AlbumItem

@Database(entities = [AlbumItem::class], version = 1)
abstract class AlbumsDB : RoomDatabase() {
    abstract fun getSaved(): AlbumsDAO
}