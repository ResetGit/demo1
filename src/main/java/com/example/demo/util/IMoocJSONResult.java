package com.example.demo.util;

/**
 * @Description: 自定义响应数据结构
 * 				这个类是提供给门户，ios，安卓，微信商城用的
 * 				门户接受此类数据后需要使用本类的方法转换成对于的数据类型格式（类，或者list）
 * 				其他自行处理
 * 				200：表示成功
 * 				500：表示错误，错误信息在msg字段中
 * 				501：bean验证错误，不管多少个错误都以map形式返回
 * 				502：拦截器拦截到用户token出错
 * 				555：异常抛出信息
 */
public class IMoocJSONResult {

    // 响应业务状态
    private Integer status;

    private Integer code;

    private Integer count;
    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;
    
    private String ok;	// 不使用

    public static IMoocJSONResult build(Integer status, String msg, Object data,Integer code,Integer count) {
        return new IMoocJSONResult(status, msg, data,code,count);
    }

    public static IMoocJSONResult ok(Object data) {
        return new IMoocJSONResult(data);
    }

    public static IMoocJSONResult ok() {
        return new IMoocJSONResult(null);
    }
    
    public static IMoocJSONResult errorMsg(String msg) {
        return new IMoocJSONResult(500, msg, null,0,null);
    }
    
    public static IMoocJSONResult errorMap(Object data) {
        return new IMoocJSONResult(501, "error", data,0,null);
    }
    
    public static IMoocJSONResult errorTokenMsg(String msg) {
        return new IMoocJSONResult(502, msg, null,0,null);
    }
    
    public static IMoocJSONResult errorException(String msg) {
        return new IMoocJSONResult(555, msg, null,0,null);
    }


    public IMoocJSONResult(Integer status, String msg, Object data, Integer code, Integer count) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.code = code;
        this.count = count;
    }

    public IMoocJSONResult(Object data) {
        this.code = 0;
        this.status = 200;
        this.msg = "OK";
        this.data = data;
        this.count = count;
    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

	public String getOk() {
		return ok;
	}

	public void setOk(String ok) {
		this.ok = ok;
	}
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = 0;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
