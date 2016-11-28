package name.sportivka.mvpdiexample.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;

import io.realm.Realm;
import io.realm.RealmResults;
import name.sportivka.mvpdiexample.io.repository.MainRepository;
import name.sportivka.mvpdiexample.model.Task;
import name.sportivka.mvpdiexample.ui.adapter.TaskListAdapter;
import name.sportivka.mvpdiexample.ui.fragment.CreateTaskFragment;
import name.sportivka.mvpdiexample.ui.view.MainMvpView;

/**
 * Created by daniil on 28.11.16.
 */

public class MainPresenter implements Presenter<MainMvpView>, TaskListAdapter.ChangeTaskStatusListener {


    private MainRepository mainRepository;
    private MainMvpView mainMvpView;
    private Context context;
    private TaskListAdapter taskListAdapter;
    private TabLayout.OnTabSelectedListener tabSelectedListener;
    private CreateTaskFragment.OnCreateDialogListener createDialogListener;

    public MainPresenter(Context context) {
        this.context = context;
        this.mainRepository = new MainRepository(Realm.getDefaultInstance());
        attachView((MainMvpView) context);
    }

    @Override
    public void attachView(MainMvpView view) {
        this.mainMvpView = view;
        init();
    }

    @Override
    public void detachView() {
        this.mainMvpView = null;
        mainRepository.closeRealm();
    }


    private void init() {
        taskListAdapter = new TaskListAdapter(context, this, mainRepository.getActiveTasks(), true, true);
    }


    private void createTask(final String title, final String body, final int color) {
        mainRepository.add(title, body, color);
        mainMvpView.taskAdded();
    }

    @Override
    public void statusChanged(final boolean status, Task task) {
        mainRepository.update(task, status);
        mainMvpView.taskStatusChanged(status);
    }

    @Override
    public void taskDelete(int position, RealmResults<Task> realmResults) {
        mainRepository.remove(realmResults, position);
        mainMvpView.taskDeleted();
    }

    @NonNull
    public TaskListAdapter getTaskListAdapter() {
        return taskListAdapter;
    }

    @NonNull
    public TabLayout.OnTabSelectedListener getTabSelectedListener() {
        if (tabSelectedListener == null) {
            tabSelectedListener = new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                            taskListAdapter.updateRealmResults(mainRepository.getActiveTasks());
                            break;
                        case 1:
                            taskListAdapter.updateRealmResults(mainRepository.getCompletedTasks());
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            };
        }
        return tabSelectedListener;
    }

    @NonNull
    public CreateTaskFragment.OnCreateDialogListener getOnCreateDialogListener() {
        if (createDialogListener == null) {
            createDialogListener = new CreateTaskFragment.OnCreateDialogListener() {
                @Override
                public void onTaskCreated(String title, String body, Integer color) {
                    createTask(title, body, color);
                }
            };
        }
        return createDialogListener;
    }

}
