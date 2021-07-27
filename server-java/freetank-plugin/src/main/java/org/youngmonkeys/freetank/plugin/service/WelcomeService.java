package org.youngmonkeys.freetank.plugin.service;

import org.youngmonkeys.freetank.common.service.CommonService;
import org.youngmonkeys.freetank.plugin.config.PluginConfig;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;

@EzySingleton
public class WelcomeService {

	@EzyAutoBind
	private PluginConfig config;
	
	@EzyAutoBind
	private CommonService commonService;
	
	public String welcome(String username) {
		return config.getWelcomePrefix() + " " + username + "!";
	}
	
}
