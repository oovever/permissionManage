package com.oovever.service;

import com.google.common.base.Preconditions;
import com.oovever.dao.SysUserMapper;
import com.oovever.exception.ParamException;
import com.oovever.model.SysUser;
import com.oovever.param.UserParam;
import com.oovever.util.BeanValidator;
import com.oovever.util.MD5Util;
import com.oovever.util.PasswordUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author OovEver
 * 2018/1/14 23:49
 */
@Service
public class SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * 新增用户service
     * @param param 新增用户参数
     */
    public void save(UserParam param) {
//        检查用户参数是否合法
        BeanValidator.check(param);
        if(checkTelephoneExist(param.getTelephone(), param.getId())) {
            throw new ParamException("电话已被占用");
        }
        if(checkEmailExist(param.getMail(), param.getId())) {
            throw new ParamException("邮箱已被占用");
        }
        String password = PasswordUtil.randomPassword();
        String encryptedPassword = MD5Util.encrypt(password);
        SysUser user = SysUser.builder().username(param.getUsername()).telephone(param.getTelephone()).mail(param.getMail())
                .password(encryptedPassword).deptId(param.getDeptId()).status(param.getStatus()).remark(param.getRemark()).build();
        user.setOperator("system");//TODO
        user.setOperateIp("127.0.0.1");//TODO
        user.setOperateTime(new Date());
        // TODO: sendEmail
        sysUserMapper.insertSelective(user);

    }

    /**
     * 更新用户操作
     * @param param 要更新的用户参数
     */
    public void update(UserParam param) {
        BeanValidator.check(param);
        if(checkTelephoneExist(param.getTelephone(), param.getId())) {
            throw new ParamException("电话已被占用");
        }
        if(checkEmailExist(param.getMail(), param.getId())) {
            throw new ParamException("邮箱已被占用");
        }
        SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的用户不存在");
        SysUser after = SysUser.builder().id(param.getId()).username(param.getUsername()).telephone(param.getTelephone()).mail(param.getMail())
                .deptId(param.getDeptId()).status(param.getStatus()).remark(param.getRemark()).build();
        after.setOperator("system");//TODO
        after.setOperateIp("127.0.0.1");//TODO);
        after.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(after);
    }
    /**
     *检查email是否存在
     * @param mail email信息
     * @param userId 用户id
     * @return 返回email是否存在
     */
    public boolean checkEmailExist(String mail, Integer userId) {
        return false;
    }

    /**
     * 检查电话是否存在
     * @param telephone 电话
     * @param userId 用户Id
     * @return 返回电话是否存在
     */
    public boolean checkTelephoneExist(String telephone, Integer userId) {
        return false;
    }
}
