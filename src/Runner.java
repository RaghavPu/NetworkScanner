import java.util.List;

public class Runner
{
    public static void main(String[] args) throws IllegalArgumentException
    {
        HostManager hostManager = new HostManager("192.168.1.0", "192.168.1.255");
        System.out.println(hostManager.toString());
//        System.out.println(IPManager.getIPRange("192.168.1.10", "192.168.2.250"));
//        List<IPAddress> ipAddresses = IPManager.verifyRemoteConnections("192.168.1.0", "192.168.2.250");
//        System.out.println(IPManager.findMacAddresses(ipAddresses));
//        IPManager.findMacAddress(new IPAddress("192.168.86.1"));
//        System.out.println(IPAddress.cidrToIpRange("192.168.1.0/32"));
    }
}
