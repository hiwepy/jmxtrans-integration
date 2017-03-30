/*
 * Copyright (c) 2010-2016 the original author or authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package org.jmxtrans.embedded.output.influxdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jmxtrans.embedded.util.HostnameUtil;

/**
 * @author Kristoffer Erlandsson
 */
public class InfluxMetricConverter {

    public static InfluxMetric convertToInfluxMetric(String metricName, Object value, List<InfluxTag> additionalTags, long timestamp) {
        List<InfluxTag> tagsFromMetricName = parseTags(metricName);
        List<InfluxTag> allTags = new ArrayList<InfluxTag>(additionalTags);
        allTags.addAll(tagsFromMetricName);
        return new InfluxMetric(parseMeasurement(metricName), allTags, value, timestamp);
    }

    private static String parseMeasurement(String metricName) {
        return metricName.split(",")[0].trim();
    }

    private static List<InfluxTag> parseTags(String metricName) {
        int startOfTags = metricName.indexOf(',');
        if (startOfTags < 0) {
            return new ArrayList<InfluxTag>();
        }
        return tagsFromCommaSeparatedString(metricName.substring(startOfTags + 1));
    }

    public static List<InfluxTag> tagsFromCommaSeparatedString(String s) {
        List<InfluxTag> tags = new ArrayList<InfluxTag>();
        if (s.trim().isEmpty()) {
            return tags;
        }
        String[] parts = s.split(",");
        for (String tagPart : parts) {
            tags.add(parseOneTag(tagPart));
        }
        return tags;
    }

    private static InfluxTag parseOneTag(String part) {
        String[] nameAndValue = part.trim().split("=");
        if (nameAndValue.length != 2) {
            throw new FailedToConvertToInfluxMetricException(
                    "Error when parsing influx tags from substring " + part + ", must be on format <name>=<value>,...");
        }
        String tagVal = nameAndValue[1].trim();
        
        // 1、获取系统的相关属性，包括文件编码、操作系统名称、区域、用户名等，此属性一般由jvm自动获取，不能设置
        Properties properties = System.getProperties();
        if(properties.contains(tagVal)){
        	tagVal = System.getProperty(tagVal);
        }
        
        // 2、获取指定的环境变量的值。环境变量是依赖于系统的外部命名值
 		String result = System.getenv(tagVal);
 		if (result != null) {
 			tagVal =  result;
 		}
     		
        //3、获取特殊标记值
        if("#hostname#".equalsIgnoreCase(nameAndValue[1].trim())){
        	tagVal = HostnameUtil.getHostName();
        }
        
        InfluxTag tag = new InfluxTag(nameAndValue[0].trim(), tagVal);
        return tag;
    }

    @SuppressWarnings("serial")
    public static class FailedToConvertToInfluxMetricException extends RuntimeException {

        public FailedToConvertToInfluxMetricException(String msg) {
            super(msg);
        }

    }
}
