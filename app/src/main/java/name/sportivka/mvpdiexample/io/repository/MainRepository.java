package name.sportivka.mvpdiexample.io.repository;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import name.sportivka.mvpdiexample.model.Task;

/**
 * Created by daniil on 29.11.16.
 */

public class MainRepository {

    Realm realm;

    public MainRepository(Realm realm) {
        this.realm = realm;
    }

    public RealmResults<Task> getActiveTasks() {
        return realm.where(Task.class).equalTo("status", false).findAllSorted("dateCreated", Sort.DESCENDING);
    }

    public RealmResults<Task> getCompletedTasks() {
        return realm.where(Task.class).equalTo("status", true).findAllSorted("dateCreated", Sort.DESCENDING);
    }


    public void update(final Task item, final boolean status) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                item.setStatus(status);
                realm.copyToRealmOrUpdate(item);
            }
        });
    }

    public void remove(final RealmResults realmResults, final int position) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmResults.deleteFromRealm(position);
            }
        });
    }

    public void add(String title, String body, int color) {
        final Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setDateCreated(new Date());
        task.setTitle(title);
        task.setBody(body);
        task.setColor(color);
        task.setStatus(false);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(task);
            }
        });
    }

    public void closeRealm() {
        realm.close();
        realm = null;
    }
}
