// components/booking/PoliciesSection.tsx
import policies from "../../constants/policies";

export default function PoliciesSection() {
    return (
        <section className="py-16 px-8 md:px-20 bg-[#252218] border-t border-[#c9a84c]/15">
            <div className="max-w-6xl mx-auto">
                <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light text-[#c9a84c]">Good to Know</p>
                <h2 className="text-3xl font-normal mb-8 text-[#f0ebe0] font-display">Policies</h2>
                <div className="grid sm:grid-cols-3 gap-6">
                    {policies.map(({ icon: Icon, title, body }) => (
                        <div key={title} className="border border-[#c9a84c]/15 bg-[#0f0e0c]/40 p-7">
                            <Icon size={16} className="text-[#c9a84c] mb-4" />
                            <h4 className="text-base font-normal mb-3 text-[#f0ebe0] font-display">{title}</h4>
                            <p className="text-sm text-[#9a9080] font-light leading-relaxed">{body}</p>
                        </div>
                    ))}
                </div>
            </div>
        </section>
    );
}