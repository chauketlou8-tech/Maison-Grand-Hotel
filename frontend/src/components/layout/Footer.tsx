import navLinks from "../../constants/navLinks.js";
import { Link } from "react-router-dom";
import { MapPin, Phone, Mail } from "lucide-react";

export default function Footer() {
    return (
        <footer className="py-16 px-8 md:px-20 border-t border-neutral-700 bg-black/95">
            <div className="max-w-6xl mx-auto">
                <div className="grid grid-cols-1 md:grid-cols-4 gap-12 mb-12">
                    <div className="md:col-span-2">
                        <p className="text-xl tracking-[0.2em] uppercase text-white mb-1 font-light" style={{ fontFamily: "'Playfair Display', serif" }}>
                            Maison
                        </p>
                        <p className="text-[10px] tracking-[0.4em] uppercase text-neutral-400 mb-4">GRAND HÔTEL</p>
                        <p className="text-sm text-neutral-400 font-light leading-relaxed max-w-xs">
                            A century-old address on the Rue de la Paix, steps from the Place Vendôme. Forty-seven rooms, two restaurants, one uncompromising standard.
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
                        </div>
                    </div>
                    <div>
                        <p className="text-[10px] tracking-[0.35em] uppercase text-neutral-400 mb-5">Contact</p>
                        <div className="flex flex-col gap-3 text-sm text-neutral-400 font-light">
                            <span className="flex items-start gap-2"><MapPin size={13} className="text-white mt-0.5 flex-shrink-0" />08 Greenwood Street, Gardens, Cape Town, 7700, South Africa</span>
                            <span className="flex items-center gap-2"><Phone size={13} className="text-white flex-shrink-0" />+27 21 987 6543</span>
                            <span className="flex items-center gap-2"><Mail size={13} className="text-white flex-shrink-0" />stay@maisongrandhotel.co.za</span>
                        </div>
                    </div>
                </div>
                <div className="border-t border-neutral-700 pt-8 flex flex-col sm:flex-row justify-between items-center gap-4">
                    <p className="text-xs text-neutral-500 font-light tracking-wider">
                        © {new Date().getFullYear()} Maison Grand Hôtel. All rights reserved.
                    </p>
                    <div className="flex gap-6 text-xs text-neutral-500 font-light">
                        <button className="hover:text-white transition-colors">Privacy Policy</button>
                        <button className="hover:text-white transition-colors">Terms of Use</button>
                        <button className="hover:text-white transition-colors">Cookie Settings</button>
                    </div>
                </div>
            </div>
        </footer>
    );
}