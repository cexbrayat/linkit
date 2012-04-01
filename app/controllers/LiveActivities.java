package controllers;

import com.google.gson.Gson;
import models.activity.Activity;
import play.Logger;
import play.libs.F.EventStream;
import play.mvc.Http;
import play.mvc.WebSocketController;

import java.util.ArrayList;
import java.util.List;

/**
 * Websocket tests
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class LiveActivities extends WebSocketController {

    /**
     * The live activities stream.
     */
    public static EventStream<Activity> liveStream = new EventStream<Activity>();
    private static Gson gson = new Gson();
    private static List<String> connecteds = new ArrayList<String>();

    /**
     * Listen to live activities.
     */
    public static void listen() {
        while (inbound.isOpen()) {
            try {
                //broadcast
                Activity activity = await(liveStream.nextEvent());
                if (activity != null) {
                    Logger.info("activity send " + activity + " from " + activity.member);
                    Logger.info("connecteds " + connecteds);
                    //bug json de serialize
                    //ne renvoyer l'activité que si cest une activité d'un linked d'un user connecte
                    outbound.send(gson.toJson(activity.at));
                }

                //listening
                Http.WebSocketEvent e = await(inbound.nextEvent());
                for (String msg : Http.WebSocketEvent.TextFrame.match(e)) {
                    Logger.info("Message received " + msg);
                    ConnectionMessage connectionMessage = gson.fromJson(msg, ConnectionMessage.class);
                    Logger.info("connection " + connectionMessage.toString());
                    Logger.info("connecteds " + connecteds);
                    if (connectionMessage.connect) {
                        connecteds.add(connectionMessage.id);
                    } else {
                        connecteds.remove(connectionMessage.id);
                    }
                    Logger.info("connecteds " + connecteds);
                }
                for (Http.WebSocketClose closed : Http.WebSocketEvent.SocketClosed.match(e)) {
                    Logger.info("Socket closed!");
                    //et oui mais je sais pas qui...
                }
            } catch (Throwable t) {
                Logger.error(t, "Error while broadcasting live activities");
            }
        }
    }

    public class ConnectionMessage {
        public String id;
        public boolean connect;

        public String toString(){
            return id + " " + connect;
        }
    }
}