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
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import name.sportivka.mvpdiexample.R;
import name.sportivka.mvpdiexample.model.Task;

/**
 * Created by daniil on 28.11.16.
 */

public class TaskListAdapter extends RealmBasedRecyclerViewAdapter<Task, TaskListAdapter.ViewHolderTask> {
    private ChangeTaskStatusListener changeTaskStatusListener;


    public TaskListAdapter(Context context, ChangeTaskStatusListener changeTaskStatusListener, RealmResults realmResults, boolean automaticUpdate, boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
        this.changeTaskStatusListener = changeTaskStatusListener;
    }

    @Override
    public void onViewRecycled(RealmViewHolder holder) {
        super.onViewRecycled(holder);
        ((ViewHolderTask) holder).clear();
    }

    @Override
    public ViewHolderTask onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.task_item_view, viewGroup, false);
        return new ViewHolderTask(v);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolderTask viewHolderTask, int position) {
        viewHolderTask.bind(realmResults.get(position));
    }

    @Override
    public void onItemSwipedDismiss(int position) {
        changeTaskStatusListener.taskDelete(position, realmResults);
    }

    public interface ChangeTaskStatusListener {
        void statusChanged(boolean status, Task task);

        void taskDelete(int position, RealmResults<Task> realmResults);
    }

    abstract class BaseViewHolder extends RealmViewHolder {
        BaseViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        abstract void clear();

        abstract void bind(Task task);
    }

    class ViewHolderTask extends BaseViewHolder {

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
        }

        @Override
        void clear() {
            titleTextView.setText("");
            bodyTextView.setText("");
            dateCreatedTextView.setText("");
            changeStatusButton.setOnCheckedChangeListener(null);
            changeStatusButton.setChecked(false);
        }

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
                    changeTaskStatusListener.statusChanged(status, task);
                }
            });
        }
    }
}
