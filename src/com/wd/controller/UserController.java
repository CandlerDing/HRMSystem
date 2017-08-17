package com.wd.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wd.domain.User;
import com.wd.service.HrmService;
import com.wd.util.common.HrmConstants;
import com.wd.util.tag.PageModel;

/**
 * 处理用户请求控制器
 * @author WD-PC
 *
 */
@Controller
public class UserController {
	/** 自动注入HrmService */
	@Autowired
	@Qualifier("hrmService")
	private HrmService hrmService;
	
	/**
	 * 处理用户登录请求
	 * @param loginname
	 * @param password
	 * @param session
	 * @param mv
	 * @return
	 */
	@RequestMapping(value="/login")
	public ModelAndView login(@RequestParam("loginname") String loginname,@RequestParam("password") String password,HttpSession session,ModelAndView mv) {
		User user = hrmService.login(loginname, password);
		if(user != null) {
			session.setAttribute(HrmConstants.USER_SESSION, user);
			mv.setViewName("redirect:/main");
		}else {
			mv.addObject("message", "登录名或密码错误，请重新登录");
			mv.setViewName("forward:/loginForm");
		}
		return mv;
	}
	
	/**
	 * 处理查询用户请求
	 * @param pageIndex 请求的是第几页
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/user/selectUser")
	public String selectUser(Integer pageIndex,@ModelAttribute User user,Model model) {
		System.out.println("user = " + user);
		PageModel pageModel = new PageModel();
		if(pageIndex != null) {
			pageModel.setPageIndex(pageIndex);
		}
		/** 查询用户信息 */
		List<User> users = hrmService.findUser(user, pageModel);
		model.addAttribute("users", users);
		model.addAttribute("pageModel", pageModel);
		return "user/user";
	}
	
	/**
	 * 处理添加用户请求
	 * @param flag 1表示跳转到添加页面，2表示执行添加操作
	 * @param user
	 * @param mv
	 * @return
	 */
	@RequestMapping(value="/user/addUser")
	public ModelAndView addUser(String flag,@ModelAttribute User user,ModelAndView mv) {
		if(flag.equals("1")) {
			// 设置跳转到添加页面
			mv.setViewName("/user/showAddUser");
		}else {
			// 执行添加操作
			hrmService.addUser(user);
			// 设置客户端跳转到查询请求
			mv.setViewName("redirect:/user/selectUser");
		}
		return mv;
	}
	
	/**
	 * 处理删除用户请求
	 * @param ids 需要删除的id字符串
	 * @param mv
	 * @return
	 */
	@RequestMapping(value="/user/removeUser")
	public ModelAndView removeUser(String ids,ModelAndView mv) {
		//分解id字符串
		String[] idArray = ids.split(",");
		for(String id : idArray) {
			hrmService.removeUserById(Integer.parseInt(id));
		}
		//设置客户端跳转到查询请求
		mv.setViewName("redirect:/user/selectUser");
		return mv;
	}
	
	/**
	 * 处理修改用户请求
	 * @param String flag 标记， 1表示跳转到修改页面，2表示执行修改操作
	 * @param User user  要修改用户的对象
	 * @param ModelAndView mv
	 * */
	@RequestMapping(value="/user/updateUser")
	public ModelAndView updateUser(String flag,@ModelAttribute User user,ModelAndView mv) {
		if(flag.equals("1")) {
			//根据id查询用户
			User target = hrmService.findUserById(user.getId());
			mv.addObject("user", target);
			mv.setViewName("/user/showUpdateUser");
		}else {
			//执行修改操作
			hrmService.modifyUser(user);
			mv.setViewName("redirect:/user/selectUser");
		}
		return mv;
	}
	
	
	
}

















