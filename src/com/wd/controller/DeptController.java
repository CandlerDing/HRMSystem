package com.wd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wd.domain.Dept;
import com.wd.service.HrmService;
import com.wd.util.tag.PageModel;

/**
 * 处理部门请求控制器
 * @author WD-PC
 *
 */

@Controller
public class DeptController {
	
	@Autowired
	@Qualifier("hrmService")
	private HrmService hrmService;
	
	/**
	 * 处理查找部门请求
	 * @param model
	 * @param pageIndex
	 * @param dept
	 * @return
	 */
	@RequestMapping(value="/dept/selectDept")
	public String selectDept(Model model,Integer pageIndex,@ModelAttribute Dept dept) {
		PageModel pageModel = new PageModel();
		if(pageIndex != null) {
			pageModel.setPageIndex(pageIndex);
		}
		List<Dept> depts = hrmService.findDept(dept, pageModel);
		model.addAttribute("depts", depts);
		model.addAttribute("pageModel", pageModel);
		return "dept/dept";
	}
	
	/**
	 * 处理添加部门请求
	 * @param flag 1表示跳转到添加页面，2表示执行添加操作
	 * @param dept
	 * @param mv
	 * @return
	 */
	@RequestMapping(value="/dept/addDept")
	public ModelAndView addDept(String flag,@ModelAttribute Dept dept,ModelAndView mv) {
		if(flag.equals("1")) {
			mv.setViewName("dept/showAddDept");
		}else {
			hrmService.addDept(dept);
			mv.setViewName("redirect:/dept/selectDept");
		}
		return mv;
	}
	
	/**
	 * 处理删除部门请求
	 * @param ids
	 * @param mv
	 * @return
	 */
	@RequestMapping(value="/dept/removeDept")
	public ModelAndView removeDept(String ids,ModelAndView mv) {
		String[] idArray = ids.split(",");
		for(String id : idArray) {
			hrmService.removeDeptById(Integer.parseInt(id));
		}
		mv.setViewName("redirect:/dept/selectDept");
		return mv;
	}
	
	/**
	 * 处理部门修改请求
	 * @param flag
	 * @param dept
	 * @param mv
	 * @return
	 */
	@RequestMapping(value="/dept/updateDept")
	public ModelAndView updateDept(String flag,@ModelAttribute Dept dept,ModelAndView mv) {
		if(flag.equals("1")) {
			Dept target = hrmService.findDeptById(dept.getId());
			mv.addObject("dept", target);
			mv.setViewName("dept/showUpdateDept");
		}else {
			hrmService.modifyDept(dept);
			mv.setViewName("redirect:/dept/selectDept");
		}
		return mv;
	}
}

