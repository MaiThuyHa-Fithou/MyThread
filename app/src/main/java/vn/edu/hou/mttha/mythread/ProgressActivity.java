package vn.edu.hou.mttha.mythread;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProgressActivity extends AppCompatActivity {

    ProgressBar myProgress;
    Button btnDownload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_progress);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getViews();
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //khoi tao doi tuong myProgressAsynctask
                new MyProgressAsyncTask().execute();
            }
        });
    }

    private void getViews(){
        myProgress = findViewById(R.id.myProgress);
        btnDownload = findViewById(R.id.btnDownload);

    }
    private class MyProgressAsyncTask extends AsyncTask<Void,Integer,String>{

        @Override
        protected String doInBackground(Void... voids) {
            for(int i=0;i<=100;i++){
                //thuc hien cong viec duoi background o day
                publishProgress(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return "DONE!";
        }
        //co the implemment cac phuong thuc con lai neu can

        @Override
        protected void onProgressUpdate(Integer... values) {
           //cap nhat ui sau moi lan thuc thi
            myProgress.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
           //sau khi thuc hien xong, hien thong bao cho nguoi dung
            Toast.makeText(ProgressActivity.this,result,Toast.LENGTH_LONG).show();
        }
    }
}