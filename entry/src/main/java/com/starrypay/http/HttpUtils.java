package com.starrypay.http;

import com.google.gson.Gson;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.time.Duration;

public class HttpUtils {

    private static Retrofit retrofit;

    public static void init(String url) {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build();
                return chain.proceed(request);
            }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(Duration.ofSeconds(10));
        builder.writeTimeout(Duration.ofSeconds(10));
        builder.connectTimeout(Duration.ofSeconds(10));
        builder.addInterceptor(interceptor);
        OkHttpClient client = builder.build();


        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }

}
