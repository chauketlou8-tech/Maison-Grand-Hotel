const websocket = new WebSocket("ws://localhost:3003");

websocket.onopen = () => {
    console.log("Connected to WebSocket server");
    websocket.send(JSON.stringify({
        type: "connection",
        message: "Starting connection to the server",
    }));
};

websocket.onmessage = (e) => {
    try {
        const data = JSON.parse(e.data);
        console.log("Received:", data);

        if (data.type === "availableRooms") {
            console.log("Available rooms:", data.data.rooms);
        }
    }
    catch (error) {
        console.error("Error parsing message:", error);
    }
};

websocket.onclose = () => {
    console.log("WebSocket closed");
};

websocket.onerror = (e) => {
    console.error("WebSocket error:", e);
};

function sendMessage(type: string, data: any) {
    if (websocket.readyState === WebSocket.OPEN) {
        websocket.send(JSON.stringify({ type, data }));
    }
    else {
        console.warn("WebSocket is not open");
    }
}

export { websocket, sendMessage };