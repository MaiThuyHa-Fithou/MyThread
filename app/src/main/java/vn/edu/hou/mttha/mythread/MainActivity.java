package vn.edu.hou.mttha.mythread;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtCount;
    Button btnStart, btnOpen;
    //khai bao 1 doi tuong Handler de xu ly
    Handler myHandler ;
    boolean isCounting;
    //khai bao cac cong viec can thuc hien trong handler
    private static final int MSG_UPDATE_COUNT =100;
    private static final int MSG_UDATE_DONE=101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getViews();
        listenerHandler();


    }

    private void getViews(){
        txtCount = findViewById(R.id.txtCount);
        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);
        btnOpen = findViewById(R.id.btnOpen);
        btnOpen.setOnClickListener(this);

    }

    //xu ly su kien handlerlistener
    private void listenerHandler(){
        //khoi tao doi tuong handler de chuan bi cho viec update ui
        myHandler =new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                //xu ly cac message nhan duoc o message pool
                if (msg.what==MSG_UPDATE_COUNT){
                    //todo
                    isCounting=true;
                    //cap nhat giao dien ui
                    int count = msg.getData().getInt("count");
                    txtCount.setText(""+count);
                }else if(msg.what==MSG_UDATE_DONE){
                    //todo
                    isCounting=false;
                    txtCount.setText("DONE!");
                }
                super.handleMessage(msg);
            }
        };
    }
    //tao mot luong thread de thuc hien viec tang bien count
    private void countNumber(){
        //khoi tao mot luong thread background de thuc hien hanh dong tang so dem
        new Thread(new Runnable() {
            @Override
            public void run() {
                //thuc hien thay doi so luong dem
                for (int i=0; i<=10;i++){
                    //khai bao mot message de chua du lieu day vao message pool
                    Message message = new Message();
                    message = myHandler.obtainMessage();
                    message.what=MSG_UPDATE_COUNT;
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", i);
                    message.setData(bundle);
                    //su dung doi tuong handler de day message vao message pool
                    myHandler.sendMessage(message);
                    try {
                        //thoi gian ngu cua thread sau moi lan lap lai
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    //sau 10 lan se ket thuc cong viec cap nhat so dem
                    myHandler.sendEmptyMessage(MSG_UDATE_DONE);
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.btnStart) {
            if (!isCounting)
                countNumber();

        }
        else if(view.getId()==R.id.btnOpen){
            //mo activity moi - ProgressActivity
                Intent intent = new Intent(MainActivity.this, ProgressActivity.class);
                startActivity(intent);
            }
    }
}