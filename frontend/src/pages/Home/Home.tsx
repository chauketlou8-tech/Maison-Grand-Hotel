import HeroBanner from "../../components/home/HeroBanner";
import About from "../../components/home/About";
import Rooms from "../../components/home/Rooms";
import Dining from "../../components/home/Dining";
import GalleryCarousel from "../../components/home/GalleryCarousel";
import Testimonials from "../../components/home/Testimonials";
import Booking from "../../components/home/Booking";
import Stats from "../../components/home/Stats";

export default function Home() {
    return (
        <div>
            <HeroBanner />

            <About />

            <Stats />

            <Rooms />

            <Dining />

            <GalleryCarousel />

            <Testimonials />

            <Booking />

        </div>
    );
}