package com.linpeng.icamera.widgets;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.linpeng.icamera.R;

/**
 * Created by Lin on 2015/8/11.
 */
public class KMDialog {
    private static ProgressDialog mpDialog;
    private static TextView title, msg1;
    private static Button bt1, bt2;

    /**
     * @author Lin dialog 接口
     */
    public interface IDiaolog {
        public void toDo();
    }

    /**
     * @param title_text
     * @param msg1_text
     * @param bt1_text
     * @param bt2_text
     * @param toDoInterface
     */
    public static void showDialog(Context context, String title_text, String msg1_text,String bt1_text, String bt2_text,final IDiaolog toDoInterface) {
        // 获取自定义布局
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.km_dialog, null);
        title = (TextView) view.findViewById(R.id.dialog_title);
        msg1 = (TextView) view.findViewById(R.id.dialog_phone);
        bt1 = (Button) view.findViewById(R.id.bt1_dialog);
        bt2 = (Button) view.findViewById(R.id.bt2_dialog);
        title.setText(title_text);
        msg1.setText(msg1_text);
        bt1.setText(bt1_text);
        bt2.setText(bt2_text);

        // 创建对话框并显示
        final Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.dimAmount = .7f;
        params.alpha = 0.9f;
        window.setAttributes(params);
        dialog.show();
        bt1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toDoInterface.toDo();
                dialog.dismiss();
            }
        });
    }

    public static ProgressDialog ProgressDialog(Context context,String msg) {
        mpDialog = new ProgressDialog(context);
        mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
        mpDialog.setMessage(msg);
        mpDialog.setIndeterminate(false);// 设置进度条是否为不明确
        mpDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
        return mpDialog;
    }

}
