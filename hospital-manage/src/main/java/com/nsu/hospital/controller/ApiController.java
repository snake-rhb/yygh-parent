package com.nsu.hospital.controller;

import com.nsu.hospital.mapper.HospitalSetMapper;
import com.nsu.hospital.model.HospitalSet;
import com.nsu.hospital.service.ApiService;
import com.nsu.hospital.util.YyghException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author qy
 *
 */
@Api(tags = "医院管理接口")
@Controller
@RequestMapping
public class ApiController extends BaseController {

	@Autowired
	private ApiService apiService;

	@Autowired
	private HospitalSetMapper hospitalSetMapper;

	@RequestMapping("/hospitalSet/index")
	public String getHospitalSet(ModelMap model,RedirectAttributes redirectAttributes, HttpSession session) {
		// 从session中获取保存的数据
		HospitalSet hospitalSet = (HospitalSet) session.getAttribute("hospitalSet");
		if(hospitalSet == null) {
			// 就查询出第一个
			hospitalSet = hospitalSetMapper.selectById(1);
			// 将保存的数据存到session中，转发给下一个请求
			session.setAttribute("hospitalSet", hospitalSet);
		}
		model.addAttribute("hospitalSet", hospitalSet);
		return "hospitalSet/index";
	}

	@RequestMapping(value="/hospitalSet/save")
	public String createHospitalSet(ModelMap model, HospitalSet hospitalSet, HttpSession session) {
		// hospitalSetMapper.updateById(hospitalSet);
		// 判断数据库中是否存在该数据
		HashMap<String, Object> map = new HashMap<>();
		map.put("hoscode", hospitalSet.getHoscode());
		List<HospitalSet> hospitalSetList = hospitalSetMapper.selectByMap(map);

		if(hospitalSetList.size() == 0) {
			// 保存一条数据
			hospitalSetMapper.insert(hospitalSet);
			// 将保存的数据存到session中，转发给下一个请求
			session.setAttribute("hospitalSet", hospitalSet);
		} else {
			// 将保存的数据存到session中，转发给下一个请求
			session.setAttribute("hospitalSet", hospitalSetList.get(0));
		}

		return "redirect:/hospitalSet/index";
	}

	@RequestMapping("/hospital/index")
	public String getHospital(ModelMap model,HttpServletRequest request,RedirectAttributes redirectAttributes,
							  HttpSession session) {
		try {
			// 查询session中缓存的信息
			HospitalSet hospitalSet = (HospitalSet) session.getAttribute("hospitalSet");
			if(hospitalSet == null) {
				// 查找数据库中id为1的医院
				hospitalSet = hospitalSetMapper.selectById(1);
			}

			// HospitalSet hospitalSet = hospitalSetMapper.selectById(1);
			// 判断医院是否存在，以及是否符合规范
			if(null == hospitalSet || StringUtils.isEmpty(hospitalSet.getHoscode()) || StringUtils.isEmpty(hospitalSet.getSignKey())) {
				this.failureMessage("先设置医院code与签名key", redirectAttributes);
				return "redirect:/hospitalSet/index";
			}

			// 医院存在，就存到request域中
			model.addAttribute("hospital", apiService.getHospital(hospitalSet));
		} catch (YyghException e) {
			this.failureMessage(e.getMessage(), request);
		} catch (Exception e) {
			this.failureMessage("数据异常", request);
		}
		return "hospital/index";
	}

	@RequestMapping(value="/hospital/create")
	public String createHospital(ModelMap model) {
		return "hospital/create";
	}

	@RequestMapping(value="/hospital/save",method=RequestMethod.POST)
	public String saveHospital(String data, HttpServletRequest request, HttpSession session) {
		try {
			apiService.saveHospital(data, session);
		} catch (YyghException e) {
			return this.failurePage(e.getMessage(),request);
		} catch (Exception e) {
			return this.failurePage("数据异常",request);
		}
		return this.successPage(null,request);
	}

	@RequestMapping("/department/list")
	public String findDepartment(ModelMap model,
								 @RequestParam(defaultValue = "1") int pageNum,
								 @RequestParam(defaultValue = "10") int pageSize,
								 HttpServletRequest request,RedirectAttributes redirectAttributes,
								 HttpSession session) {
		try {
			HospitalSet hospitalSet = (HospitalSet) session.getAttribute("hospitalSet");
			if(hospitalSet == null) {
				hospitalSet = hospitalSetMapper.selectById(1);
			}

			if(null == hospitalSet || StringUtils.isEmpty(hospitalSet.getHoscode()) || StringUtils.isEmpty(hospitalSet.getSignKey())) {
				this.failureMessage("先设置医院code与签名key", redirectAttributes);
				return "redirect:/hospitalSet/index";
			}

			model.addAllAttributes(apiService.findDepartment(pageNum, pageSize, session));
		} catch (YyghException e) {
			this.failureMessage(e.getMessage(), request);
		} catch (Exception e) {
			this.failureMessage("数据异常", request);
		}
		return "department/index";
	}

	@RequestMapping(value="/department/create")
	public String create(ModelMap model) {
		return "department/create";
	}

	@RequestMapping(value="/department/save",method=RequestMethod.POST)
	public String save(String data, HttpServletRequest request, HttpSession session) {
		try {
			apiService.saveDepartment(data, session);
		} catch (YyghException e) {
			return this.failurePage(e.getMessage(),request);
		} catch (Exception e) {
			return this.failurePage("数据异常",request);
		}
		return this.successPage(null,request);
	}

	@RequestMapping("/schedule/list")
	public String findSchedule(ModelMap model,
								 @RequestParam(defaultValue = "1") int pageNum,
								 @RequestParam(defaultValue = "10") int pageSize,
							   HttpServletRequest request,RedirectAttributes redirectAttributes,
							   HttpSession session) {
		try {
			HospitalSet hospitalSet = (HospitalSet) session.getAttribute("hospitalSet");
			if(hospitalSet == null) {
				hospitalSet = hospitalSetMapper.selectById(1);
			}

			if(null == hospitalSet || StringUtils.isEmpty(hospitalSet.getHoscode()) || StringUtils.isEmpty(hospitalSet.getSignKey())) {
				this.failureMessage("先设置医院code与签名key", redirectAttributes);
				return "redirect:/hospitalSet/index";
			}

			model.addAllAttributes(apiService.findSchedule(pageNum, pageSize, session));
		} catch (YyghException e) {
			this.failureMessage(e.getMessage(), request);
		} catch (Exception e) {
			this.failureMessage("数据异常", request);
		}
		return "schedule/index";
	}

	@RequestMapping(value="/schedule/create")
	public String createSchedule(ModelMap model) {
		return "schedule/create";
	}

	@RequestMapping(value="/schedule/save",method=RequestMethod.POST)
	public String saveSchedule(String data, HttpServletRequest request, HttpSession session) {
		try {
			//data = data.replaceAll("\r\n", "").replace(" ", "");
			apiService.saveSchedule(data, session);
		} catch (YyghException e) {
			return this.failurePage(e.getMessage(),request);
		} catch (Exception e) {
			e.printStackTrace();
			return this.failurePage("数据异常："+e.getMessage(),request);
		}
		return this.successPage(null,request);
	}

	@RequestMapping(value="/hospital/createBatch")
	public String createHospitalBatch(ModelMap model) {
		return "hospital/createBatch";
	}

	@RequestMapping(value="/hospital/saveBatch",method=RequestMethod.POST)
	public String saveBatchHospital(HttpServletRequest request) {
		try {
			apiService.saveBatchHospital();
		} catch (YyghException e) {
			return this.failurePage(e.getMessage(),request);
		} catch (Exception e) {
			return this.failurePage("数据异常",request);
		}
		return this.successPage(null,request);
	}

	@RequestMapping(value="/department/remove/{depcode}",method=RequestMethod.GET)
	public String removeDepartment(ModelMap model, @PathVariable String depcode, RedirectAttributes redirectAttributes,
								   HttpSession session) {
		apiService.removeDepartment(depcode, session);

		this.successMessage(null, redirectAttributes);
		return "redirect:/department/list";
	}

	@RequestMapping(value="/schedule/remove/{hosScheduleId}",method=RequestMethod.GET)
	public String removeSchedule(ModelMap model, @PathVariable String hosScheduleId, RedirectAttributes redirectAttributes,
								 HttpSession session) {
		apiService.removeSchedule(hosScheduleId, session);

		this.successMessage(null, redirectAttributes);
		return "redirect:/schedule/list";
	}

}

