package com.polaris.workflow.domain.vo;

import com.polaris.common.translation.annotation.Translation;
import com.polaris.common.translation.constant.TransConstant;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 抄送对象
 *
 * @author AprilWind
 */
@Data
public class FlowCopyVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名称
     */
    @Translation(type = TransConstant.USER_ID_TO_NICKNAME, mapper = "userId")
    private String userName;

    public FlowCopyVo(Long userId) {
        this.userId = userId;
    }

}
