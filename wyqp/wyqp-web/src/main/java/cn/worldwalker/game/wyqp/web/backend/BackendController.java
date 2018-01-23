package cn.worldwalker.game.wyqp.web.backend;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.worldwalker.game.wyqp.common.backend.BackendService;
import cn.worldwalker.game.wyqp.common.backend.GameModel;
import cn.worldwalker.game.wyqp.common.backend.GameQuery;
import cn.worldwalker.game.wyqp.common.result.Result;
import cn.worldwalker.game.wyqp.common.utils.DateUtil;
import cn.worldwalker.game.wyqp.common.utils.RequestUtil;

@Controller
@RequestMapping("backend/")
public class BackendController {
	@Autowired
	private BackendService backendService;
	
	@RequestMapping("company/index")
	public ModelAndView gamePlayIndex(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("backend/company/index");
		return mv;
	}
	
	@RequestMapping("download/index")
	public ModelAndView gameMallIndex(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("backend/download/index");
		return mv;
	}
	
	@RequestMapping("proxy/index")
	public ModelAndView gameProxyIndex(){
		ModelAndView mv = new ModelAndView();
		mv.addObject("isAdmin", backendService.isAdmin());
		mv.setViewName("backend/proxy/index");
		return mv;
	}
	
	@RequestMapping("proxy/proxyInfo")
	public ModelAndView gameProxyHome(){
		ModelAndView mv = new ModelAndView();
		mv.addObject("realName", RequestUtil.getUserSession().getRealName());
		mv.addObject("proxyId", RequestUtil.getUserSession().getProxyId());
		mv.addObject("playerId", RequestUtil.getUserSession().getPlayerId());
		mv.addObject("mobilePhone", RequestUtil.getUserSession().getMobilePhone());
		mv.addObject("wechatNum", RequestUtil.getUserSession().getWechatNum());
		GameQuery gameQuery = new GameQuery();
		gameQuery.setProxyId(RequestUtil.getProxyId());
		Result result = backendService.getProxyInfo(gameQuery);
		if (result.getCode() == 0) {
			GameModel gameModel = (GameModel)result.getData();
			mv.addObject("totalIncome", gameModel.getTotalIncome());
			mv.addObject("extractAmount", gameModel.getExtractAmount());
			mv.addObject("remainderAmount", gameModel.getRemainderAmount());
		}
		mv.setViewName("backend/proxy/proxyInfo");
		return mv;
	}
	
	@RequestMapping("proxy/getProxyInfo")
	@ResponseBody
	public Result getProxyInfo(){
		GameQuery gameQuery = new GameQuery();
		gameQuery.setProxyId(RequestUtil.getProxyId());
		return backendService.getProxyInfo(gameQuery);
	}
	
	@RequestMapping("proxy/billingDetails")
	public ModelAndView gameProxyBillingDetails(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("backend/proxy/billingDetails");
		Date endDate = new Date();
		Date startDate = DateUtil.getNDayBefore(endDate, 30);
		mv.addObject("startDate", DateUtil.getDateFormat(startDate));
		mv.addObject("endDate", DateUtil.getDateFormat(endDate));
		return mv;
	}
	@RequestMapping("proxy/getBillingDetails")
	@ResponseBody
	public Result getBillingDetails(@RequestBody GameQuery gameQuery){
		return backendService.getBillingDetails(gameQuery);
	}
	
	@RequestMapping("proxy/myMembers")
	public ModelAndView gameProxyMyMembers(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("backend/proxy/myMembers");
		Date endDate = new Date();
		Date startDate = DateUtil.getNDayBefore(endDate, 30);
		mv.addObject("startDate", DateUtil.getDateFormat(startDate));
		mv.addObject("endDate", DateUtil.getDateFormat(endDate));
		mv.addObject("isAdmin", backendService.isAdmin());
		return mv;
	}
	@RequestMapping("proxy/getMyMembers")
	@ResponseBody
	public Result getMyMembers(@RequestBody GameQuery gameQuery){
		return backendService.getMyMembers(gameQuery);
	}
	/**
	 * 提现记录
	 * @return
	 */
	@RequestMapping("proxy/withDrawalRecords")
	public ModelAndView gameWithDrawalRecords(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("backend/proxy/withDrawalRecords");
		Date endDate = new Date();
		Date startDate = DateUtil.getNDayBefore(endDate, 30);
		mv.addObject("startDate", DateUtil.getDateFormat(startDate));
		mv.addObject("endDate", DateUtil.getDateFormat(endDate));
		return mv;
	}
	@RequestMapping("proxy/getWithDrawalRecords")
	@ResponseBody
	public Result getWithDrawalRecords(@RequestBody GameQuery gameQuery){
		return backendService.getWithDrawalRecords(gameQuery);
	}
	
	/**
	 * 概率控制
	 * @param gameQuery
	 * @return
	 */
	@RequestMapping("proxy/getWinProbability")
	@ResponseBody
	public Result getWinProbability(@RequestBody GameQuery gameQuery){
		return backendService.getWinProbability(gameQuery);
	}
	
	@RequestMapping("proxy/winProbability")
	public ModelAndView winProbability(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("backend/proxy/winProbability");
		return mv;
	}
	
	@RequestMapping("proxy/modifyWinProbability")
	@ResponseBody
	public Result modifyWinProbability(@RequestBody GameQuery gameQuery){
		return backendService.modifyWinProbability(gameQuery);
	}
	
	@RequestMapping("proxy/proxyManagement")
	public ModelAndView proxyManagement(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("backend/proxy/proxyManagement");
		Date endDate = new Date();
		Date startDate = DateUtil.getNDayBefore(endDate, 30);
		mv.addObject("startDate", DateUtil.getDateFormat(startDate));
		mv.addObject("endDate", DateUtil.getDateFormat(endDate));
		return mv;
	}
	
	@RequestMapping("proxy/getProxys")
	@ResponseBody
	public Result getProxys(@RequestBody GameQuery gameQuery){
		return backendService.getProxys(gameQuery);
	}
	
	@RequestMapping("proxy/modifyProxy")
	@ResponseBody
	public Result modifyProxy(@RequestBody GameQuery gameQuery){
		return backendService.modifyProxy(gameQuery);
	}
	
	/**
	 * 赠送房卡页面
	 * @return
	 */
	@RequestMapping("proxy/giveAwayRoomCards")
	public ModelAndView giveAwayRoomCards(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("backend/proxy/giveAwayRoomCards");
		return mv;
	}
	
	@RequestMapping("proxy/doGiveAwayRoomCards")
	@ResponseBody
	public Result doGiveAwayRoomCards(Integer toPlayerId, Integer roomCardNum){
		return backendService.doGiveAwayRoomCards(toPlayerId, roomCardNum);
	}
	
	@RequestMapping("proxy/getUserByPlayerId")
	@ResponseBody
	public Result getUserByPlayerId(Integer playerId){
		GameQuery gameQuery = new GameQuery();
		gameQuery.setPlayerId(playerId);
		return backendService.getUserByCondition(gameQuery);
	}
	
	@RequestMapping("proxy/doModifyPassword")
	@ResponseBody
	public Result doModifyPassword(@RequestBody GameQuery gameQuery){
		return backendService.doModifyPassword(gameQuery);
	}
	
}
