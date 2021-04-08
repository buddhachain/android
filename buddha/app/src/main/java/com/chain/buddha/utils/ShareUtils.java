package com.chain.buddha.utils;

import android.content.Context;
import android.content.Intent;

public class ShareUtils {

    public static void shareText(Context context, String text) {

        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(textIntent, "分享佛系"));
    }


    public static void shareImg(Context context, String text) {

//        String path = context.getResources().getResourcesUri(R.mipmap.ic_me);
//        Intent imageIntent = new Intent(Intent.ACTION_SEND);
//        imageIntent.setType("image/jpeg");
//        imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
//        context.startActivity(Intent.createChooser(imageIntent, "分享"));
    }
}
