package cn.worldwalker.game.wyqp.web.backend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.worldwalker.game.wyqp.common.backend.GameQuery;
import cn.worldwalker.game.wyqp.common.backend.BackendService;
import cn.worldwalker.game.wyqp.common.result.Result;

@Controller
public class LoginController {
	@Autowired
	private BackendService gameService;
	
	@RequestMapping("login/index")
	public ModelAndView index(String redirectUrl,HttpServletResponse response, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		mv.addObject("redirectUrl", redirectUrl);
		mv.setViewName("backend/proxy/login");
		return mv;
	}
	
	@RequestMapping("login/doLogin")
	@ResponseBody
	public Result doLogin(String mobilePhone, String password){
		GameQuery gameQuery = new GameQuery();
		gameQuery.setMobilePhone(mobilePhone);
		gameQuery.setPassword(password);
		return gameService.doLogin(gameQuery);
		
	}
	
	
}
