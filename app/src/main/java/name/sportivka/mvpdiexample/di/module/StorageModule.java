package name.sportivka.mvpdiexample.di.module;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by daniil on 29.11.16.
 */

@Module(includes = AppModule.class)
public class StorageModule {

    @Provides
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }
}
