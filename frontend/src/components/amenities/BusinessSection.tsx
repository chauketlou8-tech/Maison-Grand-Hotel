import business from "../../constants/business";

export default function BusinessSection() {
    return (
        <section id="business" className="py-24 px-8 md:px-20 border-t border-[#c9a84c]/15 bg-[#0f0e0c]">
            <div className="max-w-6xl mx-auto grid md:grid-cols-2 gap-16 items-start">
                <div>
                    <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light text-[#c9a84c]">Work &amp; Meetings</p>
                    <h2 className="text-4xl md:text-5xl font-normal mb-6 text-[#f0ebe0] font-display">Business Facilities</h2>
                    <p className="font-light leading-relaxed mb-8 text-[#f0ebe0]/55">Three meeting rooms, all naturally lit and discreetly staffed. From intimate board sessions to product presentations for thirty, every space is equipped with 4K screens, Barco Clickshare, and exceptional catering.</p>
                    <div className="overflow-x-auto">
                        <table className="w-full text-sm">
                            <thead>
                            <tr className="border-b border-[#c9a84c]/15">
                                <th className="text-left py-3 text-xs tracking-[0.15em] uppercase font-normal text-[#9a9080]">Room</th>
                                <th className="text-left py-3 text-xs tracking-[0.15em] uppercase font-normal text-[#9a9080]">Capacity</th>
                                <th className="text-left py-3 text-xs tracking-[0.15em] uppercase font-normal text-[#9a9080]">Area</th>
                                <th className="text-left py-3 text-xs tracking-[0.15em] uppercase font-normal text-[#9a9080]">Light</th>
                            </tr>
                            </thead>
                            <tbody className="divide-y divide-[#c9a84c]/8">
                            {business.map((b) => (
                                <tr key={b.name}>
                                    <td className="py-4 text-xs text-[#f0ebe0]/75">{b.name}</td>
                                    <td className="py-4 text-xs text-[#9a9080]">{b.capacity}</td>
                                    <td className="py-4 text-xs text-[#9a9080]">{b.area}</td>
                                    <td className="py-4 text-xs text-[#9a9080]">{b.light}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </div>
                <div className="relative overflow-hidden bg-[#1c1917]">
                    <img src="https://images.unsplash.com/photo-1497366216548-37526070297c?w=700&h=500&fit=crop&auto=format" alt="Meeting room" className="w-full h-80 object-cover opacity-70" />
                </div>
            </div>
        </section>
    );
}