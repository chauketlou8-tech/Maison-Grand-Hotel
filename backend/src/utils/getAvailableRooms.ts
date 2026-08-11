import connection from "../config/connection"

export default async function getAvailableRooms() {
    const result = await connection.query("select * from rooms where roomstatus = 'available'");
    return result.rows;
}