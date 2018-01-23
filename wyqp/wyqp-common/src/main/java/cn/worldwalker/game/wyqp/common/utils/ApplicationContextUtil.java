package cn.worldwalker.game.wyqp.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextUtil implements ApplicationContextAware  {
	public static ApplicationContext ctx=null; 
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		ctx=arg0;
	}
}
