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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.View.RoundImageView;
import com.hhstu.cyy.school.View.RoundNetImageView;
import com.hhstu.cyy.school.tool.Constants;
import com.hhstu.cyy.school.tool.FileUtil;
import com.hhstu.cyy.school.tool.ImageUtils;
import com.hhstu.cyy.school.tool.StringUtils;
import com.hhstu.cyy.school.tool.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/26.
 */
public class UserInfoActivity extends BaseActivity {
    private EditText et_account, et_student_id;
    private TextView tv_phone;
    private RoundImageView iv_icon;
    //上传图片
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
        setContentView(R.layout.activity_userinfo);
        findViewById(R.id.iv_back).setOnClickListener(this);
        TextView tv_action = (TextView) findViewById(R.id.tv_action);
        iv_icon = (RoundImageView) findViewById(R.id.iv_icon);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        et_account = (EditText) findViewById(R.id.et_account);
        et_student_id = (EditText) findViewById(R.id.et_student_id);
        iv_icon.setImageResource(R.mipmap.ic_launcher);
        tv_action.setOnClickListener(this);
        tv_action.setText("保存");
        ((TextView) findViewById(R.id.tv_title)).setText("个人资料");
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_icon).setOnClickListener(this);
        findViewById(R.id.view_resetpassword).setOnClickListener(this);
        findViewById(R.id.view_out).setOnClickListener(this);
        initData();
    }

    private void initData() {
        tv_phone.setText(Utils.getLoginTel(getContext()));
        et_account.setText(Utils.getLoginName(getContext()));
        et_student_id.setText(Utils.getLoginNumber(getContext()));
        protraitPath = Utils.getLoginImage(getContext());
        if (!Utils.isEmpty(protraitPath)) {
            protraitFile = new File(protraitPath);
            if (!StringUtils.isEmpty(protraitPath) && protraitFile.exists()) {
                protraitBitmap = ImageUtils.loadImgThumbnail(protraitPath, 200, 200);
            } else {
                Utils.showToast(getContext(), "图像不存在，可能已被删除");
            }
            if (protraitBitmap != null) {
                iv_icon.setImageBitmap(protraitBitmap);
            }
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_action:
                try {
                    saveData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_icon:
                change();
                break;
            case R.id.view_out:
                //退出登录
                new AlertDialog.Builder(this).setTitle("提示")
                        .setMessage("确定退出吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“确认”后的操作
                                SharedPreferences.Editor sp_ed = getSharedPreferences(Constants.SP_CONFIG, Context.MODE_PRIVATE).edit();
                                sp_ed.putString(Constants.SP_KEU_USER_LOGIN_INFO, null);
                                sp_ed.putBoolean(Constants.SP_KEY_ISLOGIN, false);
                                sp_ed.commit();
                                setResult(RESULT_OK);
                                finish();
                            }
                        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();
                break;
            case R.id.view_resetpassword:
                intent = new Intent(getContext(), ResetPasswordActivity.class);
                startActivityForResult(intent, 11);
                break;
        }
    }

    private void saveData() throws JSONException {
        SQLiteDatabase db2 = helper.getReadableDatabase();
        Cursor cursor = db2.query("person", null, "_id=" + Utils.getLoginId(getContext()), null, null, null, null);
        if (cursor.getCount() == 0) {
            Utils.showToast(getContext(), "会员失效");
        } else {
            while (cursor.moveToNext()) {
                //根据字段名称  得到下标
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                ContentValues values2 = new ContentValues();
                values2.put("name", et_account.getText().toString());
                values2.put("number", et_student_id.getText().toString());
                values2.put("image", protraitPath);
                /**
                 * 参数1:表名
                 * 参数2：要修改的值
                 * 参数3：where 条件语句  _id = ?
                 * 参数4：条件语句的值   new String []{"1"}
                 */
                int num = db2.update("person", values2, "_id = ?", new String[]{String.valueOf(id)});
                if (num > 0) {
                    Utils.showToast(getContext(), "保存成功");
                    JSONObject object = new JSONObject(getSharedPreferences(Constants.SP_CONFIG, Context.MODE_PRIVATE).getString(Constants.SP_KEU_USER_LOGIN_INFO, null));
                    object.put("name", et_account.getText().toString());
                    object.put("number", et_student_id.getText().toString());
                    object.put("image", protraitPath);
                    SharedPreferences.Editor editor = getSharedPreferences(Constants.SP_CONFIG, Context.MODE_PRIVATE).edit();
                    editor.putString(Constants.SP_KEU_USER_LOGIN_INFO, object.toString());
                    editor.commit();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Utils.showToast(getContext(), "保存失败");
                }

            }
        }
        db2.close();
    }

    private void change() {
        new AlertDialog.Builder(getContext())
                .setTitle("设置头像")
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
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
}
