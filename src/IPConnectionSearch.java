public class IPConnectionSearch implements Runnable
{
    private IPAddress ipAddress;
    private MacAddress macAddress;

    public IPConnectionSearch(IPAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void run() {
        boolean connectionMade = IPManager.checkForConnection(ipAddress);
        if (connectionMade) {
            macAddress = IPManager.findMacAddress(ipAddress);
            Host host = new Host(ipAddress, macAddress);
            System.out.println(host.toString());
            HostManager.hosts.add(host);
        }
    }

}
