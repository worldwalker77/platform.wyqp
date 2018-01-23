package cn.worldwalker.game.wyqp.web.job;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.worldwalker.game.wyqp.common.constant.Constant;
import cn.worldwalker.game.wyqp.common.service.RedisOperationService;

@Component(value="userInfoOverTimeCleanJob")
public class UserInfoOverTimeCleanJob {
	
	private final static Log log = LogFactory.getLog(UserInfoOverTimeCleanJob.class);
	
	@Autowired
	public RedisOperationService redisOperationService;
	
	public void doTask(){
		Map<String, Long> map = redisOperationService.getAllTokenTimeMap();
		Set<Entry<String, Long>> set = map.entrySet();
		for(Entry<String, Long> entry : set){
			try {
				String token = entry.getKey();
				Long time = entry.getValue();
				if (System.currentTimeMillis() - time > Constant.userInfoOverTimeLimit*1000) {
					redisOperationService.delUserInfo(token);
				}
			} catch (Exception e) {
				log.error("删除token异常", e);
			}
		}
		
	}
	
}
