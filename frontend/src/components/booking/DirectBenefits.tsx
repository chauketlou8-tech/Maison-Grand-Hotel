import { Check } from "lucide-react";
import directBenefits from "../../constants/directBenefits";

export default function DirectBenefits() {
    return (
        <section className="py-8 px-8 md:px-20 bg-[#0f0e0c] border-b border-[#c9a84c]/10 relative">
            <div className="absolute inset-0 bg-[#c9a84c]/10" />
            <div className="max-w-6xl mx-auto flex flex-wrap gap-4 items-center relative z-10">
                <span className="text-[10px] tracking-[0.3em] uppercase font-light flex-shrink-0 text-[#c9a84c]">Book Direct:</span>
                {directBenefits.map((b) => (
                    <span key={b} className="flex items-center gap-2 text-xs text-[#f0ebe0]/70 font-light">
                        <Check size={11} className="text-[#c9a84c] flex-shrink-0" />
                        {b}
                    </span>
                ))}
            </div>
        </section>
    );
}