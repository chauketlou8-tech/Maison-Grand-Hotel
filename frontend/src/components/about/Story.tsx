import { useState, useEffect } from "react";
import type { Settings } from "../../types/settings";
import type { Room } from "../../types/Room";
import getSettings from "../../services/getSettings";
import getRooms from "../../services/getRooms";

export default function Story() {
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
        <section className="py-28 px-8 md:px-20" style={{ backgroundColor: "#0f0e0c" }}>
            <div className="max-w-6xl mx-auto grid md:grid-cols-2 gap-16 items-center">
                <div>
                    <p className="text-[11px] tracking-[0.5em] uppercase mb-4 font-light" style={{ color: "#c9a84c" }}>Our Story</p>
                    <h2 className="text-4xl md:text-5xl font-normal leading-tight mb-6 text-white" style={{ fontFamily: "'Playfair Display', serif" }}>Born from a vision<br />of <em style={{ color: "#c9a84c" }}>timeless hospitality</em></h2>
                    <p className="text-white/55 font-light leading-relaxed mb-5">{settings?.hotel_name} opened its doors in 2026 in {location}. Built for discerning travellers who value substance over spectacle, it brings a fresh perspective to a timeless craft.</p>
                    <p className="text-white/55 font-light leading-relaxed mb-5">The original vision was simple: create a haven where every guest feels like the only guest. A place where luxury is measured not in gold leaf, but in the warmth of a genuine smile and the precision of a perfectly made bed.</p>
                    <p className="text-white/55 font-light leading-relaxed mb-8">Today, that vision endures. {rooms.length} rooms and suites, three restaurants, and a staff who remember your name.</p>
                </div>
                <div className="grid grid-cols-2 gap-4">
                    <img src="../../../assets/about/hotelExterior.avif" alt="Hotel exterior" className="w-full h-64 object-cover opacity-80" />
                    <img src="../../../assets/about/hotelRoom.avif" alt="Hotel room" className="w-full h-64 object-cover opacity-80 mt-8" />
                    <img src="../../../assets/about/hotelLobby.avif" alt="Hotel lobby" className="w-full h-64 object-cover opacity-80 -mt-8" />
                    <img src="../../../assets/about/restaurant.avif" alt="Restaurant" className="w-full h-64 object-cover opacity-80" />
                </div>
            </div>
        </section>
    )
}