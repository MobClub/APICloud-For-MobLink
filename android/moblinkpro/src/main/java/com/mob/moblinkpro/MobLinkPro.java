package com.mob.moblinkpro;


import com.mob.moblink.ActionListener;
import com.mob.moblink.MobLink;
import com.mob.moblink.Scene;
import com.mob.tools.utils.Hashon;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MobLinkPro extends UZModule {

    public static  UZModuleContext moduleContext;
    public static Scene scene;


    public MobLinkPro(UZWebView webView) {
        super(webView);
    }

    public void jsmethod_getMobId(final UZModuleContext moduleContext){
        String pathString = moduleContext.optString("path");
        String paramsString=moduleContext.optString("params");
        MobLinkLog.getInstance().d(MobLinkLog.FORMAT,"getMobId scene path:"+pathString+" params:"+paramsString );
        Scene scene=new Scene();
        Hashon hashon=new Hashon();
        HashMap params =hashon.fromJson(paramsString);
        scene.setPath( pathString);
        scene.setParams(params);
        MobLink.getMobID(scene, new ActionListener<String>() {
            @Override
            public void onResult(String mobId) {
                JSONObject ret=new JSONObject();
                try {
                    ret.put("mobid",mobId);
                    MobLinkLog.getInstance().d(MobLinkLog.FORMAT,"getMobId :" + mobId);
                } catch (JSONException e) {
                    e.printStackTrace();
                    MobLinkLog.getInstance().d(MobLinkLog.FORMAT,"that getMobID create resultJson is a JSONException \n" + e.getMessage() );
                }
                moduleContext.success(ret,true);
            }

            @Override
            public void onError(Throwable throwable) {
                JSONObject err=new JSONObject();
                try {
                    err.put("code","-1");
                    err.put("userInfo",throwable.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                    MobLinkLog.getInstance().d(MobLinkLog.FORMAT,"that getMobID create errorJson is a JSONException \n" + e.getMessage() );
                }
                MobLinkLog.getInstance().d(MobLinkLog.FORMAT,"net error \n" + throwable.getMessage() );
                moduleContext.error(null,err,true);
            }
        });
    }

    /**
     * 注册页面场景的回掉
     * 有两个情况
     * 1.app已安装后，直接由scheme唤起app，这时目标的web页面还未打开，此时先预存scene，等到此次回调注册后在进行返回场景数据。
     * 2.app新安装后者从应用宝打开，此时由app从sever端获取场景数据，由于这是个网络请求，应答时间不一样，会有以下两种情况，
     *      ·.在网络请求返回前，web页面还未打开，则与情况1相同。
     *      ·当网速很慢时候，web页面已经打开，则web页面执行restoreScene等待回调，此时需要把，Appliction.moduleContext
     * @param moduleContext
     */
    public  synchronized  void jsmethod_restoreScene(final UZModuleContext moduleContext){
        MobLinkLog.getInstance().d(MobLinkLog.FORMAT,"inject restoreScene");
        MobLinkPro.moduleContext=moduleContext;
        sceneCallback(scene);
    }

    public  static void sceneCallback( Scene scene){
        if(scene == null || moduleContext == null ) {
            MobLinkLog.getInstance().d(MobLinkLog.FORMAT,"sceneCallback  save scene or moduleContext:" );
            MobLinkPro.scene = scene;
            return;
        }
        try {
            JSONObject ret=new JSONObject();
            ret.put("path",scene.getPath());
            JSONObject paramsJson=new JSONObject(new Hashon().fromHashMap(scene.getParams()));
            ret.put("params",paramsJson);
            moduleContext.success(ret,false);
            MobLinkPro.scene = null;
            MobLinkLog.getInstance().d(MobLinkLog.FORMAT,"sceneCallback execute restore scene:"+scene.getPath()+" params:"+  new Hashon().fromHashMap(scene.getParams())+" moduleContext:"+moduleContext.toString() );
        } catch (JSONException e) {
            MobLinkLog.getInstance().e(MobLinkLog.FORMAT,"the sceneCallback create a JSONException\n" + e.getMessage() );
        }
    }


}
