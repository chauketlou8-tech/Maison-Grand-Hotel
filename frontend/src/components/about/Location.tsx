import { MapPin, Clock, ArrowRight } from "lucide-react";
import { Link } from "react-router-dom";

export default function Location() {
    return (
        <section className="py-28 px-8 md:px-20" style={{ backgroundColor: "#1a1814", borderTop: "1px solid rgba(201, 168, 76, 0.15)", borderBottom: "1px solid rgba(201, 168, 76, 0.15)" }}>
            <div className="max-w-6xl mx-auto grid md:grid-cols-2 gap-16 items-center">
                <div>
                    <p className="text-[11px] tracking-[0.5em] uppercase mb-4 font-light" style={{ color: "#c9a84c" }}>Location</p>
                    <h2 className="text-4xl md:text-5xl font-normal leading-tight mb-6 text-white" style={{ fontFamily: "'Playfair Display', serif" }}>The heart of<br /><em style={{ color: "#c9a84c" }}>Paris</em></h2>
                    <div className="flex items-start gap-3 mb-4">
                        <MapPin size={18} style={{ color: "#c9a84c" }} className="mt-1 flex-shrink-0" />
                        <p className="text-white/55 font-light leading-relaxed">08 Greenwood, 75002 Cape Town, South Africa</p>
                    </div>
                    <div className="flex items-start gap-3 mb-4">
                        <Clock size={18} style={{ color: "#c9a84c" }} className="mt-1 flex-shrink-0" />
                        <p className="text-white/55 font-light leading-relaxed">Check-in: 3:00 PM &bull; Check-out: 12:00 PM</p>
                    </div>
                    <p className="text-white/55 font-light leading-relaxed mb-8">Steps from the Place Vendôme, the Opéra Garnier, and the finest shopping in Cape Town. Our location has been the choice of discerning travellers for a century.</p>
                    <Link to="/booking" className="inline-flex items-center gap-3 px-8 py-4 text-sm tracking-[0.2em] uppercase font-medium transition-colors duration-200" style={{ backgroundColor: "#c9a84c", color: "#0f0e0c" }}>
                        Book Your Stay <ArrowRight size={15} />
                    </Link>
                </div>
                <div className="relative">
                    <img src="../../../assets/about/paris.avif" alt="Cape Town" className="w-full object-cover opacity-80" />
                </div>
            </div>
        </section>
    )
}