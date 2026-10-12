import { useState, useEffect } from "react";
import type { Settings } from "../../types/settings";
import getSettings from "../../services/getSettings";

export default function HeroBanner() {
    const [settings, setSettings] = useState<Settings>(null);

    useEffect(() => {
        async function set() {
            setSettings(await getSettings());
        }

        void set();
    }, []);

    const location = settings ? [settings.hotel_city, settings.hotel_country].filter(Boolean).join(", ") : "";

    return (
        <section className="relative h-[60vh] min-h-[500px] flex items-center" style={{ backgroundColor: "#0f0e0c" }}>
            <img src="../../../assets/about/hotelInterior.avif" alt="Maison Grand Hôtel interior" className="absolute inset-0 w-full h-full object-cover opacity-40" />
            <div className="absolute inset-0 bg-gradient-to-r from-[#0f0e0c]/90 via-[#0f0e0c]/50 to-transparent" />
            <div className="relative z-10 px-8 md:px-20 max-w-4xl">
                <p className="text-[11px] tracking-[0.5em] uppercase mb-4 font-light" style={{ color: "#c9a84c" }}>About {settings?.hotel_name}</p>
                <h1 className="text-5xl md:text-7xl font-normal text-white leading-[1.05] mb-6" style={{ fontFamily: "'Playfair Display', serif" }}>
                    A new chapter of
                    <br />
                    <em style={{ color: "#c9a84c" }}>quiet distinction</em>
                </h1>
                <p className="text-lg text-white/60 max-w-2xl font-light leading-relaxed">Since 2026, {settings?.hotel_name} has stood as a testament to timeless elegance in {location}.</p>
            </div>
        </section>
    )
}