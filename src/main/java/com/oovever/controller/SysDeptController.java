package com.oovever.controller;

import com.oovever.common.JsonData;
import com.oovever.dto.DeptLevelDto;
import com.oovever.param.DeptParam;
import com.oovever.service.SysDeptService;
import com.oovever.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author OovEver
 * 2018/1/13 23:45
 */
@Controller
@RequestMapping("/sys/dept")
@Slf4j
public class SysDeptController {
    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private SysTreeService sysTreeService;

    /**
     * 进入页面接口
     * @return 页面
     */
    @RequestMapping("/page.json")
    public ModelAndView page() {
        return new ModelAndView("dept");
    }
    /**
     * 保存部门信息
     * @param param 参数信息
     * @return 返回保存结果
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveDept(DeptParam param) {
        sysDeptService.save(param);
        return JsonData.success();
    }

    /**
     * 部门树请求
     * @return 部门树
     */
    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree() {
        List<DeptLevelDto> dtoList = sysTreeService.deptTree();
        return JsonData.success(dtoList);
    }

    /**
     * 更新部门
     * @param param 参数信息
     * @return 更新结果
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateDept(DeptParam param) {
        sysDeptService.update(param);
        return JsonData.success();
    }
}
