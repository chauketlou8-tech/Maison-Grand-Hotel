export default function HeroBanner() {
    return (
        <section className="relative h-[55vh] min-h-[420px] flex items-end pb-16 pt-20" style={{ backgroundColor: "#0f0e0c" }}>
            <img src="https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=1600&h=900&fit=crop&auto=format" alt="Hotel room interior" className="absolute inset-0 w-full h-full object-cover opacity-40" />

            <div className="absolute inset-0 bg-gradient-to-t from-[#0f0e0c] via-[#0f0e0c]/50 to-[#0f0e0c]/20" />

            <div className="relative z-10 px-8 md:px-20 max-w-6xl mx-auto w-full">
                <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light" style={{ color: "#c9a84c" }}>Accommodation</p>
                <h1 className="text-5xl md:text-6xl font-normal" style={{ fontFamily: "'Playfair Display', serif", color: "#f0ebe0" }}>Rooms &amp; Suites</h1>
                <p className="font-light mt-4 max-w-lg" style={{ color: "rgba(240, 235, 224, 0.5)" }}>Ten room categories across twenty floors. Every room hand-appointed, every view intentional.</p>
            </div>
        </section>
    );
}