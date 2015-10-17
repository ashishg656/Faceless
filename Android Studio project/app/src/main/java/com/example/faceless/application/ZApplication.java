package com.example.faceless.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.faceless.R;
import com.example.faceless.utils.NutraBaseImageDecoder;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

public class ZApplication extends Application {

    static ZApplication sInstance;
    private RequestQueue mRequestQueue;
    public static File cacheDir;
    public static final String IMAGE_DIRECTORY_NAME = "Faceless";

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(),
                "Faceless");
        initImageLoader(getApplicationContext());
    }

    public static String getBaseUrl() {
        return "http://instirepo-instirepo.rhcloud.com/faceless/";
    }

    public static synchronized ZApplication getInstance() {
        if (sInstance == null)
            sInstance = new ZApplication();
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, @NonNull String tag) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public String getImageUrl(String imgUrl) {
        StringBuilder builder = new StringBuilder("http://instirepo-instirepo.rhcloud.com/");
        builder.append(imgUrl);
        return builder.toString();
    }

    public static void initImageLoader(Context context) {
        BitmapFactory.Options decodingOptions = new BitmapFactory.Options();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true).cacheInMemory(true)
                .cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.ic_placeholder)
                .decodingOptions(decodingOptions)

                .bitmapConfig(Bitmap.Config.ARGB_8888).build();

        final int memClass = ((ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        final int cacheSize = 1024 * 1024 * memClass / 8;

        System.out.println("Memory cache size" + cacheSize);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(5).memoryCacheSize(cacheSize)
                .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(300)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDecoder(new NutraBaseImageDecoder(true))
                .denyCacheImageMultipleSizesInMemory()
                .defaultDisplayImageOptions(options)
                .tasksProcessingOrder(QueueProcessingType.FIFO).build();

        ImageLoader.getInstance().init(config);
    }
}
