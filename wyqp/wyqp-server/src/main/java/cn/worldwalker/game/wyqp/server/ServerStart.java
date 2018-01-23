package cn.worldwalker.game.wyqp.server;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class ServerStart implements InitializingBean{
	
	@Resource(name = "webSocketServer")
	private Server webSocketServer;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					webSocketServer.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
	}

}
