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

    private TaskListAdapter taskListAdapter;
    private Realm realm;
    private RealmResults<Task> tasks;

    @OnClick(R.id.fab)
    void addTaskClick() {

        CreateTaskFragment createTaskDialog = new CreateTaskFragment();
        createTaskDialog.setOnDialogResultListener(new CreateTaskFragment.OnCreateDialogListener() {
            @Override
            public void onTaskCreated(String title, String body, Integer color) {
                createTask(title, body, color);
            }
        });
        createTaskDialog.show(getSupportFragmentManager(), "createDialog");
    }

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

    private void initRealm() {
        realm = Realm.getDefaultInstance();
        getTasks(false);
    }

    private void getTasks(boolean status) {
        tasks = realm.where(Task.class).equalTo("status", status).findAllSorted("dateCreated", Sort.DESCENDING);
        if (taskListAdapter != null) {
            taskListAdapter.updateRealmResults(tasks);
        }
    }

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

    private void initTaskList() {
        taskListAdapter = new TaskListAdapter(this, this, realm, tasks, true, true);
        taskList.setAdapter(taskListAdapter);
        taskList.setRefreshing(false);
    }

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
        realm.close();
        realm = null;
    }

    @Override
    public void statusChanged(boolean status) {
        Snackbar.make(mainView, status ? R.string.task_completed_msg : R.string.task_reopened_msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void taskDelete() {
        Snackbar.make(mainView, R.string.task_deleted_msg, Snackbar.LENGTH_SHORT).show();
    }
}
