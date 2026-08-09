import HeroBanner from "../../components/about/HeroBanner";
import Story from "../../components/about/Story";
import Stats from "../../components/about/Stats";
import Values from "../../components/about/Values";
import Location from "../../components/about/Location";
import CTA from "../../components/about/CTA";

export default function About() {
    return (
        <div>
            <HeroBanner />

            <Story />

            <Stats />

            <Values />

            <Location />

            <CTA />
        </div>
    );
}