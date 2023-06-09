package com.ffapp.ffxx.ffplayers.comman;

import android.widget.RelativeLayout;

import java.io.File;

public interface OnItemRestoreListener {
    void OnClick(int postion);
    void OnItemClick(int pos, File file, RelativeLayout layout);
}
