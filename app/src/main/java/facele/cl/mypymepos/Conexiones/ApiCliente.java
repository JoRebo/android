package facele.cl.mypymepos.Conexiones;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import facele.cl.mypymepos.Activities.Login;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Coto on 16-11-16.
 */

public class ApiCliente {
    private static APIPlug REST_CLIENT;
    private static final String API_URL = "https://maullin.docele.com/cl-pos/";

    static {
        setupRestClient();
    }

    private ApiCliente() {
    }

    public static APIPlug get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(Login.baseContext), CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        httpClient.cookieJar(new JavaNetCookieJar(cookieManager));

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        REST_CLIENT = retrofit.create(APIPlug.class);
    }

}
