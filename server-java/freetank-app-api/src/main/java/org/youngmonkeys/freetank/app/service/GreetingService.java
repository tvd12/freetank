package org.youngmonkeys.freetank.app.service;


import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import org.youngmonkeys.freetank.app.config.AppConfig;
import org.youngmonkeys.freetank.common.service.CommonService;

@EzySingleton
public class GreetingService {

	@EzyAutoBind
	private AppConfig appConfig;
	
	@EzyAutoBind
	private CommonService commonService;
	
	public String hello(String nickName) {
		return appConfig.getHelloPrefix() + " " + nickName + "!";
	}
	
	public String go(String nickName) {
		return appConfig.getGoPrefix() + " " + nickName + "!";
	}
	
}
