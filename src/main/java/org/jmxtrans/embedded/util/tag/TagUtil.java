package org.jmxtrans.embedded.util.tag;

import java.util.Properties;

import org.jmxtrans.embedded.output.influxdb.InfluxTag;

public class TagUtil {

    public static InfluxTag parseTag(String tagName,String tagVal) {  
    	
    	//1、环境变量取值
    	String tagVal_ = getTagValFromEnv(tagVal);
    	
    	//2、获取特殊标记值
        if(tagVal.equalsIgnoreCase(TagEnum.CANONICAL_HOST_NAME.getName())){
        	tagVal_ = TagEnum.CANONICAL_HOST_NAME.getDefault();
        }else if(tagVal.equalsIgnoreCase(TagEnum.HOST_NAME.getName())){
        	tagVal_ = TagEnum.HOST_NAME.getDefault();
        }else if(tagVal.equalsIgnoreCase(TagEnum.HOST_ADDRESS.getName())){
        	tagVal_ = TagEnum.HOST_ADDRESS.getDefault();
        }else if(tagVal.equalsIgnoreCase(TagEnum.MAC_ADDRESS.getName())){
        	tagVal_ = TagEnum.MAC_ADDRESS.getDefault();
        }
        
        return new InfluxTag(tagName, tagVal_);
    }
	
    public static String getTagValFromEnv(String tagEnvName) {
		assert tagEnvName != null;
		String tagVal = null;
		// 1、获取系统的相关属性，包括文件编码、操作系统名称、区域、用户名等，此属性一般由jvm自动获取，不能设置
        Properties properties = System.getProperties();
        if(properties.contains(tagEnvName)){
        	tagVal = System.getProperty(tagEnvName);
        }
        
        // 2、获取指定的环境变量的值。环境变量是依赖于系统的外部命名值
 		String result = System.getenv(tagEnvName);
 		if (result != null) {
 			tagVal =  result;
 		}
 		return tagVal;
    }
    
}
