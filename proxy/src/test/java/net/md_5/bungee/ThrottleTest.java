package net.md_5.bungee;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Assert;
import org.junit.Test;

public class ThrottleTest
{

    @Test
    public void testThrottle() throws InterruptedException, UnknownHostException
    {
    	int throttleMillis = 1000;
        ConnectionThrottle throttle = new ConnectionThrottle( throttleMillis );
        InetAddress address;

        try
        {
            address = InetAddress.getLocalHost();
        } catch ( UnknownHostException ex )
        {
            address = InetAddress.getByName( null );
        }

        Assert.assertFalse( "Address should not be throttled", throttle.throttle( address ) );
        Assert.assertTrue( "Address should be throttled", throttle.throttle( address ) );

        Thread.sleep( throttleMillis * 2 );
        Assert.assertFalse( "Address should not be throttled", throttle.throttle( address ) );
    }
}
