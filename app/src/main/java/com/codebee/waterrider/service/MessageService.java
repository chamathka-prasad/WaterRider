package com.codebee.waterrider.service;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codebee.waterrider.R;

public class MessageService {

    private Context context;
    private String message;
    private boolean status;

    private boolean back;
    private ProgressBar progressBar;
    int prograssStatus = 0;
    int time = 40;

    public MessageService(Context context, String message, boolean status, boolean back) {
        this.context = context;
        this.message = message;
        this.status = status;
        this.back = back;
    }

    private Handler handler = new Handler();

    public MessageService(Context context, String message, boolean status) {
        this.context = context;
        this.message = message;
        this.status = status;
    }

    public Thread setMessage() {

        Dialog dialog = new Dialog(context);
        dialog.setContentView(com.codebee.waterrider.R.layout.message_dialog_custom);

        TextView text = (TextView) dialog.findViewById(R.id.messageText);
        text.setText(message);


        ImageView image2 = (ImageView) dialog.findViewById(R.id.imageView3);
        image2.setImageResource(R.drawable.cancel_icon);


        ImageView image = (ImageView) dialog.findViewById(R.id.imageViewMessage1);

//        Picasso.get().load(R.drawable.baseline_warning_amber_24)
//                .resize(40,40)
//                .into(image);

        if (status == true) {
            image.setImageResource(R.drawable.success_icon);
            time = 15;

        } else {
            image.setImageResource(R.drawable.baseline_warning_amber_24);
            time = 40;
        }
        dialog.findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = 40;
                dialog.cancel();

            }
        });

        progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar2);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (prograssStatus <= 100) {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(prograssStatus);

                        }
                    });
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    prograssStatus++;
                }
                dialog.cancel();
                prograssStatus = 0;
                time = 40;

            }
        });
        dialog.show();
        t.start();


        return t;
    }
}
