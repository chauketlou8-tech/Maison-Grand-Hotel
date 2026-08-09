export default function FilterBar({ filter, setFilter, count }) {
    return (
        <section className="sticky top-[72px] z-30 border-b px-8 md:px-20" style={{ backgroundColor: "rgba(15, 14, 12, 0.95)", backdropFilter: "blur(8px)", borderColor: "rgba(201, 168, 76, 0.15)" }}>
            <div className="max-w-6xl mx-auto flex items-center gap-0 py-0">
                {(["all", "rooms", "suites"] as const).map((f) => (
                    <button key={f} onClick={() => setFilter(f)} className={`px-6 py-4 text-sm tracking-[0.15em] uppercase border-b-2 transition-all duration-200 -mb-px ${filter === f ? "border-primary text-primary" : "border-transparent text-muted-foreground hover:text-foreground"}`} style={{ borderColor: filter === f ? "#c9a84c" : "transparent", color: filter === f ? "#c9a84c" : "#9a9080" }}>
                        {f === "all" ? "All" : f === "rooms" ? "Rooms" : "Suites"}
                    </button>
                ))}
                <span className="ml-auto text-xs font-light tracking-wider" style={{ color: "#9a9080" }}>{count} {count === 1 ? "category" : "categories"}</span>
            </div>
        </section>
    );
}