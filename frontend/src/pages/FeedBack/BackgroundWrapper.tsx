export const BackgroundWrapper = ({ children }: { children: React.ReactNode }) => (
    <section className="min-h-screen relative flex items-center justify-center py-20 px-6 bg-[#0f0e0c]">
        <div className="absolute inset-0 z-0 bg-cover bg-center bg-no-repeat opacity-40" style={{ backgroundImage: "url('https://images.unsplash.com/photo-1566073771259-6a8506099945?q=80&w=2070&auto=format&fit=crop')" }}/>
        <div className="absolute inset-0 z-0 bg-gradient-to-b from-[#0f0e0c]/80 via-[#0f0e0c]/60 to-[#0f0e0c]" />

        <div className="flex flex-col items-center relative z-10 w-full max-w-md">
            {children}
        </div>
    </section>
);