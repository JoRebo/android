package facele.cl.mypymepos.Conexiones;

import facele.cl.mypymepos.Modelo.RESTEmision;
import facele.cl.mypymepos.Modelo.RESTEmisionObtener;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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
    Call<RESTEmision> postEmitir(@Path("abonadoIdentificacion") String abonadoIdentificacion,
                                 @Query("datafonoIdentificacion") int datafonoIdentificacion,
                                 @Query("vendedorEmail") String vendedorEmail,
                                 @Body RequestBody body);

    @GET("{abonadoIdentificacion}/emision/v10/{id}")
    Call<RESTEmisionObtener> getEmitir(@Path("abonadoIdentificacion") String abonadoIdentificacion,
                                       @Path("id") Integer emisionId);

    //@GET("homerest")
    //Call<HomeRest> homerest(@Query("fcm") String fcm);

}
