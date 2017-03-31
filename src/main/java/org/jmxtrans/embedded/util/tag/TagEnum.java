package org.jmxtrans.embedded.util.tag;

import java.util.Locale;

import org.jmxtrans.embedded.util.network.InetAddressUtils;
import org.jmxtrans.embedded.util.network.MacAddressUtils;

public enum TagEnum {

	CANONICAL_HOST_NAME("#canonical#"){
		
		@Override
		public String getDefault() {
			return InetAddressUtils.getCanonicalHostName();
		}
		
	},

	HOST_NAME("#hostname#"){
		
		@Override
		public String getDefault() {
			return InetAddressUtils.getHostName();
		}
		
	},
	
	HOST_ADDRESS("#ip#"){
		
		@Override
		public String getDefault() {
			return InetAddressUtils.getHostAddress();
		}
		
	},
	
	MAC_ADDRESS("#mac#"){
		
		@Override
		public String getDefault() {
			return MacAddressUtils.getMacAddress();
		}
		
	};
	
	protected String name;
	protected String defaultValue = "";

	private TagEnum(String name) {
		this.name = name;
	}
	
	private TagEnum(String name,String defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public String getDefault() {
		return defaultValue;
	}
	
	static TagEnum valueOfIgnoreCase(String parameter) {
		return valueOf(parameter.toUpperCase(Locale.ENGLISH).trim());
	}
	
	static TagEnum valueOfIgnoreCase(String parameter,String defaultValue) {
		TagEnum parm = valueOf(parameter.toUpperCase(Locale.ENGLISH).trim());
		parm.defaultValue = defaultValue;
		return parm;
	}
	
}
