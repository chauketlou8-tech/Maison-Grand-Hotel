import gallery from "../../constants/gallery";
import { useState, useRef, useEffect } from "react";
import { ChevronLeft, ChevronRight } from "lucide-react";

export default function GalleryCarousel() {

    const [galleryIndex, setGalleryIndex] = useState(0);
    const intervalRef = useRef(null);

    function prevSlide() {
        setGalleryIndex((i) => (i === 0 ? gallery.length - 1 : i - 1));
    }
    function nextSlide() {
        setGalleryIndex((i) => (i === gallery.length - 1 ? 0 : i + 1));
    }

    useEffect(() => {
        if (intervalRef.current) clearInterval(intervalRef.current);

        intervalRef.current = setInterval(() => {
            setGalleryIndex(prev => (prev + 1) % gallery.length);
        }, 0);

        return () => {
            if (intervalRef.current) {
                clearInterval(intervalRef.current);
                intervalRef.current = null;
            }
        };
    }, []);

    return (
        <section className="py-16 px-8 md:px-20">
            <div className="max-w-6xl mx-auto">
                <div className="relative overflow-hidden bg-stone-900 mb-4" style={{ aspectRatio: "16/7" }}>
                    {gallery.map(({ img, alt }, i) => (
                        <div key={alt} className="absolute inset-0 transition-opacity duration-500" style={{ opacity: i === galleryIndex ? 1 : 0, pointerEvents: i === galleryIndex ? "auto" : "none" }}>
                            <img src={img} alt={alt} className="w-full h-full object-cover" />
                            <div className="absolute inset-0 bg-gradient-to-t from-background/40 to-transparent" />
                            <p className="absolute bottom-5 left-6 text-xs tracking-[0.25em] uppercase text-foreground/70">{alt}</p>
                        </div>
                    ))}
                    <button onClick={prevSlide} className="absolute left-4 top-1/2 -translate-y-1/2 w-10 h-10 border border-foreground/20 bg-background/60 hover:bg-background/90 hover:border-primary flex items-center justify-center transition-all duration-200" aria-label="Previous image">
                        <ChevronLeft size={18} className="text-foreground" />
                    </button>
                    <button onClick={nextSlide} className="absolute right-4 top-1/2 -translate-y-1/2 w-10 h-10 border border-foreground/20 bg-background/60 hover:bg-background/90 hover:border-primary flex items-center justify-center transition-all duration-200" aria-label="Next image">
                        <ChevronRight size={18} className="text-foreground" />
                    </button>
                    <div className="absolute top-5 right-6 text-xs font-mono text-foreground/50 tracking-widest">
                        {String(galleryIndex + 1).padStart(2, "0")} / {String(gallery.length).padStart(2, "0")}
                    </div>
                </div>
                <div className="grid grid-cols-5 gap-2">
                    {gallery.map(({ img, alt }, i) => (
                        <button key={alt} onClick={() => setGalleryIndex(i)} className={`overflow-hidden bg-stone-900 transition-all duration-200 ${i === galleryIndex ? "ring-1 ring-primary opacity-100" : "opacity-50 hover:opacity-75"}`} aria-label={alt}>
                            <img src={img} alt={alt} className="w-full h-16 object-cover" />
                        </button>
                    ))}
                </div>
            </div>
        </section>
    )
}