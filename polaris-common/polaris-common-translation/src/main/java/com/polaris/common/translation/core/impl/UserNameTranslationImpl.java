package com.polaris.common.translation.core.impl;

import com.polaris.common.spi.service.UserService;
import com.polaris.common.translation.annotation.TranslationType;
import com.polaris.common.translation.constant.TransConstant;
import com.polaris.common.translation.core.TranslationInterface;
import lombok.AllArgsConstructor;

/**
 * 用户名翻译实现
 *
 * @author Lion Li
 */
@AllArgsConstructor
@TranslationType(type = TransConstant.USER_ID_TO_NAME)
public class UserNameTranslationImpl implements TranslationInterface<String> {

    private final UserService userService;

    @Override
    public String translation(Object key, String other) {
        if (key instanceof Long id) {
            return userService.selectUserNameById(id);
        }
        return null;
    }
}
