import { Link } from "react-router-dom";
import { ArrowRight, Coffee } from "lucide-react";

export default function CTA() {
    return (
        <section className="py-16 px-8 md:px-20 border-t border-[#c9a84c]/15 text-center bg-[#1a1814]">
            <Coffee size={20} className="text-[#c9a84c] mx-auto mb-4" />
            <h3 className="text-3xl font-normal mb-3 text-[#f0ebe0] font-display">Ready to arrive?</h3>
            <p className="text-sm font-light mb-6 text-[#9a9080]">Every amenity is included for hotel guests.</p>
            <Link to="/booking" className="inline-flex items-center justify-center gap-3 px-8 py-4 text-sm tracking-[0.2em] uppercase font-medium transition-colors bg-[#c9a84c] text-[#0f0e0c] hover:bg-[#c9a84c]/90">
                Reserve Your Stay <ArrowRight size={14} />
            </Link>
        </section>
    );
}