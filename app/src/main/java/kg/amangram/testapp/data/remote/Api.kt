package kg.amangram.testapp.data.remote

import io.reactivex.Observable
import kg.amangram.testapp.data.model.AlbumDetailResponse
import kg.amangram.testapp.data.model.AlbumResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("albums")
    fun getAlbums(): Observable<Response<AlbumResponse>>

    @GET("photos")
    fun getAlbum(@Query("albumId") albumId: Int): Observable<Response<AlbumDetailResponse>>
}