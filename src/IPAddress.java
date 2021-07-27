import java.util.Arrays;

public class IPAddress
{
    private int[] address;

    public IPAddress(String ipString)
    {
        String[] ipDivisions = ipString.split("\\.");

        if (ipDivisions.length != 4)
            throw new IllegalArgumentException("IP Address does not consist of two bytes");

        address = new int[ipDivisions.length];
        for (int i = 0; i < address.length; i++)
            address[i] = Integer.parseInt(ipDivisions[i]);
    }

    public IPAddress(int firstByte, int secondByte, int thirdByte, int fourthByte)
    {
        address = new int[]{firstByte, secondByte, thirdByte, fourthByte};
    }

    public int[] getAddress()
    {
        return address;
    }

    public int getAddress(int num)
    {
        return address[num];
    }

    public void setAddress(int octetNumber, int val)
    {
        address[octetNumber] = val;
    }

    public static String[] cidrToIpRange(String cidr)
    {
        String[] cidrComponents = cidr.split("/");
        System.out.println(Arrays.toString(cidrComponents));

        IPAddress start = new IPAddress(cidrComponents[0]);
        IPAddress end = new IPAddress(cidrComponents[0]);

        int bytesToShift = 32 - Integer.parseInt(cidrComponents[1]);
        setOctetValues(start, end, bytesToShift);


        return new String[]{start.toString(), end.toString()};
    }

    private static void setOctetValues(IPAddress start, IPAddress end, int bytesToShift)
    {
        for (int i = 1; i <= 4; i++)
        {
            int shiftNum = Math.max(Math.min(8, bytesToShift - ((4 - i) * 8)), 0);
            if (shiftNum != 0)
            {
                start.setAddress(i - 1, 0);
                end.setAddress(i - 1, (int)Math.pow(2, shiftNum) - 1);
            }
        }
        System.out.println(start.toString() + " : " + end.toString());
    }

    @Override
    public String toString()
    {
        return address[0] + "." +
                address[1] + "." +
                address[2] + "." +
                address[3];
    }
}
