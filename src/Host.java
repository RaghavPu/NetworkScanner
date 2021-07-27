import java.util.Locale;

public class Host
{
    private IPAddress ipAddress;
    private MacAddress macAddress;
    private String vendor;

    public Host(IPAddress ipAddress, MacAddress macAddress)
    {
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.vendor = getVendor(macAddress);
    }


    public static String getVendor(MacAddress macAddress) {
        String macAddressString = macAddress.toString()
                .replace(':', '-')
                .substring(0, 8)
                .toUpperCase();

        return OUIMapCreator.OUI_VENDOR_MAP.get(macAddressString);
    }

    @Override
    public String toString()
    {
        return ipAddress.toString() + "\t" +
                macAddress.toString() + "\t" +
                vendor;
    }
}
