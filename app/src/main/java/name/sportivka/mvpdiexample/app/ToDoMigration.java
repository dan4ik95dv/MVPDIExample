package name.sportivka.mvpdiexample.app;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;

/**
 * Created by daniil on 28.11.16.
 */
class ToDoMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        if (oldVersion == 0) {
            //empty migration
            oldVersion++;
        }
    }
}
