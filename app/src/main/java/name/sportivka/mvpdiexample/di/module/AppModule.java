package name.sportivka.mvpdiexample.di.module;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import name.sportivka.mvpdiexample.app.App;

/**
 * Created by daniil on 29.11.16.
 */
@Module
public class AppModule {
    private final App mApp;

    public AppModule(@NonNull App app) {
        this.mApp = app;
    }


    @Provides
    @Singleton
    App provideApp() {
        return mApp;
    }

    @Provides
    Context provideAppContext() {
        return App.getInstance();
    }

}
