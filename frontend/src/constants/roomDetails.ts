// constants/roomDetails.ts
import type { Room } from "../types/Room";
import type { RoomMeta } from "../types/RoomMeta";
import type { RoomTeaser } from "../types/RoomTeaser";

const ROOM_META: Record<string, RoomMeta> = {
    SI: {
        name: "Single Room",
        size: "18 m²",
        img: "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?w=800&h=600&fit=crop&auto=format",
        features: ["Single bed (90×200)", "City-side view", "Walk-in shower", "Writing desk", "Nespresso machine", "42″ TV", "In-room safe", "Complimentary Wi-Fi"],
        desc: "Compact and precisely appointed. The Single is designed for the solo traveller who values character over square footage — everything in reach, nothing unnecessary."
    },
    ST: {
        name: "Standard Room",
        size: "26 m²",
        img: "https://images.unsplash.com/photo-1560185007-cde436f6a4d0?w=800&h=600&fit=crop&auto=format",
        features: ["Double bed (140×200)", "Courtyard view", "Rainfall shower", "Nespresso machine", "50″ OLED TV", "In-room safe", "Minibar", "Complimentary Wi-Fi"],
        desc: "The Standard is our opening argument for the hotel: quiet, hand-detailed, and more considered than the category suggests."
    },
    DO: {
        name: "Double Room",
        size: "32 m²",
        img: "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=800&h=600&fit=crop&auto=format",
        features: ["Double bed (160×200)", "Garden or city view", "Deep bath + shower", "Double vanity", "Minibar", "Nespresso machine", "55″ OLED TV", "In-room safe"],
        desc: "A room sized for two without sacrifice. The Double offers either garden or city aspect, a proper bath, and the same hand-embroidered linens found in our grandest suites."
    },
    QU: {
        name: "Queen Room",
        size: "38 m²",
        img: "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=800&h=600&fit=crop&auto=format",
        features: ["Queen bed (160×200)", "Courtyard view", "Freestanding bath", "Walk-in wardrobe", "Minibar", "Nespresso machine", "55″ OLED TV", "Bathrobes & slippers"],
        desc: "The Queen pairs a freestanding copper bath with a serene courtyard aspect."
    },
    KI: {
        name: "King Room",
        size: "44 m²",
        img: "https://images.unsplash.com/photo-1618773928121-c32242e63f39?w=800&h=600&fit=crop&auto=format",
        features: ["King bed (180×200)", "Rooftop view", "Marble bathroom", "Rainfall shower + bath", "Minibar", "Nespresso machine", "65″ OLED TV", "In-room safe"],
        desc: "Floor-to-ceiling windows frame the rooftop. The King is our most-requested room category."
    },
    DE: {
        name: "Deluxe Room",
        size: "52 m²",
        img: "https://images.unsplash.com/photo-1566665797739-1674de7a421a?w=800&h=600&fit=crop&auto=format",
        features: ["Super-king bed (200×200)", "Panoramic city view", "Dressing room", "Marble double vanity", "Soaking tub + shower", "Premium minibar", "Bose sound system", "Evening turndown"],
        desc: "The Deluxe is the fullest expression of a room before the step into suites."
    },
    EX: {
        name: "Executive Room",
        size: "58 m²",
        img: "https://images.unsplash.com/photo-1595576508898-0ad5c879a061?w=800&h=600&fit=crop&auto=format",
        features: ["Super-king bed (200×200)", "Executive lounge access", "Dedicated workspace", "Printer & shredder", "Nespresso & kettle", "Premium minibar", "65″ 4K TV", "Daily newspaper"],
        desc: "Designed for extended stays and working visits."
    },
    FA: {
        name: "Family Room",
        size: "72 m²",
        img: "https://images.unsplash.com/photo-1560185007-cde436f6a4d0?w=800&h=600&fit=crop&auto=format",
        features: ["King bed + two singles", "Garden-facing terrace", "Children's welcome kit", "Two bathrooms", "Kitchenette", "Board games & books", "Cot available on request", "Babysitting service"],
        desc: "A full king bedroom connected to a separate children's area, both opening onto a garden-facing terrace."
    },
    SU: {
        name: "Suite",
        size: "95 m²",
        img: "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=800&h=600&fit=crop&auto=format",
        features: ["Super-king bed (200×200)", "Panoramic view", "Private terrace", "Separate living room", "Butler service", "Premium minibar", "Bose sound system", "Twice-daily housekeeping"],
        desc: "The Suite commands 270-degree views. A private terrace, separate drawing room, and on-call butler."
    },
    PE: {
        name: "Penthouse",
        size: "210 m²",
        img: "https://images.unsplash.com/photo-1566665797739-1674de7a421a?w=800&h=600&fit=crop&auto=format",
        features: ["Super-king bed (220×220)", "360° rooftop terrace", "Private plunge pool", "Full kitchen", "Dedicated butler", "Chauffeur service", "In-suite spa treatments", "Wine cellar access"],
        desc: "The Penthouse occupies the entire top floor."
    }
};

const SUITE_CODES = ["SU", "PE"];

export function buildRoomTeasers(rooms: Room[]): RoomTeaser[] {
    const grouped = new Map<string, Room[]>();

    for (const room of rooms) {
        if (!grouped.has(room.roomtype)) grouped.set(room.roomtype, []);
        grouped.get(room.roomtype)!.push(room);
    }

    const teasers: RoomTeaser[] = [];

    for (const [code, group] of grouped) {
        const meta = ROOM_META[code];
        if (!meta) continue;

        const floors = group.map((r) => r.floornumber);
        const minFloor = Math.min(...floors);
        const maxFloor = Math.max(...floors);

        teasers.push({
            id: code.toLowerCase(),
            code,
            name: meta.name,
            category: SUITE_CODES.includes(code) ? "suites" : "rooms",
            size: meta.size,
            floors: minFloor === maxFloor ? String(minFloor) : `${minFloor}–${maxFloor}`,
            price: Math.min(...group.map((r) => r.price)),
            img: meta.img,
            features: meta.features,
            desc: meta.desc
        });
    }

    return teasers;
}