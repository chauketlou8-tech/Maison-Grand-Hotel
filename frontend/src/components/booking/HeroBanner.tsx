export default function HeroBanner() {
    return (
        <section className="relative h-[40vh] min-h-[320px] flex items-end pb-12 pt-20 bg-[#0f0e0c]">
            <img src="https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=1600&h=700&fit=crop&auto=format" alt="Hotel lobby" className="absolute inset-0 w-full h-full object-cover opacity-30" />
            <div className="absolute inset-0 bg-gradient-to-t from-[#0f0e0c] via-[#0f0e0c]/60 to-[#0f0e0c]/20" />
            <div className="relative z-10 px-8 md:px-20 max-w-6xl mx-auto w-full">
                <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light text-[#c9a84c]">Plan Your Stay</p>
                <h1 className="text-5xl md:text-6xl font-normal text-[#f0ebe0] font-display">Reservations</h1>
            </div>
        </section>
    );
}