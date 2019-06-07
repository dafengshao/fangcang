package com.learn.ren;

import java.security.InvalidParameterException;
import java.util.Arrays;

public final class IpInfo {
	private byte[] bytes = new byte[4];;

	public IpInfo(String ip) {
		String[] split = ip.split("[.]");
		if(split.length!=4) {
			throw new InvalidParameterException("ip格式错误,ip="+ip);
		}
		//this.hashCode = ip.hashCode();
		for(int i=0;i<4 ;i++){
            int m=Integer.parseInt(split[i]);
            byte b=(byte) m;
            if(m>127){ //如果超出byte表数范围,则变成负数
                b=(byte)(127-m);
            }
            bytes[i]=b;
        }
	}
	
	
	public byte[] getBytes() {
		return bytes;
	}


	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}


	@Override
	public String toString() {
		StringBuilder ipStr = new StringBuilder();
		for(int i=0;i<4;i++){
            String tmp=String.valueOf(bytes[i]);
            if(bytes[i]<0){
                tmp=String.valueOf(127+Math.abs(bytes[i]));
            }
            ipStr.append(tmp);
            if(i<3){
            	ipStr.append(".");
            }
        }
		return ipStr.toString();
	}
	
	@Override
	public int hashCode() {
		return this.getBytes()[0]*2+this.getBytes()[1]*2+this.getBytes()[2]*2+this.getBytes()[3]*2;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IpInfo other = (IpInfo) obj;
		return other.getBytes()[0]==this.getBytes()[0]
				&&other.getBytes()[1]==this.getBytes()[1]
						&&other.getBytes()[2]==this.getBytes()[2]
								&&other.getBytes()[3]==this.getBytes()[3]
						;
	}
	
}
