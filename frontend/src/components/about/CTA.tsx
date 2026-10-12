import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { ArrowRight } from "lucide-react";
import type { Settings } from "../../types/settings";
import getSettings from "../../services/getSettings";

export default function CTA() {
    const [settings, setSettings] = useState<Settings>(null);

    useEffect(() => {
        async function set() {
            setSettings(await getSettings());
        }

        void set();
    }, []);

    return (
        <section className="relative py-28 px-8 md:px-20 overflow-hidden" style={{ backgroundColor: "#0f0e0c" }}>
            <img src="../../../assets/about/hotelExterior2.avif" alt="Hotel exterior" className="absolute inset-0 w-full h-full object-cover opacity-20" />
            <div className="absolute inset-0 bg-gradient-to-r from-[#0f0e0c]/90 via-[#0f0e0c]/60 to-transparent" />
            <div className="relative z-10 max-w-2xl">
                <p className="text-[11px] tracking-[0.5em] uppercase mb-4 font-light" style={{ color: "#c9a84c" }}>Experience {settings?.hotel_name}</p>
                <h2 className="text-4xl md:text-5xl font-normal text-white mb-6" style={{ fontFamily: "'Playfair Display', serif" }}>Come stay with us<br /><em style={{ color: "#c9a84c" }}>this season</em></h2>
                <p className="text-white/55 font-light mb-8 max-w-md">Discover the quiet distinction that has defined {settings?.hotel_name} since we opened our doors.</p>
                <Link to="/booking" className="inline-flex items-center gap-3 px-8 py-4 text-sm tracking-[0.2em] uppercase font-medium transition-colors duration-200" style={{ backgroundColor: "#c9a84c", color: "#0f0e0c" }}>
                    Reserve Now <ArrowRight size={15} />
                </Link>
            </div>
        </section>
    )
}