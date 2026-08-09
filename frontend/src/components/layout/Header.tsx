import navLinks from "../../constants/navLinks";
import { useState, useEffect } from "react";
import { X, Menu } from "lucide-react";
import { useLocation, Link } from "react-router-dom";

export default function Header() {

    const [menuOpen, setMenuOpen] = useState<boolean>(false);
    const [scrolled, setScrolled] = useState<boolean>(false);
    const location = useLocation();

    useEffect(() => {
        const onScroll = () => setScrolled(window.scrollY > 60);
        window.addEventListener("scroll", onScroll, { passive: true });
        return () => window.removeEventListener("scroll", onScroll);
    }, []);

    useEffect(() => {
        setMenuOpen(false);
        window.scrollTo({ top: 0, behavior: "instant" });
    }, [location.pathname]);

    return (
        <header className="fixed top-0 inset-x-0 z-50">
            <div className={`flex items-center justify-between px-8 py-5 transition-all duration-500 ${scrolled ? "bg-black/90 backdrop-blur-sm border-b border-amber-500/20" : "bg-transparent"}`}>
                <Link to="/" className="flex flex-col leading-none cursor-pointer">
                    <span className="text-xl tracking-[0.2em] uppercase font-light" style={{ fontFamily: "'Playfair Display', serif", letterSpacing: "0.25em", color: "#c9a84c" }}>
                        Maison
                    </span>

                    <span className="text-[10px] tracking-[0.4em] uppercase text-white/50 font-light">
                        GRAND HÔTEL
                    </span>
                </Link>

                <nav className="hidden md:flex items-center gap-10">
                    {navLinks.map((link) => (
                        <Link key={link} to={`/${link === "Book" ? "booking" : link.toLowerCase()}`} className="text-sm tracking-[0.15em] uppercase transition-colors duration-200" style={{ color: "#eee" }}>
                            {link}
                        </Link>
                    ))}
                </nav>

                <button onClick={() => setMenuOpen(!menuOpen)} className="md:hidden transition-colors" style={{ color: "#c9a84c" }}>
                    {menuOpen ? <X size={22} /> : <Menu size={22} />}
                </button>
            </div>

            {menuOpen && (
                <div className="md:hidden border-b px-8 py-6 flex flex-col gap-5" style={{ backgroundColor: "#0f0e0c", borderColor: "#c9a84c/20" }}>
                    {navLinks.map((link) => (
                        <Link key={link} to={`/${link.toLowerCase()}`} className="text-sm tracking-[0.2em] uppercase font-light transition-colors" style={{ color: "#fff" }} onClick={() => setMenuOpen(false)}>
                            {link}
                        </Link>
                    ))}
                </div>
            )}
        </header>
    );
}