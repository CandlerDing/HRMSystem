package com.wd.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wd.domain.Document;
import com.wd.domain.User;
import com.wd.service.HrmService;
import com.wd.util.common.HrmConstants;
import com.wd.util.tag.PageModel;

/**
 * 处理上传下载文件请求控制器
 * @author WD-PC
 *
 */
@Controller
public class DocumentController {

	/** 自动注入HrmService */
	@Autowired
	@Qualifier("hrmService")
	private HrmService hrmService;
	
	/**
	 * 查询文件
	 */
	@RequestMapping(value="/document/selectDocument")
	public String selectDocument(Integer pageIndex,@ModelAttribute Document document,Model model) {
		PageModel pageModel = new PageModel();
		if(pageIndex != null) {
			pageModel.setPageIndex(pageIndex);
		}
		List<Document> documents = hrmService.findDocument(document, pageModel);
		model.addAttribute("documents", documents);
		model.addAttribute("pageModel", pageModel);
		return "document/document";
	}
	
	/**
	 * 处理添加请求
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping(value="/document/addDocument")
	public ModelAndView addDocument(String flag,@ModelAttribute Document document,ModelAndView mv,HttpSession session) throws IllegalStateException, IOException {
		if(flag.equals("1")) {
			mv.setViewName("document/showAddDocument");
		}else {
			//上传文件路径
			String path = session.getServletContext().getRealPath("/upload/");
			//上传文件名
			String fileName = document.getFile().getOriginalFilename();
			//将上传文件保存到一个目标文档中
			document.getFile().transferTo(new File(path + File.separator + fileName));
			
			//设置文件名
			document.setFileName(fileName);
			//设置关联的user对象
			User user = (User) session.getAttribute(HrmConstants.USER_SESSION);
			document.setUser(user);
			//插入数据库
			hrmService.addDocument(document);
			//返回
			mv.setViewName("redirect:/document/selectDocument");
		}
		return mv;
	}
	
	/**
	 * 删除文档
	 */
	@RequestMapping(value="/document/removeDocument")
	public ModelAndView removeDocument(String ids,ModelAndView mv) {
		String[] idArray = ids.split(",");
		for(String id : idArray) {
			hrmService.removeDocumentById(Integer.parseInt(id));
		}
		mv.setViewName("redirect:/document/selectDocument");
		return mv;
	}
	
	/**
	 * 修改文档
	 * flag 1表示跳转到修改页面，2表示执行修改操作
	 */
	@RequestMapping(value="/document/updateDocument")
	public ModelAndView updateDocument(String flag,@ModelAttribute Document document,ModelAndView mv) {
		if(flag.equals("1")) {
			Document target = hrmService.findDocumentById(document.getId());
			mv.addObject("document", target);
			mv.setViewName("document/showUpdateDocument");
		}else {
			hrmService.modifyDocument(document);
			mv.setViewName("redirect:/document/selectDocument");
		}
		return mv;
	}
	
	/**
	 * 处理文档下载请求
	 * @throws IOException 
	 */
	@RequestMapping(value="/document/downLoad")
	public ResponseEntity<byte[]> downLoad(Integer id,HttpSession session) throws IOException{
		Document target = hrmService.findDocumentById(id);
		String fileName = target.getFileName();
		//上传文件路径
		String path = session.getServletContext().getRealPath("/upload");
		//获得要下载文件的file对象
		File file = new File(path + File.separator + fileName);
		// 创建springframework的HttpHeaders对象
		HttpHeaders headers = new HttpHeaders();
		// 下载显示的文件名，解决中文名称乱码问题  
		String downLoadFileName = new String(fileName.getBytes("UTF-8"),"iso-8859-1");
		// 通知浏览器以attachment（下载方式）打开图片
		headers.setContentDispositionFormData("attachment", downLoadFileName);
		// application/octet-stream ： 二进制流数据（最常见的文件下载）。
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		// 201 HttpStatus.CREATED
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers,HttpStatus.CREATED);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}







