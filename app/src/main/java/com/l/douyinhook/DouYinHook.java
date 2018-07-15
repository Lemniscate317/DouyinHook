package com.l.douyinhook;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Method;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Lemniscate on 2018/7/15.
 */

public class DouYinHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.ss.android.ugc.aweme")) {
            XposedBridge.log("find douyin");
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log("after attach application");
                    ClassLoader classLoader = ((Context) param.args[0]).getClassLoader();

                    Class<?> aweme = classLoader.loadClass("com.ss.android.ugc.aweme.feed.model.Aweme");
                    Class<?> clazz = classLoader.loadClass("com.ss.android.ugc.aweme.feed.share.a.b");

                    if (clazz != null && aweme != null) {
                        XposedBridge.log("all not null");
                        XposedHelpers.findAndHookMethod(clazz, "a", aweme, String.class, new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                super.beforeHookedMethod(param);
                                XposedBridge.log("before hook a");
                                try {
                                    XposedBridge.log("awe:" + param.args[0]);

//                                    Method getRtmpPullUrlMethod = param.args[0].getClass().getMethod("getRtmpPullUrl", new Class<?>[]{});
//                                    String getRtmpPullUrl = (String) getRtmpPullUrlMethod.invoke(param.args[0], new Object[]{});
//                                    XposedBridge.log("getRtmpPullUrl:" + getRtmpPullUrl);


                                    Method videoMethod = param.args[0].getClass().getMethod("getVideo", new Class<?>[]{});
                                    Object videoObj = videoMethod.invoke(param.args[0], new Object[]{});
                                    XposedBridge.log("videoobj:" + videoObj);

                                    Method isHasWaterMarkMethod = videoObj.getClass().getMethod("isHasWaterMark", new Class<?>[]{});
                                    boolean isHasWaterMark = (boolean) isHasWaterMarkMethod.invoke(videoObj, new Object[]{});
                                    XposedBridge.log("isHasWaterMark:" + isHasWaterMark);

                                    Method getDownloadAddrMethod = videoObj.getClass().getMethod("getDownloadAddr", new Class<?>[]{});
                                    Object urlModel = getDownloadAddrMethod.invoke(videoObj, new Object[]{});
                                    XposedBridge.log("urlModel:" + urlModel);

                                    Method getUri = urlModel.getClass().getMethod("getUri", new Class<?>[]{});
                                    String videoUri = (String) getUri.invoke(urlModel, new Object[]{});
                                    XposedBridge.log("videoUri:" + videoUri);
                                    Method getUrlList = urlModel.getClass().getMethod("getUrlList", new Class<?>[]{});
                                    List<String> urlList = (List<String>) getUrlList.invoke(urlModel, new Object[]{});
                                    XposedBridge.log("urlList:" + urlList);

                                    Method getPlayAddrMethod = videoObj.getClass().getMethod("getPlayAddr", new Class<?>[]{});
                                    Object VideoUrlModelObj = getPlayAddrMethod.invoke(videoObj, new Object[]{});
                                    XposedBridge.log("VideoUrlModelObj:" + VideoUrlModelObj.toString());
                                    Method getUrlList1Method = VideoUrlModelObj.getClass().getMethod("getUrlList");
                                    List<String> noWaterMarkList = (List<String>) getUrlList1Method.invoke(VideoUrlModelObj, new Object[]{});

                                    urlList.clear();
                                    urlList.addAll(noWaterMarkList);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    XposedBridge.log(e.getCause() + e.getMessage());
                                }
                            }
                        });
                    } else {
                        XposedBridge.log("clazz:" + clazz == null ? "null" : "not null" + "   " + aweme == null ? "null" : "not null");
                    }
                }
            });


        }

//        if (lpparam.appInfo.packageName.equals("com.l.xposed_test")) {
//            Class<?> clazz = lpparam.classLoader.loadClass("com.l.xposed_test.MainActivity");
//
//            XposedBridge.log("toast hack start11111");
//
//            XposedHelpers.findAndHookMethod(clazz, "toastMessage", new XC_MethodHook() {
//                @Override
//                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                    super.beforeHookedMethod(param);
//                }
//
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    super.afterHookedMethod(param);
//                    param.setResult("you are hack111");
//                }
//            });
//        }
    }
}
