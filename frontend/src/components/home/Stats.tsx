import stats from "../../constants/stats";

export default function Stats() {
    return (
        <section className="py-16 px-8 md:px-20 bg-secondary border-y border-border">
            <div className="max-w-6xl mx-auto grid grid-cols-2 md:grid-cols-4 gap-8">
                {stats.map(({ value, label, icon: Icon }) => (
                    <div key={label} className="flex flex-col items-center text-center gap-3">
                        <Icon size={18} className="text-primary" />
                        <p className="text-4xl font-light text-foreground" style={{ fontFamily: "'Playfair Display', serif" }}>{value}</p>
                        <p className="text-xs tracking-[0.2em] uppercase text-muted-foreground">{label}</p>
                    </div>
                ))}
            </div>
        </section>
    )
}