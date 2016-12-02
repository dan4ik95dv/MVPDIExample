package name.sportivka.mvpdiexample.ui.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import name.sportivka.mvpdiexample.R;
import name.sportivka.mvpdiexample.model.Task;

/**
 * Created by daniil on 28.11.16.
 */

public class TaskListAdapter extends RealmBasedRecyclerViewAdapter<Task, TaskListAdapter.ViewHolderTask> {
    //listener для отправки коллбеков в активность.
    private ChangeTaskStatusListener changeTaskStatusListener;
    //Объект работы  с дб.
    private Realm realm;

    public TaskListAdapter(Context context, ChangeTaskStatusListener changeTaskStatusListener, Realm realm, RealmResults realmResults, boolean automaticUpdate, boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
        this.realm = realm;
        this.changeTaskStatusListener = changeTaskStatusListener;
    }

    @Override
    public void onViewRecycled(RealmViewHolder holder) {
        super.onViewRecycled(holder);
        ((ViewHolderTask) holder).clear();
    }

    /*
           Создание ViewHolder
         */
    @Override
    public ViewHolderTask onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.task_item_view, viewGroup, false);
        return new ViewHolderTask(v);
    }

    /*
       Биндинг содержимого обьекта задания в вьюшку
     */
    @Override
    public void onBindRealmViewHolder(ViewHolderTask viewHolderTask, int position) {
        viewHolderTask.bind(realmResults.get(position));
    }

    /*
       При свайпе в сторону будет происходить вызов удаления задания
     */
    @Override
    public void onItemSwipedDismiss(int position) {
        super.onItemSwipedDismiss(position);
        changeTaskStatusListener.taskDelete();
    }

    public interface ChangeTaskStatusListener {
        //Изменение состояния задания
        void statusChanged(boolean status);

        //Удаление задания
        void taskDelete();
    }

    abstract class BaseViewHolder extends RealmViewHolder {
        BaseViewHolder(View view) {
            super(view);
        }

        abstract void clear();

        abstract void bind(Task task);
    }

    class ViewHolderTask extends BaseViewHolder {
         /*
         * @BindView, @OnClick etc... это аннотации библиотеки Butterknife
         * Butterknife позволяет уменьшить количество кода за счет кодогенерации на этапе сборки
         * проекта.
         */

        @BindView(R.id.title_text_task_item_view)
        TextView titleTextView;

        @BindView(R.id.body_text_task_item_view)
        TextView bodyTextView;

        @BindView(R.id.date_created_text_task_item_view)
        TextView dateCreatedTextView;

        @BindView(R.id.status_task_item_check_box)
        AppCompatCheckBox changeStatusButton;


        ViewHolderTask(View itemView) {
            super(itemView);
            //Подключение ButterKnife
            ButterKnife.bind(this, itemView);
        }

        //Очистка ViewHolder
        @Override
        void clear() {
            titleTextView.setText("");
            bodyTextView.setText("");
            dateCreatedTextView.setText("");
            changeStatusButton.setOnCheckedChangeListener(null);
            changeStatusButton.setChecked(false);
        }

        //Биндинг содержимого task во ViewHolder
        @Override
        void bind(final Task task) {
            ((CardView) itemView).setCardBackgroundColor(task.getColor());
            titleTextView.setText(task.getTitle());
            bodyTextView.setText(task.getBody());
            dateCreatedTextView.setText(DateUtils.getRelativeDateTimeString(getContext(), task.getDateCreated().getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0));
            changeStatusButton.setOnCheckedChangeListener(null);
            changeStatusButton.setChecked(task.getStatus());
            changeStatusButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, final boolean status) {
                    changeTaskStatusListener.statusChanged(status);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            task.setStatus(status);
                        }
                    });
                }
            });
        }
    }
}
