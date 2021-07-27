import java.util.ArrayList;
import java.util.List;

public class HostManager
{
    private List<Host> hosts;

    public HostManager(String startIP, String endIP) {
        hosts = new ArrayList<Host>();

        List<IPAddress> ipAddresses = IPManager.verifyRemoteConnections(startIP, endIP);
        List<MacAddress> macAddresses = IPManager.findMacAddresses(ipAddresses);

        for (int i = 0; i < ipAddresses.size(); i++)
            hosts.add(new Host(ipAddresses.get(i), macAddresses.get(i)));
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
