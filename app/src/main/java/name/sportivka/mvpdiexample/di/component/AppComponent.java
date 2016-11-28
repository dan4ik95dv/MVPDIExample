package name.sportivka.mvpdiexample.di.component;

import javax.inject.Singleton;

import dagger.Component;
import name.sportivka.mvpdiexample.di.module.AppModule;
import name.sportivka.mvpdiexample.di.module.StorageModule;

/**
 * Created by daniil on 29.11.16.
 */

@Singleton
@Component(modules = {AppModule.class, StorageModule.class})
public interface AppComponent {

}