import type { RoomExtra } from "../types/RoomExtra";

const roomExtras: Record<string, RoomExtra> = {
    SI: { bed: "Single",     terrace: "—",    butler: "—" },
    ST: { bed: "Double",     terrace: "—",    butler: "—" },
    DO: { bed: "Double",     terrace: "—",    butler: "—" },
    QU: { bed: "Queen",      terrace: "—",    butler: "—" },
    KI: { bed: "King",       terrace: "—",    butler: "—" },
    DE: { bed: "Super-King", terrace: "—",    butler: "—" },
    EX: { bed: "Super-King", terrace: "—",    butler: "—" },
    FA: { bed: "King + 2×",  terrace: "✓",    butler: "—" },
    SU: { bed: "Super-King", terrace: "✓",    butler: "✓" },
    PE: { bed: "220×220",    terrace: "360°", butler: "✓" }
};

export default roomExtras;