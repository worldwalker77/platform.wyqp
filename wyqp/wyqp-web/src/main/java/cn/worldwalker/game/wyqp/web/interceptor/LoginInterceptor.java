package cn.worldwalker.game.wyqp.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.worldwalker.game.wyqp.common.utils.RequestUtil;

public class LoginInterceptor  extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
		if (!RequestUtil.isUserSessionExist()) {
			String uri = httpServletRequest.getRequestURI();
			String queryString = httpServletRequest.getQueryString();
			httpServletResponse.sendRedirect("/login/index?redirectUrl=" + uri + "?" + queryString);
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) throws Exception {
	
	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handle, Exception e) {
		
	}
}
