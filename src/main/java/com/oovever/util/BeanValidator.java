package com.oovever.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oovever.exception.ParamException;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * @author OovEver
 * 2018/1/13 22:36
 */
public class BeanValidator {
//    全局校验工厂
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     * 校验类
     * @param t 字段
     * @param groups 错误信息
     * @param <T> 类型
     * @return 返回验证结果
     */
    public static <T> Map<String, String> validate(T t, Class... groups) {
        Validator validator      = validatorFactory.getValidator();
//        获取校验结果
        Set       validateResult = validator.validate(t, groups);
//        无错误 直接返回空的Map
        if (validateResult.isEmpty()) {
            return Collections.emptyMap();
        }else {
            LinkedHashMap errors   = Maps.newLinkedHashMap();
            Iterator      iterator = validateResult.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation violation = (ConstraintViolation)iterator.next();
//              有问题的字段与错误信息
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return errors;
        }
    }

    /**
     *  校验List
     * @param collection 要校验的list
     * @return 返回校验结果
     */
    public static Map<String, String> validateList(Collection<?> collection) {
//        如果是空值 直接抛出异常
        Preconditions.checkNotNull(collection);
        Iterator iterator = collection.iterator();
        Map errors;

        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object, new Class[0]);
        } while (errors.isEmpty());

        return errors;
    }

    /**
     *  包装validate与validateList
     * @param first
     * @param objects
     * @return
     */
    public static Map<String, String> validateObject(Object first, Object... objects) {
        if (objects != null && objects.length > 0) {
            return validateList(Lists.asList(first, objects));
        } else {
            return validate(first, new Class[0]);
        }
    }

    /**
     * 参数校验
     * @param param 要检查的参数
     * @throws ParamException  返回异常的参数信息
     */
    public static void check(Object param) throws ParamException {
        Map<String, String> map = BeanValidator.validateObject(param);
        if (MapUtils.isNotEmpty(map)) {
            throw new ParamException(map.toString());
        }
    }
}
