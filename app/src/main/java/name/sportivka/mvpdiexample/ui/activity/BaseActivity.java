package name.sportivka.mvpdiexample.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by daniil on 28.11.16.
 */


public class BaseActivity extends AppCompatActivity {

    Unbinder unbinder;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //Подключаем аннотированные ui-компоненты
        unbinder = ButterKnife.bind(this);
    }


    @Override
    protected void onDestroy() {
        //Отбиндиваем аннотированные ui-компоненты
        unbinder.unbind();

        super.onDestroy();
    }

    /*
     Открыть ссылку в браузере.
    */
    public void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }


}