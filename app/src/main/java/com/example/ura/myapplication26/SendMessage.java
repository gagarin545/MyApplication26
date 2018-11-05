package com.example.ura.myapplication26;


import android.widget.EditText;

import static com.example.ura.myapplication26.Variable.writer;


public class SendMessage implements Runnable {
    private static String message;

    public SendMessage(EditText text) {
        super();
        message = String.valueOf(text.getText());
    }

    public SendMessage(String text) {
        super();
        message = String.valueOf(text);
    }

    public void run() {
        try {
            if ( message.length() != 0 ) {
                writer.println(message);
                writer.flush();
                // Log.e(debug, "SM->Посылаю: " + message);
            }
        } catch (Exception ex) { ex.printStackTrace(); } // Log.e(debug, "SendMessage Ошибка записи");      }
    }
}
