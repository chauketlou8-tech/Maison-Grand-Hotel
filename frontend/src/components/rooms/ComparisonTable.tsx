import { useState, useEffect } from "react";
import compareFeatures from "../../constants/compareFeatures";
import roomExtras from "../../constants/roomExtras";
import type { Room } from "../../types/Room";
import type { RoomTeaser } from "../../types/RoomTeaser";
import getRooms from "../../services/getRooms";
import { buildRoomTeasers } from "../../constants/roomDetails";

const ORDER = ["SI", "ST", "DO", "QU", "KI", "DE", "EX", "FA", "SU", "PE"];

export default function ComparisonTable() {
    const [rooms, setRooms] = useState<Room[]>([]);

    useEffect(() => {
        async function set() {
            setRooms(await getRooms());
        }

        void set();
    }, []);

    const teasers: RoomTeaser[] = buildRoomTeasers(rooms)
        .sort((a, b) => ORDER.indexOf(a.code) - ORDER.indexOf(b.code));

    const valueFor = (t: RoomTeaser, feature: string): string => {
        const extra = roomExtras[t.code];

        switch (feature) {
            case "Bed":        return extra?.bed ?? "—";
            case "Size":       return t.size;
            case "Floors":     return t.floors;
            case "Terrace":    return extra?.terrace ?? "—";
            case "Butler":     return extra?.butler ?? "—";
            case "Price from": return `R${t.price.toLocaleString()}`;
            default:           return "—";
        }
    };

    return (
        <section className="py-20 px-8 md:px-20 border-t" style={{ backgroundColor: "#252218", borderColor: "rgba(201, 168, 76, 0.15)" }}>
            <div className="max-w-6xl mx-auto">
                <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light" style={{ color: "#c9a84c" }}>At a Glance</p>
                <h2 className="text-4xl font-normal mb-12" style={{ fontFamily: "'Playfair Display', serif", color: "#f0ebe0" }}>Room Comparison</h2>
                <div className="overflow-x-auto">
                    <table className="w-full text-sm min-w-[640px]">
                        <thead>
                        <tr className="border-b" style={{ borderColor: "rgba(201, 168, 76, 0.15)" }}>
                            <th className="text-left py-4 pr-6 text-xs tracking-[0.2em] uppercase font-normal w-28" style={{ color: "#9a9080" }}>Feature</th>
                            {teasers.map((r) => (
                                <th key={r.code} className="text-left py-4 px-3 font-normal whitespace-nowrap" style={{ color: "rgba(240, 235, 224, 0.8)" }}>
                                    <span className="text-xs tracking-wider block">{r.name}</span>
                                    <code className="text-[10px] font-mono" style={{ color: "rgba(201, 168, 76, 0.6)" }}>{r.code}</code>
                                </th>
                            ))}
                        </tr>
                        </thead>
                        <tbody>
                        {compareFeatures.map((feat) => (
                            <tr key={feat} className="border-b" style={{ borderColor: "rgba(201, 168, 76, 0.08)" }}>
                                <td className="py-4 pr-6 text-xs tracking-[0.1em] w-28" style={{ color: "#9a9080" }}>{feat}</td>
                                {teasers.map((r) => {
                                    const v = valueFor(r, feat);
                                    return (
                                        <td key={r.code} className="py-4 px-3 text-xs" style={{ color: v === "✓" || v === "360°" ? "#c9a84c" : v === "—" ? "rgba(154, 144, 128, 0.3)" : "rgba(240, 235, 224, 0.7)" }}>
                                            {v}
                                        </td>
                                    );
                                })}
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    );
}