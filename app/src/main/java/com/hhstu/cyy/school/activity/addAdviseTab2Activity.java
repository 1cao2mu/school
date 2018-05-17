package com.hhstu.cyy.school.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.View.RoundImageView;
import com.hhstu.cyy.school.tool.Data;
import com.hhstu.cyy.school.tool.FileUtil;
import com.hhstu.cyy.school.tool.ImageUtils;
import com.hhstu.cyy.school.tool.StringUtils;
import com.hhstu.cyy.school.tool.Utils;

import org.json.JSONException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class addAdviseTab2Activity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    BottomNavigationBar bottomNavigationBar;
    RoundImageView iv_icon;
    TextView tv_name;
    TextInputLayout til;
    EditText et_content;
    ImageView iv_content;

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
        setContentView(R.layout.activity_add_find_tab1);
        ((TextView) findViewById(R.id.tv_action)).setText("发布");
        ((TextView) findViewById(R.id.tv_title)).setText("新建议");
        findViewById(R.id.tv_action).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        iv_icon = (RoundImageView) findViewById(R.id.iv_icon);
        tv_name = (TextView) findViewById(R.id.tv_name);
        if (Utils.isEmpty(Utils.getLoginName(getContext()))) {
            tv_name.setText("未命名");
        } else {
            tv_name.setText(Utils.getLoginName(getContext()));
        }
        String protraitPath = Utils.getLoginImage(getContext());
        if (!Utils.isEmpty(protraitPath)) {
            if (!StringUtils.isEmpty(protraitPath) && new File(protraitPath).exists()) {
                Bitmap protraitBitmap = ImageUtils.loadImgThumbnail(protraitPath, 200, 200);
                iv_icon.setImageBitmap(protraitBitmap);
            } else {
                Utils.showToast(getContext(), "图像不存在，可能已被删除");
            }
        } else {
            iv_icon.setImageResource(R.mipmap.ic_launcher);
        }

        et_content = (EditText) findViewById(R.id.et_content);
        iv_content = (ImageView) findViewById(R.id.iv_content);
        iv_content.setMinimumHeight(Utils.getScreenWidth(getContext()) - 10);
        iv_content.setMinimumWidth(Utils.getScreenWidth(getContext()) - 10);
        til = (TextInputLayout) findViewById(R.id.til);
        til.setHint("群众的建议是宝贵的,请把你的意见大声说出来吧");
        bottomNavigationBar.unHide();
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.clearAll();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_CLASSIC);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_menu_slideshow, "相册").setActiveColor(R.color.text_black)).setInActiveColor(R.color.text_black)
                .addItem(new BottomNavigationItem(R.drawable.ic_menu_camera, "相机").setActiveColor(R.color.text_black)).setInActiveColor(R.color.text_black)
                .initialise();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_action:
                String content = et_content.getText().toString();
                if (Utils.isEmpty(content)) {
                    Utils.showToast(getContext(), "内容为空,不能发布");
                } else {
                    try {
                        boolean b = Data.addAdviseTab(helper, Utils.getLoginId(getContext()), Utils.getLoginName(getContext()), Utils.getLoginImage(getContext()), protraitPath, content);
                        if (b) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Utils.showToast(getContext(), "操作失败,请重试");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case 0:
                startImagePick();
                break;
            case 1:
                startTakePhoto();
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {
        switch (position) {
            case 0:
                startImagePick();
                break;
            case 1:
                startTakePhoto();
                break;
        }
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
                    .getAbsolutePath() + "/School/Camera/";
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
                    .loadImgThumbnail(protraitPath, 400, 400);
        } else {
            Utils.showToast(getContext(), "图像不存在，上传失败");
        }
        if (protraitBitmap != null) {
            iv_content.setImageBitmap(protraitBitmap);
            isChangeFace = true;
        }
    }


}
