import { useState, useEffect } from "react";
import { ChevronDown, ChevronUp } from "lucide-react";
import type { Settings } from "../../types/settings";
import getSettings from "../../services/getSettings";
import buildFaqs from "../../constants/faqs";

export default function FaqSection() {
    const [openFaq, setOpenFaq] = useState<number | null>(null);
    const [settings, setSettings] = useState<Settings>(null);

    useEffect(() => {
        async function set() {
            setSettings(await getSettings());
        }

        void set();
    }, []);

    const faqs = buildFaqs(settings);

    return (
        <section className="py-20 px-8 md:px-20 border-t" style={{ backgroundColor: "#0f0e0c", borderColor: "rgba(201, 168, 76, 0.15)" }}>
            <div className="max-w-3xl mx-auto">
                <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light" style={{ color: "#c9a84c" }}>Need to Know</p>
                <h2 className="text-4xl font-normal mb-12" style={{ fontFamily: "'Playfair Display', serif", color: "#f0ebe0" }}>Frequently Asked</h2>
                <div className="flex flex-col divide-y" style={{ borderColor: "rgba(201, 168, 76, 0.15)" }}>
                    {faqs.map((faq, i) => (
                        <div key={i}>
                            <button className="w-full text-left flex items-center justify-between py-6 gap-4 transition-colors duration-200" style={{ color: "rgba(240, 235, 224, 0.85)" }} onClick={() => setOpenFaq(openFaq === i ? null : i)}>
                                <span className="text-sm font-light tracking-wide">{faq.q}</span>
                                {openFaq === i ? <ChevronUp size={16} style={{ color: "#c9a84c" }} className="flex-shrink-0" /> : <ChevronDown size={16} style={{ color: "#9a9080" }} className="flex-shrink-0" />}
                            </button>
                            {openFaq === i && (
                                <div className="pb-6 text-sm font-light leading-relaxed" style={{ color: "#9a9080" }}>
                                    {faq.a}
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            </div>
        </section>
    );
}