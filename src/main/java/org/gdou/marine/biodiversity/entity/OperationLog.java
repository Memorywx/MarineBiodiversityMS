package org.gdou.marine.biodiversity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("operation_log")
public class OperationLog extends BaseEntity {

    private Long userId;
    private String username;
    private String operation;
    private String method;
    private String requestUrl;
    private String requestParams;
    private String ipAddress;
    private Integer status;
    private String errorMsg;
}
