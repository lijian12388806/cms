/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xkw.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class ReflectionUtils {

    @SuppressWarnings("rawtypes")
	public static Class[] EMPTY_CLASS_ARRAY = {};
    public static Object[] EMPTY_OBJECT_ARRAY = {};

    public static Object getPropertyValue(Object targetObject, String propertyName) throws
            NoSuchMethodException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {
        String[] tokens = propertyName.split("\\.");
        Object result = null;
        for (String token : tokens) {
            if (result != null) {
                targetObject = result;
            }
            String getter = getter(token);
            Method method = targetObject.getClass().getMethod(getter, EMPTY_CLASS_ARRAY);
            result = method.invoke(targetObject, EMPTY_OBJECT_ARRAY);
            if(result == null) {
                return null;
            }
        }
        return result;
    }

    public static String getter(String property) {
        return "get" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
    }

    public static final Method findMethod(String name, Class targetClass, boolean allowParamAncestorClass, Class<?>... parameterTypes) throws NoSuchMethodException {
        try {
            return targetClass.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            if (!allowParamAncestorClass || parameterTypes == null || parameterTypes.length == 0) {
                throw e;
            }
        }
        for (Method method : targetClass.getMethods()) {
            Class<?>[] methodParameterTypes = method.getParameterTypes();
            if (methodParameterTypes.length != parameterTypes.length) {
                continue;
            }
            if (!method.getName().equals(name)) {
                continue;
            }
            boolean match = true;
            for (int i = 0; i < parameterTypes.length; i++) {
                if (!isDescendantClassOrImplementation(parameterTypes[i], methodParameterTypes[i])) {
                    match = false;
                    break;
                }
            }
            if (match) {
                return method;
            }
        }
        throw new NoSuchMethodException();
    }

    public static boolean isDescendantClassOrImplementation(Class descendandClass, Class targetClass) {
        while ((descendandClass = descendandClass.getSuperclass()) != null) {
            if (targetClass.isInterface()) {
                for (Class interfaceClass : descendandClass.getInterfaces()) {
                    if (interfaceClass == targetClass) {
                        return true;
                    }
                }
            }
            if (descendandClass == targetClass) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 动态设置
     *
     * @param pObj
     * @param pMethodName
     * @param
     */
    public static void setSpecifyMethodValue(Object pObj, String pMethodName, Class [] pParamClassArr, Object [] pParamObjArr) {
        try {
            Method method = pObj.getClass().getMethod(pMethodName, pParamClassArr);
            method.invoke(pObj, pParamObjArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getSpecifyMethodValue(Object pObj, String pMethodName) {
        Object value = null;
        try {
            Method method = pObj.getClass().getMethod(pMethodName, (Class<?>)null);
            value = method.invoke(pObj, (Object[])null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 得到OBJ List的指定方法的值的集合
     *
     * @param pObjList
     * @return value集合
     */
    public static List getSpecifyMethodValues(List pObjList, String pMethodName) {
        List retObjects = null;

        for (int i = 0; pObjList != null && i < pObjList.size(); i++) {
            if (retObjects == null) {
                retObjects = new ArrayList();
            }
            Object obj = pObjList.get(i);
            Object value = getSpecifyMethodValue(obj, pMethodName);
            retObjects.add(value);
        }

        return retObjects;
    }        
}
