import HeroBanner from "../../components/dining/HeroBanner";
import AuroreSection from "../../components/dining/AuroreSection";
import AtriumSection from "../../components/dining/AtriumSection";
import PrivateDiningSection from "../../components/dining/PrivateDiningSection";
import WineSection from "../../components/dining/WineSection";
import CTA from "../../components/dining/CTA";

export default function Dining() {
    return (
        <div>
            <HeroBanner />
            <AuroreSection />
            <AtriumSection />
            <PrivateDiningSection />
            <WineSection />
            <CTA />
        </div>
    );
}