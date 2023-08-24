package com.devinsight.vegiedo.view.mypage;

import android.content.Intent;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ImagePickerUtil {

    // For Fragment
    public static void selectImageFromGallery(Fragment fragment, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        fragment.startActivityForResult(intent, requestCode);
    }

    // For Activity
    public static void selectImageFromGallery(AppCompatActivity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, requestCode);
    }
}


