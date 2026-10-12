import { useState, useEffect } from "react";
import { Calendar, Users, UtensilsCrossed, Star } from "lucide-react";
import type { Room } from "../../types/Room";
import getRooms from "../../services/getRooms";
import year from "../../constants/year";

export default function Stats() {
    const [rooms, setRooms] = useState<Room[]>([]);

    useEffect(() => {
        async function set() {
            setRooms(await getRooms());
        }

        void set();
    }, []);

    const stats = [
        { value: String(year), label: "Year Founded", icon: Calendar },
        { value: String(rooms.length), label: "Rooms & Suites", icon: Users },
        { value: "3", label: "Restaurants", icon: UtensilsCrossed },
        { value: String(new Date().getFullYear() - year), label: "Years of Excellence", icon: Star }
    ];

    return (
        <section className="py-20 px-8 md:px-20" style={{ backgroundColor: "#1a1814", borderTop: "1px solid rgba(201, 168, 76, 0.15)", borderBottom: "1px solid rgba(201, 168, 76, 0.15)" }}>
            <div className="max-w-6xl mx-auto grid grid-cols-2 md:grid-cols-4 gap-8">
                {stats.map(({ value, label, icon: Icon }) => (
                    <div key={label} className="flex flex-col items-center text-center gap-3">
                        <Icon size={18} style={{ color: "#c9a84c" }} />
                        <p className="text-4xl font-light" style={{ fontFamily: "'Playfair Display', serif", color: "#f0ebe0" }}>{value}</p>
                        <p className="text-xs tracking-[0.2em] uppercase" style={{ color: "#9a9080" }}>{label}</p>
                    </div>
                ))}
            </div>
        </section>
    )
}