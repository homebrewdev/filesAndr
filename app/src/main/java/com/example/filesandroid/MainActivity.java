package com.example.filesandroid;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String LOG_TAG = "myLogs";
    private String FILENAME = "file";

    final String DIR_SD = "MyFiles";
    //private String FILENAME_SD = "file";

    private EditText etext ; //для ввода текстовой инфы для записи в файл
    private EditText editFilename; //для ввода имени файла в\из котрого будет
    // произведено чтение\запись текстовой инфы


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etext = (EditText) findViewById(R.id.editText1);
        editFilename = (EditText) findViewById(R.id.editFilename);
        editFilename.setText(FILENAME);
    }


    //обрабатываем нажатия на кнопки
    public void onclick(View v) throws IOException {
        switch (v.getId()) {
            case R.id.btnWrite:
                writeFile();
                break;
            case R.id.btnRead:
                readFile();
                break;
            case R.id.btnWriteSD:
                writeFileSD();
                break;
            case R.id.btnReadSD:
                readFileSD();
                break;
        }
    }

    //после нажатия на кнопку ЗАПИСАТЬ ФАЙЛ выполняется метод writeFile()
    void writeFile() {
        try {
            //считываем название файла
            FILENAME = editFilename.getText().toString();

            // отрываем поток для записи
            BufferedWriter buffWriter = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILENAME, MODE_PRIVATE)));

            // пишем данные
            // пишем в файл c именем %FILENAME% текстовые данные вытащенные из editText
            buffWriter.write(etext.getText().toString());

            // закрываем поток
            buffWriter.close();

            //выводим результат записи в файл логом
            Log.d(LOG_TAG, "Файл успешно записан.");
            Log.d(LOG_TAG, "Содержимое записанное в файл = " + etext.getText().toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //после нажатия на кнопку "ПРОЧЕСТЬ ФАЙЛ" выполняется метод readFile()
    void readFile() {
        try {
            //считываем название файла
            FILENAME = editFilename.getText().toString();

            // открываем поток для чтения
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));

            String str;
            String finalString = "";//строка котрая аккумулирует все строки котрые мы прочитали из
            //файла %FILENAME%

            TextView textViewOutput = (TextView)findViewById(R.id.textView3);
            // читаем содержимое
            while ((str = buffReader.readLine()) != null) {
                finalString = finalString+str;
                Log.d(LOG_TAG, "\n" + str);
            }

            //отображаем то что прочитали в файле %FILENAME% текстовые данные в поле EditText
            textViewOutput.setText(finalString);

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //после нажатия на кнопку "ЗАПИСАТЬ ФАЙЛ НА SD" выполняется метод writeFileSD()
    void writeFileSD() throws IOException {

        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        //вытаскиваем название директории для SD card
        File sdPath = Environment.getExternalStorageDirectory();

        try {
            //читаем имя файла из поля - filename на устройстве
            FILENAME = editFilename.getText().toString();

            // получаем путь к SD
            //File sdPath = Environment.getExternalStorageDirectory();
            //File sdPath = new File(Environment.DIRECTORY_DCIM);

            // добавляем свой каталог к пути
            //sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
            Log.d(LOG_TAG,"Путь = " + sdPath.toString());

            // создаем каталог
            boolean isFileCreate = sdPath.mkdirs();
            if (isFileCreate) Log.d(LOG_TAG,"File "+sdPath.toString()+" is created!");


            // формируем объект File, который содержит путь к файлу
            File sdFile = new File(sdPath, FILENAME);
            //boolean createNewFile = sdFile.createNewFile();

            Log.d(LOG_TAG,"Файл = " + sdFile.toString());

            // открываем поток для записи
            BufferedWriter buffWriter = new BufferedWriter(new FileWriter(sdFile));

            // пишем данные
            //bw.write("Содержимое файла на SD");
            buffWriter.write(etext.getText().toString());

            // закрываем поток
            buffWriter.close();

            //логгируем что все Окей
            Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath()+" .Все OK! )");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //после нажатия на кнопку "ПРОЧЕСТЬ ФАЙЛ с SD" выполняется метод readFileSD()
    void readFileSD() {
        //читаем имя файла из поля - filename на устройстве
        FILENAME = editFilename.getText().toString();

        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();

        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);

        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME);
        try {
            // открываем поток для чтения
            BufferedReader buffReader = new BufferedReader(new FileReader(sdFile));
            String str = "";
            String finalString = "";
            TextView textViewOutput = (TextView)findViewById(R.id.textView3);
            // читаем содержимое
            while ((str = buffReader.readLine()) != null) {
                finalString = finalString+str;
                Log.d(LOG_TAG, str);
            }
            //выводим в поле Edit то, что прочитали из файла %FILENAME% на SD card
            textViewOutput.setText(finalString);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}