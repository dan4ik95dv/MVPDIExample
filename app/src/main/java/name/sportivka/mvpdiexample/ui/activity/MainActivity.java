package name.sportivka.mvpdiexample.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import name.sportivka.mvpdiexample.R;
import name.sportivka.mvpdiexample.di.component.activity.DaggerMainComponent;
import name.sportivka.mvpdiexample.di.module.activity.MainModule;
import name.sportivka.mvpdiexample.presenter.MainPresenter;
import name.sportivka.mvpdiexample.ui.fragment.CreateTaskFragment;
import name.sportivka.mvpdiexample.ui.view.MainMvpView;
import name.sportivka.mvpdiexample.util.Constants;

public class MainActivity extends BaseActivity implements MainMvpView {

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

    @Inject
    MainPresenter mainPresenter;


    @OnClick(R.id.fab)
    void addTaskClick() {
        showCreateTaskDialog();
    }

    private void showCreateTaskDialog() {
        CreateTaskFragment createTaskDialog = new CreateTaskFragment();
        createTaskDialog.setOnDialogResultListener(mainPresenter.getOnCreateDialogListener());
        createTaskDialog.show(getSupportFragmentManager(), "createDialog");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerMainComponent.builder().mainModule(new MainModule(this)).build().inject(this);

        setContentView(R.layout.activity_main);

        setSupportActionBar(toolbar);
        taskList.setAdapter(mainPresenter.getTaskListAdapter());
        tabLayout.addOnTabSelectedListener(mainPresenter.getTabSelectedListener());

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
        mainPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void taskStatusChanged(boolean status) {
        Snackbar.make(mainView, status ? R.string.task_completed_msg : R.string.task_reopened_msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void taskDeleted() {
        Snackbar.make(mainView, R.string.task_deleted_msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void taskAdded() {
        Snackbar.make(mainView, R.string.task_add_msg, Snackbar.LENGTH_SHORT).show();
    }
}
