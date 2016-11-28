package name.sportivka.mvpdiexample.di.component.activity;

import dagger.Component;
import name.sportivka.mvpdiexample.di.module.activity.MainModule;
import name.sportivka.mvpdiexample.di.scope.activity.MainScope;
import name.sportivka.mvpdiexample.ui.activity.MainActivity;

/**
 * Created by daniil on 29.11.16.
 */


@MainScope
@Component(modules = MainModule.class)
public interface MainComponent {

    void inject(MainActivity activity);

}