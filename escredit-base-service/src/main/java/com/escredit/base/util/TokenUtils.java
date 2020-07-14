package com.escredit.base.util;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class TokenUtils {
    /**
     * 自动生成用户令牌
     */
    public static String generation(){
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }
}
