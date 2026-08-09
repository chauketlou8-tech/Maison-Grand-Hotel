import { Wine } from "lucide-react";
import wines from "../../constants/wines";

export default function WineSection() {
    return (
        <section className="py-24 px-8 md:px-20 border-t border-[#c9a84c]/15 bg-[#1a1814]">
            <div className="max-w-6xl mx-auto">
                <div className="flex items-end justify-between mb-10">
                    <div>
                        <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light text-[#c9a84c]">Cellar Selection</p>
                        <h2 className="text-4xl font-normal text-[#f0ebe0] font-display">The Wine List</h2>
                    </div>
                    <div className="hidden sm:flex items-center gap-2 text-sm text-[#9a9080]">
                        <Wine size={14} className="text-[#c9a84c]" />
                        <span className="font-light">900+ references</span>
                    </div>
                </div>
                <div className="divide-y divide-[#c9a84c]/15">
                    {wines.map((wine) => (
                        <div key={wine.label} className="flex items-center justify-between py-5 gap-6 hover:text-[#c9a84c] transition-colors duration-200 group">
                            <div className="flex-1">
                                <span className="text-[10px] tracking-[0.3em] uppercase text-[#c9a84c]/70 mr-3">{wine.region}</span>
                                <span className="text-sm text-[#f0ebe0]/75 font-light">{wine.label}</span>
                            </div>
                            <span className="text-xs text-[#9a9080] tracking-wider flex-shrink-0">{wine.vintage}</span>
                            <span className="text-sm text-[#c9a84c] font-light flex-shrink-0">{wine.price}</span>
                        </div>
                    ))}
                </div>
                <p className="text-xs text-[#9a9080] font-light italic mt-4">Prices per bottle. Half-bottles and Coravin pours available on request.</p>
            </div>
        </section>
    );
}