package com.shengtu.yunjihome.net.weex;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.shengtu.yunjihome.LYApplication;

public class WXHttpManager {

  private static WXHttpManager wxHttpManager;
  private WXOkHttpDispatcher mOkHttpDispatcher;
  private Handler mHandler = new Handler(new Handler.Callback() {

    @Override
    public boolean handleMessage(Message msg) {
      WXHttpTask httpTask = (WXHttpTask) msg.obj;
      if (httpTask == null || httpTask.requestListener == null) {
        return true;
      }
      WXHttpResponse response = httpTask.response;
      if(response == null){
        Toast.makeText(LYApplication.getContext(), "网络出现错误，请重新访问", Toast.LENGTH_SHORT).show();
        return false;
      }
      if(response.code == 304){
        httpTask.requestListener.onByCache(httpTask);
      } else if (response == null || response.code >= 300) {
        httpTask.requestListener.onError(httpTask);
      } else {
        httpTask.requestListener.onSuccess(httpTask);
      }
      return true;
    }
  });

  private WXHttpManager() {
    mOkHttpDispatcher = new WXOkHttpDispatcher(mHandler);
  }

  public static WXHttpManager getInstance() {
    if (wxHttpManager == null) {
      wxHttpManager = new WXHttpManager();
    }
    return wxHttpManager;
  }

  public void sendRequest(WXHttpTask httpTask) {
    mOkHttpDispatcher.dispatchSubmit(httpTask);
  }
}