package com.demo.androidapp.util;

import android.util.Log;

import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.utils.AndroidNetwork;

import org.json.JSONObject;

import java.io.File;

public class PictureUploadUtil {

    private static final String ACCESS_KEY = "eMtqpp7xxLyHj-xmNHiUOGvHPgfcXG0CPy3cz8LD";

    private static final String SECRET_Key = "pm_TvO87fjZAEuJPAdxXfTiYWbl7o0je5jLLH7uE";

    private static UploadManager uploadManager;

    private static Configuration config = new Configuration.Builder()
            .connectTimeout(90)              // 链接超时。默认90秒
            .useHttps(true)                  // 是否使用https上传域名
            .useConcurrentResumeUpload(true) // 使用并发上传，使用并发上传时，除最后一块大小不定外，其余每个块大小固定为4M，
            .concurrentTaskCount(3)          // 并发上传线程数量为3
            .responseTimeout(90)             // 服务器响应超时。默认90秒
            .recorder(null)              // recorder分片上传时，已上传片记录器。默认null
            .recorder(null, null)      // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
            .zone(FixedZone.zone0)           // 设置区域，不指定会自动选择。指定不同区域的上传域名、备用域名、备用IP。
            .build();
    // 重用uploadManager。一般地，只需要创建一个uploadManager对象
    public static void upLoadFile(File file,String key,String token) {
        uploadManager = new UploadManager(config);
        uploadManager.put(file, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res 包含 hash、key 等信息，具体字段取决于上传策略的设置
                        if(info.isOK()) {
                            Log.i("qiniu", "Upload Success");
                        } else {
                            Log.i("qiniu", "Upload Fail");
                            //如果失败，这里可以把 info 信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                    }
                }, null);
    }

}
