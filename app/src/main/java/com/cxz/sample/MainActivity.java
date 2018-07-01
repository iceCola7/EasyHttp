package com.cxz.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cxz.sample.rxeasyhttp.RxHttpManager;
import com.cxz.sample.rxeasyhttp.callback.OnResultCallBack;
import com.cxz.sample.rxeasyhttp.subscriber.HttpSubscriber;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private HttpSubscriber mHttpSubscriber;
    private Button button;
    private TextView tv_result;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        tv_result = findViewById(R.id.tv_result);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = "";
                tv_result.setText("");
                RxHttpManager.getInstance().getArticlesNoCache(mHttpSubscriber);
            }
        });

        mHttpSubscriber = new HttpSubscriber(new OnResultCallBack<Bean>() {
            @Override
            public void onSuccess(Bean bean) {
                tv_result.setText(bean.toString());
            }

            @Override
            public void onError(int code, String errorMsg) {
                tv_result.setText("onError: Code: " + code + "  errorMsg: " + errorMsg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHttpSubscriber.unSubscribe();
    }
}
