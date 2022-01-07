package org.ito.blog.utils;

/**
 * 用于管理端大部分增删改查API的返回值
 * 在加入了“修改文章时获取原文章信息”管理端API时，就发现这也其实不太好，字段不够用，又得重写一个Result类，
 * 其实可以统一建立一个JsonResult类，类中用泛型存储值，用Integer code存储操作是否成功
 */
public class JsonResult {
    private Integer code;
    private String description;

    public JsonResult(){
    }
    public JsonResult(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
