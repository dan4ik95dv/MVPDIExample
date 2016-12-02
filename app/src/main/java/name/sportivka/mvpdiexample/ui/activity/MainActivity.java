package name.sportivka.mvpdiexample.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import name.sportivka.mvpdiexample.R;
import name.sportivka.mvpdiexample.model.Task;
import name.sportivka.mvpdiexample.ui.adapter.TaskListAdapter;
import name.sportivka.mvpdiexample.ui.fragment.CreateTaskFragment;
import name.sportivka.mvpdiexample.util.Constants;

public class MainActivity extends BaseActivity implements TaskListAdapter.ChangeTaskStatusListener {

    /*
     * @BindView, @OnClick etc... это аннотации библиотеки Butterknife
     * Butterknife позволяет уменьшить количество кода за счет кодогенерации на этапе сборки
     * проекта.
     * В классе BaseActivity описано подключение и отключение библиотеки.
     */

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mainView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton addTaskButton;

    @BindView(R.id.task_list)
    RealmRecyclerView taskList;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    //Адаптер в котором будут хранятся елементы карточек заданий.
    private TaskListAdapter taskListAdapter;

    //Обьект для работы с базой
    private Realm realm;

    //Хранение результата запроса к базе
    private RealmResults<Task> tasks;

    //При нажатии на кнопку добавления задания (Floating Action Button)
    @OnClick(R.id.fab)
    void addTaskClick() {

        //Отображение диалога создания задания.
        CreateTaskFragment createTaskDialog = new CreateTaskFragment();
        createTaskDialog.setOnDialogResultListener(new CreateTaskFragment.OnCreateDialogListener() {
            @Override
            public void onTaskCreated(String title, String body, Integer color) {
                createTask(title, body, color);
            }
        });
        createTaskDialog.show(getSupportFragmentManager(), "createDialog");
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
        Snackbar.make(mainView, R.string.task_add_msg, Snackbar.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolbar();
        initRealm();
        initTaskList();
        initTabLayout();
    }

    /*
    Инициализация обьекта дб Realm.
    */
    private void initRealm() {
        realm = Realm.getDefaultInstance();
        getTasks(false);
    }

    /*
    Получение списка заданий
    */
    private void getTasks(boolean status) {
        tasks = realm.where(Task.class).equalTo("status", status).findAllSorted("dateCreated", Sort.DESCENDING);
        if (taskListAdapter != null) {
            taskListAdapter.updateRealmResults(tasks);
        }
    }

    /*
    Инициализация обьекта TabLayout
    */
    private void initTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        getTasks(false);
                        break;
                    case 1:
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
        });

    }


    /*
       Инициализация адаптера карточек
     */
    private void initTaskList() {
        taskListAdapter = new TaskListAdapter(this, this, realm, tasks, true, true);
        taskList.setAdapter(taskListAdapter);
        taskList.setRefreshing(false);
    }

    /*
       Инициализация toolbar
     */
    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                goToUrl(Constants.GITHUB_URL);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();//Закрытие дб
        realm = null;
    }

    /*
       Событие при изменении статуса задания
    */
    @Override
    public void statusChanged(boolean status) {
        Snackbar.make(mainView, status ? R.string.task_completed_msg : R.string.task_reopened_msg, Snackbar.LENGTH_SHORT).show();
    }

    /*
        Событие при удалении задания
    */
    @Override
    public void taskDelete() {
        Snackbar.make(mainView, R.string.task_deleted_msg, Snackbar.LENGTH_SHORT).show();
    }
}
