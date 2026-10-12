import crypto from 'crypto';
import { v4 as uuIdv4 } from "uuid";
import bcrypt from "bcryptjs";
import type {Room} from "../types/room";
import connection from "../config/connection";

class HelperFunctions {

    //this function helps to generate a string for use (e.g. a jwt secrete)
    public static generateRandomString() {
        return crypto.randomBytes(200).toString('hex');
    }

    //function to generate random id's
    public generateRandomID() {
        return uuIdv4();
    }

    //function to hash a password
    public async hashPassword(password: string) {
        const salt = await bcrypt.genSalt(20);
        return await bcrypt.hash(password, salt);
    }

    //regex email validator
    public validateEmail(email: string): boolean {
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        return emailRegex.test(email.trim());
    }
    public isValidDate(date: any): boolean {
        if (!date) return false;

        if (date instanceof Date) {
            return !isNaN(date.getTime())
        }

        const d = new Date(date);
        return d instanceof Date && !isNaN(d.getTime());
    }

    public isValidSignature(rawBody: Buffer, headers: Record<string, any>, yoco_webhook_secret: string): boolean {
        const TOLERANCE_SECONDS = 3 * 60;

        const id = headers["webhook-id"];
        const timestamp = headers["webhook-timestamp"];
        const signatureHeader = headers["webhook-signature"];
        if (!id || !timestamp || !signatureHeader) return false;

        const nowSeconds = Math.floor(Date.now() / 1000);
        if (Math.abs(nowSeconds - Number(timestamp)) > TOLERANCE_SECONDS) return false;

        const secretBytes = Buffer.from(yoco_webhook_secret.replace(/^whsec_/, ""), "base64");
        const signedContent = `${id}.${timestamp}.${rawBody.toString("utf8")}`;
        const expected = crypto.createHmac("sha256", secretBytes).update(signedContent).digest("base64");

        return signatureHeader.split(" ").some((part: string) => {
            const candidate = part.split(",")[1];
            if (!candidate) return false;
            const a = Buffer.from(candidate, "base64");
            const b = Buffer.from(expected, "base64");
            return a.length === b.length && crypto.timingSafeEqual(a, b);
        });
    }

    public createGuestID(room: Room): string {
        const floorNumber = room?.floornumber.toString().padStart(2, "0");
        const roomNumber = room?.roomnumber.toString().padStart(3, "0");

        return `G${floorNumber}-${room.roomtype}-${roomNumber}`;
    }

    public async getAvailableRooms() {
        const result = await connection.query(`select * from rooms where roomstatus = 'available'`);
        return result.rows;
    }

    public async getSettings() {
        const result = await connection.query(`select * from settings`);
        return result.rows[0];
    }

    public async getRooms(): Promise<Room[]> {
        const result = await connection.query(`select * from rooms`);
        return result.rows;
    }
}

export default new HelperFunctions();