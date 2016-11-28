package name.sportivka.mvpdiexample.di.module.presenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import name.sportivka.mvpdiexample.di.module.StorageModule;
import name.sportivka.mvpdiexample.io.repository.MainRepository;

/**
 * Created by daniil on 29.11.16.
 */

@Module(includes = StorageModule.class)
public class MainPresenterModule {

    @Provides
    @Singleton
    MainRepository provideMainRepository(Realm realm) {
        return new MainRepository(realm);
    }
}
