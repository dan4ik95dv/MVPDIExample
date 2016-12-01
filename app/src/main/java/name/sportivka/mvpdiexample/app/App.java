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
import name.sportivka.mvpdiexample.util.Constants;

/**
 * Created by daniil on 28.11.16.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /*
        * Инициализация Инструментов отладки Realm базы
        * Вызов консоли осуществляется с помощью доступа через chrome* браузер
        * chrome://inspect
        */
        initDebugTools();

        //Инициализация NoSQL Realm базы
        initRealm();
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
        //Подключение библиотеки для расширения количества методов в приложении
        //Смотреть "проблема 65k методов dex"
        MultiDex.install(this);
    }


}
