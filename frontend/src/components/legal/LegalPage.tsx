import { useEffect, useRef, useState, type ReactNode } from "react";
import { Link } from "react-router-dom";
import { ArrowUp, ChevronDown } from "lucide-react";

type Section = {
    id: string;
    title: string;
    body: ReactNode;
};

type Related = {
    to: string;
    label: string;
    desc: string;
};

type LegalPageProps = {
    eyebrow: string;
    title: string;
    updated: string;
    intro: string;
    sections: Section[];
    related?: Related[];
    heroImage?: string;
    closingImage?: string;
    hotelName?: string;
};

export default function LegalPage({
                                      eyebrow,
                                      title,
                                      updated,
                                      intro,
                                      sections,
                                      related = [],
                                      heroImage,
                                      closingImage,
                                      hotelName = "Maison Grand Hôtel",
                                  }: LegalPageProps) {
    const [active, setActive] = useState(sections[0]?.id ?? "");
    const [mobileOpen, setMobileOpen] = useState(false);
    const refs = useRef<Record<string, HTMLElement | null>>({});

    useEffect(() => {
        const obs = new IntersectionObserver(
            (entries) => {
                for (const e of entries) {
                    if (e.isIntersecting) setActive(e.target.id);
                }
            },
            { rootMargin: "-15% 0px -75% 0px", threshold: 0 }
        );

        Object.values(refs.current).forEach((el) => el && obs.observe(el));
        return () => obs.disconnect();
    }, []);

    function scrollTo(id: string) {
        const el = refs.current[id];
        if (!el) return;
        const y = el.getBoundingClientRect().top + window.scrollY - 80;
        window.scrollTo({ top: y, behavior: "smooth" });
        setMobileOpen(false);
    }

    const today = new Date(updated).toLocaleDateString("en-ZA", {
        day: "2-digit",
        month: "long",
        year: "numeric",
    });

    return (
        <div className="bg-[#0f0e0c] text-[#f0ebe0] min-h-screen font-sans">

            {/* ── Hero — short, quiet ─────────────────────────── */}
            <header className="relative border-b border-[#c9a84c]/10">
                {heroImage && (
                    <div className="relative h-[28vh] md:h-[32vh] overflow-hidden">
                        <img
                            src={heroImage}
                            alt=""
                            className="absolute inset-0 w-full h-full object-cover opacity-30"
                        />
                        <div className="absolute inset-0 bg-gradient-to-b from-[#0f0e0c]/60 via-[#0f0e0c]/80 to-[#0f0e0c]" />
                    </div>
                )}

                <div className="max-w-3xl mx-auto px-6 md:px-8 pt-10 pb-12">
                    <p className="text-[11px] tracking-[0.35em] uppercase text-[#c9a84c] mb-4 font-light">
                        {eyebrow}
                    </p>
                    <h1 className="text-4xl md:text-5xl font-normal tracking-tight text-[#f0ebe0] font-display leading-[1.1] mb-5">
                        {title}
                    </h1>
                    <p className="text-base md:text-lg text-[#f0ebe0]/80 leading-relaxed mb-6 max-w-2xl font-light">
                        {intro}
                    </p>
                    <p className="text-xs text-[#9a9080] tracking-wider uppercase font-light">
                        Last updated {today}
                    </p>
                </div>
            </header>

            {/* ── Mobile TOC ─────────────────────────────────── */}
            <div className="lg:hidden sticky top-0 z-30 bg-[#0f0e0c]/95 backdrop-blur border-b border-[#c9a84c]/10">
                <button
                    onClick={() => setMobileOpen((o) => !o)}
                    className="w-full flex items-center justify-between px-6 py-4 text-left"
                >
                    <span className="text-xs tracking-[0.2em] uppercase text-[#9a9080] font-light">
                        Contents
                    </span>
                    <ChevronDown
                        size={16}
                        className={`text-[#9a9080] transition-transform ${mobileOpen ? "rotate-180" : ""}`}
                    />
                </button>
                {mobileOpen && (
                    <nav className="px-6 pb-4 flex flex-col gap-1 max-h-[60vh] overflow-y-auto">
                        {sections.map((s) => (
                            <button
                                key={s.id}
                                onClick={() => scrollTo(s.id)}
                                className={`text-left text-sm py-2 transition-colors font-light ${
                                    active === s.id
                                        ? "text-[#c9a84c]"
                                        : "text-[#f0ebe0]/70 hover:text-[#f0ebe0]"
                                }`}
                            >
                                {s.title}
                            </button>
                        ))}
                    </nav>
                )}
            </div>

            {/* ── Main body ──────────────────────────────────── */}
            <main className="max-w-6xl mx-auto px-6 md:px-8 py-12 md:py-16">
                <div className="grid lg:grid-cols-[220px_1fr] gap-12 lg:gap-20">

                    {/* Desktop TOC */}
                    <aside className="hidden lg:block">
                        <div className="sticky top-12">
                            <p className="text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-4 font-light">
                                Contents
                            </p>
                            <nav className="flex flex-col">
                                {sections.map((s) => (
                                    <button
                                        key={s.id}
                                        onClick={() => scrollTo(s.id)}
                                        className={`text-left text-sm py-1.5 transition-colors border-l-2 pl-4 -ml-px font-light ${
                                            active === s.id
                                                ? "border-[#c9a84c] text-[#c9a84c]"
                                                : "border-transparent text-[#9a9080] hover:text-[#f0ebe0]"
                                        }`}
                                    >
                                        {s.title}
                                    </button>
                                ))}
                            </nav>
                        </div>
                    </aside>

                    {/* Content */}
                    <div className="max-w-2xl">
                        <div className="flex flex-col gap-14">
                            {sections.map((s) => (
                                <section
                                    key={s.id}
                                    id={s.id}
                                    ref={(el) => { refs.current[s.id] = el; }}
                                    className="scroll-mt-24"
                                >
                                    <h2 className="text-xl md:text-2xl font-normal text-[#f0ebe0] font-display tracking-tight mb-5 pb-3 border-b border-[#c9a84c]/15">
                                        {s.title}
                                    </h2>

                                    <div className="flex flex-col gap-4 text-[15px] md:text-base leading-[1.75] text-[#f0ebe0]/75 font-light [&_a]:text-[#c9a84c] [&_a]:underline [&_a]:underline-offset-4 [&_a:hover]:text-[#d4b558] [&_strong]:text-[#f0ebe0] [&_strong]:font-normal [&_ul]:flex [&_ul]:flex-col [&_ul]:gap-2 [&_ul]:pl-5 [&_li]:list-disc [&_li]:marker:text-[#c9a84c]/40">
                                        {s.body}
                                    </div>
                                </section>
                            ))}
                        </div>

                        {/* Related documents */}
                        {related.length > 0 && (
                            <div className="mt-20 pt-10 border-t border-[#c9a84c]/15">
                                <p className="text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-5 font-light">
                                    Related documents
                                </p>
                                <div className="grid sm:grid-cols-2 gap-4">
                                    {related.map((r) => (
                                        <Link
                                            key={r.to}
                                            to={r.to}
                                            className="group block p-5 border border-[#c9a84c]/15 hover:border-[#c9a84c]/40 hover:bg-[#15120f] transition-colors"
                                        >
                                            <p className="text-base font-medium text-[#f0ebe0] mb-1.5 group-hover:text-[#c9a84c] transition-colors">
                                                {r.label}
                                            </p>
                                            <p className="text-sm text-[#9a9080] leading-relaxed font-light">
                                                {r.desc}
                                            </p>
                                        </Link>
                                    ))}
                                </div>
                            </div>
                        )}

                        {/* Footer */}
                        <div className="mt-16 pt-8 border-t border-[#c9a84c]/15 flex flex-wrap items-center justify-between gap-4">
                            <p className="text-sm text-[#9a9080] font-light">
                                Questions?{" "}
                                <a
                                    href="mailto:stay@maisongrandhotel.co.za"
                                    className="text-[#c9a84c] hover:underline underline-offset-4"
                                >
                                    stay@maisongrandhotel.co.za
                                </a>
                            </p>
                            <button
                                onClick={() => window.scrollTo({ top: 0, behavior: "smooth" })}
                                className="text-xs tracking-[0.2em] uppercase text-[#9a9080] hover:text-[#c9a84c] transition-colors inline-flex items-center gap-2 font-light"
                            >
                                <ArrowUp size={12} /> Back to top
                            </button>
                        </div>
                    </div>
                </div>
            </main>

            {/* ── Closing image band ─────────────────────────── */}
            {closingImage && (
                <section className="relative h-[30vh] md:h-[36vh] overflow-hidden border-t border-[#c9a84c]/15">
                    <img
                        src={closingImage}
                        alt=""
                        className="absolute inset-0 w-full h-full object-cover opacity-40"
                    />
                    <div className="absolute inset-0 bg-gradient-to-t from-[#0f0e0c] via-transparent to-transparent" />
                    <div className="absolute bottom-6 left-6 md:left-8 right-6 md:right-8">
                        <div className="max-w-6xl mx-auto">
                            <p className="text-[10px] tracking-[0.4em] uppercase text-[#c9a84c]/70 font-light">
                                {hotelName}
                            </p>
                        </div>
                    </div>
                </section>
            )}
        </div>
    );
}