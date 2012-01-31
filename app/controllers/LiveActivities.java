package controllers;

import play.Logger;
import play.libs.F.EventStream;
import play.mvc.WebSocketController;

/**
 * Websocket tests
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class LiveActivities extends WebSocketController {

    /** The live activities stream. */
    public static EventStream<String> liveStream = new EventStream<String>();

    /**
     * Listen to live activities.
     */
    public static void listen() {
        while (inbound.isOpen()) {
            try {
                Logger.info("Waiting for next activities...");
                String search = await(liveStream.nextEvent());
                if (search != null) {
                    outbound.send(search);
                }
            } catch (Throwable t) {
                Logger.error(t, "Error while broadcasting live activities");
            }
        }
    }
}