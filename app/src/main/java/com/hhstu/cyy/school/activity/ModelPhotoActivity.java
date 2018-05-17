package com.hhstu.cyy.school.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hhstu.cyy.school.Application.MineApplication;
import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.fragment.Tab1Fragment;
import com.hhstu.cyy.school.fragment.Tab2Fragment;
import com.hhstu.cyy.school.fragment.Tab3Fragment;
import com.hhstu.cyy.school.fragment.Tab4Fragment;
import com.hhstu.cyy.school.test.MultiPartStringRequest;
import com.hhstu.cyy.school.tool.FileUtil;
import com.hhstu.cyy.school.tool.ImageUtils;
import com.hhstu.cyy.school.tool.StringUtils;
import com.hhstu.cyy.school.tool.Utils;
import com.hhstu.cyy.school.tool.VolleyErrorHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ModelPhotoActivity extends BaseActivity {

    private NetworkImageView iv_body_card, iv_student_card, iv_mark_order, iv_temp;

    private String[] photo_name = new String[]{"1iv_body_card.jpg", "2iv_student_card.jpg", "3iv_mark_order.jpg"};
    private int[] tempint = new int[]{1, 2, 3};
    private TreeMap<String, Integer> order = new TreeMap<>();
    private String file_name = "";
    private int file_int;
    private String[] items = new String[]{"选择本地图片", "拍照"};
    TreeMap<String, File> files = new TreeMap<>();
    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreinfo);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_body_card).setOnClickListener(this);
        findViewById(R.id.iv_student_card).setOnClickListener(this);
        findViewById(R.id.iv_mark_order).setOnClickListener(this);

        iv_body_card = (NetworkImageView) findViewById(R.id.iv_body_card);
        iv_body_card.setDefaultImageResId(R.mipmap.photo_carmer);
        iv_body_card.setErrorImageResId(R.mipmap.photo_carmer);
        iv_body_card.setImageUrl(" ", MineApplication.getInstance().getImageLoader());
        iv_body_card.setScaleType(ImageView.ScaleType.FIT_CENTER);

        iv_student_card = (NetworkImageView) findViewById(R.id.iv_student_card);
        iv_student_card.setDefaultImageResId(R.mipmap.photo_carmer);
        iv_student_card.setErrorImageResId(R.mipmap.photo_carmer);
        iv_student_card.setImageUrl(" ", MineApplication.getInstance().getImageLoader());
        iv_student_card.setScaleType(ImageView.ScaleType.FIT_CENTER);

        iv_mark_order = (NetworkImageView) findViewById(R.id.iv_mark_order);
        iv_mark_order.setDefaultImageResId(R.mipmap.photo_carmer);
        iv_mark_order.setErrorImageResId(R.mipmap.photo_carmer);
        iv_mark_order.setImageUrl(" ", MineApplication.getInstance().getImageLoader());
        iv_mark_order.setScaleType(ImageView.ScaleType.FIT_CENTER);

        TextView tv_action = (TextView) findViewById(R.id.tv_action);
        tv_action.setOnClickListener(this);
        tv_action.setText("保存");
        ((TextView) findViewById(R.id.tv_title)).setText("身份信息");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                setResult(11);
                finish();
                break;
            case R.id.tv_action:
                String order = "";
                if (files.get(photo_name[0]) != null) {
                    order = "1";
                }
                if (files.get(photo_name[1]) != null) {
                    if (Utils.isEmpty(order)) {
                        order = "2";
                    } else {
                        order = order + ",2";
                    }
                }
                if (files.get(photo_name[2]) != null) {
                    if (Utils.isEmpty(order)) {
                        order = "3";
                    } else {
                        order = order + ",3";
                    }
                }
                final HashMap<String, String> params = new HashMap<>();
                params.put("MemberID", "143");
                if (!Utils.isEmpty(order)) {
                    params.put("order", order);
                }
                dialog.show();
                String URL_BASE = "http://xmyapi.dz.palmapp.cn/";
                String URL_USER_UPDATEUSER = "/user/UpdateUser/";
                String uri = URL_BASE + URL_USER_UPDATEUSER;
                addPutUploadFileRequest(uri, files, params);
                break;

            case R.id.iv_body_card:
                iv_temp = iv_body_card;
                file_name = photo_name[0];
                file_int = tempint[0];
                change();
                break;
            case R.id.iv_student_card:
                iv_temp = iv_student_card;
                file_name = photo_name[1];
                file_int = tempint[1];
                change();
                break;
            case R.id.iv_mark_order:
                iv_temp = iv_mark_order;
                file_int = tempint[2];
                file_name = photo_name[2];
                change();
                break;
        }
    }

    private void change() {
        new AlertDialog.Builder(getContext())
                .setTitle("设置头像")
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToSelectPicture(which);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private String theLarge, theThumbnail;
    private File imgFile;

    private void goToSelectPicture(int position) {
        switch (position) {
            case 0:
                Intent intent;
                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "选择图片"),
                            IMAGE_REQUEST_CODE);
                } else {
                    intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "选择图片"),
                            IMAGE_REQUEST_CODE);
                }
                break;
            case 1:
                // 判断是否挂载了SD卡
                String savePath = "";
                String storageState = Environment.getExternalStorageState();
                if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                    savePath = Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/school/Camera/";
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

                theLarge = savePath + fileName;// 该照片的绝对路径

                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent,
                        CAMERA_REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent imageReturnIntent) {
        if (resultCode != Activity.RESULT_OK)
            return;
        new Thread() {
            private String selectedImagePath;

            @Override
            public void run() {
                Bitmap bitmap = null;
                if (requestCode == IMAGE_REQUEST_CODE) {
                    if (imageReturnIntent == null)
                        return;
                    Uri selectedImageUri = imageReturnIntent.getData();
                    if (selectedImageUri != null) {
                        selectedImagePath = ImageUtils.getImagePath(
                                selectedImageUri, ModelPhotoActivity.this);
                    }

                    if (selectedImagePath != null) {
                        theLarge = selectedImagePath;
                    } else {
                        bitmap = ImageUtils.loadPicasaImageFromGalley(
                                selectedImageUri, ModelPhotoActivity.this);
                    }

                    if (Utils.isMethodsCompat(android.os.Build.VERSION_CODES.ECLAIR_MR1)) {
                        String imaName = FileUtil.getFileName(theLarge);
                        if (imaName != null)
                            bitmap = ImageUtils.loadImgThumbnail(ModelPhotoActivity.this,
                                    imaName,
                                    MediaStore.Images.Thumbnails.MICRO_KIND);
                    }
                    if (bitmap == null && !StringUtils.isEmpty(theLarge))
                        bitmap = ImageUtils
                                .loadImgThumbnail(theLarge, 100, 100);
                } else if (requestCode == CAMERA_REQUEST_CODE) {
                    // 拍摄图片
                    if (bitmap == null && !StringUtils.isEmpty(theLarge)) {
                        bitmap = ImageUtils
                                .loadImgThumbnail(theLarge, 100, 100);
                    }
                }

                if (bitmap != null) {// 存放照片的文件夹
                    String savePath = Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/school/Camera/";
                    File savedir = new File(savePath);
                    if (!savedir.exists()) {
                        savedir.mkdirs();
                    }
                    String largeFileName = FileUtil.getFileName(theLarge);
                    String largeFilePath = savePath + largeFileName;
                    // 判断是否已存在缩略图
                    if (largeFileName.startsWith("thumb_") && new File(largeFilePath).exists()) {
                        theThumbnail = largeFilePath;
                        imgFile = new File(theThumbnail);
                    } else {
                        // 生成上传的800宽度图片
                        String thumbFileName = "thumb_" + largeFileName;
                        theThumbnail = savePath + thumbFileName;
                        if (new File(theThumbnail).exists()) {
                            imgFile = new File(theThumbnail);
                        } else {
                            try {
                                // 压缩上传的图片
                                ImageUtils.createImageThumbnail(getContext(),
                                        theLarge, theThumbnail, 800, 80);
                                imgFile = new File(theThumbnail);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    // 保存动弹临时图片
                    // ((AppContext) getApplication()).setProperty(
                    // tempTweetImageKey, theThumbnail);

                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && msg.obj != null) {
                // 显示图片
                iv_temp.setImageBitmap((Bitmap) msg.obj);
                files.put(file_name, imgFile);
                order.put(file_name, file_int);
            }
        }
    };

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     */
    private void getImageToView(Intent data) {
        try {
            Bundle extras = data.getExtras();
            if (extras != null) {
                final Bitmap photo = extras.getParcelable("data");
                Drawable drawable = new BitmapDrawable(photo);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(11);
        finish();
    }

    public void addPutUploadFileRequest(final String url, final Map<String, File> files, final Map<String, String> params) {
        if (null == url) {
            return;
        }
        MultiPartStringRequest multiPartRequest = new MultiPartStringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("flag") && jsonObject.getInt("flag") == 0) {
                        Utils.showToast(getContext(), jsonObject.getString("msg"));
                    } else {
                        Utils.showToast(getContext(), "保存成功");
                        setResult(11);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Utils.showToast(getContext(), VolleyErrorHelper.getMessage(error, getContext()));
            }
        }) {
            @Override
            public Map<String, File> getFileUploads() {
                return files;
            }

            @Override
            public Map<String, String> getStringUploads() {
                return params;
            }
        };
        Log.i(TAG, " volley put : uploadFile " + url);
        MineApplication.getInstance().addToRequestQueue(multiPartRequest, TAG);
    }
}
