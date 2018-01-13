package com.oovever.controller;

import com.oovever.common.ApplicationContextHelper;
import com.oovever.common.JsonData;
import com.oovever.dao.SysAclModuleMapper;
import com.oovever.dao.TestDao;
import com.oovever.exception.PermissionException;
import com.oovever.model.SysAclModule;
import com.oovever.param.TestVo;
import com.oovever.util.BeanValidator;
import com.oovever.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author OovEver
 * 2018/1/12 15:38
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {
    @RequestMapping("/hello")
    @ResponseBody
    public JsonData hello() {
        log.info("hello");
//        throw new PermissionException("test exception");
        return JsonData.success("hello,prmission");
    }
    @RequestMapping("/validate.json")
    @ResponseBody
    public JsonData validate(TestVo vo) {
        log.info("vo");
        SysAclModuleMapper moduleMapper = ApplicationContextHelper.popBean(SysAclModuleMapper.class);
        SysAclModule module = moduleMapper.selectByPrimaryKey(1);
        log.info(JsonMapper.obj2String(module));

//        throw new PermissionException("test exception");
        try {
            Map<String, String> map = BeanValidator.validate(vo);
            if (map != null && map.entrySet().size() > 0) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    log.info("{}->{}", entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {

        }

        return JsonData.success("hello,validate");
    }
}
