// package com.WhseApi.query;
//
// import android.app.Activity;
// import android.content.ClipData;
// import android.content.ClipboardManager;
// import android.content.Context;
// import android.content.Intent;
// import android.graphics.Bitmap;
// import android.net.Uri;
// import android.text.TextUtils;
// import android.util.Log;
// import android.view.View;
// import android.widget.AdapterView;
// import android.widget.Button;
// import android.widget.ListView;
// import android.widget.TextView;
//
// import com.alibaba.fastjson.JSON;
// import com.google.gson.Gson;
// import com.manager.farmer.Interface.SocketCallBack;
// import com.manager.farmer.R;
// import com.manager.farmer.adapter.DoUpdateAppAdapter;
// import com.manager.farmer.annoation.ActivityConfig;
// import com.manager.farmer.base.BaseActivity;
// import com.manager.farmer.bean.ResInfo;
// import com.manager.farmer.dialog.LoginLanzouDialog;
// import com.manager.farmer.dialog.UploadLanzouDialog;
// import com.manager.farmer.dialog.UploadLoginLanzouDialog;
// import com.manager.farmer.lanzou.CookieManager;
// import com.manager.farmer.lanzou.LoginTask;
// import com.manager.farmer.lanzou.SetFileDesc;
// import com.manager.farmer.lanzou.UploadImg;
// import com.manager.farmer.lanzou.bean.UploadResult;
// import com.manager.farmer.ui.ToastUtils;
// import com.manager.farmer.utils.APKUtils;
// import com.manager.farmer.utils.BYProtectUtils;
// import com.manager.farmer.utils.Conf;
// import com.manager.farmer.utils.Custom;
// import com.manager.farmer.utils.FileEntry;
// import com.manager.farmer.utils.FileSizeUtils;
// import com.manager.farmer.utils.FileUtils;
// import com.manager.farmer.utils.HttpUtils;
// import com.manager.farmer.utils.ListUtil;
// import com.manager.farmer.utils.SignUtil;
// import com.manager.farmer.utils.SocketUtils;
// import com.manager.farmer.utils.ZipOutUtil;
// import com.manager.farmer.utils.alert;
// import com.manager.farmer.utils.update.AppDetailInfo;
// import com.manager.farmer.utils.update.AppState;
// import com.manager.farmer.utils.update.CacheInfoModel;
// import com.manager.farmer.utils.update.ImageUpdateCallback;
// import com.manager.farmer.bean.LanZouPassModel;
// import com.manager.farmer.utils.update.ResInfoIntent;
// import com.manager.farmer.utils.update.UpdateCacheUtil;
//
// import org.json.JSONArray;
// import org.json.JSONException;
// import org.json.JSONObject;
// import org.xutils.common.Callback;
// import org.xutils.http.HttpMethod;
// import org.xutils.http.RequestParams;
// import org.xutils.x;
//
// import java.io.ByteArrayInputStream;
// import java.io.DataOutputStream;
// import java.io.File;
// import java.io.FileInputStream;
// import java.io.FileOutputStream;
// import java.io.IOException;
// import java.io.InputStream;
// import java.net.HttpURLConnection;
// import java.net.Proxy;
// import java.net.URL;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.zip.ZipFile;
// import java.util.zip.ZipOutputStream;
//
// import okhttp3.Call;
// import okhttp3.FormBody;
// import okhttp3.MediaType;
// import okhttp3.MultipartBody;
// import okhttp3.OkHttpClient;
// import okhttp3.Request;
// import okhttp3.RequestBody;
// import okhttp3.Response;
//
// /**
//  * ??????????????????app???????????? TODO Lznby
//  */
// @ActivityConfig(layoutId = R.layout.activity_do_update_app, title = "????????????")
// public class DoUpdateAppActivity extends BaseActivity {
//
//     ListView listview;
//     DoUpdateAppAdapter adapter;
//     ArrayList<CacheInfoModel> resList = new ArrayList<CacheInfoModel>();
//     ArrayList<CacheInfoModel> upPageList = new ArrayList<CacheInfoModel>();
//     Button btCheckAll;
//     Button btCheckBack;
//     Button btCopyAll;
//     Button btCopyLink;
//     Button btUpload;
//
//     /**
//      * ????????????????????????
//      */
//     private ClipboardManager cm;
//     private ClipData mClipData;
//
//     int startSize = 0;
//
//     public static final String DO_UPDATE_APP_ACTIVITY_INTENT = "DoUpdateAppActivity";
//
//     @Override
//     public void initView() {
//         listview = find(R.id.activity_bought_ListView);
//         adapter = new DoUpdateAppAdapter(context, resList);
//         btCheckAll = find(R.id.btCheckAll);
//         btCheckBack = find(R.id.btCheckBack);
//         btCopyAll = find(R.id.btCopyAll);
//         btCopyLink = find(R.id.btCopyLink);
//         btUpload = find(R.id.btUpload);
//     }
//
//     @Override
//     public void onCreate() {
//
//         initCache();// ????????????
//
//         adapter.setOnItemRecallListener(new DoUpdateAppAdapter.OnItemRecallListener() {
//             @Override
//             public void onRecallClick(int i) {
//                 // todo ??????
//                 breakOutDo(i,true);
//             }
//         });
//
//         listview.setAdapter(adapter);
//
//         startSize = 0;
//
//
//         listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//             @Override
//             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                 ResInfo resInfo = resList.get(position).getResInfo();
//                 String ID = resInfo.ID;
//                 Intent intent = new Intent(context, ResViewerActivity.class);
//                 intent.putExtra("ID", ID);
//                 intent.putExtra("Name", resInfo.Name);
//                 intent.putExtra("Icon", resInfo.Icon);
//                 startActivity(intent);
//             }
//         });
//
//
//         // TODO ??????
//         btCheckAll.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view) {
//                 for(int i = 0; i< resList.size(); i++) {
//                     resList.get(i).getResInfo().Checked = true;
//                 }
//                 runOnUiThread(new Runnable() {
//                     @Override
//                     public void run() {
//                         adapter.notifyDataSetChanged();
//                     }
//                 });
//             }
//         });
//
//         // TODO ??????
//         btCheckBack.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view) {
//                 for(int i = 0; i< resList.size(); i++) {
//                     resList.get(i).getResInfo().Checked = !resList.get(i).getResInfo().Checked;
//                 }
//                 runOnUiThread(new Runnable() {
//                     @Override
//                     public void run() {
//                         adapter.notifyDataSetChanged();
//                     }
//                 });
//             }
//         });
//
//         // TODO ??????????????????
//         btCopyAll.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view) {
//                 StringBuilder copyValue = new StringBuilder();
//                 int no = 0;
//                 for (int i = 0; i < resList.size(); i++) {
//                     if (resList.get(i).getResInfo().getChecked() && resList.get(i).getState() >= AppState.LANZOU_ICON_UPDATE_SUCCESS.toInto()) {
//                         no = no + 1;
//                         copyValue.append(no)
//                                 .append("???")
//                                 .append(resList.get(i).getResInfo().getName()).append("\n").append(resList.get(i).getAppDetailInfo().getContent())
//                                 .append("\n")
//                                 .append("??????????????????")
//                                 .append(resList.get(i).getLanZouPassModel().getInfo().getIs_newd())
//                                 .append("/")
//                                 .append(resList.get(i).getLanZouPassModel().getInfo().getF_id())
//                                 .append("\n??????????????????")
//                                 .append(resList.get(i).getLanZouPassModel().getInfo().getPwd())
//                                 .append("\n")
//                                 .append("?????????????????????????????????")
//                                 .append("\n");
//                     }
//                 }
//                 cm =(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
//                 mClipData = ClipData.newPlainText("Label", copyValue.toString());
//                 cm.setPrimaryClip(mClipData);
//                 runOnUiThread(new Runnable() {
//                     @Override
//                     public void run() {
//                         ToastUtils.show("?????????????????????????????????????????????");
//                     }
//                 });
//             }
//         });
//
//         // TODO ????????????+??????
//         btCopyLink.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view) {
//                 StringBuilder copyValue = new StringBuilder();
//                 int no = 0;
//                 for (int i = 0; i < resList.size(); i++) {
//                     no = no + 1;
//                     if (resList.get(i).getResInfo().getChecked() && resList.get(i).getState() >= AppState.LANZOU_ICON_UPDATE_SUCCESS.toInto()) {
//                         copyValue.append(no)
//                                 .append("???")
//                                 .append(resList.get(i).getResInfo().getName()).append("\n")
//                                 .append("??????????????????")
//                                 .append(resList.get(i).getLanZouPassModel().getInfo().getIs_newd())
//                                 .append("/")
//                                 .append(resList.get(i).getLanZouPassModel().getInfo().getF_id())
//                                 .append("\n??????????????????")
//                                 .append(resList.get(i).getLanZouPassModel().getInfo().getPwd())
//                                 .append("\n");
//                     }
//                 }
//                 cm =(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
//                 mClipData = ClipData.newPlainText("Label", copyValue.toString());
//                 cm.setPrimaryClip(mClipData);
//                 runOnUiThread(new Runnable() {
//                     @Override
//                     public void run() {
//                         ToastUtils.show("?????????????????????????????????????????????");
//                     }
//                 });
//             }
//         });
//
//         // TODO ????????????????????????
//         btUpload.setOnClickListener(new View.OnClickListener() {
//             // ?????????????????????????????????
//             @Override
//             public void onClick(View v) {
//                 upPageList = new ArrayList<>();
//                 for(int i = 0; i < resList.size();i++){
//                     if (
//                             resList.get(i).getState() == AppState.LANZOU_ICON_UPDATE_SUCCESS.toInto() ||
//                                     resList.get(i).getState() == AppState.LANZOU_ICON_UPDATE_FAIL.toInto() ||
//                                     resList.get(i).getState() == AppState.APP_PUBLISH_FAIL.toInto()
//                     ) {
//                         upPageList.add(resList.get(i));
//                     }
//                 }
//                 if (upPageList.size() == 0) {
//                     ((Activity)context).runOnUiThread(new Runnable() {
//                         @Override
//                         public void run() {
//                             new alert(context, "???????????????????????????");
//                         }
//                     });
//                     return;
//                 }
//                 if (UpdateCacheUtil.haveSavePageAccount(context)) {
//                     createPageNoPop(0,false);
//                 } else {
//                     createPage(0,false);
//                 }
//             }
//         });
//         // ?????????????????????????????? singleDo:false????????????????????? single:true???????????????
//         onDownload(0,false);
//     }
//
//     private void initCache() {
//         // ??????????????????????????????????????????
//         ResInfoIntent resInfoIntent = getIntent().getParcelableExtra(DO_UPDATE_APP_ACTIVITY_INTENT);
//         if (resInfoIntent == null) return;
//         if (resInfoIntent.getResInfo() == null) return;
//         if (resInfoIntent.getResInfo().size() <= 0) return;
//         for (int i = 0; i < resInfoIntent.getResInfo().size(); i++) {
//             CacheInfoModel cacheInfoModel = new CacheInfoModel();
//             cacheInfoModel.setResInfo(resInfoIntent.getResInfo().get(i));
//             cacheInfoModel.getResInfo().setChecked(false);
//             resList.add(cacheInfoModel);
//         }
//         // ??????????????????
//         List<CacheInfoModel> cacheInfoModels = UpdateCacheUtil.getInfo(context);
//         if (cacheInfoModels == null) return;
//         if (cacheInfoModels.size() <= 0) return;
//         // ?????????????????????????????????????????????PakName
//         for (int i = 0; i < resList.size(); i++) {
//             for (int j = 0; j < cacheInfoModels.size(); j++) {
//                 if (TextUtils.equals(resInfoIntent.getResInfo().get(i).ID, cacheInfoModels.get(j).getResInfo().ID)) {
//                     ResInfo resInfo = resList.get(i).getResInfo();
//                     resInfo.setChecked(false);
//                     resList.set(i, cacheInfoModels.get(j));
//                     resList.get(i).setResInfo(resInfo);
//                 }
//             }
//         }
//
//     }
//
//     /**
//      * ??????????????????????????????????????????????????????????????????
//      */
//     private void onDownload(int current, boolean singleDo) {
//         new Thread() {
//             @Override
//             public void run() {
//                 // ????????????
//                 if (resList == null) return;
//                 if (resList.size() == 0) return;
//                 breakOutDo(current, singleDo);
//                 //getResInfoSingle(current, singleDo);// ??????????????????
//             }
//         }.start();
//     }
//
//     /**
//      * 1. ??????app??????????????????
//      *
//      * @param current   ??????????????????
//      * @param singleDo  ????????????????????????
//      */
//     public void getResInfoSingle(int current, boolean singleDo) {
//         try {
//             JSONObject json = new JSONObject();
//             json.put("Type", Custom.TYPE_GET_RESINFO);
//             json.put("Token", conf.getToken());
//             json.put("ID", resList.get(current).getResInfo().getID());
//             new SocketUtils(context, json, new SocketCallBack() {
//                 @Override
//                 public void onStart() {
//                 }
//
//                 @Override
//                 public void onSuccess(String result) {
//                     try {
//                         JSONObject backJson = new JSONObject(result);
//                         if (backJson.getBoolean("result")) {
//                             String Name = backJson.getString("Name");
//                             String PakName = backJson.getString("PakName");
//                             String VersionName = backJson.getString("VersionName");
//                             String Size = backJson.getString("Size");
//                             String Content = backJson.getString("Content");
//                             String Photos = backJson.getString("Photos");
//                             String Money = backJson.getString("Money");
//                             String Author = backJson.optString("Author", "");
//                             String AuthorID = backJson.optString("AuthorID", "");
//                             String MD5 = backJson.getString("MD5");
//                             String isOpen = backJson.getString("isOpen");
//                             String CreateTime = backJson.getString("CreateTime");
//                             boolean isBought = backJson.getBoolean("isBought");
//                             String Icon = backJson.getString("Icon");
//                             String url = backJson.getString("Url");
//                             boolean isOpening = backJson.getBoolean("isOpening");
//                             boolean isPass = backJson.getBoolean("isPass");
//                             int openSum = backJson.optInt("OpenSum");
//                             int joinGroupSum = backJson.optInt("joinGroupSum");
//                             int joinQQSum = backJson.optInt("joinQQSum");
//                             int OpenUrlSum = backJson.optInt("OpenUrlSum");
//                             String AppPageWarn = backJson.optString("AppPageWarn", "");
//                             AppDetailInfo appDetailInfo = new AppDetailInfo();
//                             appDetailInfo.setName(Name);
//                             appDetailInfo.setPakName(PakName);
//                             appDetailInfo.setVersionName(VersionName);
//                             appDetailInfo.setSize(Size);
//                             appDetailInfo.setContent(Content);
//                             appDetailInfo.setPhotos(Photos);
//                             appDetailInfo.setMoney(Money);
//                             appDetailInfo.setAuthor(Author);
//                             appDetailInfo.setAuthorID(AuthorID);
//                             appDetailInfo.setMD5(MD5);
//                             appDetailInfo.setIsOpen(isOpen);
//                             appDetailInfo.setCreateTime(CreateTime);
//                             appDetailInfo.setIsBought(isBought);
//                             appDetailInfo.setIcon(Icon);
//                             appDetailInfo.setUrl(url);
//                             appDetailInfo.setIsOpening(isOpening);
//                             appDetailInfo.setIsPass(isPass);
//                             appDetailInfo.setOpenSum(openSum);
//                             appDetailInfo.setJoinGroupSum(joinGroupSum);
//                             appDetailInfo.setJoinQQSum(joinQQSum);
//                             appDetailInfo.setOpenUrlSum(OpenUrlSum);
//                             appDetailInfo.setAppPageWarn(AppPageWarn);
//                             Log.e("?????????????????????:", Content);
//                             Log.e("?????????????????????:", Name);
//                             Log.e("???????????????Logo:", resList.get(current).getResInfo().getIcon());
//                             Log.e("???????????????Photos:", Photos);
//                             List urls = ListUtil.StringToList(Photos);
//                             for (int i = 0; i < urls.size(); i++) {
//                                 Log.e("?????????????????????" + i + ":", "http://" + Conf.getHost() + "/" + "apk/" + PakName + "/img/" + urls.get(i));
//                             }
//                             Gson gson = new Gson();
//                             Log.e("???" + current + "???????????????", gson.toJson(backJson));
//                             resList.get(current).setAppDetailInfo(appDetailInfo);// ??????????????????
//                             resList.get(current).setState(AppState.GET_APP_DETAIL_SUCCESS.toInto());// ?????????????????????????????????
//                             ((Activity)context).runOnUiThread(new Runnable() {
//                                 @Override
//                                 public void run() {
//                                     adapter.notifyDataSetChanged();
//                                 }
//                             });
//                             createApp(current,singleDo);// ???????????????>??????????????????
//                         }
//                     } catch (JSONException e) {
//                         Log.e("??????APP??????","??????"+e);
//                         if (!singleDo && ((current + 1) < resList.size())) {
//                             breakOutDo(current+1,false);
//                         }
//                         resList.get(current).setState(AppState.GET_APP_DETAIL_FAIL.toInto());// ?????????????????????????????????
//                         ((Activity)context).runOnUiThread(new Runnable() {
//                             @Override
//                             public void run() {
//                                 adapter.notifyDataSetChanged();
//                             }
//                         });
//                     }
//                 }
//
//                 @Override
//                 public void onFailure(String error) {
//                     if (!singleDo && ((current + 1) < resList.size())) {
//                         getResInfoSingle(current+1, false);
//                     }
//                     resList.get(current).setState(AppState.GET_APP_DETAIL_FAIL.toInto());// ?????????????????????????????????
//                     ((Activity)context).runOnUiThread(new Runnable() {
//                         @Override
//                         public void run() {
//                             adapter.notifyDataSetChanged();
//                         }
//                     });
//                 }
//
//                 @Override
//                 public void onFinished() {
//                 }
//             });
//         } catch (Exception e) {
//             if (!singleDo && ((current + 1) < resList.size())) {
//                 getResInfoSingle(current+1, false);
//             }
//             resList.get(current).setState(AppState.GET_APP_DETAIL_FAIL.toInto());// ?????????????????????????????????
//             ((Activity)context).runOnUiThread(new Runnable() {
//                 @Override
//                 public void run() {
//                     adapter.notifyDataSetChanged();
//                 }
//             });
//         }
//     }
//
//     /**
//      * 2. ???????????????????????????????????????
//      *
//      * @param current
//      */
//     public void createApp(int current, boolean singleDo) {
//         try {
//             JSONObject json = new JSONObject();
//             json.put("Type", Custom.TYPE_CREATEAPP);
//             json.put("Token", conf.getToken());
//             json.put("PakName", resList.get(current).getAppDetailInfo().getPakName());
//             new SocketUtils(context, json, new SocketCallBack() {
//                 @Override
//                 public void onStart() {
//
//                 }
//
//                 @Override
//                 public void onSuccess(String result) {
//                     try {
//                         JSONObject backJson = new JSONObject(result);
//                         if (backJson.getBoolean("result")) {
//                             resList.get(current).setAppDownLoadUrl(backJson.getString("Url"));// ??????app??????Url
//                             resList.get(current).setState(AppState.GET_APP_PACKAGE_INFO_SUCCESS.toInto());// ????????????????????????
//                             ((Activity)context).runOnUiThread(new Runnable() {
//                                 @Override
//                                 public void run() {
//                                     adapter.notifyDataSetChanged();
//                                 }
//                             });
//                             Log.e("??????????????????","????????????");
//                             downloading(current,singleDo);
//                         } else {
//                             String msg = backJson.optString("msg", "????????????");
//                             Log.e("??????????????????",""+msg);
//                             if (!singleDo && ((current + 1) < resList.size())) {
//                                 getResInfoSingle(current + 1, false);
//                             }
//                             resList.get(current).setState(AppState.GET_APP_PACKAGE_INFO_FAIL.toInto());// ????????????????????????
//                             ((Activity)context).runOnUiThread(new Runnable() {
//                                 @Override
//                                 public void run() {
//                                     adapter.notifyDataSetChanged();
//                                 }
//                             });
//                         }
//                     } catch (JSONException e) {
//                         Log.e("??????????????????","??????" + e);
//                         if (!singleDo && ((current + 1) < resList.size())) {
//                             getResInfoSingle(current + 1, false);
//                         }
//                         resList.get(current).setState(AppState.GET_APP_PACKAGE_INFO_FAIL.toInto());// ????????????????????????
//                         ((Activity)context).runOnUiThread(new Runnable() {
//                             @Override
//                             public void run() {
//                                 adapter.notifyDataSetChanged();
//                             }
//                         });
//                     }
//                 }
//
//                 @Override
//                 public void onFailure(String error) {
//                     Log.e("??????????????????",error);
//                     if (!singleDo && ((current + 1) < resList.size())) {
//                         getResInfoSingle(current + 1, false);
//                     }
//                     resList.get(current).setState(AppState.GET_APP_PACKAGE_INFO_FAIL.toInto());// ????????????????????????
//                     ((Activity)context).runOnUiThread(new Runnable() {
//                         @Override
//                         public void run() {
//                             adapter.notifyDataSetChanged();
//                         }
//                     });
//                 }
//
//                 @Override
//                 public void onFinished() {
//
//                 }
//             });
//         } catch (Exception e) {
//             Log.e("??????????????????","??????" + e);
//             if (!singleDo && ((current + 1) < resList.size())) {
//                 getResInfoSingle(current + 1, false);
//             }
//             resList.get(current).setState(AppState.GET_APP_PACKAGE_INFO_FAIL.toInto());// ????????????????????????
//             ((Activity)context).runOnUiThread(new Runnable() {
//                 @Override
//                 public void run() {
//                     adapter.notifyDataSetChanged();
//                 }
//             });
//         }
//
//     }
//
//     /**
//      * 3. ????????????
//      *
//      * @param currentt
//      */
//     public void downloading(int currentt, boolean singleDo) {
//         Log.e("??????????????????","???????????????"+currentt);
//         final File file = new File(conf.getAppPath() + "/" + resList.get(currentt).getAppDetailInfo().getMD5());
//         if (file.exists() && FileSizeUtils.getAutoFileOrFilesSize(file.getAbsolutePath()).equals(resList.get(currentt).getAppDetailInfo().getSize())) {
//             resList.get(currentt).setPackLocal(file.getAbsolutePath());
//             creating(null, currentt,singleDo);// ????????????
//             //creating(file.getAbsolutePath(), currentt,singleDo);// ????????????
//             return;
//         }
//         RequestParams requestParams = new RequestParams(resList.get(currentt).getAppDownLoadUrl());// ????????????URL
//         requestParams.setSaveFilePath(file.getAbsolutePath());
//         requestParams.setAutoRename(true);
//         requestParams.setProxy(Proxy.NO_PROXY);
//         requestParams.setMethod(HttpMethod.GET);
//         requestParams.setCharset("UTF-8");
//         x.http().get(requestParams, new Callback.ProgressCallback<File>() {
//             @Override
//             public void onSuccess(File result) {
//                 resList.get(currentt).setState(AppState.APP_DOWNLOAD_SUCCESS.toInto());// ?????????????????????
//                 runOnUiThread(new Runnable() {
//                     @Override
//                     public void run() {
//                         adapter.notifyDataSetChanged();
//                     }
//                 });
//                 resList.get(currentt).setLocalCachePath(file.getAbsolutePath());// ?????????????????????
//                 resList.get(currentt).setPackLocal(file.getAbsolutePath());
//                 creating(null, currentt, singleDo);// ????????????
//                 //creating(file.getAbsolutePath(), currentt, singleDo);// ????????????
//             }
//
//             @Override
//             public void onError(Throwable ex, boolean isOnCallback) {
//                 if (ex.getClass().equals(ArrayIndexOutOfBoundsException.class)) {
//                     resList.get(currentt).setState(AppState.APP_DOWNLOAD_SUCCESS.toInto());// ?????????????????????
//                     runOnUiThread(new Runnable() {
//                         @Override
//                         public void run() {
//                             adapter.notifyDataSetChanged();
//                         }
//                     });
//                     resList.get(currentt).setPackLocal(file.getAbsolutePath());
//                     creating(null, currentt, singleDo);// ????????????
//                     //creating(file.getAbsolutePath(), currentt, singleDo);// ????????????
//                 } else {
//                     Log.e("APP??????","????????????");
//                     resList.get(currentt).setState(AppState.APP_DOWNLOAD_FAIL.toInto());// ????????????
//                     runOnUiThread(new Runnable() {
//                         @Override
//                         public void run() {
//                             adapter.notifyDataSetChanged();
//                         }
//                     });
//                     if (!singleDo && (currentt + 1) < resList.size()) {
//                         getResInfoSingle(currentt + 1, false);
//                     }
//                 }
//             }
//
//             @Override
//             public void onCancelled(CancelledException cex) {
//                 Log.e("APP??????","????????????");
//                 resList.get(currentt).setState(AppState.APP_DOWNLOAD_FAIL.toInto());// ????????????
//                 runOnUiThread(new Runnable() {
//                     @Override
//                     public void run() {
//                         adapter.notifyDataSetChanged();
//                     }
//                 });
//                 if (!singleDo && (currentt + 1) < resList.size()) {
//                     getResInfoSingle(currentt + 1, false);
//                 }
//             }
//
//             @Override
//             public void onFinished() { }
//
//             @Override
//             public void onWaiting() { }
//
//             @Override
//             public void onStarted() { }
//
//             @Override
//             public void onLoading(long total, long current, boolean isDownloading) {
//                 try {
//                     if (isDownloading) {
//                         resList.get(currentt).getResInfo().setPercent("????????????"+(int) (current * 1.0f / total * 100)+"%");
//                         runOnUiThread(new Runnable() {
//                             @Override
//                             public void run() {
//                                 adapter.notifyDataSetChanged();
//                             }
//                         });
//                     }
//                 } catch (Exception e) {
//
//                 }
//             }
//         });
//     }
//
//     /**
//      * 4. ????????????
//      *
//      * @param path1
//      * @param current
//      */
//     public void creating(final String path1, int current, boolean singleDo) {
//         String path = resList.get(current).getPackLocal();
//         new Thread() {
//             @Override
//             public void run() {
//                 File file = new File(path);
//                 try {
//                     Thread.sleep(1000);
//                 } catch (InterruptedException e) {
//                 }
//                 try {
//                     ZipFile zipFile = new ZipFile(path);
//                     ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(path + ".out"));
//                     ZipOutUtil.AddFile(zipOutputStream, "assets/assets_token.txt", FileUtils.toByteArray(new ByteArrayInputStream(conf.getToken().getBytes())));
//                     ZipOutUtil.AddFile(zipOutputStream, BYProtectUtils.getAssetsName("assets/server.txt"), FileUtils.toByteArray(FileEntry.encrypt(new ByteArrayInputStream(Conf.getHost().getBytes()), "1234567890098769")));
//                     List saveList = new ArrayList();
//                     saveList.add("assets/assets_token.txt");
//                     saveList.add(BYProtectUtils.getAssetsName("assets/server.txt"));
//                     ZipOutUtil.Sava(zipFile, zipOutputStream, saveList, new ZipOutUtil.ZipSavsCallback() {
//                         @Override
//                         public void onStep(ZipOutUtil.Step step) {
//                             if (step == step.START) {
//
//                             }
//                         }
//
//                         @Override
//                         public void onProgress(int progress, int total) {
//
//                         }
//                     });
//                     File finalOut = new File(conf.getAppPath() + "/" + APKUtils.getLabel(path) + ".apk");
//                     new SignUtil().signApk(path + ".out", finalOut.getAbsolutePath());
//                     new File(path).delete();
//                     new File(path + ".out").delete();
//                     // ????????????
//                     resList.get(current).setLocalCachePath(finalOut.getAbsolutePath());// ??????????????????
//                     resList.get(current).setState(AppState.APP_PACKAGE_SUCCESS.toInto());//????????????
//                     ((Activity) context).runOnUiThread(new Runnable() {
//                         @Override
//                         public void run() {
//                             adapter.notifyDataSetChanged();
//                         }
//                     });
//                     // ?????????????????????????????????
//                     if (UpdateCacheUtil.haveSaveLanZouAccount()) {
//                         uploadLanZouNoPop(current, singleDo);// ??????????????????????????????
//                     } else {
//                         uploadLanZou(current, singleDo);// ??????????????????????????????
//                     }
//
//                 } catch (Exception e) {
//                     e.printStackTrace();
//                     resList.get(current).setState(AppState.APP_PACKAGE_FAIL.toInto());//????????????
//                     ((Activity) context).runOnUiThread(new Runnable() {
//                         @Override
//                         public void run() {
//                             Log.e("????????????:", "" + e.toString());
//                             adapter.notifyDataSetChanged();
//                         }
//                     });
//                     if (!singleDo && ((current + 1) < resList.size())) {
//                         getResInfoSingle(current + 1, false);
//                     }
//                 }
//             }
//         }.start();
//
//
//     }
//
//     /**
//      * 5.1 ???????????????????????????(???????????????????????????????????????)
//      * @param current
//      */
//     public void uploadLanZou(int current, boolean singleDo) {
//         ((Activity) context).runOnUiThread(new Runnable() {
//             @Override
//             public void run() {
//                 LoginLanzouDialog loginLanzouDialog = new LoginLanzouDialog(context, new LoginLanzouDialog.CallBack() {
//                     @Override
//                     public void onLoginSuccess() {
//                         String name = APKUtils.getLabel(resList.get(current).getLocalCachePath()) + ".apk";
//                         new Thread(new Runnable() {
//                             @Override
//                             public void run() {
//                                 try {
//                                     HttpURLConnection connection = (HttpURLConnection) new URL("http://up.woozooo.com/fileup.php").openConnection();
//                                     connection.setRequestMethod("POST");
//                                     connection.setUseCaches(true);
//                                     connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=Sj2vIIaaq1bE2TbvjZoeX0n1vDzHJP");
//                                     connection.setRequestProperty("Charset", "utf-8");
//                                     connection.setRequestProperty("cookie", CookieManager.getCookies());
//                                     connection.connect();
//                                     DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
//                                     //??????????????????
//                                     final String start = "--NplZovL2JkwQxTTHYpUStExVwrkJkGr4mIABv7H"
//                                             + "Content-Disposition: form-data; name=\"task\"Content-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\n\n1\n--NplZovL2JkwQxTTHYpUStExVwrkJkGr4mIABv7H\nContent-Disposition: form-data; name=\"folder_id\"\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\n\n-1\n--NplZovL2JkwQxTTHYpUStExVwrkJkGr4mIABv7H\nContent-Disposition: form-data; name=\"upload_file\"; filename="
//                                             + name + "\nContent-Type: application/octet-stream\nContent-Transfer-Encoding: binary\n\n--Sj2vIIaaq1bE2TbvjZoeX0n1vDzHJP\nContent-Disposition: form-data; name=\"task\"\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\n\n1\n--Sj2vIIaaq1bE2TbvjZoeX0n1vDzHJP\nContent-Disposition: form-data; name=\"folder_id\"\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\n\n-1\n--Sj2vIIaaq1bE2TbvjZoeX0n1vDzHJP\nContent-Disposition: form-data; name=\"upload_file\"; filename="
//                                             + name + "\nContent-Type: application/vnd.android.package-archive\nContent-Transfer-Encoding: binary\n\n";
//                                     final String end = "\n--Sj2vIIaaq1bE2TbvjZoeX0n1vDzHJP--multipart/form-data; boundary=Sj2vIIaaq1bE2TbvjZoeX0n1vDzHJP\n--NplZovL2JkwQxTTHYpUStExVwrkJkGr4mIABv7H--";
//                                     outputStream.writeUTF(start);
//                                     FileInputStream fis = new FileInputStream(resList.get(current).getLocalCachePath());
//                                     byte[] bs = new byte[1024];
//                                     int length = -1;
//                                     while ((length = fis.read(bs)) != -1) {
//                                         outputStream.write(bs, 0, length);
//                                     }
//                                     fis.close();
//                                     outputStream.writeBytes(end);
//                                     outputStream.flush();
//                                     int responseCode = connection.getResponseCode();
//                                     if (responseCode == 200) {
//                                         InputStream is = connection.getInputStream();
//                                         String result = HttpUtils.dealResponseResult(is);
//                                         //Log.e("????????????",result);//????????????????????????  ?????????????????????????????????????????????????????????
//                                         UploadResult uploadResult = JSON.parseObject(result, UploadResult.class);//
//                                         if (uploadResult != null && uploadResult.getText() != null && uploadResult.getText().size() > 0) {
//                                             // ???????????????????????????(????????????)
//                                             resList.get(current).setUploadResult(uploadResult);// ????????????????????????
//                                             resList.get(current).setState(AppState.APP_UPLOAD_SUCCESS.toInto());// ?????????????????????
//                                             ((Activity) context).runOnUiThread(new Runnable() {
//                                                 @Override
//                                                 public void run() {
//                                                     adapter.notifyDataSetChanged();
//                                                 }
//                                             });
//                                             findLanZouPassword(current, singleDo);// ?????????????????????????????????
//                                         } else {
//                                             resList.get(current).setState(AppState.APP_UPLOAD_FAIL.toInto());// ?????????????????????
//                                             Log.e("????????????","1");
//                                             ((Activity) context).runOnUiThread(new Runnable() {
//                                                 @Override
//                                                 public void run() {
//                                                     adapter.notifyDataSetChanged();
//                                                 }
//                                             });
//                                             if (!singleDo && ((current + 1) < resList.size())) {
//                                                 getResInfoSingle(current + 1, false);
//                                             }
//                                         }
//                                     } else {
//                                         resList.get(current).setState(AppState.APP_UPLOAD_FAIL.toInto());// ?????????????????????
//                                         Log.e("????????????","2");
//                                         ((Activity) context).runOnUiThread(new Runnable() {
//                                             @Override
//                                             public void run() {
//                                                 adapter.notifyDataSetChanged();
//                                             }
//                                         });
//                                         if (!singleDo && ((current + 1) < resList.size())) {
//                                             getResInfoSingle(current + 1, false);
//                                         }
//                                     }
//                                 } catch (Exception e) {
//                                     e.printStackTrace();
//                                     resList.get(current).setState(AppState.APP_UPLOAD_FAIL.toInto());// ?????????????????????
//                                     Log.e("????????????","3");
//                                     ((Activity) context).runOnUiThread(new Runnable() {
//                                         @Override
//                                         public void run() {
//                                             adapter.notifyDataSetChanged();
//                                         }
//                                     });
//                                     if (!singleDo && ((current + 1) < resList.size())) {
//                                         getResInfoSingle(current + 1, false);
//                                     }
//                                 }
//                             }
//                         }).start();
//                     }
//
//                     @Override
//                     public void onLoginFiled() {
//
//                     }
//
//                     @Override
//                     public void clickButton() {
//
//                     }
//                 });
//                 loginLanzouDialog.show();
//             }
//         });
//     }
//
//     /**
//      * 5.2 ???????????????????????????(???????????????????????????)
//      * @param current
//      */
//     public void uploadLanZouNoPop(int current, boolean singleDo) {
//
//         new LoginTask(UpdateCacheUtil.getLanZouAccount(), UpdateCacheUtil.getLanZouPassword(), new LoginTask.CallBack() {
//             @Override
//             public void onLoginSuccess() {
//                 ((Activity) context).runOnUiThread(new Runnable() {
//                     @Override
//                     public void run() {
//                         // ????????????
//                         String name = APKUtils.getLabel(resList.get(current).getLocalCachePath()) + ".apk";
//                         new Thread(new Runnable() {
//                             @Override
//                             public void run() {
//                                 try {
//                                     HttpURLConnection connection = (HttpURLConnection) new URL("http://up.woozooo.com/fileup.php").openConnection();
//                                     connection.setRequestMethod("POST");
//                                     connection.setUseCaches(true);
//                                     connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=Sj2vIIaaq1bE2TbvjZoeX0n1vDzHJP");
//                                     connection.setRequestProperty("Charset", "utf-8");
//                                     connection.setRequestProperty("cookie", CookieManager.getCookies());
//                                     connection.connect();
//                                     DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
//                                     //??????????????????
//                                     final String start = "--NplZovL2JkwQxTTHYpUStExVwrkJkGr4mIABv7H"
//                                             + "Content-Disposition: form-data; name=\"task\"Content-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\n\n1\n--NplZovL2JkwQxTTHYpUStExVwrkJkGr4mIABv7H\nContent-Disposition: form-data; name=\"folder_id\"\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\n\n-1\n--NplZovL2JkwQxTTHYpUStExVwrkJkGr4mIABv7H\nContent-Disposition: form-data; name=\"upload_file\"; filename="
//                                             + name + "\nContent-Type: application/octet-stream\nContent-Transfer-Encoding: binary\n\n--Sj2vIIaaq1bE2TbvjZoeX0n1vDzHJP\nContent-Disposition: form-data; name=\"task\"\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\n\n1\n--Sj2vIIaaq1bE2TbvjZoeX0n1vDzHJP\nContent-Disposition: form-data; name=\"folder_id\"\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\n\n-1\n--Sj2vIIaaq1bE2TbvjZoeX0n1vDzHJP\nContent-Disposition: form-data; name=\"upload_file\"; filename="
//                                             + name + "\nContent-Type: application/vnd.android.package-archive\nContent-Transfer-Encoding: binary\n\n";
//                                     final String end = "\n--Sj2vIIaaq1bE2TbvjZoeX0n1vDzHJP--multipart/form-data; boundary=Sj2vIIaaq1bE2TbvjZoeX0n1vDzHJP\n--NplZovL2JkwQxTTHYpUStExVwrkJkGr4mIABv7H--";
//                                     outputStream.writeUTF(start);
//                                     FileInputStream fis = new FileInputStream(resList.get(current).getLocalCachePath());
//                                     byte[] bs = new byte[1024];
//                                     int length = -1;
//                                     while ((length = fis.read(bs)) != -1) {
//                                         outputStream.write(bs, 0, length);
//                                     }
//                                     fis.close();
//                                     outputStream.writeBytes(end);
//                                     outputStream.flush();
//                                     int responseCode = connection.getResponseCode();
//                                     if (responseCode == 200) {
//                                         InputStream is = connection.getInputStream();
//                                         String result = HttpUtils.dealResponseResult(is);
//                                         //Log.e("????????????",result);//????????????????????????  ?????????????????????????????????????????????????????????
//                                         UploadResult uploadResult = JSON.parseObject(result, UploadResult.class);//
//                                         if (uploadResult != null && uploadResult.getText() != null && uploadResult.getText().size() > 0) {
//                                             // ???????????????????????????(????????????)
//                                             resList.get(current).setUploadResult(uploadResult);// ????????????????????????
//                                             resList.get(current).setState(AppState.APP_UPLOAD_SUCCESS.toInto());// ?????????????????????
//                                             ((Activity) context).runOnUiThread(new Runnable() {
//                                                 @Override
//                                                 public void run() {
//                                                     adapter.notifyDataSetChanged();
//                                                 }
//                                             });
//                                             findLanZouPassword(current, singleDo);// ?????????????????????????????????
//                                         } else {
//                                             resList.get(current).setState(AppState.APP_UPLOAD_FAIL.toInto());// ?????????????????????
//                                             Log.e("????????????","1");
//                                             ((Activity) context).runOnUiThread(new Runnable() {
//                                                 @Override
//                                                 public void run() {
//                                                     adapter.notifyDataSetChanged();
//                                                 }
//                                             });
//                                             if (!singleDo && ((current + 1) < resList.size())) {
//                                                 getResInfoSingle(current + 1, false);
//                                             }
//                                         }
//                                     } else {
//                                         resList.get(current).setState(AppState.APP_UPLOAD_FAIL.toInto());// ?????????????????????
//                                         Log.e("????????????","2");
//                                         ((Activity) context).runOnUiThread(new Runnable() {
//                                             @Override
//                                             public void run() {
//                                                 adapter.notifyDataSetChanged();
//                                             }
//                                         });
//                                         if (!singleDo && ((current + 1) < resList.size())) {
//                                             getResInfoSingle(current + 1, false);
//                                         }
//                                     }
//                                 } catch (Exception e) {
//                                     e.printStackTrace();
//                                     resList.get(current).setState(AppState.APP_UPLOAD_FAIL.toInto());// ?????????????????????
//                                     Log.e("????????????","3");
//                                     ((Activity) context).runOnUiThread(new Runnable() {
//                                         @Override
//                                         public void run() {
//                                             adapter.notifyDataSetChanged();
//                                         }
//                                     });
//                                     if (!singleDo && ((current + 1) < resList.size())) {
//                                         getResInfoSingle(current + 1, false);
//                                     }
//                                 }
//                             }
//                         }).start();
//                     }
//                 });
//             }
//             @Override
//             public void onLoginFailure() {
//                 ((Activity) context).runOnUiThread(new Runnable() {
//                     @Override
//                     public void run() {
//                         ToastUtils.show("?????????????????????");
//                         uploadLanZou(current, singleDo);// ???????????????????????????
//                     }
//                 });
//             }
//         });
//     }
//
//     /**
//      * 6. ?????????????????????
//      * @param current
//      */
//     private void findLanZouPassword(int current, boolean singleDo) {
//         // ?????????????????????????????????
//         OkHttpClient okHttpClient = new OkHttpClient();
//         RequestBody requestBody = new FormBody.Builder()
//                 .add("task", "22")// task 22??????????????????????????????
//                 .add("file_id", resList.get(current).getUploadResult().getText().get(0).getId())
//                 .build();
//         Request request = new Request.Builder()
//                 .url("https://pc.woozooo.com/doupload.php")
//                 .post(requestBody)
//                 .header("Cookie",CookieManager.getCookies())
//                 .build();
//         okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
//             @Override
//             public void onFailure(Call call, IOException e) {
//                 resList.get(current).setState(AppState.GET_PASS_FAIL.toInto());// ???????????????????????????
//                 ((Activity) context).runOnUiThread(new Runnable() {
//                     @Override
//                     public void run() {
//                         runOnUiThread(new Runnable() {
//                             @Override
//                             public void run() {
//                                 adapter.notifyDataSetChanged();
//                             }
//                         });
//                     }
//                 });
//                 if (!singleDo && ((current + 1) < resList.size())) {
//                     getResInfoSingle(current + 1, false);
//                 }
//             }
//             @Override
//             public void onResponse(Call call, Response response) throws IOException {
//                 Gson gson = new Gson();
//                 LanZouPassModel lanZouPassModel;
//                 try {
//                     lanZouPassModel = gson.fromJson(response.body().string(),LanZouPassModel.class);
//                     Log.e("????????????",gson.toJson(lanZouPassModel));
//                     if (lanZouPassModel == null) {
//                         resList.get(current).setState(AppState.GET_PASS_FAIL.toInto());// ???????????????????????????
//                         ((Activity) context).runOnUiThread(new Runnable() {
//                             @Override
//                             public void run() {
//                                 adapter.notifyDataSetChanged();
//                             }
//                         });
//                         if (!singleDo && ((current + 1) < resList.size())) {
//                             getResInfoSingle(current + 1, false);
//                         }
//                         return;
//                     }
//                     if (lanZouPassModel.getZt() == 1) {
//                         resList.get(current).setState(AppState.GET_PASS_SUCCESS.toInto());// ???????????????????????????
//                         resList.get(current).setLanZouPassModel(lanZouPassModel);//??????????????????
//                         ((Activity) context).runOnUiThread(new Runnable() {
//                             @Override
//                             public void run() {
//                                 adapter.notifyDataSetChanged();
//                             }
//                         });
//                         updateLanZouNote(current, singleDo);// ?????????????????????????????????
//                     } else {
//                         resList.get(current).setState(AppState.GET_PASS_FAIL.toInto());// ???????????????????????????
//                         ((Activity) context).runOnUiThread(new Runnable() {
//                             @Override
//                             public void run() {
//                                 adapter.notifyDataSetChanged();
//                             }
//                         });
//                         if (!singleDo && ((current + 1) < resList.size())) {
//                             getResInfoSingle(current + 1, false);
//                         }
//                     }
//                 } catch (Exception e) {
//                     Log.e("????????????",e.toString());
//                     resList.get(current).setState(AppState.GET_PASS_FAIL.toInto());// ???????????????????????????
//                     ((Activity) context).runOnUiThread(new Runnable() {
//                         @Override
//                         public void run() {
//                             adapter.notifyDataSetChanged();
//                         }
//                     });
//                     if (!singleDo && ((current + 1) < resList.size())) {
//                         getResInfoSingle(current + 1, false);
//                     }
//                 }
//             }
//         });
//     }
//
//     /**
//      * 7. ?????????????????????
//      * @param current
//      */
//     private void updateLanZouNote(int current, boolean singleDo) {
//         String id = "";
//         try {
//             id = resList.get(current).getUploadResult().getText().get(0).getId();
//         } catch (Exception e) {
//             Log.e("????????????","??????Id??????");
//         }
//         String finalId = id;
//         new Thread() {
//             @Override
//             public void run() {
//                 new SetFileDesc(finalId, resList.get(current).getAppDetailInfo().getContent(), new SetFileDesc.CallBack() {
//                     @Override
//                     public void onSuccess() {
//                         resList.get(current).setState(AppState.LANZOU_NOTE_UPDATE_SUCCESS.toInto());//???????????????????????????
//                         ((Activity) context).runOnUiThread(new Runnable() {
//                             @Override
//                             public void run() {
//                                 adapter.notifyDataSetChanged();
//                             }
//                         });
//                         updateLanZouIcon(current, singleDo);
//                     }
//
//                     @Override
//                     public void onFailure() {
//                         resList.get(current).setState(AppState.LANZOU_NOTE_UPDATE_FAIL.toInto());//???????????????????????????
//                         ((Activity) context).runOnUiThread(new Runnable() {
//                             @Override
//                             public void run() {
//                                 adapter.notifyDataSetChanged();
//                             }
//                         });
//                         updateLanZouIcon(current, singleDo);// ???????????????????????????????????????????????????
//                     }
//                 });
//             }
//         }.start();
//     }
//
//     /**
//      * 8. ?????????????????????
//      * @param current
//      */
//     private void updateLanZouIcon(int current, boolean singleDo) {
//         File iconFile = null;
//         try {
//             Bitmap bitmap = UploadLanzouDialog.drawableToBitmap(APKUtils.getApkIcon(resList.get(current).getLocalCachePath()));
//             if (bitmap != null) {
//                 String sdcardPath = System.getenv("EXTERNAL_STORAGE");
//                 String dir = sdcardPath + "/DCIM/Banquan8";
//                 File file = new File(dir);
//                 if (!file.exists()) {
//                     file.mkdirs();
//                 }
//                 iconFile = new File(dir + APKUtils.getPkgName(resList.get(current).getLocalCachePath()) + ".png");
//                 FileOutputStream outputStream = new FileOutputStream(iconFile);
//                 bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//                 Uri uri = Uri.fromFile(iconFile);
//                 context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         if (iconFile == null || !iconFile.exists() || iconFile.length() == 0) {
//             resList.get(current).setState(AppState.LANZOU_ICON_UPDATE_FAIL.toInto());// ???????????????????????????
//             ((Activity) context).runOnUiThread(new Runnable() {
//                 @Override
//                 public void run() {
//                     adapter.notifyDataSetChanged();
//                 }
//             });
//             // TODO ??????????????????
//             UpdateCacheUtil.cacheInfo(context,resList);// ????????????
//             if (!singleDo && ((current + 1) < resList.size())) {
//                 //getResInfoSingle(current + 1, false);
//                 breakOutDo(current+1,false);
//             }
//             return;
//         }
//         final String iconPath = iconFile.getAbsolutePath();
//         resList.get(current).setLanZouIcon(iconPath);
//         new Thread() {
//             @Override
//             public void run() {
//                 super.run();
//                 new UploadImg(resList.get(current).getUploadResult().getText().get(0).getId(), resList.get(current).getLanZouIcon(), new UploadImg.CallBack() {
//                     @Override
//                     public void onSuccess() {
//                         resList.get(current).setState(AppState.LANZOU_ICON_UPDATE_SUCCESS.toInto());// ?????????Icon????????????
//                         ((Activity) context).runOnUiThread(new Runnable() {
//                             @Override
//                             public void run() {
//                                 adapter.notifyDataSetChanged();
//                             }
//                         });
//                         // TODO ??????????????????
//                         UpdateCacheUtil.cacheInfo(context,resList);// ????????????
//                         if (!singleDo && ((current + 1) < resList.size())) {
//                             //getResInfoSingle(current + 1, false);
//                             breakOutDo(current+1,false);
//                         }
//                     }
//
//                     @Override
//                     public void onFailure() {
//                         resList.get(current).setState(AppState.LANZOU_ICON_UPDATE_FAIL.toInto());// ?????????Icon????????????
//                         ((Activity) context).runOnUiThread(new Runnable() {
//                             @Override
//                             public void run() {
//                                 adapter.notifyDataSetChanged();
//                             }
//                         });
//                         // TODO ??????????????????
//                         UpdateCacheUtil.cacheInfo(context,resList);// ????????????
//                         if (!singleDo && ((current + 1) < resList.size())) {
//                             //getResInfoSingle(current + 1, false);
//                             breakOutDo(current+1,false);
//                         }
//                     }
//
//                     @Override
//                     public void onNotVIP() {
//                         resList.get(current).setState(AppState.LANZOU_ICON_UPDATE_FAIL.toInto());// ?????????Icon????????????
//                         ((Activity)context).runOnUiThread(new Runnable() {
//                             @Override
//                             public void run() {
//                                 ToastUtils.show("??????????????????VIP?????????????????????????????????");
//                                 adapter.notifyDataSetChanged();
//                             }
//                         });
//                         // TODO ??????????????????
//                         UpdateCacheUtil.cacheInfo(context,resList);// ????????????
//                         if (!singleDo && ((current + 1) < resList.size())) {
//                             //getResInfoSingle(current + 1, false);
//                             breakOutDo(current+1,false);
//                         }
//                     }
//                 });
//             }
//         }.start();
//     }
//
//     /**
//      * 9.1 ?????????????????????????????????????????????
//      *
//      * @param current  ????????????????????????
//      * @param singleDo ???????????????????????????
//      */
//     private void createPageNoPop(int current, boolean singleDo) {
//         upPageList.get(current).setState(AppState.APP_PUBLISH_BEGIN.toInto());// ??????????????????????????????
//         ((Activity) context).runOnUiThread(new Runnable() {
//             @Override
//             public void run() {
//                 adapter.notifyDataSetChanged();
//             }
//         });
//         updateImageCo(current,singleDo);// TODO ???????????????0???
//     }
//
//     /**
//      * 9.2 ??????????????????????????????????????????????????????
//      */
//     private void createPage(int current, boolean singleD) {
//         ((Activity) context).runOnUiThread(new Runnable() {
//             @Override
//             public void run() {
//                 UploadLoginLanzouDialog uploadLoginLanzouDialog = new UploadLoginLanzouDialog(DoUpdateAppActivity.this, new UploadLoginLanzouDialog.Callback() {
//                     @Override
//                     public void doSomething() {
//                         createPageNoPop(current, singleD);
//                     }
//                 });
//                 uploadLoginLanzouDialog.show();
//             }
//         });
//
//     }
//
//     /**
//      * 10.1 ??????Icon??????
//      * @param current
//      */
//     private void updateImageCo(int current, boolean singleDo) {
//         new Thread(new Runnable() {
//             @Override
//             public void run() {
//                 try {
//                     // ??????Icon
//                     String iconUrl = upPageList.get(current).getAppDetailInfo().getIcon();// Icon????????????
//                     String fileName = upPageList.get(current).getAppDetailInfo().getPakName()+"icon.png";
//                     UpdateCacheUtil.saveFile(iconUrl, new ImageUpdateCallback() {
//                         @Override
//                         public void success(String localPath) {
//                             try {
//                                 try {
//                                     Thread.sleep(1000);
//                                 } catch (Exception e) {
//                                     Log.e("??????????????????","???"+current + "????????????");
//                                 }
//                                 Log.e("??????ICON??????","???"+current + "Local:"+localPath);
//                                 OkHttpClient client = new OkHttpClient().newBuilder()
//                                         .build();
//                                 RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                                         .addFormDataPart("file", localPath, RequestBody.create(MediaType.parse("application/octet-stream"), new File(localPath)))
//                                         .build();
//                                 Request request = new Request.Builder()
//                                         .url(UpdateCacheUtil.getUpImageUrl(context))
//                                         .method("POST", body)
//                                         .build();
//                                 Response response = client.newCall(request).execute();
//                                 try {
//                                     JSONArray Jobject = new JSONArray(response.body().string());
//                                     JSONObject jsonObject = Jobject.getJSONObject(0);
//                                     if (jsonObject.optInt("status") == 201) {
//                                         upPageList.get(current).setIconUrl(jsonObject.getString("url"));
//                                         for (int i = 0; i < resList.size(); i++) {
//                                             if (resList.get(i).getResInfo().getID() == upPageList.get(current).getResInfo().getID()) {
//                                                 resList.get(i).setState(AppState.PAGE_ICON_SUCCESS.toInto());
//                                                 resList.get(i).setIconUrl(jsonObject.getString("url"));
//                                             }
//                                         }
//                                         upPageList.get(current).setState(AppState.PAGE_ICON_SUCCESS.toInto());
//                                         Log.e("??????ICON??????","???"+current + ":"+upPageList.get(current).getIconUrl());
//                                         ((Activity) context).runOnUiThread(new Runnable() {
//                                             @Override
//                                             public void run() {
//                                                 adapter.notifyDataSetChanged();
//                                             }
//                                         });
//                                         // TODO ???????????????(????????????)
//                                         imageUploadCo(current,singleDo);
//                                     } else {
//                                         UpdateCacheUtil.setPageAccount(context,"","","");//??????????????????????????????
//                                         ((Activity) context).runOnUiThread(new Runnable() {
//                                             @Override
//                                             public void run() {
//                                                 ToastUtils.show("????????????????????????");
//                                             }
//                                         });
//                                         createPage(current,singleDo);/// ???????????????????????????
//                                     }
//                                 } catch (Exception e) {
//                                     UpdateCacheUtil.setPageAccount(context,"","","");//??????????????????????????????
//                                     ((Activity) context).runOnUiThread(new Runnable() {
//                                         @Override
//                                         public void run() {
//                                             ToastUtils.show("????????????????????????");
//                                         }
//                                     });
//                                     createPage(current,singleDo);/// ???????????????????????????
//                                     /*for (int i = 0; i < resList.size(); i++) {
//                                         if (resList.get(i).getResInfo().getID() == upPageList.get(current).getResInfo().getID()) {
//                                             resList.get(i).setState(AppState.PAGE_ICON_FAIL.toInto());
//                                         }
//                                     }
//                                     upPageList.get(current).setState(AppState.PAGE_ICON_FAIL.toInto());
//                                     ((Activity) context).runOnUiThread(new Runnable() {
//                                         @Override
//                                         public void run() {
//                                             adapter.notifyDataSetChanged();
//                                         }
//                                     });
//                                     // TODO ???????????????(????????????)
//                                     if (!singleDo && (upPageList.size() > (current + 1))) {
//                                         //TODO ???????????????
//                                         createPageNoPop(current+1,false);
//                                     }*/
//                                 }
//                                 /*client.newCall(request).enqueue(new okhttp3.Callback() {
//                                     @Override
//                                     public void onFailure(Call call, IOException e) {
//                                         for (int i = 0; i < resList.size(); i++) {
//                                             if (resList.get(i).getResInfo().getID() == upPageList.get(current).getResInfo().getID()) {
//                                                 resList.get(i).setState(AppState.PAGE_ICON_FAIL.toInto());
//                                             }
//                                         }
//                                         upPageList.get(current).setState(AppState.PAGE_ICON_FAIL.toInto());
//                                         ((Activity) context).runOnUiThread(new Runnable() {
//                                             @Override
//                                             public void run() {
//                                                 adapter.notifyDataSetChanged();
//                                             }
//                                         });
//                                         // TODO ???????????????(????????????)
//                                         if (!singleDo && (upPageList.size() > (current + 1))) {
//                                             //TODO ???????????????
//                                             createPageNoPop(current+1,false);
//                                         }
//                                     }
//                                     @Override
//                                     public void onResponse(Call call, Response response) throws IOException {
//                                         try {
//                                             JSONArray Jobject = new JSONArray(response.body().string());
//                                             JSONObject jsonObject = Jobject.getJSONObject(0);
//                                             if (jsonObject.optInt("status") == 201) {
//                                                 upPageList.get(current).setIconUrl(jsonObject.getString("url"));
//                                                 for (int i = 0; i < resList.size(); i++) {
//                                                     if (resList.get(i).getResInfo().getID() == upPageList.get(current).getResInfo().getID()) {
//                                                         resList.get(i).setState(AppState.PAGE_ICON_SUCCESS.toInto());
//                                                         resList.get(i).setIconUrl(jsonObject.getString("url"));
//                                                     }
//                                                 }
//                                                 upPageList.get(current).setState(AppState.PAGE_ICON_SUCCESS.toInto());
//                                                 Log.e("??????ICON??????","???"+current + ":"+upPageList.get(current).getIconUrl());
//                                                 ((Activity) context).runOnUiThread(new Runnable() {
//                                                     @Override
//                                                     public void run() {
//                                                         adapter.notifyDataSetChanged();
//                                                     }
//                                                 });
//                                                 // TODO ???????????????(????????????)
//                                                 imageUploadCo(current,singleDo);
//                                             } else {
//                                                 UpdateCacheUtil.setPageAccount(context,"","","");//??????????????????????????????
//                                                 ((Activity) context).runOnUiThread(new Runnable() {
//                                                     @Override
//                                                     public void run() {
//                                                         ToastUtils.show("????????????????????????");
//                                                     }
//                                                 });
//                                                 createPage(current,singleDo);/// ???????????????????????????
//                                             }
//                                         } catch (Exception e) {
//                                             for (int i = 0; i < resList.size(); i++) {
//                                                 if (resList.get(i).getResInfo().getID() == upPageList.get(current).getResInfo().getID()) {
//                                                     resList.get(i).setState(AppState.PAGE_ICON_FAIL.toInto());
//                                                 }
//                                             }
//                                             upPageList.get(current).setState(AppState.PAGE_ICON_FAIL.toInto());
//                                             ((Activity) context).runOnUiThread(new Runnable() {
//                                                 @Override
//                                                 public void run() {
//                                                     adapter.notifyDataSetChanged();
//                                                 }
//                                             });
//                                             // TODO ???????????????(????????????)
//                                             if (!singleDo && (upPageList.size() > (current + 1))) {
//                                                 //TODO ???????????????
//                                                 createPageNoPop(current+1,false);
//                                             }
//                                         }
//                                     }
//                                 });*/
//                             } catch (Exception e) {
//                                 UpdateCacheUtil.setPageAccount(context,"","","");//??????????????????????????????
//                                 ((Activity) context).runOnUiThread(new Runnable() {
//                                     @Override
//                                     public void run() {
//                                         ToastUtils.show("????????????????????????");
//                                     }
//                                 });
//                                 createPage(current,singleDo);/// ???????????????????????????
//                             }
//                         }
//
//                         @Override
//                         public void fail() {
//                             //UpdateCacheUtil.setPageAccount(context,"","","");//??????????????????????????????
//                             ((Activity) context).runOnUiThread(new Runnable() {
//                                 @Override
//                                 public void run() {
//                                     ToastUtils.show("?????????????????????????????????????????????");
//                                 }
//                             });
//                             createPage(current,singleDo);/// ???????????????????????????
//                         }
//                     }, fileName);
//                 } catch (Exception e) {
//                     UpdateCacheUtil.setPageAccount(context,"","","");//??????????????????????????????
//                     ((Activity) context).runOnUiThread(new Runnable() {
//                         @Override
//                         public void run() {
//                             ToastUtils.show("????????????????????????");
//                         }
//                     });
//                     createPage(current,singleDo);/// ???????????????????????????
//                 }
//             }
//         }).start();
//     }
//
//     /**
//      * 10.2 ??????????????????
//      * @param current
//      * @param singleDo
//      */
//     private void imageUploadCo(int current, boolean singleDo) {
//         // ??????????????????
//         new Thread(new Runnable() {
//             @Override
//             public void run() {
//                 try {
//                     // ????????????
//                     try {
//                         Thread.sleep(1000);
//                     } catch (Exception e) {
//                         Log.e("??????????????????","???"+current + "????????????");
//                     }
//                     List<String> urls = ListUtil.StringToList(upPageList.get(current).getAppDetailInfo().getPhotos());// APP????????????
//                     String downloadImageUrl = "http://" + Conf.getHost() + "/" + "apk/" + upPageList.get(current).getAppDetailInfo().getPakName() + "/img/" + urls.get(0);
//                     UpdateCacheUtil.saveFile(downloadImageUrl, new ImageUpdateCallback() {
//                         @Override
//                         public void success(String localPath) {
//                             try {
//                                 try {
//                                     Thread.sleep(400);
//                                 } catch (Exception e) {
//                                     Log.e("??????????????????","???"+current + "????????????");
//                                 }
//                                 Log.e("??????????????????","???"+current + "Local:"+localPath);
//                                 OkHttpClient client = new OkHttpClient().newBuilder()
//                                         .build();
//                                 RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                                         .addFormDataPart("file", localPath, RequestBody.create(MediaType.parse("application/octet-stream"), new File(localPath)))
//                                         .build();
//                                 Request request = new Request.Builder()
//                                         .url(UpdateCacheUtil.getUpImageUrl(context))
//                                         .method("POST", body)
//                                         .build();
//                                 Response response = client.newCall(request).execute();
//                                 try {
//                                     JSONArray Jobject = new JSONArray(response.body().string());
//                                     JSONObject jsonObject = Jobject.getJSONObject(0);
//                                     if (jsonObject.optInt("status") == 201) {
//                                         upPageList.get(current).setHomeImageUrl(jsonObject.getString("url"));
//                                         for (int i = 0; i < resList.size(); i++) {
//                                             if (resList.get(i).getResInfo().getID() == upPageList.get(current).getResInfo().getID()) {
//                                                 resList.get(i).setState(AppState.PAGE_HOME_IMAGE_SUCCESS.toInto());
//                                                 resList.get(i).setHomeImageUrl(jsonObject.getString("url"));
//                                             }
//                                         }
//                                         Log.e("??????????????????","???"+current + ":"+upPageList.get(current).getHomeImageUrl());
//                                         upPageList.get(current).setState(AppState.PAGE_HOME_IMAGE_SUCCESS.toInto());
//                                         ((Activity) context).runOnUiThread(new Runnable() {
//                                             @Override
//                                             public void run() {
//                                                 adapter.notifyDataSetChanged();
//                                             }
//                                         });
//                                         updatePage(current,singleDo);// ????????????
//                                     } else {
//                                         UpdateCacheUtil.setPageAccount(context,"","","");//??????????????????????????????
//                                         ((Activity) context).runOnUiThread(new Runnable() {
//                                             @Override
//                                             public void run() {
//                                                 ToastUtils.show("????????????????????????");
//                                             }
//                                         });
//                                         createPage(current,singleDo);/// ???????????????????????????
//                                     }
//                                 } catch (Exception e) {
//                                     UpdateCacheUtil.setPageAccount(context,"","","");//??????????????????????????????
//                                     ((Activity) context).runOnUiThread(new Runnable() {
//                                         @Override
//                                         public void run() {
//                                             ToastUtils.show("????????????????????????");
//                                         }
//                                     });
//                                     createPage(current,singleDo);/// ???????????????????????????
//                                 }
//                             } catch (Exception e) {
//                                 UpdateCacheUtil.setPageAccount(context,"","","");//??????????????????????????????
//                                 ((Activity) context).runOnUiThread(new Runnable() {
//                                     @Override
//                                     public void run() {
//                                         ToastUtils.show("????????????????????????");
//                                     }
//                                 });
//                                 createPage(current,singleDo);/// ???????????????????????????
//                             }
//                         }
//
//                         @Override
//                         public void fail() {
//                             UpdateCacheUtil.setPageAccount(context,"","","");//??????????????????????????????
//                             ((Activity) context).runOnUiThread(new Runnable() {
//                                 @Override
//                                 public void run() {
//                                     ToastUtils.show("????????????????????????");
//                                 }
//                             });
//                             createPage(current,singleDo);/// ???????????????????????????
//                         }
//                     }, upPageList.get(current).getAppDetailInfo().getPakName()+"home.png");
//                 } catch (Exception e) {
//                     ((Activity) context).runOnUiThread(new Runnable() {
//                         @Override
//                         public void run() {
//                             ToastUtils.show("?????????????????????????????????????????????");
//                         }
//                     });
//                     createPage(current,singleDo);/// ???????????????????????????
//                 }
//             }
//         }).start();
//     }
//
//     /**
//      * 11. ????????????
//      * @param current
//      */
//     private void updatePage(int current, boolean singleDo) {
//         new Thread(new Runnable() {
//             @Override
//             public void run() {
//                 try {
//                     Thread.currentThread().sleep(100);
//                     LanZouPassModel.InfoBean lanZouPassModel = upPageList.get(current).getLanZouPassModel().getInfo();
//                     String downUrl = lanZouPassModel.getIs_newd()+"/"+lanZouPassModel.getF_id()+"|"+lanZouPassModel.getPwd();
//                     String homeImage = "";
//                     if (!TextUtils.isEmpty(upPageList.get(current).getHomeImageUrl())) {
//                         homeImage = upPageList.get(current).getHomeImageUrl().replace(UpdateCacheUtil.getPageWebUrl(context),"");
//                     }
//                     String iconUrl = "";
//                     if (!TextUtils.isEmpty(upPageList.get(current).getIconUrl())) {
//                         iconUrl = upPageList.get(current).getIconUrl().replace(UpdateCacheUtil.getPageWebUrl(context),"");
//                     }
//                     Log.e("?????????????????????ICON",current+":"+iconUrl);
//                     Log.e("?????????????????????Home",homeImage);
//                     String contentImage = "<br><img src=\""+homeImage+"\" />";
//                     Map<String,String> params = new HashMap<>();
//                     params.put("user",UpdateCacheUtil.getPageAccount(context));//????????????
//                     params.put("password",UpdateCacheUtil.getPagePassword(context));//????????????
//                     params.put("title",upPageList.get(current).getAppDetailInfo().getName()); // ??????
//                     params.put("text",upPageList.get(current).getAppDetailInfo().getContent()+contentImage);// ????????????
//                     params.put("cate","3");// ???????????????????????????2 ????????????3 ????????????4 ????????????5
//                     params.put("tags","????????????");// tag???????????????,??????????????????tag
//                     params.put("img",homeImage);//????????????
//                     params.put("sofename",upPageList.get(current).getAppDetailInfo().getName());//????????????
//                     params.put("sofeico",iconUrl);//????????????
//                     params.put("sodown",downUrl);// ????????????
//                     //params.put("sodownb",downUrl);// ????????????1
//                     //params.put("sodownc",downUrl);// ????????????2
//                     params.put("sofem",upPageList.get(current).getAppDetailInfo().getSize());// ????????????
//                     //params.put("sofeewm",downUrl);// ???????????????
//
//                     String finalUrl =UpdateCacheUtil. getAppendUrl(UpdateCacheUtil.getPageWebUrl(context),params);
//                     OkHttpClient client = new OkHttpClient().newBuilder()
//                             .build();
//                     Request request = new Request.Builder()
//                             .url(finalUrl)
//                             .method("GET", null)
//                             .build();
//                     client.newCall(request).enqueue(new okhttp3.Callback() {
//                         @Override
//                         public void onFailure(Call call, IOException e) {
//                             Log.e("??????????????????","????????????");
//                             for (int i = 0; i < resList.size(); i++) {
//                                 if (resList.get(i).getResInfo().getID() == upPageList.get(current).getResInfo().getID()) {
//                                     resList.get(i).setState(AppState.APP_PUBLISH_FAIL.toInto());
//                                 }
//                             }
//                             upPageList.get(current).setState(AppState.APP_PUBLISH_FAIL.toInto());// ???????????????????????????
//                             ((Activity) context).runOnUiThread(new Runnable() {
//                                 @Override
//                                 public void run() {
//                                     adapter.notifyDataSetChanged();
//                                 }
//                             });
//                             if (!singleDo && (upPageList.size() > current + 1)) {
//                                 //TODO ???????????????
//                                 createPageNoPop(current+1,false);
//                             }
//                         }
//
//                         @Override
//                         public void onResponse(Call call, Response response) throws IOException {
//                             try {
//                                 JSONArray Jobject = new JSONArray(response.body().string());
//                                 JSONObject jsonObject = Jobject.getJSONObject(0);
//                                 if (jsonObject.optInt("status") == 201) {
//                                     for (int i = 0; i < resList.size(); i++) {
//                                         if (resList.get(i).getResInfo().getID() == upPageList.get(current).getResInfo().getID()) {
//                                             resList.get(i).setState(AppState.APP_PUBLISH_SUCCESS.toInto());
//                                         }
//                                     }
//                                     upPageList.get(current).setState(AppState.APP_PUBLISH_SUCCESS.toInto());// ??????????????????
//                                     UpdateCacheUtil.cacheInfo(context,resList);// ????????????
//                                     ((Activity) context).runOnUiThread(new Runnable() {
//                                         @Override
//                                         public void run() {
//                                             adapter.notifyDataSetChanged();
//                                         }
//                                     });
//                                     if (!singleDo && (upPageList.size() > (current + 1))) {
//                                         //TODO ???????????????
//                                         createPageNoPop(current+1,false);
//                                         Log.e("??????????????????","???"+(current+1)+"???");
//                                     }
//                                 } else {
//                                     Log.e("????????????","????????????");
//                                     for (int i = 0; i < resList.size(); i++) {
//                                         if (resList.get(i).getResInfo().getID() == upPageList.get(current).getResInfo().getID()) {
//                                             resList.get(i).setState(AppState.APP_PUBLISH_FAIL.toInto());
//                                         }
//                                     }
//                                     upPageList.get(current).setState(AppState.APP_PUBLISH_FAIL.toInto());// ???????????????????????????
//                                     ((Activity) context).runOnUiThread(new Runnable() {
//                                         @Override
//                                         public void run() {
//                                             adapter.notifyDataSetChanged();
//                                             ToastUtils.show("????????????????????????");
//                                         }
//                                     });
//                                     UpdateCacheUtil.setPageAccount(context,"","","");//??????????????????????????????
//                                     createPage(current,singleDo);// ??????????????????
//                                 }
//                             } catch (Exception e) {
//                                 Log.e("????????????","??????"+e);
//                                 for (int i = 0; i < resList.size(); i++) {
//                                     if (resList.get(i).getResInfo().getID() == upPageList.get(current).getResInfo().getID()) {
//                                         resList.get(i).setState(AppState.APP_PUBLISH_FAIL.toInto());
//                                     }
//                                 }
//                                 upPageList.get(current).setState(AppState.APP_PUBLISH_FAIL.toInto());// ??????????????????
//                                 ((Activity) context).runOnUiThread(new Runnable() {
//                                     @Override
//                                     public void run() {
//                                         adapter.notifyDataSetChanged();
//                                     }
//                                 });
//                                 if (!singleDo && (upPageList.size() > (current + 1))) {
//                                     //TODO ???????????????
//                                     createPageNoPop(current+1,false);
//                                 }
//                             }
//                         }
//                     });
//                 } catch (Exception e) {
//                     Log.e("??????????????????","????????????"+e);
//                     for (int i = 0; i < resList.size(); i++) {
//                         if (resList.get(i).getResInfo().getID() == upPageList.get(current).getResInfo().getID()) {
//                             resList.get(i).setState(AppState.APP_PUBLISH_FAIL.toInto());
//                         }
//                     }
//                     upPageList.get(current).setState(AppState.APP_PUBLISH_FAIL.toInto());// ??????????????????????????????
//                     ((Activity) context).runOnUiThread(new Runnable() {
//                         @Override
//                         public void run() {
//                             adapter.notifyDataSetChanged();
//                         }
//                     });
//                     if (!singleDo && (upPageList.size() > (current + 1))) {
//                         //TODO ???????????????
//                         createPageNoPop(current+1,false);
//                     }
//                 }
//             }
//         }).start();
//     }
//
//
//     /**
//      * ????????????????????????????????????
//      * @param current
//      * @param singleDo
//      */
//     private void breakOutDo(int current, boolean singleDo) {
//         switch (resList.get(current).getState()) {
//             case 0 : {
//                 //AppState.NO_DO.toInto()
//                 getResInfoSingle(current,singleDo);
//                 break;
//             }
//             case 1: {
//                 //AppState.GET_APP_DETAIL_SUCCESS.toInto()
//                 createApp(current,singleDo);
//                 break;
//             }
//             case 2: {
//                 //AppState.GET_APP_DETAIL_FAIL.toInto()
//                 getResInfoSingle(current,singleDo);
//                 break;
//             }
//             case 3: {
//                 //AppState.GET_APP_PACKAGE_INFO_SUCCESS.toInto()
//                 downloading(current,singleDo);
//                 break;
//             }
//             case 4: {
//                 //AppState.GET_APP_PACKAGE_INFO_FAIL.toInto()
//                 createApp(current,singleDo);
//                 break;
//             }
//             case 5: {
//                 //AppState.APP_DOWNLOAD_SUCCESS.toInto()
//                 creating(null,current,singleDo);
//                 break;
//             }
//             case 6: {
//                 //AppState.APP_DOWNLOAD_FAIL.toInto()
//                 createApp(current,singleDo);
//                 break;
//             }
//             case 7: {
//                 //AppState.APP_PACKAGE_SUCCESS.toInto()
//                 if (UpdateCacheUtil.haveSaveLanZouAccount()) {
//                     uploadLanZouNoPop(current, singleDo);// ??????????????????????????????
//                 } else {
//                     uploadLanZou(current, singleDo);// ??????????????????????????????
//                 }
//                 break;
//             }
//             case 8: {
//                 //AppState.APP_PACKAGE_FAIL.toInto()
//                 creating(null,current,singleDo);
//                 break;
//             }
//             case 9: {
//                 //AppState.APP_UPLOAD_SUCCESS.toInto()
//                 findLanZouPassword(current,singleDo);
//                 break;
//             }
//             case 10: {
//                 //AppState.APP_UPLOAD_FAIL.toInto()
//                 if (UpdateCacheUtil.haveSaveLanZouAccount()) {
//                     uploadLanZouNoPop(current, singleDo);// ??????????????????????????????
//                 } else {
//                     uploadLanZou(current, singleDo);// ??????????????????????????????
//                 }
//                 break;
//             }
//             case 11: {
//                 //AppState.GET_PASS_SUCCESS.toInto()
//                 updateLanZouNote(current,singleDo);
//                 break;
//             }
//             case 12: {
//                 //AppState.GET_PASS_FAIL.toInto()
//                 findLanZouPassword(current,singleDo);
//                 break;
//             }
//             case 13: {
//                 //AppState.LANZOU_NOTE_UPDATE_SUCCESS.toInto()
//                 updateLanZouIcon(current,singleDo);
//                 break;
//             }
//             case 14: {
//                 //AppState.LANZOU_NOTE_UPDATE_FAIL.toInto()
//                 updateLanZouNote(current,singleDo);
//                 break;
//             }
//             case 15: {
//                 //AppState.LANZOU_ICON_UPDATE_SUCCESS.toInto()
//                 // ????????????????????????
//                 if (!singleDo && (resList.size() > (current + 1))) {
//                     //TODO ???????????????
//                     breakOutDo(current+1,false);
//                 }
//                 break;
//             }
//             case 16: {
//                 //AppState.LANZOU_ICON_UPDATE_FAIL.toInto()
//                 // ????????????????????????
//                 if (!singleDo && (resList.size() > (current + 1))) {
//                     //TODO ???????????????
//                     breakOutDo(current+1,false);
//                 }
//                 break;
//             }
// /*            case 17: {
//                 //AppState.APP_PUBLISH_BEGIN.toInto()
//                 if (UpdateCacheUtil.haveSavePageAccount(context)) {
//                     createPageNoPop(0,false);
//                 } else {
//                     createPage(0,false);
//                 }
//                 break;
//             }
//             case 18: {
//                 //AppState.PAGE_ICON_SUCCESS.toInto()
//                 imageUploadCo(current,singleDo);
//                 break;
//             }
//             case 19: {
//                 //AppState.PAGE_ICON_FAIL.toInto()
//                 if (UpdateCacheUtil.haveSavePageAccount(context)) {
//                     createPageNoPop(0,false);
//                 } else {
//                     createPage(0,false);
//                 }
//                 break;
//             }
//             case 20: {
//                 //AppState.PAGE_HOME_IMAGE_SUCCESS.toInto()
//                 updatePage(current,singleDo);
//                 break;
//             }
//             case 21: {
//                 //AppState.PAGE_HOME_IMAGE_FAIL.toInto()
//                 imageUploadCo(current,singleDo);
//                 break;
//             }
//             case 22: {
//                 //AppState.APP_PUBLISH_SUCCESS.toInto()
//                 // ????????????
//                 if (!singleDo && (upPageList.size() > (current + 1))) {
//                     //TODO ???????????????
//                     createPageNoPop(current+1,false);
//                 }
//                 if (!singleDo && (resList.size() > (current + 1))) {
//                     //TODO ???????????????
//                     createPageNoPop(current+1,false);
//                 }
//                 break;
//             }
//             case 23: {
//                 //AppState.APP_PUBLISH_FAIL.toInto()
//                 updatePage(current,singleDo);
//                 break;
//             }*/
//             default:
//                 if (!singleDo && (resList.size() > (current + 1))) {
//                     //TODO ???????????????
//                     breakOutDo(current+1,false);
//                 }
//                 break;
//         }
//     }
//
// }
