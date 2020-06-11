package com.example.probamap;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.icu.text.IDNA;
import android.location.Location;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

import static android.provider.SyncStateContract.Helpers.update;


public class MainActivity extends AppCompatActivity {

    public static String fioDB,emailDB,phoneDB,zoneDB,profDB;
    ListView lstTask;
    ArrayAdapter<String> mAdapter;
    DbHelper dbHelper;
    Intent intent,intent1,intent2,intent3,intent4;
    private static Socket clientSocket; //сокет для общения
    private static BufferedReader reader; // нам нужен ридер читающий с консоли, иначе как
    // мы узнаем что хочет сказать клиент?
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        lstTask= (ListView) findViewById(R.id.lstTask);
        loadTaskList();

    }

    private void loadTaskList() {
        ArrayList<String> taskList=dbHelper.getTaskList();
        if(mAdapter==null){
            mAdapter=new ArrayAdapter<String>(this,R.layout.row,R.id.task_title,taskList);
            lstTask.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);

        Drawable icon= menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_add_task:
                final EditText taskEditText=new EditText(this);
                AlertDialog dialog=new AlertDialog.Builder(this).setTitle("Add New Task").setMessage("What do you wnat next").setView(taskEditText).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task =String.valueOf(taskEditText.getText());
                        dbHelper.insertNewTask(task);
                        loadTaskList();
                    }
                }).setNegativeButton("Cancel",null)
                .create();
                dialog.show();
                return true;
            case R.id.coor:
                intent1 = new Intent(MainActivity.this, CoorActivity.class);
                startActivity(intent1);
            case R.id.info_personal:
                intent2 = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent2);
            case R.id.map:
                intent3 = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent3);
            case R.id.upload:

                try {
                    try {
                        // адрес - локальный хост, порт - 4004, такой же как у сервера
                        clientSocket=new Socket("188.",0); // этой строкой мы запрашиваем
                        //  у сервера доступ на соединение
                        reader = new BufferedReader(new InputStreamReader(System.in));
                        // читать соообщения с сервера
                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        // писать туда же
                        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));


                        out.write(InfoActivity.fioDB+' '+InfoActivity.emailDB+' '+InfoActivity.phoneDB+' '+InfoActivity.zoneDB+' '+InfoActivity.profDB+(CoorActivity.lat)+' '+(CoorActivity.lon)); // отправляем сообщение на сервер
                        out.flush();
                        String serverWord = in.readLine(); // ждём, что скажет сервер
                        System.out.println(serverWord); // получив - выводим на экран
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally { // в любом случае необходимо закрыть сокет и потоки
                        System.out.println("Клиент был закрыт...");
                        clientSocket.close();
                        in.close();
                        out.close();
                    }
                } catch (IOException e) {
                    System.err.println(e);
                }




        }
        return super.onOptionsItemSelected(item);
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());

                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return null;
    }

    public void deleteTask(View view){
        View parent =(View)view.getParent();
        TextView taskTextView=(TextView)findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        dbHelper.deleteTask(task);
        loadTaskList();
    }

    public void acceptTask(View view){
        View parent =(View)view.getParent();
        TextView taskTextView=(TextView)findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        String str="Вы выполняете задание:"+task+"!";
        Toast toast = Toast.makeText(getApplicationContext(),str
                , Toast.LENGTH_LONG);
        toast.show();


        dbHelper.deleteTask(task);

        intent = new Intent(MainActivity.this, TimerActivity.class);
        startActivity(intent);
        loadTaskList();
    }

    public void infoPerson(View view){
        intent = new Intent(MainActivity.this, InfoActivity.class);
        startActivity(intent);
    }

    public void infoCoor(View view){
        intent = new Intent(MainActivity.this, CoorActivity.class);
        startActivity(intent);
    }

    public void infoMap(View view){
        intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
    }



}
