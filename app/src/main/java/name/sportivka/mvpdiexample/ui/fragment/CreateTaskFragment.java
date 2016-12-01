package name.sportivka.mvpdiexample.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.rtugeek.android.colorseekbar.ColorSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import name.sportivka.mvpdiexample.R;

/**
 * Created by daniil on 28.11.16.
 */

public class CreateTaskFragment extends DialogFragment {

    /*
     * @BindView, @OnClick etc... это аннотации библиотеки Butterknife
     * Butterknife позволяет уменьшить количество кода за счет кодогенерации на этапе сборки
     * проекта.
     * В классе BaseActivity описано подключение и отключение библиотеки.
     */

    @BindView(R.id.title_task_edit_text)
    EditText titleEditText;

    @BindView(R.id.body_task_edit_text)
    EditText bodyEditText;

    @BindView(R.id.color_task_slider)
    ColorSeekBar colorSeekBar;

    @BindView(R.id.color_choose_task_view)
    View colorChooseTaskView;

    private OnCreateDialogListener onCreateDialogListener;

    //При нажатии кнопки создания задания
    @OnClick(R.id.create_task_button)
    public void onCreateTaskButtonClick() {
        //Получаем данные полей
        boolean cancel = false;
        String title = titleEditText.getText().toString();
        String body = bodyEditText.getText().toString();
        View focusView = null;
        titleEditText.setError(null);
        bodyEditText.setError(null);

        //Проверка полей на их заполненность

        if (TextUtils.isEmpty(title)) {
            titleEditText.setError(getString(R.string.error_empty_field));
            focusView = titleEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(body)) {
            bodyEditText.setError(getString(R.string.error_empty_field));
            focusView = bodyEditText;
            cancel = true;
        }
        if (cancel) {
            if (focusView != null)
                focusView.requestFocus();
            return;
        }

        //Если все хорошо, отправляем колбек на запись задания.
        onCreateDialogListener.onTaskCreated(title, body, colorSeekBar.getColor());
        //Закрываем диалог
        getDialog().dismiss();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.create);
        return dialog;
    }
    //Создание вьшки диалога
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(R.string.create);
        //Отображаем клавиатуру при отображении диалога
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        View view = inflater.inflate(R.layout.create_task_dialog, null);
        ButterKnife.bind(this, view);
        colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int colorBarValue, int alphaBarValue, int color) {
                colorChooseTaskView.setBackgroundColor(color);
            }
        });

        colorSeekBar.setColorBarValue(0);


        return view;
    }

    public void setOnDialogResultListener(OnCreateDialogListener listener) {
        this.onCreateDialogListener = listener;
    }

    public interface OnCreateDialogListener {
        void onTaskCreated(String title, String body, Integer color);
    }

}
