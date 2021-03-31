package com.chain.buddha;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: haroro
 * @CreateDate: 3/15/21
 */
public class Constants {

    //满意度,0很赞，1满意，2体验不佳
    public final static Map<String, String> SATISFACTION_TYPE_MAP = ImmutableMap.of(
            "0", "很赞",
            "1", "满意",
            "2", "体验不佳"
    );


}
