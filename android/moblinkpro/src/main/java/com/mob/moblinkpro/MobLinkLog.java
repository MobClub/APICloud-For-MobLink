package com.mob.moblinkpro;

import com.mob.commons.logcollector.LogsCollector;
import com.mob.moblink.MobLink;
import com.mob.tools.log.NLog;

public class MobLinkLog extends NLog{

	public static final String FORMAT = "[MOBLINK APICLOUD]%s";	// [MOBLINK][CLASS][METHOD] MSG
	
	private MobLinkLog() {
		super();
		setCollector(MobLink.getSdkTag(), new LogsCollector() {
			protected int getSDKVersion() {
				return MobLink.getSdkVersion();
			}
			
			protected String getSDKTag() {
				return MobLink.getSdkTag();
			}
		});
	}
	
	protected String getSDKTag() {
		return MobLink.getSdkTag();
	}

	public static MobLinkLog prepare() {
		return new MobLinkLog();
	}
	
	public static NLog getInstance() {
		return getInstanceForSDK(MobLink.getSdkTag(), true);
	}

}
