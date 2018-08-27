

public class WXHttpManager2 {

  private static WXHttpManager2 wxHttpManager;
  private WXOkHttpDispatcher2 mOkHttpDispatcher;
  private Handler mHandler = new Handler(new Handler.Callback() {

    @Override
    public boolean handleMessage(Message msg) {
      WXHttpTask httpTask = (WXHttpTask) msg.obj;
      if (httpTask == null || httpTask.requestListener == null) {
        return true;
      }
      WXHttpResponse response = httpTask.response;
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

  private WXHttpManager2() {
    mOkHttpDispatcher = new WXOkHttpDispatcher2(mHandler);
  }

  public static WXHttpManager2 getInstance() {
    if (wxHttpManager == null) {
      wxHttpManager = new WXHttpManager2();
    }
    return wxHttpManager;
  }

  public void sendRequest(WXHttpTask httpTask) {
    mOkHttpDispatcher.dispatchSubmit(httpTask);
  }
}
