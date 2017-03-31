package org.jmxtrans.embedded;

import org.jmxtrans.embedded.util.network.MacAddressUtils;

import junit.framework.TestCase;

public class MacAddressUtils_Test extends TestCase {

	public void testMacAddress_1() {
		for (String mac : MacAddressUtils.getAllMacAddresses()) {
			System.out.println("Mac Address is : "+ mac);
		}
	}

	public void testRemoteMacAddr_2() {
		System.out.println("MacAddress is : " + MacAddressUtils.getMacAddress());
	}
	
	public void testRemoteMacAddr_4() {
		System.out.println("RemoteMacAddr is : " + MacAddressUtils.getRemoteMacAddr("192.168.31.54"));
	}
	
	public void testRemoteMacAddr_3() {
		System.out.println("HostMacAddress is : " + MacAddressUtils.getHostMacAddress("192.168.31.54"));
	}
	
}
