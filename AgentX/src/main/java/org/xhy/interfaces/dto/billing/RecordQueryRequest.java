package org.xhy.interfaces.dto.billing;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class RecordQueryRequest {

    /**
     * 页码，从1开始
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码必须大于0")
    private int page = 1;

    /**
     * 每页大小
     */
    @NotNull(message = "每页大小不能为空")
    @Min(value = 1, message = "每页大小必须大于0")
    private int size = 10;

    private String userId;

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getPageNum() {
        return page;
    }

    public void setPageNum(Integer pageNum) {
        this.page = pageNum;
    }

    public Integer getPageSize() {
        return size;
    }

    public void setPageSize(Integer pageSize) {
        this.size = pageSize;
    }
} 