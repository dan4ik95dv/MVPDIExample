package name.sportivka.mvpdiexample.ui.view;

/**
 * Created by daniil on 28.11.16.
 */

public interface MainMvpView extends MvpView {
    void taskAdded();

    void taskDeleted();

    void taskStatusChanged(boolean status);
}
