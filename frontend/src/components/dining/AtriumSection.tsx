import { Clock, Users } from "lucide-react";
import atriumMenu from "../../constants/atriumMenu";

export default function AtriumSection() {
    return (
        <section className="py-24 px-8 md:px-20 bg-[#252218] border-t border-[#c9a84c]/15">
            <div className="max-w-6xl mx-auto">
                <div className="grid md:grid-cols-2 gap-16 items-start mb-16">
                    <div className="order-2 md:order-1 relative bg-[#1c1917]">
                        <img src="https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=700&h=560&fit=crop&auto=format" alt="The Atrium all-day dining" className="w-full h-80 object-cover opacity-80" />
                    </div>
                    <div className="order-1 md:order-2">
                        <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light text-[#c9a84c]">All-Day Dining</p>
                        <h2 className="text-4xl md:text-5xl font-normal mb-5 text-[#f0ebe0] font-display">The Atrium</h2>
                        <p className="font-light leading-relaxed mb-4 text-[#f0ebe0]/55">Beneath the soaring glass ceiling of our 1920s atrium, The Atrium serves from morning until well past midnight. It is the hotel's living room — unhurried at breakfast, convivial at lunch, and intimate after dark.</p>
                        <p className="font-light leading-relaxed mb-8 text-[#f0ebe0]/55">The bar programme rivals any standalone cocktail bar in Paris. Our head bartender, Mathieu Carre, works seasonally and keeps a back-bar of over 400 bottles.</p>
                        <div className="grid grid-cols-2 gap-4 text-sm">
                            <div className="border border-[#c9a84c]/15 p-4">
                                <Clock size={13} className="text-[#c9a84c] mb-2" />
                                <p className="text-[#9a9080] text-xs tracking-wider">Hours</p>
                                <p className="text-[#f0ebe0]/75">07:00 – 02:00</p>
                            </div>
                            <div className="border border-[#c9a84c]/15 p-4">
                                <Users size={13} className="text-[#c9a84c] mb-2" />
                                <p className="text-[#9a9080] text-xs tracking-wider">Covers</p>
                                <p className="text-[#f0ebe0]/75">86 seats + bar</p>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="border-t border-[#c9a84c]/15 pt-12">
                    <h3 className="text-3xl font-normal mb-8 text-[#f0ebe0] font-display">Daily Menu</h3>
                    <div className="grid md:grid-cols-3 gap-5">
                        {atriumMenu.map((section) => (
                            <div key={section.course} className="border border-[#c9a84c]/15 bg-[#0f0e0c]/40 p-7">
                                <p className="text-[10px] tracking-[0.4em] uppercase text-[#c9a84c] mb-5">{section.course}</p>
                                <div className="flex flex-col gap-4">
                                    {section.items.map((item) => (
                                        <p key={item.name} className="text-sm text-[#f0ebe0]/70 font-light leading-snug">{item.name}</p>
                                    ))}
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </section>
    );
}