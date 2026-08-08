import { Routes, Route } from "react-router-dom";
import PageLayout from "../components/layout/PageLayout";
            
import Home from "../pages/Home"
            
export default function AppRoutes() {
    return (
        <PageLayout>
            <Routes >
                <Route path="/" element={ <Home/> } />
            </Routes>
        </PageLayout>
)
} 