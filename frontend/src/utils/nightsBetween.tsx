export default function nightsBetween(a: Date, b: Date) {
    if (!a || !b) return 0;
    const diff = new Date(b).getTime() - new Date(a).getTime();
    return Math.max(0, Math.floor(diff / 86400000));
}