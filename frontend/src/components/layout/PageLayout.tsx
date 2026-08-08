import type { ReactNode } from "react";
import Header from "./Header";
import Footer from "./Footer";

export default function PageLayout({ children }: { children: ReactNode }) {
    return (
        <div>
            <Header/>
            <main>{children}</main>
            <Footer/>
        </div>
    )
}