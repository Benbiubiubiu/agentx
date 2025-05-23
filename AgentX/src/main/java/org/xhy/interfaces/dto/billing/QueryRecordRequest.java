package org.xhy.interfaces.dto.billing;

/**
 * XHY
 * 2025/5/18 12:15
 * 接收分页查询的参数（查询消费记录）
 * @author Ben，微信：wz_Fung_Ben，邮箱：842609063@qq.con <br/>
 **/
public class QueryRecordRequest {
    private String userId;
    private Integer page;
    private Integer pageSize;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
