package de.teamFive.common.controller;

import de.teamFive.common.entities.CSMChoseOption;
import de.teamFive.common.entities.SSMGameState;
import de.teamFive.common.entities.WebSocketMessage;
import de.teamFive.common.services.GameProgressHandlerBean;
import de.teamFive.common.timer.SSMTimingMessage;
import de.teamFive.common.timer.TimingBean;
import de.teamFive.session.SessionService;

import javax.ejb.EJB;
import javax.ejb.Timer;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/game/", configurator = GetHttpSessionConfigurator.class)
public class GameStateEndpoint {
    @EJB
    SessionService sessionService;

    @EJB
    GameProgressHandlerBean gphb;

    @EJB
    private TimingBean timingBean;

    private Timer timer;

    private Session wsSession;
    private String sessionId;

    private Logger logger = Logger.getLogger(GameStateEndpoint.class.getCanonicalName());

    /**
     * Parses given message into JSON and sends it to the current client
     *
     * @param message to be parsed and sent
     */
    void sendToClient(WebSocketMessage message) {
        Jsonb jsonb = JsonbBuilder.create();
        try {
            String serialized = jsonb.toJson(message);
            logger.info(message + " was serialised " + serialized);
            wsSession.getBasicRemote().sendText(serialized);
        } catch (IOException e) {
            throw new RuntimeException("could not transform the message object ");
        }
    }

    /**
     * Parses given JSON-message and tries to retrieve used type
     *
     * @param message to be parsed and analysed
     * @return a message type
     */
    String getMessageType(String message) {
        JsonReader jr = Json.createReader(new StringReader(message));

        JsonObject obj = jr.readObject();

        try {
            return obj.getString("type");
        } catch (Exception e) {
            throw new RuntimeException("Received a message which had no type", e);
        }
    }

    /**
     * Checks if current {@link Session} is valid
     */
    private void ensureHTTPSessionIsValid() {
        if (!sessionService.doesExist(sessionId)) {
            try {
                wsSession.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            throw new RuntimeException("there is not http session for this websocket session");
        }
    }

    /**
     * If a websocket connection was established this function is called to save both
     * the {@link Session} itself and the sessionId
     *
     * @param session to be saved
     * @param config  which provides the sessionId
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        logger.log(Level.INFO, "session open {0}", session.getId());
        wsSession = session;
        sessionId = (String) config.getUserProperties()
                .get(HttpSession.class.getCanonicalName());

        wsSession.setMaxIdleTimeout(-1);
        ensureHTTPSessionIsValid();

        logger.info("Session " + sessionId + " was invalidated");

        sendToClient(gphb.getCurrentState(sessionId));
        timer = timingBean.startTimer(600000, () -> {
            this.sendToClient(new SSMTimingMessage("timer-message", "Test"));
        });
    }

    /**
     * Logs if a given session is closed
     *
     * @param session to be logged
     */
    @OnClose
    public void onClose(Session session) {
        logger.log(Level.INFO, "session close " + session.getId());
        timer.cancel();
    }

    /**
     * Logs if a given error is thrown
     *
     * @param error
     */
    @OnError
    public void onError(Throwable error) {
        logger.log(Level.INFO, "session error {0}", error);
    }

    /**
     * Retrieves the message type using the {@link GameStateEndpoint#getMessageType(String) getMessageType} funtion and
     * selects the correct parser for the specific type.
     * After that the message will parsed and a response is sent to the client.
     *
     * @param messageString to be parsed
     */
    @OnMessage
    public void onMessage(String messageString) {
        ensureHTTPSessionIsValid();

        logger.info("session " + wsSession.getId() + " with message " + messageString);

        try {
            String type = getMessageType(messageString);

            Jsonb jsonb = JsonbBuilder.create();

            switch (type) {
                case "CHOSE_OPTION":
                    CSMChoseOption parsed = jsonb.fromJson(messageString, CSMChoseOption.class);
                    SSMGameState result = gphb.applyOption(sessionId, parsed);
                    sendToClient(result);
                    logger.info("[" + wsSession.getId().toUpperCase() + "] type 'chosen-option' parsed: " + parsed + ", result: " + result);
                    break;
                default:
                    throw new RuntimeException("unknown type " + type);
            }
        } catch (Exception e) {
            new RuntimeException("could not handle message from client '" + wsSession.getId() + "' msg:\n" + messageString, e).printStackTrace();
        }
    }
}
