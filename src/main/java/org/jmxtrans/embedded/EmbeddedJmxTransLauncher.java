package org.jmxtrans.embedded;

import javax.annotation.Resource;

public class EmbeddedJmxTransLauncher {
	 
	@Resource
	protected EmbeddedJmxTrans jmxtrans;
	
	public EmbeddedJmxTrans getJmxtrans() {
		return jmxtrans;
	}

	public void setJmxtrans(EmbeddedJmxTrans jmxtrans) {
		this.jmxtrans = jmxtrans;
	}
	
}
