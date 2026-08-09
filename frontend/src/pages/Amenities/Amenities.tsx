import HeroSection from "../../components/amenities/HeroBanner";
import NavBar from "../../components/amenities/NavBar";
import SpaSection from "../../components/amenities/SpaSection";
import PoolSection from "../../components/amenities/PoolSection";
import FitnessSection from "../../components/amenities/FitnessSection";
import ConciergeSection from "../../components/amenities/ConciergeSection";
import BusinessSection from "../../components/amenities/BusinessSection";
import CTA from "../../components/amenities/CTA";

export default function AmenitiesPage() {
    return (
        <div>
            <HeroSection />
            <NavBar />
            <SpaSection />
            <PoolSection />
            <FitnessSection />
            <ConciergeSection />
            <BusinessSection />
            <CTA />
        </div>
    );
}