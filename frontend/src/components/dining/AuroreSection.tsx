import { Clock, Star, Users } from "lucide-react";
import auroreMenu from "../../constants/auroreMenu";

export default function AuroreSection() {
    return (
        <section className="py-24 px-8 md:px-20 bg-[#0f0e0c]">
            <div className="max-w-6xl mx-auto">
                <div className="grid md:grid-cols-2 gap-16 items-start mb-16">
                    <div>
                        <div className="flex items-center gap-3 mb-4">
                            <p className="text-[11px] tracking-[0.5em] uppercase font-light text-[#c9a84c]">Fine Dining</p>
                            <span className="text-[#c9a84c]/15">·</span>
                            <div className="flex items-center gap-1">
                                <Star size={11} className="fill-[#c9a84c] text-[#c9a84c]" />
                                <Star size={11} className="fill-[#c9a84c] text-[#c9a84c]" />
                                <span className="text-xs text-[#c9a84c] tracking-wider ml-1">Michelin</span>
                            </div>
                        </div>
                        <h2 className="text-4xl md:text-5xl font-normal mb-5 text-[#f0ebe0] font-display">Aurore</h2>
                        <p className="font-light leading-relaxed mb-4 text-[#f0ebe0]/55">Named for the Parisian dawn, Aurore is Chef Élise Laurent's stage for modern European cooking. Trained under Alain Ducasse and Heston Blumenthal before opening her own restaurant in Lyon, Chef Laurent joined Maison in 2019 and earned her second Michelin star within eighteen months.</p>
                        <p className="font-light leading-relaxed mb-8 text-[#f0ebe0]/55">The tasting menu changes with each season — sometimes each week — and draws on direct relationships with thirty-odd producers across France, Spain, and Italy.</p>
                        <div className="grid grid-cols-2 gap-4 text-sm">
                            <div className="border border-[#c9a84c]/15 p-4">
                                <Clock size={13} className="text-[#c9a84c] mb-2" />
                                <p className="text-[#9a9080] text-xs tracking-wider">Dinner Only</p>
                                <p className="text-[#f0ebe0]/75">18:30 – 23:00</p>
                            </div>
                            <div className="border border-[#c9a84c]/15 p-4">
                                <Users size={13} className="text-[#c9a84c] mb-2" />
                                <p className="text-[#9a9080] text-xs tracking-wider">Covers</p>
                                <p className="text-[#f0ebe0]/75">32 seats</p>
                            </div>
                        </div>
                    </div>
                    <div className="relative bg-[#1c1917]">
                        <img src="https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=700&h=560&fit=crop&auto=format" alt="Aurore restaurant interior" className="w-full h-80 object-cover opacity-80" />
                        <div className="absolute -bottom-4 -left-4 p-4 hidden md:block bg-[#c9a84c] text-[#0f0e0c]">
                            <p className="text-2xl font-light font-display">★★</p>
                            <p className="text-xs tracking-[0.2em] uppercase">Michelin 2024</p>
                        </div>
                    </div>
                </div>

                <div className="border-t border-[#c9a84c]/15 pt-12">
                    <div className="flex items-end justify-between mb-8">
                        <h3 className="text-3xl font-normal text-[#f0ebe0] font-display">Seasonal Menu</h3>
                        <p className="text-xs text-[#9a9080] font-light tracking-wider">Tasting menu · €185 per person</p>
                    </div>
                    <div className="grid md:grid-cols-2 gap-5">
                        {auroreMenu.map((section) => (
                            <div key={section.course} className="border border-[#c9a84c]/15 p-7">
                                <p className="text-[10px] tracking-[0.4em] uppercase text-[#c9a84c] mb-5">{section.course}</p>
                                <div className="flex flex-col gap-4">
                                    {section.items.map((item) => (
                                        <div key={item.name} className="flex items-start justify-between gap-4">
                                            <span className="text-sm text-[#f0ebe0]/75 font-light leading-snug">{item.name}</span>
                                            {item.note && (
                                                <span className="text-[10px] tracking-[0.15em] text-[#c9a84c]/80 flex-shrink-0 border border-[#c9a84c]/25 px-2 py-0.5">
                                                    {item.note}
                                                </span>
                                            )}
                                        </div>
                                    ))}
                                </div>
                            </div>
                        ))}
                    </div>
                    <p className="text-xs text-[#9a9080] font-light italic mt-4">Menu changes weekly. Vegetarian and dietary requirements accommodated with 48 hours notice.</p>
                </div>
            </div>
        </section>
    );
}