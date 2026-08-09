import { Link } from "react-router-dom";
import { ArrowRight } from "lucide-react";

export default function CTA() {
    return (
        <section className="py-16 px-8 md:px-20 border-t border-[#c9a84c]/15 text-center bg-[#0f0e0c]">
            <h3 className="text-3xl font-normal mb-3 text-[#f0ebe0] font-display">Reserve your table.</h3>
            <p className="text-sm text-[#9a9080] font-light mb-8 max-w-md mx-auto">Aurore books out weeks in advance. Hotel guests receive priority access — another reason to stay with us.</p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
                <Link to="/booking" className="inline-flex items-center justify-center gap-3 px-8 py-4 text-sm tracking-[0.2em] uppercase font-medium transition-colors bg-[#c9a84c] text-[#0f0e0c] hover:bg-[#c9a84c]/90">
                    Book a Stay + Table <ArrowRight size={14} />
                </Link>
                <a href="mailto:dining@maisongrandhotel.com" className="inline-flex items-center justify-center gap-3 px-8 py-4 border text-sm tracking-[0.2em] uppercase font-light transition-colors border-[#c9a84c]/15 text-[#f0ebe0]/70 hover:border-[#c9a84c] hover:text-[#c9a84c]">
                    Email Reservations
                </a>
            </div>
        </section>
    );
}