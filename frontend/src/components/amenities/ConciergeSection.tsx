import conciegeServices from "../../constants/conciergeServices";

export default function ConciergeSection() {
    return (
        <section id="concierge" className="py-24 px-8 md:px-20 border-t border-[#c9a84c]/15 bg-[#252218]">
            <div className="max-w-6xl mx-auto">
                <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light text-[#c9a84c]">Personal Service</p>
                <h2 className="text-4xl md:text-5xl font-normal mb-4 text-[#f0ebe0] font-display">Concierge Services</h2>
                <p className="font-light max-w-xl mb-14 text-[#f0ebe0]/50">Our concierge team are former chefs, gallery directors, and travel editors. They handle everything — and they know everyone.</p>
                <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-6">
                    {conciegeServices.map(({ icon: Icon, title, desc }) => (
                        <div key={title} className="p-7 border border-[#c9a84c]/15 hover:border-[#c9a84c]/30 transition-colors duration-300 group bg-[#0f0e0c]">
                            <div className="w-10 h-10 border border-[#c9a84c]/25 flex items-center justify-center mb-5 group-hover:border-[#c9a84c] transition-colors">
                                <Icon size={16} className="text-[#c9a84c]" />
                            </div>
                            <h3 className="text-lg font-normal mb-3 text-[#f0ebe0] font-display">{title}</h3>
                            <p className="text-sm font-light leading-relaxed text-[#9a9080]">{desc}</p>
                        </div>
                    ))}
                </div>
            </div>
        </section>
    );
}