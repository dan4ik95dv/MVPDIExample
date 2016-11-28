package name.sportivka.mvpdiexample.di.component.presenter;

import javax.inject.Singleton;

import dagger.Component;
import name.sportivka.mvpdiexample.di.module.presenter.MainPresenterModule;
import name.sportivka.mvpdiexample.io.repository.MainRepository;
import name.sportivka.mvpdiexample.presenter.MainPresenter;

/**
 * Created by daniil on 29.11.16.
 */

@Singleton
@Component(modules = MainPresenterModule.class)
public interface MainPresenterComponent {

    MainRepository getMainRepository();

    void inject(MainPresenter presenter);
}
