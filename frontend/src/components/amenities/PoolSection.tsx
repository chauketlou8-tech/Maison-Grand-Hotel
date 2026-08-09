import poolFeatures from "../../constants/poolFeatures";

export default function PoolSection() {
    return (
        <section id="pool" className="py-24 px-8 md:px-20 border-t border-[#c9a84c]/15 bg-[#252218]">
            <div className="max-w-6xl mx-auto grid md:grid-cols-2 gap-16 items-center">
                <div className="relative order-2 md:order-1 bg-[#1c1917]">
                    <img src="https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=700&h=520&fit=crop&auto=format" alt="Infinity pool" className="w-full h-80 object-cover opacity-80" />
                </div>
                <div className="order-1 md:order-2">
                    <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light text-[#c9a84c]">Aquatics</p>
                    <h2 className="text-4xl md:text-5xl font-normal mb-6 text-[#f0ebe0] font-display">The Pool</h2>
                    <p className="font-light leading-relaxed mb-8 text-[#f0ebe0]/55">Our 25-metre heated pool is enclosed by a soaring glass atrium and flanked by private cabanas. Whether you rise for early laps or arrive for an afternoon under warm light, the water is always at 28°C.</p>
                    <div className="grid grid-cols-2 gap-4">
                        {poolFeatures.map(({ icon: Icon, label }) => (
                            <div key={label} className="flex items-center gap-3 text-sm text-[#f0ebe0]/70">
                                <div className="w-8 h-8 border border-[#c9a84c]/30 flex items-center justify-center flex-shrink-0">
                                    <Icon size={14} className="text-[#c9a84c]" />
                                </div>
                                {label}
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </section>
    );
}