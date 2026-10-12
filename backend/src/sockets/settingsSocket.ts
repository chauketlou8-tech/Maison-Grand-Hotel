import WebSocket from "ws"
import { logger } from "../config/logger"
import HelperFunctions from "../utils/helpers"
import type { Settings } from "../types/settings"

const server = new WebSocket.Server({ port: 4001 });

server.on("connection", async (ws) => {
    logger.info("settings client connected");

    const push = async () => {
        try {
            const settings: Settings = await HelperFunctions.getSettings();

            if (ws.readyState === WebSocket.OPEN) {
                ws.send(JSON.stringify({ type: "settings", settings }));
            }
        }
        catch (error) {
            logger.error("Error pushing settings:", { error });
        }
    };

    await push();
    const interval = setInterval(push, 10_000);

    ws.on("close", () => {
        clearInterval(interval);
        logger.info("settings client disconnected");
    });

    ws.on("error", (err) => {
        logger.error("WebSocket error:", err);
    });
});

logger.info("Settings client connected on port 4001");
export default server;