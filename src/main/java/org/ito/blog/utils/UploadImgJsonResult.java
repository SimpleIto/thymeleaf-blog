package org.ito.blog.utils;

/**
 * editor.md 图片上传返回值专用Json格式
 */
public class UploadImgJsonResult {
    private Integer success;    //0 代表失败，1 代表成功
    private String message;     //描述信息
    private String url; //上传失败时，可不设置url
    public UploadImgJsonResult(){
    }
    public UploadImgJsonResult(Integer success, String message, String url) {
        this.success = success;
        this.message = message;
        this.url = url;
    }
    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
