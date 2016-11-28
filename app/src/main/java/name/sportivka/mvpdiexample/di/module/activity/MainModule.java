package name.sportivka.mvpdiexample.di.module.activity;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import name.sportivka.mvpdiexample.di.scope.activity.MainScope;
import name.sportivka.mvpdiexample.presenter.MainPresenter;

/**
 * Created by daniil on 29.11.16.
 */


@MainScope
@Module
public class MainModule {

    public Context context;

    public MainModule(Context context) {
        this.context = context;
    }

    @Provides
    public MainPresenter provideMainPresenter() {
        return new MainPresenter(context);
    }
}