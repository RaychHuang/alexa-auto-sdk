package com.amazon.sampleapp.climate;

import android.content.Context;

import com.amazon.sampleapp.GsonUtils;
import com.amazon.sampleapp.climate.bean.ClimateData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import io.reactivex.Completable;

class ClimateDataLocalSource {
    private final String mDirPath;
    private final String mFilePath;

    ClimateDataLocalSource(Context context) {
        this.mDirPath = context.getExternalFilesDir(null) + File.separator + "climate";
        this.mFilePath = mDirPath + File.separator + "settings.json";
    }

    ClimateData readClimateData() throws IOException {
        File file = new File(mFilePath);
        ClimateData data = null;
        if (file.exists()) {
            try (InputStream inStream = new FileInputStream(file)) {
                try (InputStreamReader isr = new InputStreamReader(inStream)) {
                    data = GsonUtils.toObject(isr, ClimateData.class);
                }
            }
        }
        if (data == null) {
            data = ClimateData.createDefault();
        }
        return data;
    }

    Completable writeClimateDataRx(final ClimateData data) {
        return Completable.create(emitter -> {
            ClimateDataLocalSource.this.writeClimateData(data);
            emitter.onComplete();
        });
    }

    void writeClimateData(ClimateData data) throws IOException {
        if (data == null) {
            return;
        }
        String json = GsonUtils.toJsonString(data);
        File dir = new File(mDirPath);
        File file = new File(mFilePath);
        if (file.exists() || ((dir.exists() || dir.mkdir()) && file.createNewFile())) {
            try (OutputStream outStream = new FileOutputStream(file, false)) {
                outStream.write(json.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
