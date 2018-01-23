package cn.worldwalker.game.wyqp.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.worldwalker.game.wyqp.common.constant.Constant;
import cn.worldwalker.game.wyqp.common.utils.CustomizedPropertyConfigurer;
/**
 * 
 * @ClassName: VersionHandlerInterceptor    
 * @Description:将页面要访问的静态变量设置到视图中
 * @author: jinfeng    
 * @date: 
 * @version: V1.0
 */
public class VersionHandlerInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(modelAndView == null){
			modelAndView = new ModelAndView();
		}
		modelAndView.addObject("version", System.currentTimeMillis());
		modelAndView.addObject("domain", Constant.domain);
		modelAndView.addObject("h5GameStaticUrl", "http://" + Constant.domain + Constant.h5GameStaticFolder);
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}