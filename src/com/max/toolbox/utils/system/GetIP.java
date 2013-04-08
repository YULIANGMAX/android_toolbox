
package com.max.toolbox.utils.system;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;

public class GetIP {

    public static void main(String[] args) {
        StringBuffer sbFileContent = new StringBuffer();
        try {
            InetAddress host = InetAddress.getLocalHost();

            String hostName = host.getHostName();
            String hostAddr = host.getHostAddress();
            String canonicalHostName = host.getCanonicalHostName();
            Date da = new Date();
            String osname = System.getProperty("os.name");
            String osversion = System.getProperty("os.version");
            String username = System.getProperty("user.name");
            String userhome = System.getProperty("user.home");
            String userdir = System.getProperty("user.dir");

            System.out.println("hostName:" + hostName);
            System.out.println("hostAddr:" + hostAddr);
            System.out.println("canonicalHostName:" + canonicalHostName);
            System.out.println("Current Date:" + da.toString());
            System.out.println("osname:" + osname);
            System.out.println("osversion:" + osversion);
            System.out.println("username:" + username);
            System.out.println("userhome:" + userhome);
            System.out.println("userdir:" + userdir);

            String url = "http://www.cz88.net/ip/viewip778.aspx";
            StringBuffer strForeignIP = new StringBuffer("strForeignIPUnkown");
            StringBuffer strLocation = new StringBuffer("strLocationUnkown");

            if (GetIP.getWebIp(url, strForeignIP, strLocation)) {
                System.out.println("Foreign IP is:" + strForeignIP);
                System.out.println("Location is:" + strLocation);
            }
            else {
                System.out.println("Failed to connect:" + url);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        sbFileContent.insert(0, "sucess" + "\n");
    }

    public static boolean getWebIp(String strUrl, StringBuffer strForeignIP, StringBuffer strLocation) {
        try {
            URL url = new URL(strUrl);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String s = "";
            StringBuffer sb = new StringBuffer("");
            while ((s = br.readLine()) != null) {
                sb.append(s + "\r\n");
            }
            br.close();
            String webContent = "";
            webContent = sb.toString();
            if (webContent.equals(null) || webContent.equals("")) {
                return false;
            }
            String flagofForeignIPString = "IPMessage";
            int startIP = webContent.indexOf(flagofForeignIPString) + flagofForeignIPString.length() + 2;
            int endIP = webContent.indexOf("</span>", startIP);
            strForeignIP.delete(0, webContent.length());
            strForeignIP.append(webContent.substring(startIP, endIP));
            String flagofLocationString = "AddrMessage";
            int startLoc = webContent.indexOf(flagofLocationString) + flagofLocationString.length() + 2;
            int endLoc = webContent.indexOf("</span>", startLoc);
            strLocation.delete(0, webContent.length());
            strLocation.append(webContent.substring(startLoc, endLoc));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
