import HeroBanner from "../../components/booking/HeroBanner";
import DirectBenefits from "../../components/booking/DirectBenefits";
import BookingForm from "../../components/booking/BookingForm";
import PoliciesSection from "../../components/booking/PoliciesSection";

export default function Booking() {
    return (
        <div>
            <HeroBanner />
            <DirectBenefits />
            <BookingForm />
            <PoliciesSection />
        </div>
    );
}