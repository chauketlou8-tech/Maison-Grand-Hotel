import WebSocket from "ws";
import { logger } from "../config/logger"
import HelperFunctions from "../utils/helpers";

const server = new WebSocket.Server({ port: 3003 });

server.on("connection", (ws) => {
    logger.info("client connected");

    ws.send(JSON.stringify({
        type: "connection",
        message: "Connected to server"
    }));

    ws.on("message", async (message) => {
        try {
            const data = JSON.parse(message.toString());
            logger.info("Received:", data);

            if (data.type === "getAvailableRooms") {
                const availableRooms = await HelperFunctions.getAvailableRooms();
                ws.send(JSON.stringify({
                    type: "availableRooms",
                    data: { rooms: availableRooms },
                }));
            }
        }
        catch (error) {
            logger.error("Error parsing message:", { error });
            ws.send(JSON.stringify({ type: "error", message: "Invalid message format" }));
        }
    });

    ws.on("close", () => {
        logger.info("Client disconnected");
    });

    ws.on("error", (error) => {
        logger.error("WebSocket error:", error);
    });
});

logger.info("Socket started on port 3003");

export default server;