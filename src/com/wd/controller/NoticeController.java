package com.wd.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wd.domain.Notice;
import com.wd.domain.User;
import com.wd.service.HrmService;
import com.wd.util.common.HrmConstants;
import com.wd.util.tag.PageModel;

/**
 * 处理公告请求控制器
 * @author WD-PC
 *
 */
@Controller
public class NoticeController {

	/** 自动注入HrmService */
	@Autowired
	@Qualifier("hrmService")
	private HrmService hrmService;
	
	/**
	 * 查询通告
	 */
	@RequestMapping(value="/notice/selectNotice")
	public String selectNotice(Model model,Integer pageIndex,@ModelAttribute Notice notice) {
		PageModel pageModel = new PageModel();
		if(pageIndex != null){
			pageModel.setPageIndex(pageIndex);
		}
		/** 查询用户信息     */
		List<Notice> notices = hrmService.findNotice(notice, pageModel);
		model.addAttribute("notices", notices);
		model.addAttribute("pageModel", pageModel);
		return "notice/notice";
	}
	
	/**
	 * 查看通告
	 */
	@RequestMapping(value="/notice/previewNotice")
	public String previewNotice(Integer id,Model model) {
		Notice notice = hrmService.findNoticeById(id);
		model.addAttribute("notice", notice);
		return "notice/previewNotice";
	}
	
	/**
	 * 添加公告 
	 * 1表示跳转到添加页面，2表示执行添加操作
	 */
	@RequestMapping(value="/notice/addNotice")
	public ModelAndView addNotice(String flag,@ModelAttribute Notice notice,ModelAndView mv,HttpSession session) {
		if(flag.equals("1")) {
			mv.setViewName("notice/showAddNotice");
		}else {
			User user = (User) session.getAttribute(HrmConstants.USER_SESSION);
			notice.setUser(user);
			hrmService.addNotice(notice);
			mv.setViewName("redirect:/notice/selectNotice");
		}
		return mv;
	}
	
	/**
	 * 删除公告
	 */
	@RequestMapping(value="/notice/removeNotice")
	public ModelAndView removeNotice(String ids,ModelAndView mv) {
		String[] idArray = ids.split(",");
		for(String id : idArray) {
			hrmService.removeNoticeById(Integer.parseInt(id));
		}
		mv.setViewName("redirect:/notice/selectNotice");
		return mv;
	}
	
	/**
	 * 修改公告
	 */
	@RequestMapping(value="/notice/updateNotice")
	public ModelAndView updateNotice(String flag,@ModelAttribute Notice notice,ModelAndView mv,HttpSession session) {
		if(flag.equals("1")) {
			Notice target = hrmService.findNoticeById(notice.getId());
			mv.addObject("notice", target);
			mv.setViewName("notice/showUpdateNotice");
		}else {
			hrmService.modifyNotice(notice);
			mv.setViewName("redirect:/notice/selectNotice");
		}
		return mv;
	}
}
