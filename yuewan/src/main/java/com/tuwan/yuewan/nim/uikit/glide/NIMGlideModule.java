package com.tuwan.yuewan.nim.uikit.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.sina.weibo.sdk.utils.LogUtil;
import com.tuwan.yuewan.nim.uikit.NimUIKit;

import java.io.File;
import java.io.InputStream;

/**
 * Created by huangjun on 2017/4/1.
 */
public class NIMGlideModule implements GlideModule {
    private static final String TAG = "NIMGlideModule";

    private static final int M = 1024 * 1024;
    private static final int MAX_DISK_CACHE_SIZE = 256 * M;
    private static DiskCache diskCache = null;

    /**
     * ************************ Disk Cache ************************
     */
    private static synchronized DiskCache getDiskCache() {
        if (diskCache == null) {
            diskCache = createDiskCache();
        }
        return diskCache;
    }

    private static synchronized DiskCache createDiskCache() {
        final Context context = NimUIKit.getContext();
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, context.getPackageName() + "/cache/image/");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return DiskLruCacheWrapper.get(cacheDir, MAX_DISK_CACHE_SIZE);
    }

    /**
     * ************************ Memory Cache ************************
     */

    static void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * ************************ GlideModule override ************************
     */
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                return getDiskCache();
            }
        });

        LogUtil.i(TAG, "NIMGlideModule apply options");
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new AppOkHttpUrlLoader.Factory());
    }
}
