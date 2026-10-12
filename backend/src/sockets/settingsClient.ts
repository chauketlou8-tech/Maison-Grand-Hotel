import type { Settings } from "../types/settings";
import { logger } from "../config/logger"

let setts: Settings;
const websocket = new WebSocket("ws://localhost:4001");

websocket.onopen = () => {
    logger.info("Connected to settings WebSocket server");
    websocket.send(JSON.stringify({
        type: "connection",
        message: "Starting connection to the server",
    }));
};

websocket.onmessage = (e) => {
    try {
        const data = JSON.parse(e.data);
        setts = data.settings;
    }
    catch (error) {
        console.error("Error parsing message:", error);
    }
};

export const getSettings = (): Settings | null => {
    return setts;
}

export default getSettings;