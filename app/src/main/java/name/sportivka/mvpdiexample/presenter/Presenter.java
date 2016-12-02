package name.sportivka.mvpdiexample.presenter;

/*
 * Created by daniil on 28.11.16
 */

public interface Presenter<V> {

    void attachView(V view);

    void detachView();
}