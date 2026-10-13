import { Routes, Route } from "react-router-dom";
import PageLayout from "../components/layout/PageLayout";
            
import Home from "../pages/Home";
import About from "../pages/About";
import Rooms from "../pages/Rooms";
import Amenities from "../pages/Amenities";
import Dining from "../pages/Dining";
import { Booking, BookingView, Success, FindBooking, Payment } from "../pages/Booking";
import { CookieSettings, PrivacyPolicy, TermsOfUse } from "../pages/Footer";
            
export default function AppRoutes() {
    return (
        <PageLayout>
            <Routes >
                <Route path="/" element={ <Home/> } />
                <Route path="/about" element={ <About/> } />
                <Route path="/rooms" element={ <Rooms/> } />
                <Route path="/amenities" element={ <Amenities/> } />
                <Route path="/dining" element={ <Dining/> } />
                <Route path="/booking" element={ <Booking/> } />
                <Route path="/booking/success" element={ <Success/> } />
                <Route path="/booking/payment" element={ <Payment /> } />
                <Route path="/booking/find" element={ <FindBooking/> } />
                <Route path="/booking/:reservationid" element={ <BookingView /> } />
                <Route path="/cookies" element={ <CookieSettings/> } />
                <Route path="/privacy" element={ <PrivacyPolicy/> } />
                <Route path="/terms" element={ <TermsOfUse/> } />
            </Routes>
        </PageLayout>
)
} 