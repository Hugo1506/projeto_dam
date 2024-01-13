import com.example.projeto_dam.fotoDados
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface fotoEnviar {
    @POST("folha1/")
    fun publicar(@Body dados: fotoDadosWrapper): Call<fotoDadosWrapper>

    data class fotoDadosWrapper(
        @SerializedName("folha1")
        val folha1: fotoDados
    )
}
