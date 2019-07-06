/*
 * Envirover confidential
 * 
 *  [2018] Envirover
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains the property of 
 * Envirover and its suppliers, if any.  The intellectual and technical concepts
 * contained herein are proprietary to Envirover and its suppliers and may be 
 * covered by U.S. and Foreign Patents, patents in process, and are protected
 * by trade secret or copyright law.
 * 
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Envirover.
 */

package com.envirover.spl.rest;

import java.io.IOException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.MAVLink.common.msg_high_latency;
import com.envirover.geojson.FeatureCollection;
import com.envirover.spl.uvtracks.Config;
import com.envirover.uvnet.mission.Plan;
import com.envirover.uvnet.shadow.PersistentUVShadow;
import com.envirover.uvnet.shadow.UVShadow;

/**
 * A REST resource that provides access to vehicle tracks and missions.
 * 
 */

@Path("/v1")
public class UVTracksResource {

    private final static String DEFAULT_TOP = "100";

    private final UVShadow shadow;

    public UVTracksResource() {
        Config config = Config.getInstance();
       
        try {
            config.init();
        } catch (IOException e) {
            e.printStackTrace();
        }

        shadow = new PersistentUVShadow(config.getElasticsearchEndpoint(), 
                                        config.getElasticsearchPort(),
                                        config.getElasticsearchProtocol());
    }

    /**
     * Returns track of the specified system as GeoJSON feature collection of points
     * or a line string.
     * 
     * The query supports range the tracks by start and/or end times of the reports.
     * 
     * @param sysid system Id. Default value is 1.
     * @param startTime (optional) track start time in UNIX epoch time.
     * @param endTime (optional) track end time in UNIX epoch time.
     * @param top maximum number of points returned
     * @return GeoJSON feature collection
     * @throws IOException on I/O error
     */
    @GET
    @Path("/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    public FeatureCollection getTracks(@QueryParam("sysid") Integer sysid,
            @QueryParam("startTime") Long startTime, @QueryParam("endTime") Long endTime,
            @DefaultValue(DEFAULT_TOP) @QueryParam("top") int top) throws IOException {
        if (sysid == null) {
            sysid = Config.getInstance().getMavSystemId();
        }
    
        return shadow.queryMessages(sysid, msg_high_latency.MAVLINK_MSG_ID_HIGH_LATENCY, startTime, endTime, top);
    }

    /**
     * Returns mission plan of the specified system.
     * 
     * @param sysid sysid system Id. Default value is 1.
     * @return mission plan
     * @throws IOException in case of I/O error
     */
    @GET
    @Path("/missions")
    @Produces(MediaType.APPLICATION_JSON)
    public Plan getMissions(@QueryParam("sysid") Integer sysid) throws IOException {
        if (sysid == null) {
            sysid = Config.getInstance().getMavSystemId();
        }
    
        Plan plan = new Plan(shadow.getMission(sysid));
        // plan.getMission().setVehicleType(Config.getInstance().getMavType());
        return plan;
    }

}
