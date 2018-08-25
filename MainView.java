private void loadWXfromServiceIn(final String url) {
        showProgressDialog(true);
        if (mInstance != null) {
            mInstance.destroy();
        }

        RenderContainer renderContainer = new RenderContainer(this);
        mContainer.addView(renderContainer);

        mInstance = new WXSDKInstance(this);
        mInstance.setRenderContainer(renderContainer);
        mInstance.registerRenderListener(this);
        mInstance.setNestedInstanceInterceptor(this);
        mInstance.setBundleUrl(url);
        mInstance.setTrackComponent(true);

        WXHttpTask httpTask = new WXHttpTask();
        httpTask.url = url;
        mUrl = url;
        httpTask.requestListener = new com.shengtu.yunjihome.net.weex.WXRequestListener() {

            @Override
            public void onSuccess(WXHttpTask task) {
                try {

                    String responseData = new String(task.response.data, "utf-8");

                    showProgressDialog(false);
                    refersh_error_iv.setVisibility(View.GONE);
                    mConfigMap.put("bundleUrl", url);
                    mInstance.render(TAG, new String(task.response.data, "utf-8"), mConfigMap, null, ScreenUtil.getDisplayWidth(WXPageActivity.this), ScreenUtil.getDisplayHeight(WXPageActivity.this), WXRenderStrategy.APPEND_ASYNC);

                    //保存
                    String _lastModified = SDKCacheManager.getInstance().getLastModified();
                    String _url = SDKCacheManager.getInstance().getUrl();
                    String _etag = SDKCacheManager.getInstance().getEtag();

                    if(_url!=null && _lastModified!=null && _etag!=null){

                        List<NetCacheBean> netCacheBeans = DataSupport.where("url = ?", _url).find(NetCacheBean.class);

                        if(netCacheBeans.size()>0){

                            //update
                            ContentValues values = new ContentValues();
                            values.put("lastModified", _lastModified);
                            values.put("etag", _etag);
                            int i = DataSupport.updateAll(NetCacheBean.class, values, "url = ?", _url);

                        }else{

                            //create
                            NetCacheBean netCacheBean = new NetCacheBean(_lastModified,_etag,_url);
                            boolean save = netCacheBean.save();

                        }

                        //save cacheFile
                        String name = WXFileUtils.md5(_url);
                        String filename = getCacheDir()+"/yunjihome/"+name+".js";
                        boolean saveFileSuccess = WXFileUtils.writeFile(filename,task.response.data, WXEnvironment.getApplication());

                    }


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(final WXHttpTask task) {
                showProgressDialog(false);
                refersh_error_iv.setVisibility(View.VISIBLE);
                refersh_error_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mConfigMap.put("bundleUrl", url);
                        showProgressDialog(true);
                        mInstance.renderByUrl(TAG, url, mConfigMap, null, WXRenderStrategy.APPEND_ONCE);
                        refersh_error_iv.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onByCache(WXHttpTask task) {

                String name = WXFileUtils.md5(url);
                String filename =getCacheDir()+"/yunjihome/"+name+".js";

                File file = new File(filename);
                if(file.exists())
                {
                    showProgressDialog(false);
                    refersh_error_iv.setVisibility(View.GONE);
                    mConfigMap.put("bundleUrl", url);

                    mInstance.render("WXSample", WXFileUtils.loadFileOrAsset(filename, WXPageActivity.this), mConfigMap, null, -1, -1, WXRenderStrategy.APPEND_ASYNC);
                }
                else
                {

                    showProgressDialog(false);
                    loadWXfromServiceWithoutLocalFile(url);
                }

            }

        };

        WXHttpManager.getInstance().sendRequest(httpTask);

    }


    /**
     *
     * 网络返回304，但是本地缓存已被删除的情况
     *
     */
    private void loadWXfromServiceWithoutLocalFile(final String url){

        showProgressDialog(true);
        if (mInstance != null) {
            mInstance.destroy();
        }

        RenderContainer renderContainer = new RenderContainer(this);
        mContainer.addView(renderContainer);

        mInstance = new WXSDKInstance(this);
        mInstance.setRenderContainer(renderContainer);
        mInstance.registerRenderListener(this);
        mInstance.setNestedInstanceInterceptor(this);
        mInstance.setBundleUrl(url);
        mInstance.setTrackComponent(true);

        WXHttpTask httpTask = new WXHttpTask();
        httpTask.url = url;
        mUrl = url;
        httpTask.requestListener = new com.shengtu.yunjihome.net.weex.WXRequestListener() {

            @Override
            public void onSuccess(WXHttpTask task) {
                try {

                    String responseData = new String(task.response.data, "utf-8");

                    showProgressDialog(false);
                    refersh_error_iv.setVisibility(View.GONE);
                    mConfigMap.put("bundleUrl", url);
                    mInstance.render(TAG, responseData, mConfigMap, null, ScreenUtil.getDisplayWidth(WXPageActivity.this), ScreenUtil.getDisplayHeight(WXPageActivity.this), WXRenderStrategy.APPEND_ASYNC);


                    //保存
                    String _lastModified = SDKCacheManager.getInstance().getLastModified();
                    String _url = SDKCacheManager.getInstance().getUrl();
                    String _etag = SDKCacheManager.getInstance().getEtag();

                    if(_url!=null && _lastModified!=null && _etag!=null){

                        List<NetCacheBean> netCacheBeans = DataSupport.where("url = ?", _url).find(NetCacheBean.class);

                        if(netCacheBeans.size()>0){

                            //update
                            ContentValues values = new ContentValues();
                            values.put("lastModified", _lastModified);
                            values.put("etag", _etag);
                            int i = DataSupport.updateAll(NetCacheBean.class, values, "url = ?", _url);

                        }else{

                            //create
                            NetCacheBean netCacheBean = new NetCacheBean(_lastModified,_etag,_url);
                            boolean save = netCacheBean.save();
                        }

                        //save cacheFile
                        String name = WXFileUtils.md5(_url);
                        String filename = getCacheDir()+"/yunjihome/"+name+".js";
                        boolean saveFileSuccess = WXFileUtils.writeFile(filename,task.response.data,WXEnvironment.getApplication());
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(final WXHttpTask task) {
                showProgressDialog(false);
                refersh_error_iv.setVisibility(View.VISIBLE);
                refersh_error_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mConfigMap.put("bundleUrl", url);
                        showProgressDialog(true);
                        mInstance.renderByUrl(TAG, url, mConfigMap, null, WXRenderStrategy.APPEND_ONCE);
                        refersh_error_iv.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onByCache(WXHttpTask task) {

                String name = WXFileUtils.md5(url);
                String filename = getCacheDir()+"/yunjihome/"+name+".js";

                File file = new File(filename);
                if(file.exists())
                {
                    showProgressDialog(false);
                    refersh_error_iv.setVisibility(View.GONE);
                    mConfigMap.put("bundleUrl", url);

                    mInstance.render("WXSample", WXFileUtils.loadFileOrAsset(filename, WXPageActivity.this), mConfigMap, null, -1, -1, WXRenderStrategy.APPEND_ASYNC);
                }
                else
                {
                    showProgressDialog(false);
                    loadWXfromServiceWithoutLocalFile(url);
                }

            }

        };

        WXHttpManager2.getInstance().sendRequest(httpTask);
    }
