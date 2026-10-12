import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { ArrowRight, Clock, Coffee } from "lucide-react";
import type { Settings } from "../../types/settings";
import getSettings from "../../services/getSettings";

export default function Dining() {
    const [settings, setSettings] = useState<Settings>(null);

    useEffect(() => {
        async function set() {
            setSettings(await getSettings());
        }

        void set();
    }, []);

    return (
        <section className="py-28 px-8 md:px-20" style={{ backgroundColor: "#252218" }}>
            <div className="max-w-6xl mx-auto">
                <div className="flex items-end justify-between mb-12">
                    <div>
                        <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light" style={{ color: "#c9a84c" }}>Culinary</p>
                        <h2 className="text-4xl md:text-5xl font-normal" style={{ fontFamily: "'Playfair Display', serif", color: "#f0ebe0" }}>Dining at {settings?.hotel_name}</h2>
                    </div>
                    <Link to="/dining" className="hidden sm:flex items-center gap-2 text-sm tracking-[0.15em] uppercase hover:gap-4 transition-all duration-200" style={{ color: "#c9a84c" }}>Explore Dining <ArrowRight size={14} /></Link>
                </div>

                <div className="grid md:grid-cols-3 gap-6">
                    <div className="md:col-span-2 relative overflow-hidden group" style={{ backgroundColor: "#1c1917" }}>
                        <img src="../../../assets/home/aroureResturant.avif" alt="Aurore restaurant" className="w-full h-[500px] object-cover transition-all duration-700 group-hover:scale-105" style={{ opacity: "0.7" }} />
                        <div className="absolute inset-0 bg-gradient-to-t from-[#0f0e0c]/90 via-[#0f0e0c]/30 to-transparent" />
                        <div className="absolute bottom-0 left-0 p-8">
                            <span className="text-[10px] tracking-[0.4em] uppercase" style={{ color: "#c9a84c" }}>Fine Dining</span>
                            <h3 className="text-4xl font-normal mt-1" style={{ fontFamily: "'Playfair Display', serif", color: "#f0ebe0" }}>Aurore</h3>
                            <p className="text-sm mt-2 max-w-sm" style={{ color: "rgba(240, 235, 224, 0.6)" }}>Chef Élise Laurent's celebrated tasting menus. Dinner only.</p>
                            <div className="flex items-center gap-4 mt-4">
                                <div className="flex items-center gap-2">
                                    <Clock size={14} style={{ color: "#c9a84c" }} />
                                    <span className="text-xs" style={{ color: "#9a9080" }}>Tue-Sat · 7pm-10pm</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="flex flex-col gap-6">
                        <div className="relative overflow-hidden group" style={{ backgroundColor: "#1c1917" }}>
                            <img src="../../../assets/home/theAttrium.avif" alt="The Atrium" className="w-full h-[236px] object-cover transition-all duration-700 group-hover:scale-105" style={{ opacity: "0.7" }} />
                            <div className="absolute inset-0 bg-gradient-to-t from-[#0f0e0c]/90 via-[#0f0e0c]/30 to-transparent" />
                            <div className="absolute bottom-0 left-0 p-6">
                                <span className="text-[10px] tracking-[0.4em] uppercase" style={{ color: "#c9a84c" }}>All-Day Dining</span>
                                <h3 className="text-xl font-normal mt-1" style={{ fontFamily: "'Playfair Display', serif", color: "#f0ebe0" }}>The Atrium</h3>
                                <p className="text-sm mt-1" style={{ color: "rgba(240, 235, 224, 0.6)" }}>Breakfast · Lunch · Afternoon Tea</p>
                            </div>
                        </div>

                        <div className="relative overflow-hidden group" style={{ backgroundColor: "#1c1917" }}>
                            <img src="../../../assets/home/leJurdin.avif" alt="Le Jardin restaurant" className="w-full h-[236px] object-cover transition-all duration-700 group-hover:scale-105" style={{ opacity: "0.7" }} />
                            <div className="absolute inset-0 bg-gradient-to-t from-[#0f0e0c]/90 via-[#0f0e0c]/30 to-transparent" />
                            <div className="absolute bottom-0 left-0 p-6">
                                <span className="text-[10px] tracking-[0.4em] uppercase" style={{ color: "#c9a84c" }}>Outdoor Dining</span>
                                <h3 className="text-xl font-normal mt-1" style={{ fontFamily: "'Playfair Display', serif", color: "#f0ebe0" }}>Le Jardin</h3>
                                <p className="text-sm mt-1" style={{ color: "rgba(240, 235, 224, 0.6)" }}>Seasonal cuisine in our garden terrace</p>
                            </div>
                        </div>

                        <div className="p-6 flex flex-col items-start" style={{ backgroundColor: "#1a1814", border: "1px solid rgba(201, 168, 76, 0.15)" }}>
                            <div className="flex items-center gap-2 mb-2">
                                <Coffee size={18} style={{ color: "#c9a84c" }} />
                                <span className="text-[10px] tracking-[0.4em] uppercase" style={{ color: "#c9a84c" }}>Complimentary</span>
                            </div>
                            <h3 className="text-xl font-normal" style={{ fontFamily: "'Playfair Display', serif", color: "#f0ebe0" }}>Breakfast Bouquet</h3>
                            <p className="text-sm mt-2" style={{ color: "#9a9080" }}>Fresh pastries, seasonal fruits, and artisan coffee delivered to your room every morning.</p>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    )
}