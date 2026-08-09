import { Link } from "react-router-dom";
import { ArrowRight } from "lucide-react";

export default function CTA() {
    return (
        <section className="py-16 px-8 md:px-20 border-t text-center" style={{ backgroundColor: "#1a1814", borderColor: "rgba(201, 168, 76, 0.15)" }}>
            <p className="text-sm font-light mb-2" style={{ color: "#9a9080" }}>Not sure which room is right for you?</p>
            <h3 className="text-3xl font-normal mb-6" style={{ fontFamily: "'Playfair Display', serif", color: "#f0ebe0" }}>Let our concierge guide you.</h3>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
                <Link to="/booking" className="inline-flex items-center justify-center gap-3 px-8 py-4 text-sm tracking-[0.2em] uppercase font-medium transition-colors" style={{ backgroundColor: "#c9a84c", color: "#0f0e0c" }}>
                    Reserve a Room <ArrowRight size={14} />
                </Link>
                <a href="tel:+277 904 33094" className="inline-flex items-center justify-center gap-3 px-8 py-4 border text-sm tracking-[0.2em] uppercase font-light transition-colors" style={{ borderColor: "rgba(201, 168, 76, 0.15)", color: "rgba(240, 235, 224, 0.7)" }}>
                    Call Concierge
                </a>
            </div>
        </section>
    );
}