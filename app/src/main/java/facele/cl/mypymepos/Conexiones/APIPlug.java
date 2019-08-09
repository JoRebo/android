package facele.cl.mypymepos.Conexiones;

import facele.cl.mypymepos.Modelo.RESTEmision;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Jose on 16-11-16.
 */

public interface APIPlug {

    @Headers({"Content-Type: text/xml; charset=utf-8"})
    @POST("{abonadoIdentificacion}/emision/v10")
    Call<RESTEmision> doEmitir(@Path("abonadoIdentificacion") String abonadoIdentificacion,
                               @Query("datafonoIdentificacion") int datafonoIdentificacion,
                               @Query("vendedorEmail") String vendedorEmail,
                               @Body RequestBody body);

    //@GET("homerest")
    //Call<HomeRest> homerest(@Query("fcm") String fcm);

}
