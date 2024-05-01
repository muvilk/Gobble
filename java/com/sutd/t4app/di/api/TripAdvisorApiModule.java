package com.sutd.t4app.di.api;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
@InstallIn(SingletonComponent.class)
@Module
public class TripAdvisorApiModule {
    // this class is to set up the TripAdvisor API as a retrofit instance as well as
    // integrating RxJava into it
    // This is made into Dagger module so we may use this retrofit instance in all the fragments

    @Provides
    @Singleton
    TripAdvisorService providesApiService() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.hostnameVerifier((str, sslSession) -> true);

        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);

        // Add headers
        builder.interceptors().add(chain -> {
            Request request = chain.request();

            request = request.newBuilder()
                    .build();
            return chain.proceed(request);

        });

        // Logging
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.interceptors().add(interceptor);

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd")
                .create();

        RxJava2CallAdapterFactory rxAdapter = RxJava2CallAdapterFactory
                .createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.content.tripadvisor.com/api/v1/location/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxAdapter)
                .build();
        return retrofit.create(TripAdvisorService.class);
    }


}
