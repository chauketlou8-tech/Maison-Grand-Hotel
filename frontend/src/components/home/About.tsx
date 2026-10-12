import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { ArrowRight } from "lucide-react";
import type { Settings } from "../../types/settings";
import getSettings from "../../services/getSettings";

export default function About() {
    const [settings, setSettings] = useState<Settings>(null);

    useEffect(() => {
        async function set() {
            setSettings(await getSettings());
        }

        void set();
    }, []);

    const location = settings ? [settings.hotel_street, settings.hotel_city, settings.hotel_country].filter(Boolean).join(", ") : "";

    return (
        <section className="py-28 px-8 md:px-20" style={{ backgroundColor: "#0f0e0c" }}>
            <div className="max-w-6xl mx-auto grid md:grid-cols-2 gap-16 items-center">
                <div>
                    <p className="text-[11px] tracking-[0.5em] uppercase mb-4 font-light" style={{ color: "#c9a84c" }}>Our Story</p>
                    <h2 className="text-4xl md:text-5xl font-normal leading-tight mb-6 text-white" style={{ fontFamily: "'Playfair Display', serif" }}>A new chapter of<br /><em>considered</em> hospitality.</h2>
                    <p className="text-white/55 font-light leading-relaxed mb-5">Maison Grand Hôtel opened its doors in 2026 at {location}. Built for discerning travellers who value substance over spectacle, it brings a fresh perspective to a timeless craft.</p>
                    <p className="text-white/55 font-light leading-relaxed mb-8">Every decision — from the hand-embroidered linen to the single-origin coffee served at breakfast — reflects a belief that true luxury is felt in the details, not announced in the lobby.</p>
                    <Link to="/about" className="inline-flex items-center gap-2 text-sm tracking-[0.15em] uppercase hover:gap-4 transition-all duration-200" style={{ color: "#c9a84c" }}>Discover the Experience <ArrowRight size={14} /></Link>
                </div>
                <div className="relative">
                    <img src="../../../assets/home/hotelLobby.avif" alt="Hotel grand lobby" className="w-full object-cover opacity-80" />
                    <div className="absolute -bottom-5 -left-5 p-5 hidden md:block" style={{ backgroundColor: "#1a1814", border: "1px solid #c9a84c" }}>
                        <p className="text-3xl font-light mb-1" style={{ color: "#c9a84c", fontFamily: "'Playfair Display', serif" }}>2026</p>
                        <p className="text-xs tracking-[0.2em] uppercase text-white/50">Established</p>
                    </div>
                </div>
            </div>
        </section>
    )
}