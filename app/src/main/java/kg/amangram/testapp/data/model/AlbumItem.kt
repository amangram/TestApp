package kg.amangram.testapp.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(
    tableName = "albums"
)
@Keep
@Parcelize
data class AlbumItem(
    @PrimaryKey(autoGenerate = false)
    val id: Int?,
    val title: String?,
    val userId: Int?
) : Parcelable