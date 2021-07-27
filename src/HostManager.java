import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HostManager
{
    public static List<Host> hosts;

    public HostManager(String startIP, String endIP) {
        hosts = Collections.synchronizedList(new ArrayList<Host>());

        List<IPAddress> ipRange = IPManager.getIPRange(startIP, endIP);
        for (int i = 0; i < ipRange.size(); i++)
        {
            IPConnectionSearch ipConnectionSearch = new IPConnectionSearch(ipRange.get(i));
            new Thread(ipConnectionSearch).start();
        }
//        List<IPAddress> ipAddresses = IPManager.verifyRemoteConnections(startIP, endIP);
//        List<MacAddress> macAddresses = IPManager.findMacAddresses(ipAddresses);
//
//        for (int i = 0; i < ipAddresses.size(); i++)
//            hosts.add(new Host(ipAddresses.get(i), macAddresses.get(i)));
    }

    @Override
    public String toString()
    {
        String hostsRepresentation = "";
        for (Host host : hosts)
            hostsRepresentation += host.toString() + "\n";

        return hostsRepresentation;
    }
}
