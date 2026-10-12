import HeroBanner from "../../components/home/HeroBanner";
import About from "../../components/home/About";
import Rooms from "../../components/home/Rooms";
import Dining from "../../components/home/Dining";
import GalleryCarousel from "../../components/home/GalleryCarousel";
import Testimonials from "../../components/home/Testimonials";
import Booking from "../../components/home/Booking";

export default function Home() {
    return (
        <div>
            <HeroBanner />

            <About />

            <Rooms />

            <Dining />

            <GalleryCarousel />

            {/*Change later*/}
            <Testimonials />

            <Booking />

        </div>
    );
}