export default function NavBar() {
    const items = ["Spa & Wellness", "Pool", "Fitness", "Concierge", "Business"];

    return (
        <nav className="sticky top-[72px] z-30 border-b border-[#c9a84c]/15 px-8 md:px-20 overflow-x-auto bg-[#0f0e0c]/95 backdrop-blur-sm">
            <div className="max-w-6xl mx-auto flex gap-0">
                {items.map((item) => (
                    <a key={item} href={`#${item.toLowerCase().replace(/[^a-z]/g, "-")}`} className="px-5 py-4 text-xs tracking-[0.15em] uppercase text-[#9a9080] hover:text-[#c9a84c] transition-colors whitespace-nowrap border-b-2 border-transparent hover:border-[#c9a84c]/50">
                        {item}
                    </a>
                ))}
            </div>
        </nav>
    );
}