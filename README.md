# jmxtrans-integration

embedded-jmxtrans 整合  Spring、Servlet、InfluxDB

## 1、Servlet Integration

参考EmbeddedJmxTransLoaderListener对象注解
```xml
<context-param>
	<param-name>jmxtrans.config</param-name>
	<param-value>
		classpath:jmxtrans.json
		classpath:org/jmxtrans/embedded/config/jmxtrans-internals-servlet-container.json
		classpath:org/jmxtrans/embedded/config/tomcat-7.json
		classpath:org/jmxtrans/embedded/config/jvm-sun-hotspot.json
	</param-value>
</context-param>
<listener>
	<listener-class>org.jmxtrans.embedded.servlet.EmbeddedJmxTransLoaderListener</listener-class>
</listener>
```
## 2、Spring Integration

本人参考 embedded-jmxtrans 的github地址 https://github.com/jmxtrans/embedded-jmxtrans/wiki 配置后，测试完全无效。
阅读代码后采用另种方式：

配置EmbeddedJmxTransFactory
```xml
<bean id="jmxtrans" class="org.jmxtrans.embedded.spring.EmbeddedJmxTransFactory" destroy-method="destroy" scope="singleton">
	<!-- JMX对象名称  -->
	<property name="beanName" value="jmxtrans"/>
	<!-- 监听配置文件变化: 每60秒检测一次  -->
	<property name="configurationScanPeriodInSeconds" value="60"/>
	<!-- 如果未发现指定的配置文件 ，是否忽略 -->
	<property name="ignoreConfigurationNotFound" value="true"/>
	<!-- 初始配置文件 -->
	<property name="configurationUrls">
		<list>
         	<value>classpath:jmxtrans.json</value>  
         	<value>classpath:org/jmxtrans/embedded/config/tomcat-7.json</value>
         	<value>classpath:org/jmxtrans/embedded/config/jmxtrans-internals.json</value> 
         	<value>classpath:org/jmxtrans/embedded/config/jvm-sun-hotspot.json</value>
     	</list>
	</property>
</bean>
```
使用EmbeddedJmxTransFactory；此处发现如果不引用上面的对象，则无法调用getObject方法实现对象初始化
```xml
<bean class="org.jmxtrans.embedded.EmbeddedJmxTransLauncher">
	<property name="jmxtrans" ref="jmxtrans"/>
</bean>
```

## 3、InfluxDB Integration

在第二则中提到配置文件 jmxtrans.json；为了将数据自己输出到InfluxDB,我拷贝了jmxtrans-output-influxdb的代码，做了一些调整，配置如下：


```json
{
    "queries": [
    	{
            "objectName": "Catalina:type=Manager,context=/,host=*",
            "resultAlias": "application.activeSessions",
            "attributes": [
                "activeSessions"
            ]

        }
    ],
    "outputWriters": [
        {
	        "@class": "org.jmxtrans.embedded.output.Slf4jWriter",
	        "settings": {
	            "enabled": "${jmxtrans.writer.slf4j.enabled:true}"
	        }
    	},
    	{
	        "@class": "org.jmxtrans.embedded.output.influxdb.InfluxDbOutputWriter",
	        "settings": {
	            "enabled": "${jmxtrans.writer.influxdb.enabled:true}",
	            "url": "${jmxtrans.writer.influxdb.url:http://localhost:8086}",
	            "database": "${jmxtrans.writer.influxdb.database:APP_Metrics}",
	            "user": "${jmxtrans.writer.influxdb.user:admin}",
	            "password": "${jmxtrans.writer.influxdb.password:admin}",
	            "tags": "${jmxtrans.writer.influxdb.tags:host=#hostname#}"
	            
	        }
    	}
    ]
}
```



