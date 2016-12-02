package name.sportivka.mvpdiexample.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import name.sportivka.mvpdiexample.model.Task;
import name.sportivka.mvpdiexample.ui.adapter.TaskListAdapter;
import name.sportivka.mvpdiexample.ui.fragment.CreateTaskFragment;
import name.sportivka.mvpdiexample.ui.view.MainMvpView;

/**
 * Created by daniil on 28.11.16.
 */

public class MainPresenter implements Presenter<MainMvpView>, TaskListAdapter.ChangeTaskStatusListener {

    private MainMvpView mainMvpView;
    private Context context;

    private TaskListAdapter taskListAdapter;
    private Realm realm;
    private RealmResults<Task> tasks;
    private TabLayout.OnTabSelectedListener tabSelectedListener;
    private CreateTaskFragment.OnCreateDialogListener createDialogListener;

    public MainPresenter(Context context) {
        this.context = context;
        attachView((MainMvpView) context);
    }

    //При аттаче инициализируем презентер.
    @Override
    public void attachView(MainMvpView view) {
        this.mainMvpView = view;
        init();
    }

    //При дестрое активности закрываем realm
    @Override
    public void detachView() {
        this.mainMvpView = null;
        realm.close();
        realm = null;
    }

    /*
        Инициализируем realm db и адаптер
     */
    private void init() {
        initRealm();
        taskListAdapter = new TaskListAdapter(context, this, realm, tasks, true, true);
    }

    /*
       Подключаем realm и получаем список активных задач
    */
    private void initRealm() {
        realm = Realm.getDefaultInstance();
        getTasks(false);
    }

    /*
       Получение списка заданий
     */
    private void getTasks(boolean status) {
        //Запроса получения списка заданий по статусу задания и сортировкой по дате создания
        tasks = realm.where(Task.class).equalTo("status", status).findAllSorted("dateCreated", Sort.DESCENDING);
        if (taskListAdapter != null) {
            //Обновить список обьектов в адаптере
            taskListAdapter.updateRealmResults(tasks);
        }
    }

    /*
       Процедура создания записи задания в бд.
    */
    private void createTask(final String title, final String body, final int color) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Task task = new Task();
                task.setId(UUID.randomUUID().toString());
                task.setDateCreated(new Date());
                task.setTitle(title);
                task.setBody(body);
                task.setColor(color);
                task.setStatus(false);
                realm.copyToRealm(task);
            }
        });
        mainMvpView.taskAdded();
    }

    @Override
    public void statusChanged(boolean status) {
        mainMvpView.taskStatusChanged(status);
    }

    @Override
    public void taskDelete() {
        mainMvpView.taskDeleted();
    }

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
                        case 0: //Активные
                            getTasks(false);
                            break;
                        case 1: //Завершенные
                            getTasks(true);
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
                    //Создаем задание
                    createTask(title, body, color);
                }
            };
        }
        return createDialogListener;
    }

}
