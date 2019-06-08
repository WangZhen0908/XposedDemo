package com.moon.xposed.hookbase;

/**
 * author WangZhen
 * created 2019/3/2 14:11
 */
public class HookMethod {

    private String className;
    private String methodName;
    private Class[] paramsTypes;

    public HookMethod(String className, String methodName, Class... paramsTypes) {
        this.className = className;
        this.methodName = methodName;
        this.paramsTypes = paramsTypes;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParamsTypes() {
        return paramsTypes;
    }

    public void setParamsTypes(Class[] paramsTypes) {
        this.paramsTypes = paramsTypes;
    }
}
