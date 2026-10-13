import { useState, useEffect } from "react";
import navLinks from "../../constants/navLinks.js";
import { Link } from "react-router-dom";
import { MapPin, Phone, Mail } from "lucide-react";
import type { Settings } from "../../types/settings";
import getSettings from "../../services/getSettings"

export default function Footer() {
    const [settings, setSettings] = useState<Settings>(null);

    useEffect(() => {
        async function set() {
            setSettings(await getSettings());
        }

        void set();
    }, []);

    const address = settings ? [
        settings.hotel_street,
        settings.hotel_city,
        settings.hotel_province,
        settings.hotel_postal_code,
        settings.hotel_country
    ].filter(Boolean).join(", ") : "";

    return (
        <footer className="py-16 px-8 md:px-20 border-t border-neutral-700 bg-black/95 scroll-hide">
            <div className="max-w-6xl mx-auto">
                <div className="grid grid-cols-1 md:grid-cols-4 gap-12 mb-12">
                    <div className="md:col-span-2">
                        <p className="text-xl tracking-[0.2em] uppercase text-white mb-1 font-light" style={{ fontFamily: "'Playfair Display', serif" }}>
                            {settings?.hotel_name?.split(" ")[0] ?? "Maison"}
                        </p>
                        <p className="text-[10px] tracking-[0.4em] uppercase text-neutral-400 mb-4">
                            {settings?.hotel_name?.split(" ").slice(1).join(" ") || "Grand Hôtel"}
                        </p>
                        <p className="text-sm text-neutral-400 font-light leading-relaxed max-w-xs">
                            A quiet address in {settings?.hotel_city ?? "Cape Town"}. {settings?.hotel_country ?? "South Africa"}. Rooms, suites, and a standard that doesn't announce itself.
                        </p>
                        <div className="flex gap-4 mt-6">
                            {["IG", "FB", "TW"].map((s) => (
                                <button key={s} className="text-xs tracking-widest text-neutral-400 hover:text-white transition-colors border border-neutral-600 hover:border-white/40 px-3 py-1.5">
                                    {s}
                                </button>
                            ))}
                        </div>
                    </div>
                    <div>
                        <p className="text-[10px] tracking-[0.35em] uppercase text-neutral-400 mb-5">Navigate</p>
                        <div className="flex flex-col gap-3">
                            {navLinks.map((link) => (
                                <Link key={link} to={`/${link === "Book" ? "booking" : link.toLowerCase()}`} className="text-sm text-neutral-400 hover:text-white transition-colors font-light">
                                    {link}
                                </Link>
                            ))}
                            <Link to="/booking/find" className="text-sm text-neutral-400 hover:text-white transition-colors font-light">
                                Find My Reservation
                            </Link>
                        </div>
                    </div>
                    <div>
                        <p className="text-[10px] tracking-[0.35em] uppercase text-neutral-400 mb-5">Contact</p>
                        <div className="flex flex-col gap-3 text-sm text-neutral-400 font-light">
                            <span className="flex items-start gap-2"><MapPin size={13} className="text-white mt-0.5 flex-shrink-0" />{address}</span>
                            <span className="flex items-center gap-2"><Phone size={13} className="text-white flex-shrink-0" />{settings?.hotel_phone}</span>
                            <span className="flex items-center gap-2"><Mail size={13} className="text-white flex-shrink-0" />{settings?.hotel_email}</span>
                        </div>
                    </div>
                </div>
                <div className="border-t border-neutral-700 pt-8 flex flex-col sm:flex-row justify-between items-center gap-4">
                    <p className="text-xs text-neutral-500 font-light tracking-wider">
                        © {new Date().getFullYear()} {settings?.hotel_name ?? "Maison Grand Hôtel"}. All rights reserved.
                    </p>
                    <div className="flex gap-6 text-xs text-neutral-500 font-light">
                        <Link to="/privacy" className="hover:text-white transition-colors">Privacy Policy</Link>
                        <Link to="/terms" className="hover:text-white transition-colors">Terms of Use</Link>
                        <Link to="/cookies" className="hover:text-white transition-colors">Cookie Settings</Link>
                    </div>
                </div>
            </div>
        </footer>
    );
}