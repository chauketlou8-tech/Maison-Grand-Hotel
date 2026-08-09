import gallery from "../../constants/gallery";
import { useState } from "react";
import { ChevronLeft, ChevronRight } from "lucide-react";

export default function GalleryCarousel() {

    const [galleryIndex, setGalleryIndex] = useState(0);

    function prevSlide() {
        setGalleryIndex((i) => (i === 0 ? gallery.length - 1 : i - 1));
    }
    function nextSlide() {
        setGalleryIndex((i) => (i === gallery.length - 1 ? 0 : i + 1));
    }

    return (
        <section className="py-16 px-8 md:px-20" style={{ backgroundColor: "#0f0e0c" }}>
            <div className="max-w-6xl mx-auto">
                <div className="relative overflow-hidden mb-4" style={{ backgroundColor: "#1c1917", aspectRatio: "16/7" }}>
                    {gallery.map(({ img, alt }, i) => (
                        <div key={alt} className="absolute inset-0 transition-opacity duration-500" style={{ opacity: i === galleryIndex ? 1 : 0, pointerEvents: i === galleryIndex ? "auto" : "none" }}>
                            <img src={img} alt={alt} className="w-full h-full object-cover" />
                            <div className="absolute inset-0 bg-gradient-to-t from-[#0f0e0c]/40 to-transparent" />
                            <p className="absolute bottom-5 left-6 text-xs tracking-[0.25em] uppercase" style={{ color: "rgba(240, 235, 224, 0.7)" }}>{alt}</p>
                        </div>
                    ))}
                    <button onClick={prevSlide} className="absolute left-4 top-1/2 -translate-y-1/2 w-10 h-10 border flex items-center justify-center transition-all duration-200 hover:border-primary" style={{ borderColor: "rgba(240, 235, 224, 0.2)", backgroundColor: "rgba(15, 14, 12, 0.6)" }} aria-label="Previous image">
                        <ChevronLeft size={18} style={{ color: "#f0ebe0" }} />
                    </button>
                    <button onClick={nextSlide} className="absolute right-4 top-1/2 -translate-y-1/2 w-10 h-10 border flex items-center justify-center transition-all duration-200 hover:border-primary" style={{ borderColor: "rgba(240, 235, 224, 0.2)", backgroundColor: "rgba(15, 14, 12, 0.6)" }} aria-label="Next image">
                        <ChevronRight size={18} style={{ color: "#f0ebe0" }} />
                    </button>
                    <div className="absolute top-5 right-6 text-xs font-mono tracking-widest" style={{ color: "rgba(240, 235, 224, 0.5)" }}>
                        {String(galleryIndex + 1).padStart(2, "0")} / {String(gallery.length).padStart(2, "0")}
                    </div>
                </div>
                <div className="grid grid-cols-5 gap-2">
                    {gallery.map(({ img, alt }, i) => (
                        <button key={alt} onClick={() => setGalleryIndex(i)} className={`overflow-hidden transition-all duration-200 ${i === galleryIndex ? "ring-1 opacity-100" : "opacity-50 hover:opacity-75"}`} style={{ backgroundColor: "#1c1917" }} aria-label={alt}>
                            <img src={img} alt={alt} className="w-full h-16 object-cover" />
                        </button>
                    ))}
                </div>
            </div>
        </section>
    )
}