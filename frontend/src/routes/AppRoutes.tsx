import { Routes, Route } from "react-router-dom";
import PageLayout from "../components/layout/PageLayout";
            
import Home from "../pages/Home";
import About from "../pages/About";
import Rooms from "../pages/Rooms";
import Amenities from "../pages/Amenities";
import Dining from "../pages/Dining";
import { Booking } from "../pages/Booking";
import { Success } from "../pages/Booking";
import { Payment } from "../pages/Booking";
            
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
            </Routes>
        </PageLayout>
)
} 