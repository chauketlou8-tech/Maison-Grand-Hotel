export default function Home() {
    return (
        <div className="min-h-screen bg-[#0d1117] relative overflow-hidden font-['-apple-system',_BlinkMacSystemFont,_'Segoe_UI',_Roboto,_'Helvetica_Neue',_sans-serif]">
            <div className="absolute top-[-50%] left-[-50%] w-[200%] h-[200%] pointer-events-none animate-rotateBg"
                style={{
                    background: 'radial-gradient(circle at 30% 40%, rgba(31, 111, 235, 0.08) 0%, transparent 50%), radial-gradient(circle at 70% 60%, rgba(88, 166, 255, 0.05) 0%, transparent 50%)'
                }}
            />
            
            <main className="relative z-10 max-w-[1200px] mx-auto p-8 min-h-screen flex flex-col">
                <header className="flex justify-between items-center py-4 border-b border-[rgba(255,255,255,0.05)] animate-slideDown">
                    <div className="w-10 h-10 bg-gradient-to-br from-[#1f6feb] to-[#58a6ff] rounded-lg flex items-center justify-center text-[#f0f6fc] text-xl font-bold animate-pulseGlow">
                        F
                    </div>
                    <div className="flex items-center gap-6">
                        <span className="text-xs text-[#8b949e] bg-[rgba(255,255,255,0.05)] px-3 py-1 rounded-full border border-[rgba(255,255,255,0.05)]">
                            v1.0.4
                        </span>
                        <span className="flex items-center gap-2 text-xs text-[#58a6ff] bg-[rgba(31,111,235,0.1)] px-3 py-1 rounded-full border border-[rgba(31,111,235,0.2)]">
                            <span className="w-1.5 h-1.5 bg-[#58a6ff] rounded-full animate-blink" />
                            Running
                        </span>
                    </div>
                </header>
                
                <div className="flex-1 flex flex-col justify-center py-16 animate-fadeUp [animation-delay:200ms]">
                    <h1 className="text-6xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-[#f0f6fc] to-[#8b949e] mb-2">
                        Welcome To Forge
                    </h1>
                    <p className="text-2xl font-normal text-[#c9d1d9] mb-2">
                        This is your starter Home page created by Forge.
                    </p>
                    <p className="text-lg text-[#8b949e] mb-8 max-w-[500px]">
                        Customize it to fit your app.
                    </p>
                    <div className="flex gap-4">
                        <button className="px-8 py-3 rounded-lg font-medium text-white bg-[#1f6feb] shadow-[0_4px_20px_rgba(31,111,235,0.3)] transition-all duration-300 hover:-translate-y-0.5 hover:shadow-[0_8px_30px_rgba(31,111,235,0.5)] hover:bg-[#388bfd] active:translate-y-0">
                            Get Started
                        </button>
                        <button className="px-8 py-3 rounded-lg font-medium text-[#c9d1d9] bg-transparent border border-[rgba(255,255,255,0.1)] transition-all duration-300 hover:bg-[rgba(255,255,255,0.05)] hover:border-[rgba(255,255,255,0.2)] hover:-translate-y-0.5">
                            Documentation
                        </button>
                    </div>
                </div>
                
                <div className="grid grid-cols-3 gap-8 pt-12 border-t border-[rgba(255,255,255,0.05)] animate-fadeUp [animation-delay:400ms]">
                    <div className="p-6 bg-[rgba(255,255,255,0.02)] rounded-xl border border-[rgba(255,255,255,0.04)] transition-all duration-300 hover:-translate-y-1 hover:border-[rgba(31,111,235,0.2)] hover:shadow-[0_8px_30px_rgba(0,0,0,0.3)]">
                        <span className="block text-3xl font-bold text-[#58a6ff] mb-3">F</span>
                        <h4 className="text-[#f0f6fc] text-lg mb-1">Fast</h4>
                        <p className="text-[#8b949e] text-sm">Built with performance in mind</p>
                    </div>
                    <div className="p-6 bg-[rgba(255,255,255,0.02)] rounded-xl border border-[rgba(255,255,255,0.04)] transition-all duration-300 hover:-translate-y-1 hover:border-[rgba(31,111,235,0.2)] hover:shadow-[0_8px_30px_rgba(0,0,0,0.3)]">
                        <span className="block text-3xl font-bold text-[#58a6ff] mb-3">M</span>
                        <h4 className="text-[#f0f6fc] text-lg mb-1">Modern</h4>
                        <p className="text-[#8b949e] text-sm">Latest tools and techniques</p>
                    </div>
                    <div className="p-6 bg-[rgba(255,255,255,0.02)] rounded-xl border border-[rgba(255,255,255,0.04)] transition-all duration-300 hover:-translate-y-1 hover:border-[rgba(31,111,235,0.2)] hover:shadow-[0_8px_30px_rgba(0,0,0,0.3)]">
                        <span className="block text-3xl font-bold text-[#58a6ff] mb-3">S</span>
                        <h4 className="text-[#f0f6fc] text-lg mb-1">Scalable</h4>
                        <p className="text-[#8b949e] text-sm">Ready for production</p>
                    </div>
                </div>
            </main>
        </div>
    );
}