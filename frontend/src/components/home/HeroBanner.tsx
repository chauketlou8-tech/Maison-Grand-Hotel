import { useState, useEffect } from "react";
import { ChevronDown, ArrowRight } from "lucide-react";
import { Link } from "react-router-dom";
import type { Settings } from "../../types/settings";
import getSettings from "../../services/getSettings";
import getRooms from "../../services/getRooms";
import type { Room } from "../../types/Room";

export default function HeroBanner() {
    const [settings, setSettings] = useState<Settings>(null);
    const [rooms, setRooms] = useState<Room[]>([]);

    useEffect(() => {
        async function set() {
            setSettings(await getSettings());
            setRooms(await getRooms());
        }

        void set();
    }, []);

    const location = settings ? [settings.hotel_city, settings.hotel_country].filter(Boolean).join(", ") : "";

    return (
        <section className="relative h-screen min-h-[700px] flex items-end pb-28" style={{ backgroundColor: "#0f0e0c" }}>
            <img src="../../../assets/home/heroImage.avif" alt="Maison Grand Hôtel exterior at dusk" className="absolute inset-0 w-full h-full object-cover object-center opacity-55" />
            <div className="absolute inset-0 bg-gradient-to-t from-[#0f0e0c] via-[#0f0e0c]/50 to-transparent" />
            <div className="relative z-10 px-8 md:px-20 max-w-5xl">
                <p className="text-[11px] tracking-[0.5em] uppercase mb-5 font-light" style={{ color: "#c9a84c" }}>Est. 2026 · {location}</p>
                <h1 className="text-5xl md:text-7xl font-normal text-white leading-[1.05] mb-6" style={{ fontFamily: "'Playfair Display', serif" }}>Where time<br /><em style={{ color: "#c9a84c" }}>slows</em> down.</h1>
                <p className="text-base md:text-lg text-white/70 max-w-md mb-10 font-light leading-relaxed">A quiet address in the heart of the city. {rooms.length} rooms and suites, three restaurants, and a staff who remember your name.</p>
                <div className="flex flex-col sm:flex-row gap-4">
                    <Link to="/booking" className="group inline-flex items-center gap-3 px-8 py-4 text-white text-sm tracking-[0.2em] uppercase font-medium transition-colors duration-200" style={{ backgroundColor: "#c9a84c" }}>
                        Reserve a Room <ArrowRight size={15} className="transition-transform duration-300 group-hover:translate-x-1" />
                    </Link>
                    <Link to="/rooms" className="inline-flex items-center gap-3 px-8 py-4 border border-white/30 text-white/70 text-sm tracking-[0.2em] uppercase font-light hover:border-primary hover:text-primary transition-colors duration-200">Explore Rooms</Link>
                </div>
            </div>
            <div className="absolute bottom-10 left-1/2 -translate-x-1/2 flex flex-col items-center gap-2 opacity-30">
                <div className="w-px h-10 bg-white/40 animate-pulse" />
                <ChevronDown size={14} className="text-white/40" />
            </div>
        </section>
    );
}