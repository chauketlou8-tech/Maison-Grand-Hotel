const websocket = new WebSocket("ws://localhost:3003");

const outbox: string[] = [];

websocket.onopen = () => {
    console.log("Connected to WebSocket server");

    // flush anything queued while we were connecting
    while (outbox.length > 0) {
        websocket.send(outbox.shift()!);
    }

    // always re-request on (re)connect
    websocket.send(JSON.stringify({
        type: "connection",
        message: "Starting connection to the server",
    }));
    websocket.send(JSON.stringify({
        type: "getAvailableRooms",
        data: {},
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
    const message = JSON.stringify({ type, data });

    if (websocket.readyState === WebSocket.OPEN) {
        websocket.send(message);
    }
    else if (websocket.readyState === WebSocket.CONNECTING) {
        outbox.push(message);
    }
    else {
        console.warn("WebSocket is closed, dropping message");
    }
}

export { websocket, sendMessage };