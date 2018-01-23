package cn.worldwalker.game.wyqp.common.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IPUtil {
	private static final Log log = LogFactory.getLog(IPUtil.class);
	
	public static String getLocalIp(){
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}
	
	public static String getLocalIp1(){
		StringBuilder IFCONFIG=new StringBuilder();  
		   try {  
		       for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {  
		           NetworkInterface intf = en.nextElement();  
		           for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {  
		               InetAddress inetAddress = enumIpAddr.nextElement();  
		               if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {  
		               IFCONFIG.append(inetAddress.getHostAddress().toString()+"\n");  
		               }  
		  
		           }  
		       }  
		   } catch (SocketException ex) {  
		   }  
		   System.out.println(IFCONFIG);
		   return null;
	}
	
	
	
    public static String getRemoteIp(HttpServletRequest request ) {
    	String newIP = request.getHeader("J-Forwarded-For");
    	if (newIP != null && newIP.length() != 0) {
    		log.info("J-Forwarded-For : " + newIP);
    		return newIP;
    	}
    	
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            ip = ip.split(",")[0];
        }
        return ip;
    }
    
    public static long ipToLong(String ip)
	{
		int firstDot = ip.indexOf(".");
		int secDot = ip.indexOf(".", firstDot + 1);
		int thirdDot = ip.indexOf(".", secDot + 1);

		if (-1 == firstDot || -1 == secDot || -1 == thirdDot)
		{
			throw new IllegalArgumentException("illegal argument");
		}

		String firstStr = ip.substring(0, firstDot);
		String secStr = ip.substring(firstDot + 1, secDot);
		String thirdStr = ip.substring(secDot + 1, thirdDot);
		String fourthStr = ip.substring(thirdDot + 1, ip.length());

		long firstLong = Long.parseLong(firstStr);
		firstLong = firstLong << 24;

		long secLong = Long.parseLong(secStr);
		secLong = secLong << 16;

		long thirdLong = Long.parseLong(thirdStr);
		thirdLong = thirdLong << 8;

		long fourthLong = Long.parseLong(fourthStr);

		return firstLong + secLong + thirdLong + fourthLong;
	}

	public static String longToIp(long ipLong)
	{
		long temp = 0;
		long first = ipLong / (1 << 24);
		temp = ipLong % (1 << 24);

		long sec = temp / (1 << 16);
		temp = temp % (1 << 16);

		long third = temp / (1 << 8);
		temp = temp % (1 << 8);

		long fourth = temp;

		StringBuilder sb = new StringBuilder();
		sb.append(first).append(".").append(sec).append(".").append(third)
				.append(".").append(fourth);
		
		
		return sb.toString();
	}
}
