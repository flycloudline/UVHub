package com.envirover.mavlink;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;
import com.MAVLink.ardupilotmega.msg_battery2;
import com.MAVLink.common.msg_high_latency;

import org.apache.logging.log4j.Level;
import org.junit.Test;

public class MAVLinkTest {

    @Test
    public void testPackAndParse() {
        // Test pack and parse of HIGH_LATENCY common message 
        msg_high_latency msgHighLatency = new msg_high_latency();
        msgHighLatency.airspeed = 0;
        msgHighLatency.airspeed_sp = 1;
        msgHighLatency.altitude_amsl = 2;
        msgHighLatency.altitude_sp = 3;
        msgHighLatency.base_mode = 4;
        msgHighLatency.battery_remaining = 5;
        msgHighLatency.climb_rate = 6;
        msgHighLatency.custom_mode = 7;
        msgHighLatency.failsafe = 8;
        msgHighLatency.gps_fix_type = 9;
        msgHighLatency.gps_nsat = 10;
        msgHighLatency.groundspeed = 11;
        msgHighLatency.heading = 12;
        msgHighLatency.heading_sp = 13;
        msgHighLatency.landed_state = 14;
        msgHighLatency.latitude = 15;
        msgHighLatency.longitude = 16;
        msgHighLatency.pitch = 17;
        msgHighLatency.roll = 18;
        msgHighLatency.temperature = 19;
        msgHighLatency.temperature_air = 20;
        msgHighLatency.throttle = 21;
        msgHighLatency.wp_distance = 22;
        msgHighLatency.wp_num = 23;

        MAVLinkPacket originalPacket = msgHighLatency.pack();
        originalPacket.sysid = 1;
        originalPacket.compid = 190;

        originalPacket.seq = 123;

        MAVLinkLogger.log(Level.INFO, ">>", originalPacket);
        
        byte[] data = originalPacket.encodePacket();

        MAVLinkPacket parsedPacket = parseMessage(data);

        if (parsedPacket != null) {
            MAVLinkLogger.log(Level.DEBUG, "<<", parsedPacket);

            msg_high_latency parsedHighLatency = (msg_high_latency) parsedPacket.unpack();

            assertEquals(msgHighLatency.toString(), parsedHighLatency.toString());
        } else {
            fail("Failed to parse HIGH_LATENCY message.");
        }

        // Test pack and parse of BATTERY2 ardupilotmega message
        msg_battery2 msgBattery2 = new msg_battery2();
        msgBattery2.sysid = 1;
        msgBattery2.compid = 190;
        msgBattery2.current_battery = 0;
        msgBattery2.voltage = 1;

        originalPacket = msgBattery2.pack();
        originalPacket.sysid = 1;
        originalPacket.compid = 190;

        originalPacket.seq = 123;

        MAVLinkLogger.log(Level.INFO, ">>", originalPacket);
        
        data = originalPacket.encodePacket();

        parsedPacket = parseMessage(data);

        if (parsedPacket != null) {
            MAVLinkLogger.log(Level.DEBUG, "<<", parsedPacket);

            msg_battery2 parsedBattery2 = (msg_battery2) parsedPacket.unpack();

            assertEquals(msgBattery2.toString(), parsedBattery2.toString());
        } else {
            fail("Failed to parse BATTERY2 message.");
        }
    }

    private MAVLinkPacket parseMessage(byte[] data) {
        Parser parser = new Parser();

        MAVLinkPacket packet = null;

        for (int i = 0; i < data.length; i++) {
            int c = data[i] & 0xFF;
            packet = parser.mavlink_parse_char(c);

            if (packet != null) {
                return packet;
            }
        }

        fail(String.format("Received packet count: %d, CRC error count: %d", 
                           parser.stats.receivedPacketCount,
                           parser.stats.crcErrorCount));
    
        return null;
    }
}