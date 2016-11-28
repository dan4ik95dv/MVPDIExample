package name.sportivka.mvpdiexample.di.scope.activity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by daniil on 29.11.16.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface MainScope {
}