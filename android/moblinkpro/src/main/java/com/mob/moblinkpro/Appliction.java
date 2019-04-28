package com.mob.moblinkpro;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.mob.MobSDK;
import com.mob.moblink.MobLink;
import com.mob.moblink.MobLinkActivity;
import com.mob.moblink.RestoreSceneListener;
import com.mob.moblink.Scene;
import com.mob.tools.MobLog;
import com.mob.tools.utils.Hashon;
import com.uzmap.pkg.EntranceActivity;
import com.uzmap.pkg.uzcore.uzmodule.AppInfo;
import com.uzmap.pkg.uzcore.uzmodule.ApplicationDelegate;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

import org.json.JSONException;
import org.json.JSONObject;

public class Appliction extends ApplicationDelegate {

    private static final String FEATURE_NAME="mobSDK";
    private static final String APP_KEY="Mob-AppKey";
    private static final String SECRET_KEY="Mob-AppSecret";

    public Appliction() {

    }

    @Override
    public void onApplicationCreate(Context context, AppInfo info) {
        super.onApplicationCreate(context, info);
        String appKey=info.getFeatureValue(FEATURE_NAME,APP_KEY);
        String appSecret=info.getFeatureValue(FEATURE_NAME,SECRET_KEY);
        MobSDK.init(context,appKey,appSecret);
        MobLinkLog.getInstance().d(MobLinkLog.FORMAT, "onApplicationCreate appKey:"+MobSDK.getAppkey() +" AppSecret:"+ MobSDK.getAppSecret());
        MobLink.setRestoreSceneListener(new SceneListener());

    }

    class SceneListener extends Object implements RestoreSceneListener {

        @Override
        public Class<? extends Activity> willRestoreScene(final Scene scene) {

            MobLinkLog.getInstance().d(MobLinkLog.FORMAT,"willRestoreScene scene:"+scene.getPath()+" params:"+  new Hashon().fromHashMap(scene.getParams()) );
            MobLinkPro.sceneCallback(scene);
            return  EntranceActivity.class;
        }

        @Override
        public void completeRestore(Scene scene) {

        }

        @Override
        public void notFoundScene(Scene scene) {

        }
    }


}
