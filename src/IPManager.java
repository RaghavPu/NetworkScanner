import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.net.InetAddress;

public class IPManager
{
    private static final int timeout = 300;
    private static final int retryCount = 3;


    public static List<IPAddress> getIPRange(String startIP, String endIP)
    {
        List<IPAddress> ipRange = new ArrayList<>();

        IPAddress start = new IPAddress(startIP);
        IPAddress end = new IPAddress(endIP);
        ipRecursiveHelper(ipRange, new ArrayList<Integer>(), start, end, start.getAddress(0), end.getAddress(0));
        return  ipRange;
    }

    private static void ipRecursiveHelper(List<IPAddress> ipRange, List<Integer> currentByteValues, IPAddress start, IPAddress end,
                                          int startByte, int endByte)
    {
        for (int byteVal = startByte; byteVal <= endByte; byteVal++)
        {
            if (currentByteValues.size() == 3)
            {
                ipRange.add(new IPAddress(currentByteValues.get(0), currentByteValues.get(1),
                        currentByteValues.get(2), byteVal));
            }
            else
            {
                List<Integer> newIpByteValues = new ArrayList<>(currentByteValues);
                newIpByteValues.add(byteVal);
                int newStartByte = (byteVal == start.getAddress(currentByteValues.size())) ?
                        start.getAddress(currentByteValues.size() + 1) : 0;
                int newEndByte = (byteVal == end.getAddress(currentByteValues.size())) ?
                        end.getAddress(currentByteValues.size() + 1) : 255;
                ipRecursiveHelper(ipRange, newIpByteValues, start, end, newStartByte, newEndByte);
            }
        }
    }

//    public static List<IPAddress> getIPRange(String startIP, String endIP)
//    {
//        IPAddress startIPAddress = new IPAddress(startIP);
//        IPAddress endIPAddress = new IPAddress(endIP);
//
//        List<IPAddress> ipRange = new ArrayList<>();
//
//        for (int firstByte = startIPAddress.getAddress()[0]; firstByte <= endIPAddress.getAddress()[0]; firstByte++)
//        {
//            int secondByteStart = (firstByte == startIPAddress.getAddress()[0]) ? startIPAddress.getAddress()[1] : 0;
//            int secondByteRange = (firstByte == endIPAddress.getAddress()[0]) ?
//                    endIPAddress.getAddress()[1] : 255;
//
//            for (int secondByte = startIPAddress.getAddress()[1]; secondByte <= endIPAddress.getAddress()[1]; secondByte++)
//            {
//                int thirdByteStart = (secondByte == startIPAddress.getAddress()[1]) ? startIPAddress.getAddress()[2] : 0;
//                int thirdByteRange = (secondByte == endIPAddress.getAddress()[1]) ?
//                        endIPAddress.getAddress()[2] : 255;
//
//                for (int thirdByte = startIPAddress.getAddress()[2]; thirdByte <= thirdByteRange; thirdByte++)
//                {
//                    int fourthByteStart = (thirdByte == startIPAddress.getAddress()[2]) ? startIPAddress.getAddress()[3] : 0;
//                    int fourthByteRange = (thirdByte == endIPAddress.getAddress()[2]) ?
//                            endIPAddress.getAddress()[3] : 255;
//
//                    for (int fourthByte = fourthByteStart; fourthByte <= fourthByteRange; fourthByte++)
//                    {
//                        ipRange.add(new IPAddress(firstByte, secondByte, thirdByte, fourthByte));
//                    }
//                }
//            }
//        }
//
//
//        return ipRange;
//    }

    public static List<IPAddress> verifyRemoteConnections(String startIP, String endIP)
    {
        List<IPAddress> ipRange = getIPRange(startIP, endIP);
            List<IPAddress> connectedIPAddresses = new ArrayList<>();
            for (int i = 0; i < ipRange.size(); i++)
            {
                checkForConnection(ipRange, connectedIPAddresses, i);
                System.out.println("Checking IP: " + ipRange.get(i));
            }

            System.out.print("\033[H\033[2J");
            System.out.flush();

            return connectedIPAddresses;
    }

    private static void checkForConnection(List<IPAddress> ipRange, List<IPAddress> connectedIPAddresses, int i)
    {
        IPAddress ipAddress = ipRange.get(i);
        InetAddress inetAddress = getInetAddress(ipAddress);
        if (inetAddress == null) return;


        boolean connectionMade = false;
        for (int j = 0; j < retryCount; j++)
        {
            try
            {
                connectionMade = inetAddress.isReachable(timeout);
            }
            catch (IOException e){}

            if (connectionMade) break;
        }
        if (connectionMade)
        {
            connectedIPAddresses.add(ipAddress);
            System.out.println("Connection @ " + ipAddress);
        }
    }

    private static InetAddress getInetAddress(IPAddress ipAddress)
    {
        InetAddress inetAddress = null;
        try
        {
            inetAddress = InetAddress.getByName(ipAddress.toString());
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }

        return inetAddress;
    }

    public static List<MacAddress> findMacAddresses(List<IPAddress> ipAddressList)
    {
        List<MacAddress> macAddressList = new ArrayList<>(ipAddressList.size());
        for (IPAddress ipAddress : ipAddressList)
        {
            macAddressList.add(findMacAddress(ipAddress));
        }
        return macAddressList;
    }


    public static MacAddress findMacAddress(IPAddress ipAddress)
    {
        MacAddress macAddress = null;
        String terminalOutput = runTerminalCommand(ipAddress);

        if (terminalOutput.contains("no entry"))
            throw new InputMismatchException("IP Address connection not found.");

        macAddress = new MacAddress(terminalOutput.split(" ")[3]);

        return macAddress;
    }


    //Build a Utility Class
    private static String runTerminalCommand(IPAddress ipAddress)
    {
        String terminalOutput = null;
        try
        {
            Process process = Runtime.getRuntime().exec("arp -n " + ipAddress.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            terminalOutput = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return terminalOutput;
    }
}
