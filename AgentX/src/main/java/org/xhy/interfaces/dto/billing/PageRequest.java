package org.xhy.interfaces.dto.billing;

import jakarta.validation.constraints.Min;

/**
 * 分页请求参数
 */
public class PageRequest {
    /**
     * 页码，从1开始
     */
    @Min(value = 1, message = "页码必须大于0")
    private int page = 1;

    /**
     * 每页大小
     */
    @Min(value = 1, message = "每页大小必须大于0")
    private int size = 10;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
} 