package org.jmxtrans.embedded;

import java.lang.management.ManagementFactory;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.jmxtrans.embedded.config.ConfigurationParser;
import org.jmxtrans.embedded.util.StringUtils2;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

public class JmxtransDruidTest extends TestCase {

	protected final Logger LOG = LoggerFactory.getLogger(getClass());

	public static final String DEFAULT_CONFIG_LOCATION = "classpath:jmxtrans.json";

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private EmbeddedJmxTrans embeddedJmxTrans;
	private ObjectName objectName;
	private MBeanServer mbeanServer;

	@Before
	public void testBefore() {

		LOG.debug(" Start embedded-jmxtrans ...");

		mbeanServer = ManagementFactory.getPlatformMBeanServer();

		ConfigurationParser configurationParser = new ConfigurationParser();

		List<String> configurationUrls = StringUtils2.delimitedStringToList(DEFAULT_CONFIG_LOCATION);
		embeddedJmxTrans = configurationParser.newEmbeddedJmxTrans(configurationUrls);
		String on = "org.jmxtrans.embedded:type=EmbeddedJmxTrans,name=jmxtrans,path=test";
		try {
			objectName = mbeanServer.registerMBean(embeddedJmxTrans, new ObjectName(on)).getObjectName();
		} catch (Exception e) {
			throw new EmbeddedJmxTransException("Exception registering '" + objectName + "'", e);
		}
		try {
			embeddedJmxTrans.start();
		} catch (Exception e) {
			String message = "Exception starting jmxtrans for application 'test'";
			LOG.debug(message, e);
			throw new EmbeddedJmxTransException(message, e);
		}
	}

	@After
	public void contextDestroyed() {
		LOG.debug("Stop embedded-jmxtrans ...");

		try {
			mbeanServer.unregisterMBean(objectName);
		} catch (Exception e) {
			LOG.error("Silently skip exception unregistering mbean '" + objectName + "'");
		}
		try {
			embeddedJmxTrans.stop();
		} catch (Exception e) {
			throw new EmbeddedJmxTransException("Exception stopping '" + objectName + "'", e);
		}
	}

}
