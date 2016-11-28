package name.sportivka.mvpdiexample.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import name.sportivka.mvpdiexample.BuildConfig;
import name.sportivka.mvpdiexample.di.component.DaggerAppComponent;
import name.sportivka.mvpdiexample.di.module.AppModule;
import name.sportivka.mvpdiexample.util.Constants;

/**
 * Created by daniil on 28.11.16.
 */

public class App extends Application {


    static App self;

    public static App getInstance() {
        return self;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        self = this;

        initDebugTools();
        initRealm();

        DaggerAppComponent.builder().appModule(new AppModule(this)).build();

    }

    private void initDebugTools() {
        if (BuildConfig.DEBUG) {
            Stetho.initialize(Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(RealmInspectorModulesProvider.builder(this)
                            .withDescendingOrder()
                            .withLimit(1000)
                            .databaseNamePattern(Pattern.compile(Constants.NAME_SCHEME))
                            .build())
                    .build());
        }
    }

    void initRealm() {
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .name(Constants.NAME_SCHEME)
                .schemaVersion(Constants.SCHEMA_VERSION)
                .migration(new ToDoMigration())
                .build());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
