import axios from "axios"

export default async function getRooms() {
    try {
        const { data } = await axios.get("/api/v1/rooms");
        return data.data;
    }
    catch {
        return [];
    }
}