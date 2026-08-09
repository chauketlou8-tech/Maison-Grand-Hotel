import { Link } from "react-router-dom";
import { ArrowRight } from "lucide-react";

export default function Bookings() {
    return (
        <section className="relative py-28 px-8 md:px-20 overflow-hidden" style={{ backgroundColor: "#0f0e0c" }}>
            <img src="../../../assets/home/hotelPoolAtNight.avif" alt="Hotel pool at night" className="absolute inset-0 w-full h-full object-cover opacity-30" />
            <div className="absolute inset-0 bg-gradient-to-r from-[#0f0e0c]/90 via-[#0f0e0c]/60 to-transparent" />
            <div className="relative z-10 max-w-2xl">
                <p className="text-[11px] tracking-[0.5em] uppercase mb-4 font-light" style={{ color: "#c9a84c" }}>Book Direct</p>
                <h2 className="text-4xl md:text-5xl font-normal mb-6" style={{ fontFamily: "'Playfair Display', serif", color: "#f0ebe0" }}>Best rate guaranteed<br /><em style={{ color: "#c9a84c" }}>every time.</em></h2>
                <p className="font-light mb-8 max-w-md" style={{ color: "rgba(240, 235, 224, 0.55)" }}>Reserve directly with us for our lowest available rate, complimentary early check-in, and a welcome amenity curated for your stay.</p>
                <Link to="/book" className="inline-flex items-center gap-3 px-8 py-4 text-sm tracking-[0.2em] uppercase font-medium transition-colors duration-200" style={{ backgroundColor: "#c9a84c", color: "#0f0e0c" }}>Reserve Now <ArrowRight size={15} /></Link>
            </div>
        </section>
    )
}