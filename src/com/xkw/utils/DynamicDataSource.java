/**
 * 学科网
 */
package com.xkw.utils;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
/**
 * 多数据源配置
 * @author bamboo
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	public static final String DATA_SOURCE_XYH = "dataSourceXYH";
    public static final String DATA_SOURCE_ATXMAN = "dataSourceATXMAN";
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
    public static void setCustomerType(String customerType) {
            contextHolder.set(customerType);
    }
    public static String getCustomerType() {
            return contextHolder.get();
    }
    public static void clearCustomerType() {
            contextHolder.remove();
    }
    @Override
    protected Object determineCurrentLookupKey() {
            return getCustomerType();
    }
}
