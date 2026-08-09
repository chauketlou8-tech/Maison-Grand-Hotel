import { Routes, Route } from "react-router-dom";
import PageLayout from "../components/layout/PageLayout";
            
import Home from "../pages/Home";
import About from "../pages/About";
            
export default function AppRoutes() {
    return (
        <PageLayout>
            <Routes >
                <Route path="/" element={ <Home/> } />
                <Route path="/about" element={ <About/> } />
            </Routes>
        </PageLayout>
)
} 