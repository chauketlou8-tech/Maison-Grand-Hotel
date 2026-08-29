export default function extractDOB(idNumber: string): Date | null {
    if (idNumber.length !== 13) return null;

    const year = parseInt(idNumber.slice(0, 2));
    const month = parseInt(idNumber.slice(2, 4)) - 1;
    const day = parseInt(idNumber.slice(4, 6));

    const fullYear = year > 24 ? 1900 + year : 2000 + year;

    return new Date(Date.UTC(fullYear, month, day));
}