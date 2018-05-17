package com.hhstu.cyy.school.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.hhstu.cyy.school.Application.MineApplication;
import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.tool.Constants;
import com.hhstu.cyy.school.tool.FileUtil;
import com.hhstu.cyy.school.tool.ImageUtils;
import com.hhstu.cyy.school.tool.StringUtils;
import com.hhstu.cyy.school.tool.Utils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * 我的车辆
 * Created by Administrator on 2015/11/25.
 */
public class LossThingaddActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_icon;
    private EditText et_name, et_thing_name, et_unmber, et_address;
    TextView tv_action;
    Intent inintent;

    private Bitmap photo = null;
    private JSONObject object = null;
    private String[] items = new String[]{"选择本地图片", "拍照"};
    private boolean isChangeFace = false;
    private String theLarge;
    private final static int CROP = 200;
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/School/Portrait/";
    private Uri origUri;
    private Uri cropUri;
    private File protraitFile;
    private Bitmap protraitBitmap;
    private String protraitPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loss_thing_add);
        inintent = getIntent();
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_action).setOnClickListener(this);
        findViewById(R.id.iv_icon).setOnClickListener(this);
        findViewById(R.id.up_load_icon).setOnClickListener(this);
        findViewById(R.id.iv_action).setVisibility(View.GONE);
        findViewById(R.id.tv_action).setVisibility(View.VISIBLE);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        iv_icon.setImageResource(R.mipmap.ic_launcher);

        et_name = (EditText) findViewById(R.id.et_name);
        et_thing_name = (EditText) findViewById(R.id.et_thing_name);
        et_unmber = (EditText) findViewById(R.id.et_unmber);
        et_address = (EditText) findViewById(R.id.et_address);
        tv_action = (TextView) findViewById(R.id.tv_action);
        tv_action.setText("保存");
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("添加信息");
        if (inintent.hasExtra("data")) {
            initdata();
        }
    }

    private void initdata() {
        try {
            object = new JSONObject(getIntent().getStringExtra("data"));

            if (object.has("image")) {
                protraitPath = object.getString("image");
                Bitmap b = getBitmap(object.getString("image"));
                if (b == null) {
                    iv_icon.setImageResource(R.mipmap.ic_launcher);
                } else {
                    iv_icon.setImageBitmap(b);
                }
            } else {
                iv_icon.setImageResource(R.mipmap.ic_launcher);
            }
            et_name.setText(object.getString("name"));
            et_thing_name.setText(object.getString("thingName"));
            et_unmber.setText(object.getString("tel"));
            et_address.setText(object.getString("address"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_action:
                try {
                    String name = et_name.getText().toString();
                    String thing_name = et_thing_name.getText().toString();
                    String number = et_unmber.getText().toString();
                    String address = et_address.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        Utils.showToast(getApplicationContext(), "请输入姓名");
                        return;
                    }
                    if (TextUtils.isEmpty(thing_name)) {
                        Utils.showToast(getApplicationContext(), "请输入物品名");
                        return;
                    }
                    if (TextUtils.isEmpty(number)) {
                        Utils.showToast(getApplicationContext(), "请输入电话号");
                        return;
                    }
                    if (TextUtils.isEmpty(address)) {
                        Utils.showToast(getApplicationContext(), "请输入丢失地点");
                        return;
                    }
                    SQLiteDatabase db1 = helper.getReadableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("uid", String.valueOf(Utils.getLoginId(getContext())));
                    values.put("image", protraitPath);
                    values.put("name", name);
                    values.put("thingName", thing_name);
                    values.put("tel", number);
                    values.put("address", address);
                    if (inintent.hasExtra("data")) {
                        int num = 0;
                        num = db1.update("loseThing", values, "_id = ?", new String[]{String.valueOf(object.getInt("_id"))});
                        if (num < 1) {
                            Utils.showToast(getContext(), "操作失败");
                        } else {
                            Utils.showToast(getContext(), "操作成功");
                            setResult(RESULT_OK);
                            finish();
                        }
                    } else {
                        long i = db1.insert("loseThing", null, values);
                        if (i == -1) {
                            //失败
                            Utils.showToast(getContext(), "操作失败");
                        } else {
                            //成功
                            Utils.showToast(getContext(), "操作成功");
                            setResult(RESULT_OK);
                            finish();
                        }
                        /**
                         * 参数1：表名  参数2 ：如果添加的数据是null   null  参数3：添加的数据
                         */
                    }
                    db1.close();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.up_load_icon:
            case R.id.iv_icon:
                change();
                break;
        }
    }

    private void change() {
        new AlertDialog.Builder(this)
                .setTitle("上传图片")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                startImagePick();
                                break;
                            case 1:
                                startTakePhoto();
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 选择图片裁剪
     */
    private void startImagePick() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        }
    }

    private void startTakePhoto() {
        Intent intent;
        // 判断是否挂载了SD卡
        String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/oschina/Camera/";
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        }

        // 没有挂载SD卡，无法保存文件
        if (StringUtils.isEmpty(savePath)) {
            Utils.showToast(getContext(), "无法保存照片，请检查SD卡是否挂载");
            return;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String fileName = "osc_" + timeStamp + ".jpg";// 照片命名
        File out = new File(savePath, fileName);
        Uri uri = Uri.fromFile(out);
        origUri = uri;

        theLarge = savePath + fileName;// 该照片的绝对路径

        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
    }

    // 裁剪头像的绝对路径
    private Uri getUploadTempFile(Uri uri) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(FILE_SAVEPATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            Utils.showToast(getContext(), "无法保存照片，请检查SD卡是否挂载");
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

        // 如果是标准Uri
        if (StringUtils.isEmpty(thePath)) {
            thePath = ImageUtils.getAbsoluteImagePath(this, uri);
        }
        String ext = FileUtil.getFileFormat(thePath);
        ext = StringUtils.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = "osc_crop_" + timeStamp + "." + ext;
        // 裁剪头像的绝对路径
        protraitPath = FILE_SAVEPATH + cropFileName;
        protraitFile = new File(protraitPath);

        cropUri = Uri.fromFile(protraitFile);
        return this.cropUri;
    }

    /**
     * 拍照后裁剪
     *
     * @param data 原始图片
     */
    private void startActionCrop(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("output", this.getUploadTempFile(data));
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP);// 输出图片大小
        intent.putExtra("outputY", CROP);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent imageReturnIntent) {
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
                startActionCrop(origUri);// 拍照后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
                startActionCrop(imageReturnIntent.getData());// 选图后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
                uploadNewPhoto();
                break;
        }
    }

    /**
     * 上传新照片
     */
    private void uploadNewPhoto() {
        // 获取头像缩略图
        if (!StringUtils.isEmpty(protraitPath) && protraitFile.exists()) {
            protraitBitmap = ImageUtils
                    .loadImgThumbnail(protraitPath, 200, 200);
        } else {
            Utils.showToast(getContext(), "图像不存在，上传失败");
        }
        if (protraitBitmap != null) {
            iv_icon.setImageBitmap(protraitBitmap);
            isChangeFace = true;
        }
    }

    @Nullable
    private Bitmap getBitmap(String protraitPath) {
        Bitmap protraitBitmap = null;
        if (!Utils.isEmpty(protraitPath)) {
            File protraitFile = new File(protraitPath);
            if (!StringUtils.isEmpty(protraitPath) && protraitFile.exists()) {
                protraitBitmap = ImageUtils.loadImgThumbnail(protraitPath, 200, 200);
            } else {
                Utils.showToast(getContext(), "图像不存在，可能已被删除");
            }
        }
        return protraitBitmap;
    }

}